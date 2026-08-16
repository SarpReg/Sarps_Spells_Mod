package net.sarpreg.sarpsspells.spells.sculk;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.RaycastBuilder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.eldritch_blast.EldritchBlastVisualEntity;
import io.redspace.ironsspellbooks.player.SpinAttackType;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.sarpreg.sarpsspells.SarpsSpellsMod;
import net.sarpreg.sarpsspells.player.SarpSpinAttackType;
import net.sarpreg.sarpsspells.registries.SarpySchoolRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class BloomOfSculkSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(SarpsSpellsMod.MODID, "bloom_of_sculk");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(SarpySchoolRegistry.SCULK_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(1)
            .build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.effect_length", Utils.stringTruncation(getDamage(spellLevel, caster), 2)),
                Component.translatable("ui.irons_spellbooks.distance", Utils.stringTruncation(getRange(spellLevel, caster), 1))
        );
    }

    public BloomOfSculkSpell() {
        this.manaCostPerLevel = 13;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 2;
        this.castTime = 0;
        this.baseManaCost = 60;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public SchoolType getSchoolType() {
        return SarpySchoolRegistry.SCULK.get();
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.SCULK_CLICKING);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.SCULK_SHRIEKER_SHRIEK);
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return 2;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {

        if (playerMagicData.getPlayerRecasts().hasRecastForSpell(getSpellId())) {
            var hitResult = RaycastBuilder.begin(level, entity)
                    .range(getRange(spellLevel, entity))
                    .checkForBlocks(true)
                    .bbInflation(.15f)
                    .build();
            level.addFreshEntity(new EldritchBlastVisualEntity(level, entity.getEyePosition().subtract(0, .75f, 0), hitResult.getLocation(), entity));
            if (hitResult.getType() == HitResult.Type.ENTITY) {
                Entity target = ((EntityHitResult) hitResult).getEntity();
                if (target.canBeHitByProjectile()) {
                    DamageSources.applyDamage(target, 5, getDamageSource(entity));
                }
            }
            MagicManager.spawnParticles(level, ParticleHelper.UNSTABLE_ENDER, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z, 50, 0, 0, 0, .3, false);
        }
        if (!playerMagicData.getPlayerCooldowns().isOnCooldown(this) && !playerMagicData.getPlayerRecasts().hasRecastForSpell(getSpellId())) {
            playerMagicData.getPlayerRecasts().addRecast(new RecastInstance(getSpellId(), spellLevel, getRecastCount(spellLevel, entity), 60, castSource, null), playerMagicData);

            entity.hasImpulse = true;
            float multiplier = (15 + getSpellPower(spellLevel, entity)) / 20f;

            Vec3 forward = entity.getLookAngle();

            //Create Dashing Movement Impulse
            var upwardness = forward.dot(new Vec3(0, 1, 0));
            var remap = 0.4f;
            var impulse = forward.scale(3 * multiplier).multiply(1, remap, 1);
            //Start Spin Attack
            if (entity.onGround()) {
                if (entity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.teleport(serverPlayer.getX(), serverPlayer.getY() + 1, serverPlayer.getZ(), serverPlayer.getYRot(), serverPlayer.getXRot());
                } else {
                    entity.move(MoverType.SELF, new Vec3(0.0, 1.1999999F, 0.0));
                }
                impulse.add(0, 0.5, 0);
            } else {
                impulse.add(0, 0.25, 0);
            }
            entity.setDeltaMovement(new Vec3(
                    Mth.lerp(.75f, entity.getDeltaMovement().x, impulse.x),
                    Mth.lerp(.75f, entity.getDeltaMovement().y, impulse.y),
                    Mth.lerp(.75f, entity.getDeltaMovement().z, impulse.z)
            ));
            entity.hurtMarked = true;

            entity.addEffect(new MobEffectInstance(MobEffectRegistry.VOLT_STRIKE.get(), 10, getDamage(spellLevel, entity), false, false, false));
            entity.invulnerableTime = 20;
            playerMagicData.getSyncedData().setSpinAttackType(SpinAttackType.RIPTIDE);
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void playSound(Optional<SoundEvent> sound, Entity entity) {
        if (sound == getCastFinishSound()) {
            entity.playSound(sound.get(), 3.5f, .9f + entity.level().random.nextFloat() * .2f);
        } else {
            super.playSound(sound, entity);
        }

    }

    public static float getRange(int level, LivingEntity caster) {
        return 15 + level;
    }

    private int getDamage(int spellLevel, LivingEntity caster) {
        return (int) getSpellPower(spellLevel, caster);
    }

}
