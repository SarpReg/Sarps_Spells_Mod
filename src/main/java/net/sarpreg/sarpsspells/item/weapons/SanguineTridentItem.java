package net.sarpreg.sarpsspells.item.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.effect.ImmolateEffect;
import io.redspace.ironsspellbooks.entity.spells.thrown_spear.ThrownSpear;
import io.redspace.ironsspellbooks.item.weapons.ExtendedWeaponTier;
import io.redspace.ironsspellbooks.item.weapons.IronsWeaponTier;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SanguineTridentItem extends MagicSwordItem {
    public static final int COOLDOWN_TICKS = 1 * 20;

    public <T extends Tier & IronsWeaponTier> SanguineTridentItem(T pTier, Properties pProperties, SpellDataRegistryHolder[] spellDataRegistryHolders) {
        super(pTier, pProperties, spellDataRegistryHolders);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, tooltipComponents, pIsAdvanced);
        tooltipComponents.add(
                Component.translatable(
                        "tooltip.irons_spellbooks.passive_ability_no_cooldown",
                        Component.literal(Utils.timeFromTicks(Utils.applyCooldownReduction(COOLDOWN_TICKS, MinecraftInstanceHelper.getPlayer()), 1)).withStyle(ChatFormatting.AQUA)
                ).withStyle(ChatFormatting.DARK_PURPLE)
        );
        tooltipComponents.add(Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".desc")).withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltipComponents.add(Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".occult_mark.desc",
                Component.literal(Utils.stringTruncation(ImmolateEffect.damageFor(MinecraftInstanceHelper.getPlayer()), 1)).withStyle(ChatFormatting.RED))
        ).withStyle(ChatFormatting.LIGHT_PURPLE));
    }

//    @Override
//    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
//        return super.supportsEnchantment(stack, enchantment) || enchantment.is(Enchantments.LOYALTY)|| enchantment.is(Enchantments.CHANNELING);
//    }

}
