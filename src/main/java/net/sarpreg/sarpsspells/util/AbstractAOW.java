package net.sarpreg.sarpsspells.util;

import com.google.common.util.concurrent.AtomicDouble;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.config.SpellConfigManager;
import io.redspace.ironsspellbooks.api.config.SpellConfigParameter;
import io.redspace.ironsspellbooks.api.events.ModifySpellLevelEvent;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.MagicHelper;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.network.casting.OnCastStartedPacket;
import io.redspace.ironsspellbooks.network.casting.OnClientCastPacket;
import io.redspace.ironsspellbooks.network.casting.UpdateCastingStatePacket;
import io.redspace.ironsspellbooks.player.ClientInputEvents;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import io.redspace.ironsspellbooks.util.Log;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import org.joml.Vector3f;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static io.redspace.ironsspellbooks.api.spells.SpellAnimations.*;

public abstract class AbstractAOW extends AbstractSpell {

    public AbstractAOW() {
    }

    @Override
    public boolean attemptInitiateCast(ItemStack stack, int spellLevel, Level level, Player player, CastSource castSource, boolean triggerCooldown, String castingEquipmentSlot) {
        if (Log.SPELL_DEBUG) {
            IronsSpellbooks.LOGGER.debug("AbstractSpell.attemptInitiateCast isClient:{}, spell{}({})", level.isClientSide, this.getSpellId(), spellLevel);
        }

        if (level.isClientSide) {
            return false;
        }

        var serverPlayer = (ServerPlayer) player;
        var playerMagicData = MagicData.getPlayerMagicData(serverPlayer);

        if (!playerMagicData.isCasting()) {
            CastResult castResult = canBeCastedBy(spellLevel, castSource, playerMagicData, serverPlayer);
            if (castResult.message != null) {
                serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(castResult.message));
            }

            if (!castResult.isSuccess() || !checkPreCastConditions(level, spellLevel, serverPlayer, playerMagicData) || MinecraftForge.EVENT_BUS.post(new SpellPreCastEvent(player, this.getSpellId(), spellLevel, getSchoolType(), castSource))) {
                return false;
            }

            if (serverPlayer.isUsingItem()) {
                serverPlayer.stopUsingItem();
            }
            int effectiveCastTime = getEffectiveCastTime(spellLevel, player);

            playerMagicData.initiateCast(this, spellLevel, effectiveCastTime, castSource, castingEquipmentSlot);
            playerMagicData.setPlayerCastingItem(stack);

            onServerPreCast(player.level(), spellLevel, player, playerMagicData);

            PacketDistributor.sendToPlayer(serverPlayer, new UpdateCastingStatePacket(getSpellId(), spellLevel, effectiveCastTime, castSource, castingEquipmentSlot));
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(serverPlayer, new OnCastStartedPacket(serverPlayer.getUUID(), getSpellId(), spellLevel));

            return true;
        } else {
            Utils.serverSideCancelCast(serverPlayer);
            return false;
        }
    }

    @Override
    public CastResult canBeCastedBy(int spellLevel, CastSource castSource, MagicData playerMagicData, Player player) {
        if (ServerConfigs.DISABLE_ADVENTURE_MODE_CASTING.get()) {
            if (player instanceof ServerPlayer serverPlayer && serverPlayer.gameMode.getGameModeForPlayer() == GameType.ADVENTURE) {
                return new CastResult(CastResult.Type.FAILURE, Component.translatable("ui.irons_spellbooks.cast_error_adventure").withStyle(ChatFormatting.RED));
            }
        }
        var playerMana = playerMagicData.getMana();

        boolean hasEnoughMana = playerMana - getManaCost(spellLevel) >= 0;
        boolean isSpellOnCooldown = playerMagicData.getPlayerCooldowns().isOnCooldown(this);
        boolean hasRecastForSpell = playerMagicData.getPlayerRecasts().hasRecastForSpell(getSpellId());
        if (requiresLearning() && !isLearned(player)) {
            return new CastResult(CastResult.Type.FAILURE, Component.translatable("ui.irons_spellbooks.cast_error_unlearned").withStyle(ChatFormatting.RED));
        } else if (castSource == CastSource.SCROLL && this.getRecastCount(spellLevel, player) > 0) {
            return new CastResult(CastResult.Type.FAILURE, Component.translatable("ui.irons_spellbooks.cast_error_scroll", getDisplayName(player)).withStyle(ChatFormatting.RED));
        } else if ((castSource == CastSource.SPELLBOOK || castSource == CastSource.SWORD) && isSpellOnCooldown && !(player.isCreative() && !ServerConfigs.CREATIVE_COOLDOWN.get())) {
            return new CastResult(CastResult.Type.FAILURE, Component.translatable("ui.irons_spellbooks.cast_error_cooldown", getDisplayName(player)).withStyle(ChatFormatting.RED));
        } else if (!hasRecastForSpell && castSource.consumesMana() && !hasEnoughMana && !(player.isCreative() && !ServerConfigs.CREATIVE_MANA_COST.get())) {
            return new CastResult(CastResult.Type.FAILURE, Component.translatable("ui.irons_spellbooks.cast_error_mana", getDisplayName(player)).withStyle(ChatFormatting.RED));
        } else if (!(castSource == CastSource.SWORD)) {
            return new CastResult(CastResult.Type.FAILURE, Component.translatable("ui.sarps_spells.aow", getDisplayName(player)).withStyle(ChatFormatting.RED));
        } else {
            return new CastResult(CastResult.Type.SUCCESS);
        }
    }
}
