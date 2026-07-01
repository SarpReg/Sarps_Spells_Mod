package net.sarpreg.sarpsspells.effect;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.datagen.DamageTypeTagGenerator;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sarpreg.sarpsspells.SarpsSpellsMod;

import java.util.Optional;

public class AscendanceEffect extends MagicMobEffect implements ISyncedMobEffect {

    public static final ResourceLocation ASCENDANCE_TEXTURE = ResourceLocation.fromNamespaceAndPath(SarpsSpellsMod.MODID, "textures/entity/ascendance.png");

    public AscendanceEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    public static boolean doEffect(LivingEntity livingEntity, DamageSource damageSource) {
        if (livingEntity.level().isClientSide || damageSource.is(DamageTypeTags.IS_FALL) || damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        var random = livingEntity.getRandom();
        var level = livingEntity.level();

        if (!damageSource.is(DamageTypeTags.IS_FIRE)) {
            particleCloud(livingEntity);
            level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundRegistry.FIRE_DAGGER_PARRY.get(), SoundSource.AMBIENT, 0.2F, .9F + random.nextFloat() * .2f);
        }
        return true;
    }

    private static void particleCloud(LivingEntity entity) {
        Vec3 pos = entity.position().add(0, entity.getBbHeight() / 2, 0);
        MagicManager.spawnParticles(entity.level(), ParticleHelper.ELECTRICITY, pos.x, pos.y, pos.z, 25, entity.getBbWidth() / 4, entity.getBbHeight() / 5, entity.getBbWidth() / 4, 0, false);
    }

    @Override
    public void clientTick(LivingEntity entity, MobEffectInstance instance) {
        Vec3 backwards = entity.getForward().scale(.003).reverse().add(0, 0.02, 0);
        var random = entity.getRandom();
        for (int i = 0; i < 2; i++) {
            Vec3 motion = new Vec3(
                    random.nextFloat() * 2 - 1,
                    random.nextFloat() * 2 - 1,
                    random.nextFloat() * 2 - 1
            );
            motion = motion.scale(.04f).add(backwards);
            //entity.level().addParticle(ParticleHelper.CLEANSE_PARTICLE, entity.getRandomX(.4f), entity.getRandomY(), entity.getRandomZ(.4f), motion.x, motion.y, motion.z);
        }
    }
}
