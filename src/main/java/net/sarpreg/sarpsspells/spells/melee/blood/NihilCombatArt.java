package net.sarpreg.sarpsspells.spells.melee.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sarpreg.sarpsspells.SarpsSpellsMod;
import net.sarpreg.sarpsspells.effect.OccultMarkEffect;
import net.sarpreg.sarpsspells.entity.spells.sunburst.SunburstAoe;
import net.sarpreg.sarpsspells.registries.SarpMobEffectRegistry;
import net.sarpreg.sarpsspells.util.AbstractAOW;
import net.sarpreg.sarpsspells.util.SarpSpellAnims;
import net.sarpreg.sarpsspells.util.SarparticleHelper;
import org.joml.Vector3f;

import java.util.List;
import java.util.Optional;

public class NihilCombatArt extends AbstractAOW {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(SarpsSpellsMod.MODID, "nihil");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(getRadius(spellLevel, caster), 2))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(135)
            .build();

    public NihilCombatArt() {
        this.manaCostPerLevel = 10000;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 23;
        this.baseManaCost = 200;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.SCORCH_PREPARE.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.CLEANSE_CAST.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float radius = getRadius(spellLevel, entity);
        //this is immaculately stupid
        MagicManager.spawnParticles(level, new BlastwaveParticleOptions(SchoolRegistry.BLOOD.get().getTargetingColor(), radius), entity.getX(), entity.getY() + .15f, entity.getZ(), 1, 0, 0, 0, 0, true);
        MagicManager.spawnParticles(level, ParticleHelper.BLOOD_GROUND, entity.getX(), entity.getY() + 1, entity.getZ(), 180, .25, .25, .25, 0.7f + radius * .1f, false);
        CameraShakeManager.addCameraShake(new CameraShakeData(level, 30, entity.position(), radius * 2));

        Vec3 start = entity.getBoundingBox().getCenter();
        level.getEntities(entity, entity.getBoundingBox().inflate(radius, radius, radius), (target) -> !DamageSources.isFriendlyFireBetween(target, entity) && Utils.hasLineOfSight(level, entity, target, true)).forEach(target -> {
            if (target instanceof LivingEntity livingEntity && canHit(entity, target) && livingEntity.distanceToSqr(entity) < radius * radius) {
                if (((LivingEntity) target).getEffect(SarpMobEffectRegistry.OCCULT_MARK.get()) != null) {
                    Vec3 dest = livingEntity.getBoundingBox().getCenter();
                    DamageSources.applyDamage(target, (float) OccultMarkEffect.damageFor(target), getDamageSource(entity));
                }
            }
        });
        float range = 1.7f;
        Vec3 hitLocation = Utils.moveToRelativeGroundLevel(level, Utils.raycastForBlock(level, entity.getEyePosition(), entity.getEyePosition().add(entity.getForward().multiply(range, 0, range)), ClipContext.Fluid.NONE).getLocation(), 3);
        CameraShakeManager.addCameraShake(new CameraShakeData(level, 20 + (int) radius, hitLocation, radius * 2 + 5));
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private boolean canHit(Entity owner, Entity target) {
        return target != owner && target.isAlive() && target.isPickable() && !target.isSpectator();
    }

    public float getRadius(int spellLevel, LivingEntity caster) {
        return 20;
    }

    @Override
    public void playSound(Optional<SoundEvent> sound, Entity entity) {
        sound.ifPresent((soundEvent -> entity.playSound(soundEvent, 3.0f, .9f + Utils.random.nextFloat() * .2f)));
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SarpSpellAnims.ANIMATION_NIHIL_PREPARE;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SarpSpellAnims.ANIMATION_NIHIL_CAST;
    }
}
