package net.sarpreg.sarpsspells.effect;


import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sarpreg.sarpsspells.registries.SarpMobEffectRegistry;

@Mod.EventBusSubscriber
public class MistFormEffect extends MagicMobEffect implements ISyncedMobEffect {
    public MistFormEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        livingEntity.level().addParticle(ParticleHelper.FIREFLY, livingEntity.getRandomX(3.0), livingEntity.getY() + Utils.getRandomScaled(2), livingEntity.getRandomZ(3.0), 0, Math.random(), 0);
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int pAmplifier) {
        super.onEffectAdded(livingEntity, pAmplifier);

        var targetingCondition = TargetingConditions.forCombat().ignoreLineOfSight().selector(e -> {
            //IronsSpellbooks.LOGGER.debug("InvisibilitySpell TargetingConditions:{}", e);
            return (((Mob) e).getTarget() == livingEntity);
        });

        //remove aggro from anything targeting us
        livingEntity.level().getNearbyEntities(Mob.class, targetingCondition, livingEntity, livingEntity.getBoundingBox().inflate(40D))
                .forEach(entityTargetingCaster -> {
                    //IronsSpellbooks.LOGGER.debug("InvisibilitySpell Clear Target From:{}", entityTargetingCaster);
                    entityTargetingCaster.setTarget(null);
                    entityTargetingCaster.targetSelector.getAvailableGoals().forEach(WrappedGoal::stop);
                    entityTargetingCaster.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
                });
    }
}
