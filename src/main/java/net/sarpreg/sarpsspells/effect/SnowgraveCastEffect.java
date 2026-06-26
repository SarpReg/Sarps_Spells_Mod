package net.sarpreg.sarpsspells.effect;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.mixin.LivingEntityAccessor;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.particle.SparkParticleOptions;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

public class SnowgraveCastEffect extends MagicMobEffect implements ISyncedMobEffect {
    public SnowgraveCastEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {

    }

    @Override
    public void clientTick(LivingEntity entity, MobEffectInstance instance) {
        var level = entity.level();
        for (int i = 0; i < 8; i++) {
            Vec3 random = Utils.getRandomVec3(.2);
            level.addParticle(ParticleHelper.SNOW_DUST, entity.getRandomX(2.0), entity.getY() + Utils.getRandomScaled(0.75), entity.getRandomZ(2.0), 0, Math.random() + 0.5, 0);
            level.addParticle(ParticleHelper.SNOWFLAKE, entity.getRandomX(2.0), entity.getY() + Utils.getRandomScaled(2), entity.getRandomZ(2.0), 0, Math.random() + 0.25, 0);
            level.addParticle(ParticleHelper.SNOWFLAKE, entity.getRandomX(3.0), entity.getY() + Utils.getRandomScaled(2), entity.getRandomZ(3.0), 0, Math.random(), 0);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
