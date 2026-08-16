package net.sarpreg.sarpsspells.spells.sculk;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.*;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.ray_of_frost.RayOfFrostVisualEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.EffectCommands;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.sarpreg.sarpsspells.SarpsSpellsMod;
import net.sarpreg.sarpsspells.registries.SarpySchoolRegistry;

import java.util.List;
import java.util.Optional;

public class ShriekSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(SarpsSpellsMod.MODID, "shriek");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(SarpySchoolRegistry.SCULK_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(25)
            .build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.effect_length", Utils.stringTruncation(getEffectLength(spellLevel, caster), 2)),
                Component.translatable("ui.irons_spellbooks.distance", Utils.stringTruncation(getRange(spellLevel, caster), 1))
        );
    }

    public ShriekSpell() {
        this.manaCostPerLevel = 13;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 2;
        this.castTime = 0;
        this.baseManaCost = 60;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public SchoolType getSchoolType() {
        return SarpySchoolRegistry.SCULK.get();
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.SCULK_CLICKING);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.SCULK_SHRIEKER_SHRIEK);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        CameraShakeManager.addCameraShake(new CameraShakeData(level, 20, entity.position(), 20));
        var range = getRange(spellLevel, entity);
        Vec3 start = entity.getEyePosition();
        Vec3 end = start.add(entity.getForward().scale(range));
        AABB boundingBox = entity.getBoundingBox().expandTowards(end.subtract(start));

        List<? extends Entity> entities = level.getEntities(entity, boundingBox);
        for (Entity target : entities) {
            HitResult hit = Utils.checkEntityIntersecting(target, start, end, .4f);
            if (hit.getType() != HitResult.Type.MISS && target instanceof LivingEntity) {
                LivingEntity target2 = (LivingEntity) target;
                target2.addEffect(new MobEffectInstance(MobEffects.DARKNESS, getEffectLength(spellLevel, entity) * 20, 1, false, false, true));
            }
        }
        Vec3 vec3 = entity.getLookAngle().normalize();
        for (int i = 0; i < range; i++) {
            var vec32 = vec3.scale(i).add(entity.getEyePosition());
            MagicManager.spawnParticles(level, ParticleTypes.SCULK_CHARGE_POP, vec32.x, vec32.y, vec32.z, 1, 0, 0, 0, 0, false);
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void playSound(Optional<SoundEvent> sound, Entity entity) {
        if (sound == getCastFinishSound()) {
            entity.playSound(sound.get(), 3.5f, .9f + entity.level().random.nextFloat() * .2f);
        } else {
            super.playSound(sound, entity);
        }

    }

    public static float getRange(int level, LivingEntity caster) {
        return 15 + level;
    }

    private int getEffectLength(int spellLevel, LivingEntity caster) {
        return (int) getSpellPower(spellLevel, caster);
    }

}
