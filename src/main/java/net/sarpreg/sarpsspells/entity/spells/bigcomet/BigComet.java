package net.sarpreg.sarpsspells.entity.spells.bigcomet;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.entity.spells.comet.Comet;
import io.redspace.ironsspellbooks.network.particles.FieryExplosionParticlesPacket;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ExplosionEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class BigComet extends Comet {
    public BigComet(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    public BigComet(Level pLevel, LivingEntity pShooter) {
        this(EntityRegistry.COMET.get(), pLevel);
        this.setOwner(pShooter);
    }

    public void shoot(Vec3 rotation, float innaccuracy) {
        Vec3 offset = Utils.getRandomVec3(1).normalize().scale(innaccuracy);
        super.shoot(rotation.add(offset));
    }

    @Override
    public void trailParticles() {
        var vec = getDeltaMovement();
        var length = vec.length();
        int count = (int) Math.min(20, Math.round(length) * 4) + 1;
        float f = (float) length / count;
        for (int i = 0; i < count; i++) {
            Vec3 random = Utils.getRandomVec3(0.04);
            Vec3 p = vec.scale(f * i);
            level().addParticle(ParticleHelper.UNSTABLE_ENDER, this.getX() + random.x + p.x, this.getY() + random.y + p.y, this.getZ() + random.z + p.z, random.x, random.y, random.z);
            MagicManager.spawnParticles(level(), new BlastwaveParticleOptions(SpellRegistry.STARFALL_SPELL.get().getSchoolType().getTargetingColor(), 4f), random.x, random.y, random.z, 1, 0, 0, 0, 0, true);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(level(), new BlastwaveParticleOptions(SpellRegistry.STARFALL_SPELL.get().getSchoolType().getTargetingColor(), 4f), x, y, z, 1, 0, 0, 0, 0, true);
    }

    @Override
    public float getSpeed() {
        return 1.85f;
    }

    @Override
    protected void doImpactSound(Supplier<SoundEvent> sound) {
        level().playSound(null, getX(), getY(), getZ(), sound.get(), SoundSource.NEUTRAL, .8f, 1.35f + Utils.random.nextFloat() * .3f);
    }

    @Override
    public Optional<Supplier<SoundEvent>> getImpactSound() {
        return Optional.of(() -> SoundEvents.GENERIC_EXPLODE);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (!this.level().isClientSide) {
            impactParticles(xOld, yOld, zOld);
            float explosionRadius = getExplosionRadius();
            var explosionRadiusSqr = explosionRadius * explosionRadius;
            var entities = level().getEntities(this, this.getBoundingBox().inflate(explosionRadius));
            Vec3 losPoint = Utils.raycastForBlock(level(), this.position(), this.position().add(0, 2, 0), ClipContext.Fluid.NONE).getLocation();
            for (Entity entity : entities) {
                double distanceSqr = entity.distanceToSqr(hitResult.getLocation());
                if (distanceSqr < explosionRadiusSqr && canHitEntity(entity) && Utils.hasLineOfSight(level(), losPoint, entity.getBoundingBox().getCenter(), true)) {
                    double p = (1 - distanceSqr / explosionRadiusSqr);
                    float damage = (float) (this.damage * p);
                    DamageSources.applyDamage(entity, damage, SpellRegistry.STARFALL_SPELL.get().getDamageSource(this, getOwner()));
                }
            }
            if (ServerConfigs.SPELL_GREIFING.get()) {
                Explosion explosion = new Explosion(level(), null, SpellRegistry.STARFALL_SPELL.get().getDamageSource(this, getOwner()), null, this.getX(), this.getY(), this.getZ(), this.getExplosionRadius() / 2, true, Explosion.BlockInteraction.DESTROY);
                if (!MinecraftForge.EVENT_BUS.post(new ExplosionEvent.Start(level(), explosion))) {
                    explosion.explode();
                    explosion.finalizeExplosion(false);
                }
            }
            PacketDistributor.sendToPlayersTrackingEntity(this, new FieryExplosionParticlesPacket(hitResult.getLocation().subtract(getDeltaMovement().scale(0.5)), getExplosionRadius()));
            playSound(SoundEvents.GENERIC_EXPLODE, 4.0F, (1.0F + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) * 0.7F);
            this.discardHelper(hitResult);
        }
    }

}
