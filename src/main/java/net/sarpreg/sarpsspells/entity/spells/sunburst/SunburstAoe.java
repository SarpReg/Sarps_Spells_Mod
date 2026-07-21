package net.sarpreg.sarpsspells.entity.spells.sunburst;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.entity.spells.FireEruptionAoe;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class SunburstAoe extends FireEruptionAoe {

    public SunburstAoe(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SunburstAoe(Level level, float radius) {
        this(EntityRegistry.FIRE_ERUPTION_AOE.get(), level);
        this.setRadius(radius);
        this.ambientParticles(ParticleHelper.CLEANSE_PARTICLE);
    }

    @Override
    public void applyEffect(LivingEntity target) {

    }

    @Override
    public float getParticleCount() {
        return 100f;
    }

    int waveAnim = -1; // anim step in units of blocks

    @Override
    public void tick() {
        var radius = this.getRadius();
        var level = this.level();

    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    protected boolean canHitTargetForGroundContext(LivingEntity target) {
        return !level().noCollision(target.getBoundingBox().move(new Vec3(0, -.9999, 0)));
    }

    @Override
    protected Vec3 getInflation() {
        return new Vec3(0, 5, 0);
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 3F);
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }

}
