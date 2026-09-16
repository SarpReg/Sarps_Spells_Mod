package net.sarpreg.sarpsspells.spells.holy;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.RaycastBuilder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.sarpreg.sarpsspells.SarpsSpellsMod;
import net.sarpreg.sarpsspells.registries.SarpMobEffectRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class DivineMantleSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(SarpsSpellsMod.MODID, "divine_mantle");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.effect_length", "1.5s", 1)
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(2)
            .setCooldownSeconds(45)
            .build();

    public DivineMantleSpell() {
        this.manaCostPerLevel = 3;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 1;
        this.castTime = 20;
        this.baseManaCost = 45;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.SHULKER_SHOOT);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, .35f);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {

        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData castTargetingData) {
            LivingEntity target = castTargetingData.getTarget((ServerLevel) level);

            if (target != null) {
                target.addEffect(new MobEffectInstance(SarpMobEffectRegistry.DIVINE_MANTLE.get(), 30*20, spellLevel, false, false, true));
            }
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Nullable
    private LivingEntity findTarget(LivingEntity caster) {
        var target = RaycastBuilder.begin(caster.level(), caster)
                .range(32)
                .checkForBlocks(true)
                .bbInflation(0.35f)
                .build();
        if (target instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity livingTarget) {
            return livingTarget;
        } else {
            return null;
        }
    }

    public int getDuration(int spellLevel, LivingEntity caster) {
        return 30;
    }

}