package net.sarpreg.sarpsspells.spells.melee.evocation;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.RaycastBuilder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.shield.ShieldEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sarpreg.sarpsspells.SarpsSpellsMod;
import net.sarpreg.sarpsspells.util.AbstractAOW;

import java.util.List;
import java.util.Optional;

public class MovingShieldSpell extends AbstractAOW {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(SarpsSpellsMod.MODID, "moving_shield");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.hp", Utils.stringTruncation(getShieldHP(spellLevel, caster), 1))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(8)
            .build();

    public MovingShieldSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 10;
        this.baseManaCost = 35;
        this.castTime = 0;
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
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.empty();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.ILLUSIONER_CAST_SPELL);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        ShieldEntity shield = new ShieldEntity(level, getShieldHP(spellLevel, entity));
        Vec3 spawn = RaycastBuilder.begin(level, entity)
                .range(3)
                .checkForBlocks(true)
                .build()
                .getLocation();
        shield.setPos(spawn);
        shield.setRotation(entity.getXRot(), entity.getYRot());
        level.addFreshEntity(shield);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getShieldHP(int spellLevel, LivingEntity caster) {
        return 10 + getSpellPower(spellLevel, caster);
    }

    //    @Override
//    public MutableComponent getUniqueInfo() {
//        return Component.translatable("ui.irons_spellbooks.distance", Utils.stringTruncation(getDistance(null), 1));
//    }
}
