package net.sarpreg.sarpsspells.entity.spells.sunlight_lance;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.network.particles.FieryExplosionParticlesPacket;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ExplosionEvent;
import net.sarpreg.sarpsspells.registries.EntityRegistry;
import net.sarpreg.sarpsspells.registries.SarpySpellRegistry;
import net.sarpreg.sarpsspells.util.SarparticleHelper;

import java.util.Optional;
import java.util.function.Supplier;

public class SunlightLanceProjectile extends AbstractMagicProjectile {

    @Override
    public void trailParticles() {
        Vec3 vec3 = this.position().subtract(getDeltaMovement());
        level().addParticle(SarparticleHelper.SUNSPARK, vec3.x, vec3.y, vec3.z, 0, 0, 0);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(level(), SarparticleHelper.SUNSPARK, x, y, z, 75, .1, .1, .1, 2, true);
        MagicManager.spawnParticles(level(), SarparticleHelper.SUNSPARK, x, y, z, 75, .1, .1, .1, .5, false);
    }

    @Override
    public float getSpeed() {
        return 3f;
    }

    @Override
    public Optional<Supplier<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    public SunlightLanceProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(false);
    }

    public SunlightLanceProjectile(Level levelIn, LivingEntity shooter) {
        this(EntityRegistry.SUNLIGHT_LANCE_PROJECTILE.get(), levelIn);
        setOwner(shooter);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {

    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        DamageSources.applyDamage(entityHitResult.getEntity(), damage, SarpySpellRegistry.SUNLIGHT_LANCE_SPELL.get().getDamageSource(this, getOwner()));

    }

    @Override
    protected void onHit(HitResult pResult) {
        //irons_spellbooks.LOGGER.debug("Boom");

        if (!level().isClientSide) {
//            irons_spellbooks.LOGGER.debug("{}",pos);
//            //Beam
//            for (int i = 0; i < 40; i++) {
//                Vec3 randomVec = new Vec3(
//                        Utils.random.nextDouble() * .25 - .125,
//                        Utils.random.nextDouble() * .25 - .125,
//                        Utils.random.nextDouble() * .25 - .125
//                );
//                //level.addParticle(ParticleHelper.ELECTRICITY, pos.x + randomVec.x, pos.y + randomVec.y + i * .25, pos.z + randomVec.z, randomVec.x * .2, randomVec.y * .2, randomVec.z * .2);
//                level.addParticle(ParticleHelper.ELECTRICITY, pos.x, pos.y, pos.z, 0,0,0);
//            }
        }
        this.discardHelper(pResult);
        if (!this.level().isClientSide) {
            impactParticles(xOld, yOld, zOld);
            float explosionRadius = 5;
            var explosionRadiusSqr = explosionRadius * explosionRadius;
            var entities = level().getEntities(this, this.getBoundingBox().inflate(explosionRadius));
            Vec3 losPoint = Utils.raycastForBlock(level(), this.position(), this.position().add(0, 2, 0), ClipContext.Fluid.NONE).getLocation();
            for (Entity entity : entities) {
                double distanceSqr = entity.distanceToSqr(pResult.getLocation());
                if (distanceSqr < explosionRadiusSqr && canHitEntity(entity) && Utils.hasLineOfSight(level(), losPoint, entity.getBoundingBox().getCenter(), true)) {
                    DamageSources.applyDamage(entity, this.damage, SarpySpellRegistry.SUNLIGHT_LANCE_SPELL.get().getDamageSource(this, getOwner()));
                }
            }
            PacketDistributor.sendToPlayersTrackingEntity(this, new FieryExplosionParticlesPacket(pResult.getLocation().subtract(getDeltaMovement().scale(0.5)), getExplosionRadius()));
            this.playSound(SoundRegistry.DIVINE_SMITE_CAST.get(), 4, .65f);
            MagicManager.spawnParticles(level(), new BlastwaveParticleOptions(SchoolRegistry.HOLY.get().getTargetingColor(), 5),
                    xOld, yOld, zOld, 1, 0, 0, 0, 0, true);
            MagicManager.spawnParticles(level(), ParticleTypes.ELECTRIC_SPARK, xOld, yOld, zOld, 70, 0, 0, 0, 1, false);
        }
        this.discardHelper(pResult);
        super.onHit(pResult);
    }

    public int getAge() {
        return tickCount;
    }
}
