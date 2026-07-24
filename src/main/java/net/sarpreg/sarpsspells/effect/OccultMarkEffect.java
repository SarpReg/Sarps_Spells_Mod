package net.sarpreg.sarpsspells.effect;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.network.particles.FieryExplosionParticlesPacket;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.sarpreg.sarpsspells.registries.SarpMobEffectRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

public class OccultMarkEffect extends MagicMobEffect implements ISyncedMobEffect {

    public static final int STACKS_REQUIRED = 3;
    public static final int STACKS_REQUIRED_AMPLIFIER = STACKS_REQUIRED - 1;

    /**
     * Weak map tracking Afflicted to Afflictor in order to maintain explosion killcredit
     */
    private static final Map<LivingEntity, Entity> EFFECT_CREDIT = new WeakHashMap<>();
    /**
     * Weak map tracking specific instances of the effect that should explode at a delay. Maps instance to timestamp
     */
    private static final Map<MobEffectInstance, Integer> DELAYED_INSTANCES = new WeakHashMap<>();

    public OccultMarkEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    public static MobEffectInstance addOccultMarkStack(LivingEntity entity, @Nullable Entity afflicter) {
        MobEffectInstance previous = entity.getEffect(SarpMobEffectRegistry.OCCULT_MARK.get());
        MobEffectInstance inst;
        if (previous != null) {
            inst = new MobEffectInstance(SarpMobEffectRegistry.OCCULT_MARK.get(), 20 * 8, Math.max(0, Math.min(2, previous.getAmplifier() + 1)), previous.isAmbient(), previous.isVisible(), previous.showIcon());
        } else {
            inst = new MobEffectInstance(SarpMobEffectRegistry.OCCULT_MARK.get(), 20 * 8, 0, false, false, true);
        }
        if (afflicter != null) {
            EFFECT_CREDIT.put(entity, afflicter);
        }
        entity.addEffect(inst);
        return inst;
    }

    @Override
    public void clientTick(LivingEntity livingEntity, MobEffectInstance instance) {
        int amplifier = instance.getAmplifier();
        ParticleOptions particle = ParticleTypes.SMOKE;
        if (amplifier == 1) {
            particle = ParticleHelper.FIRE;
        } else if (amplifier >= 2) {
            particle = ParticleHelper.BLOOD;
        }
        var random = livingEntity.getRandom();
        for (int i = 0; i < 2; i++) {
            Vec3 motion = new Vec3(
                    random.nextFloat() * 2 - 1,
                    random.nextFloat() * 2 - 1,
                    random.nextFloat() * 2 - 1
            );
            motion = motion.scale(.04f);
            livingEntity.level().addParticle(particle, livingEntity.getRandomX(.4f), livingEntity.getRandomY(), livingEntity.getRandomZ(.4f), motion.x, motion.y, motion.z);
        }
    }

    public static double damageFor(@Nullable Entity entity) {
        double baseDamage = 8.0;
        if (entity instanceof LivingEntity livingAttacker) {
            baseDamage = baseDamage * (livingAttacker.getEffect(SarpMobEffectRegistry.OCCULT_MARK.get()).getAmplifier() + 1) * livingAttacker.getAttributeValue(AttributeRegistry.SPELL_POWER.get()) * livingAttacker.getAttributeValue(AttributeRegistry.BLOOD_SPELL_POWER.get());
        }
        return baseDamage;
    }

    static int duration;

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        OccultMarkEffect.duration = duration;
        return amplifier >= STACKS_REQUIRED_AMPLIFIER;
    }
}
