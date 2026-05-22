package net.sarpreg.sarpsspells.effect;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.mixin.LivingEntityAccessor;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.particle.SparkParticleOptions;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;
import java.util.UUID;

public class UpdraftEffect extends MagicMobEffect implements ISyncedMobEffect {
    public UpdraftEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        var level = livingEntity.level();
        if (level.isClientSide) {
            return;
        }
        List<Entity> list = level.getEntities(livingEntity, livingEntity.getBoundingBox().inflate(.25, .5, .25));
        boolean hit = false;
        if (!hit &&
                (!level.noCollision(livingEntity.getBoundingBox().move(livingEntity.getDeltaMovement()).move(livingEntity.getDeltaMovement().normalize().scale(0.1)).deflate(0.1)))) {
            hit = true;
        }
        if (hit) {
            var x = livingEntity.getX();
            var y = livingEntity.getY();
            var z = livingEntity.getZ();

            for (int i = 0; i < 2; i++) {
                Vec3 random = Utils.getRandomVec3(.2);
                level.addParticle(new SparkParticleOptions(new Vector3f(.85f, .85f, .85f)), livingEntity.getRandomX(0.75), y + Utils.getRandomScaled(0.75), livingEntity.getRandomZ(0.75), random.x, random.y, random.z);
            }
            MagicManager.spawnParticles(level, ParticleHelper.FOG_CAMPFIRE_SMOKE, x, y, z, 2, .08, .08, .08, 0.3, false);
            MagicManager.spawnParticles(level, new BlastwaveParticleOptions(new Vector3f(.85f, .85f, .85f), 3), x, y + .15f, z, 1, 0, 0, 0, 0, true);
            level.playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE, livingEntity.getSoundSource(), 2, 0.8f);
            livingEntity.removeEffect(this);
        }
        livingEntity.fallDistance = 0;
        /*var level = livingEntity.level();
        if (level.isClientSide) {
            return;
        }
        boolean hit = false;
        UUID ignore = null;
        if (!hit &&
                (!level.noCollision(livingEntity.getBoundingBox().move(livingEntity.getDeltaMovement()).move(livingEntity.getDeltaMovement().normalize().scale(0.1)).deflate(0.1)))) {
            hit = true;
        }
        if (hit) {
            float explosionRadius = 4;
            var explosionRadiusSqr = explosionRadius * explosionRadius;
            var entities = level.getEntities(livingEntity, livingEntity.getBoundingBox().inflate(explosionRadius));
            Vec3 losPoint = Utils.raycastForBlock(level, livingEntity.position(), livingEntity.position().add(0, 1, 0), ClipContext.Fluid.NONE).getLocation();
            for (Entity entity : entities) {
                double distanceSqr = entity.distanceToSqr(livingEntity.position());
                if (ignore != entity.getUUID() && distanceSqr < explosionRadiusSqr && entity.canBeHitByProjectile() && Utils.hasLineOfSight(level, losPoint, entity.getBoundingBox().getCenter(), true)) {

                }
            }
            //livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().normalize().scale(-0.5).add(0, 0.5, 0));
            livingEntity.hurtMarked = true;

            var x = livingEntity.getX();
            var y = livingEntity.getY() + 1;
            var z = livingEntity.getZ();
            MagicManager.spawnParticles(level, ParticleHelper.FOG_CAMPFIRE_SMOKE, x, y, z, 5, .08, .08, .08, 0.3, false);
            MagicManager.spawnParticles(level, new BlastwaveParticleOptions(new Vector3f(.85f, .85f, .85f), 3), x, y + .15f, z, 1, 0, 0, 0, 0, true);
            level.playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE, livingEntity.getSoundSource(), 2, 0.8f);
            livingEntity.removeEffect(this);
        }
        livingEntity.fallDistance = 0;
        */
    }

    @Override
    public void clientTick(LivingEntity entity, MobEffectInstance instance) {
        var level = entity.level();
        for (int i = 0; i < 4; i++) {
            Vec3 random = Utils.getRandomVec3(.2);
            level.addParticle(ParticleHelper.SNOW_DUST, entity.getRandomX(0.75), entity.getY() + Utils.getRandomScaled(0.75), entity.getRandomZ(0.75), random.x, random.y, random.z);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void onEffectAdded(LivingEntity pLivingEntity, int pAmplifier) {
        super.onEffectAdded(pLivingEntity, pAmplifier);
        ((LivingEntityAccessor) pLivingEntity).setLivingEntityFlagInvoker(4, true);
    }

    @Override
    public void onEffectRemoved(LivingEntity pLivingEntity, int pAmplifier) {
        super.onEffectRemoved(pLivingEntity, pAmplifier);
        ((LivingEntityAccessor) pLivingEntity).setLivingEntityFlagInvoker(4, false);
    }
}
