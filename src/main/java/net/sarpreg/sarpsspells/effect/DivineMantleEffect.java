package net.sarpreg.sarpsspells.effect;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.datagen.DamageTypeTagGenerator;
import io.redspace.ironsspellbooks.effect.CustomDescriptionMobEffect;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.sarpreg.sarpsspells.registries.SarpMobEffectRegistry;
import net.sarpreg.sarpsspells.registries.SarparticleRegistry;
import net.sarpreg.sarpsspells.util.SarparticleHelper;

public class DivineMantleEffect extends CustomDescriptionMobEffect implements ISyncedMobEffect {
    public DivineMantleEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public Component getDescriptionLine(MobEffectInstance instance) {
        int amp = instance.getAmplifier() + 1;
        return Component.translatable("tooltip.irons_spellbooks.evasion_description", amp).withStyle(ChatFormatting.BLUE);
    }

    @Override
    public void onEffectAdded(LivingEntity pLivingEntity, int pAmplifier) {
        super.onEffectAdded(pLivingEntity, pAmplifier);
        MagicData.getPlayerMagicData(pLivingEntity).getSyncedData().setEvasionHitsRemaining(pAmplifier);
    }

    public static boolean doEffect(LivingEntity livingEntity, DamageSource damageSource) {
        if (livingEntity.level().isClientSide
                || damageSource.is(DamageTypeTags.IS_FALL)
                || damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)
                || damageSource.is(DamageTypeTagGenerator.BYPASS_EVASION)) {
            return false;
        }

        var data = MagicData.getPlayerMagicData(livingEntity).getSyncedData();
        data.subtractEvasionHit();
        if (data.getEvasionHitsRemaining() < 0) {
            livingEntity.removeEffect(SarpMobEffectRegistry.DIVINE_MANTLE.get());
        }

        double d0 = livingEntity.getX();
        double d1 = livingEntity.getY();
        double d2 = livingEntity.getZ();
        var level = livingEntity.level();
        var random = livingEntity.getRandom();

        level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundRegistry.FIRE_DAGGER_PARRY.get(), SoundSource.AMBIENT, 0.2F, .9F + random.nextFloat() * .2f);
        livingEntity.playSound(SoundEvents.ENDERMAN_TELEPORT, 2.0F, 1.0F);
        //Vanilla teleport only spawns particles from the original location, not at the destination
        particleCloud(livingEntity);
        return true;
    }

    private static void particleCloud(LivingEntity entity) {
        Vec3 pos = entity.position().add(0, entity.getBbHeight() / 2, 0);
        MagicManager.spawnParticles(entity.level(), SarparticleHelper.SUNSPARK, pos.x, pos.y, pos.z, 25, entity.getBbWidth() / 4, entity.getBbHeight() / 5, entity.getBbWidth() / 4, 0, false);
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
            entity.level().addParticle(ParticleHelper.CLEANSE_PARTICLE, entity.getRandomX(.4f), entity.getRandomY(), entity.getRandomZ(.4f), motion.x, motion.y, motion.z);
        }
    }

}
