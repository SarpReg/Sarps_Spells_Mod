package net.sarpreg.sarpsspells.spells.ice;


import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.RaycastBuilder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.magma_ball.FireField;
import io.redspace.ironsspellbooks.entity.spells.snowball.FrostField;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import io.redspace.ironsspellbooks.spells.TargetAreaCastData;
import io.redspace.ironsspellbooks.util.Log;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.sarpreg.sarpsspells.SarpsSpellsMod;
import net.sarpreg.sarpsspells.registries.SarpMobEffectRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SnowgraveSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(SarpsSpellsMod.MODID, "snowgrave");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 2)),
                Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(getRadius(caster), 1))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(SchoolRegistry.ICE_RESOURCE)
            .setMaxLevel(3)
            .setCooldownSeconds(48)
            .build();

    public SnowgraveSpell() {
        this.manaCostPerLevel = 50;
        this.baseSpellPower = 7;
        this.spellPowerPerLevel = 3;
        this.castTime = 70;
        this.baseManaCost = 200;

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
        return Optional.of(SoundRegistry.FROSTWAVE_PREPARE.get());
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 16, .35f);
    }

    @Override
    public void onServerPreCast(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, castTime, 1, false, false, false));
        entity.addEffect(new MobEffectInstance(SarpMobEffectRegistry.SNOWGRAVE_CAST.get(), castTime, 1, false, false, false));

        super.onServerPreCast(level, spellLevel, entity, playerMagicData);
    }

    @Override
    public void onServerCastComplete(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData, boolean cancelled) {
        if (cancelled) {
            entity.removeEffect(MobEffects.LEVITATION);
            entity.removeEffect(SarpMobEffectRegistry.SNOWGRAVE_CAST.get());
        }

        super.onServerCastComplete(level, spellLevel, entity, playerMagicData, cancelled);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, castTime, 0, false, false, false));

        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData castTargetingData) {
            LivingEntity target = castTargetingData.getTarget((ServerLevel) level);

            if (target != null) {
                Vec3 targetArea = castTargetingData.getTargetPosition((ServerLevel) level);
                MagicManager.spawnParticles(level, ParticleTypes.SNOWFLAKE, targetArea.x, targetArea.y, targetArea.z, 25, 1, 1, 1, 1, true);
                MagicManager.spawnParticles(level, ParticleHelper.SNOWFLAKE, targetArea.x, targetArea.y + 1, targetArea.z, 25, .25, 1.5, .25, 1, false);
                level.playSound(null, targetArea.x, targetArea.y, targetArea.z, SoundRegistry.FROSTBITE_FREEZE.get(), SoundSource.PLAYERS, 2, Utils.random.nextIntBetweenInclusive(8, 12) * .1f);
                var radius = 2.5f;
                var radiusSqr = radius * radius;
                var damage = getDamage(spellLevel, entity);
                var source = getDamageSource(entity);
                DamageSources.ignoreNextKnockback(target);
                DamageSources.applyDamage(target, damage, source);
                FrostField fire = new FrostField(level);
                fire.setOwner(entity);
                fire.setDuration(200);
                fire.setDamage(damage * .1f);
                fire.setRadius(radius);
                fire.setCircular();
                fire.moveTo(targetArea);
                level.addFreshEntity(fire);

                target.addEffect(new MobEffectInstance(MobEffectRegistry.CHILLED.get(), 140, 1, false, false, true));
                target.addEffect(new MobEffectInstance(SarpMobEffectRegistry.SNOWGRAVE_CAST.get(), 40, 1, false, false, false));
            }
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float horizontalDistanceSqr(LivingEntity livingEntity, Vec3 vec3) {
        var dx = livingEntity.getX() - vec3.x;
        var dz = livingEntity.getZ() - vec3.z;
        return (float) (dx * dx + dz * dz);
    }

    @Override
    public SpellDamageSource getDamageSource(@Nullable Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setFreezeTicks(100);
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster);
    }

    private int getFreezeTime(int spellLevel, LivingEntity caster) {
        return (int) (getSpellPower(spellLevel, caster) * 15);
    }

    private float getRadius(LivingEntity caster) {
        return 2.5f;
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
//
//    @Override
//    public Vector3f getTargetingColor() {
//        return new Vector3f(.7f, .3f, .15f);
//    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.ANIMATION_INSTANT_CAST;
    }
}
