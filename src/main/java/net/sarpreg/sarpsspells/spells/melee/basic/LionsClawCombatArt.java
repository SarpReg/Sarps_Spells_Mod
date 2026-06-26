package net.sarpreg.sarpsspells.spells.melee.basic;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sarpreg.sarpsspells.SarpsSpellsMod;
import net.sarpreg.sarpsspells.particle.MeleeStrikeParticleOptions;
import net.sarpreg.sarpsspells.registries.SarpySchoolRegistry;
import net.sarpreg.sarpsspells.util.AbstractAOW;
import net.sarpreg.sarpsspells.util.SarpSpellAnims;
import org.joml.Vector3f;

import java.util.List;
import java.util.Optional;

public class LionsClawCombatArt extends AbstractAOW {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(SarpsSpellsMod.MODID, "lions_claw");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 2)),
                Component.translatable("ui.sarps_spells.meleespell")
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(SarpySchoolRegistry.COMBATART_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(14)
            .build();

    public LionsClawCombatArt() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 1;
        this.castTime = 25;
        this.baseManaCost = 0;
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

    /*@Override
    public Optional<SoundEvent> getCastStartSound() {
        return null;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return null;
    }*/

    @Override
    public SchoolType getSchoolType() {
        return SarpySchoolRegistry.COMBATART.get();
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float radius = 3.25f;
        float distance = 1.9f;
        Vec3 forward = entity.getForward();
        Vec3 hitLocation = entity.position().add(0, entity.getBbHeight() * .3f, 0).add(forward.scale(distance));
        var entities = level.getEntities(entity, AABB.ofSize(hitLocation, radius * 2, radius, radius * 2));
        var damageSource = this.getDamageSource(entity);
        for (Entity targetEntity : entities) {
            if (targetEntity instanceof LivingEntity && targetEntity.isAlive() && entity.isPickable() && targetEntity.position().subtract(entity.getEyePosition()).dot(forward) >= 0 && entity.distanceToSqr(targetEntity) < radius * radius && Utils.hasLineOfSight(level, entity.getEyePosition(), targetEntity.getBoundingBox().getCenter(), true)) {
                Vec3 offsetVector = targetEntity.getBoundingBox().getCenter().subtract(entity.getEyePosition());
                if (offsetVector.dot(forward) >= 0) {
                    if (DamageSources.applyDamage(targetEntity, getDamage(spellLevel, entity), damageSource)) {
                        MagicManager.spawnParticles(level, ParticleTypes.POOF, targetEntity.getX(), targetEntity.getY() + targetEntity.getBbHeight() * .5f, targetEntity.getZ(), 30, targetEntity.getBbWidth() * .5f, targetEntity.getBbHeight() * .5f, targetEntity.getBbWidth() * .5f, .03, false);
                        EnchantmentHelper.doPostDamageEffects(entity, targetEntity);
                    }
                }
            }
        }
        boolean mirrored = playerMagicData.getCastingEquipmentSlot().equals(SpellSelectionManager.OFFHAND);
        MagicManager.spawnParticles(level, new MeleeStrikeParticleOptions((float) forward.x, (float) forward.y, (float) forward.z, mirrored, true, 1f), hitLocation.x, hitLocation.y + .5, hitLocation.z, 1, 0, 0, 0, 0, true);


        var x = entity.getX();
        var y = entity.getY();
        var z = entity.getZ();


        MagicManager.spawnParticles(level, ParticleTypes.POOF, x, y, z, 30, 0, 0, 0, 1, false);
        MagicManager.spawnParticles(level, new BlastwaveParticleOptions(new Vector3f(.85f, .85f, .85f), 5), x, y - 0.8f, z, 1, 0, 0, 0, 0, true);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return getSpellPower(spellLevel, entity) + Utils.getWeaponDamage(entity, MobType.UNDEFINED);
    }

    @Override
    public void playSound(Optional<SoundEvent> sound, Entity entity) {
        sound.ifPresent((soundEvent -> entity.playSound(soundEvent, 3.0f, .9f + Utils.random.nextFloat() * .2f)));
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SarpSpellAnims.ANIMATION_LIONS_CLAW;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SarpSpellAnims.ANIMATION_LIONS_CLAW_END;
    }
}
