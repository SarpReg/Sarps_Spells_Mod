package net.sarpreg.sarpsspells.spells.holy;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.RaycastBuilder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.spells.fiery_dagger.FieryDaggerEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.sarpreg.sarpsspells.SarpsSpellsMod;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class OldHolyDaggerRainSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(SarpsSpellsMod.MODID, "holy_dagger_rain");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getSpellPower(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.radius", "5")
                //Component.translatable("ui.sarps_spells.meleespell")
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(13)
            .build();

    public OldHolyDaggerRainSpell() {
        this.manaCostPerLevel = 30;
        this.baseSpellPower = 7;
        this.spellPowerPerLevel = 1;
        this.castTime = 30;
        this.baseManaCost = 150;
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
        return Optional.of(SoundEvents.SHULKER_SHOOT);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, .35f);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {

        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData castTargetingData) {
            LivingEntity target = castTargetingData.getTarget((ServerLevel) level);

            if (target != null) {
                entity.playSound(SoundRegistry.FIERY_DAGGER_THROW.get(), 2f, Utils.random.nextIntBetweenInclusive(80, 110) * .01f);

                Vec3 start = entity.getEyePosition();
                Vec3 targetPos = target.position();
                Vec3 deltaAim = targetPos.subtract(start);
                // throw 3 daggers at 45 degree angles, centered around our target's postion
                Vec3 aim = start.add(deltaAim.yRot(0));
                int delay = Utils.random.nextIntBetweenInclusive(10, 40);

                FieryDaggerEntity dagger = new FieryDaggerEntity(level);
                dagger.setOwner(entity);
                dagger.setPos(start);
                dagger.delay = delay;
                dagger.setDamage((float) (entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * .75));
                dagger.setExplosionRadius(4 + Utils.random.nextFloat() * 2);
                dagger.setNoGravity(false);

                Vec3 horizontal = aim.subtract(start).multiply(1, 0, 1);
                double horizontalSpeed = 1 * Mth.cos(Mth.PI * .25f) + 0.5; // + 0.5 for extra oomph
                double distance = horizontal.length();
                double ticks = distance / horizontalSpeed;

                // y(t) = -1/2(g)(t^2) + v0*t
                // => v0 = [y1 + 1/2(g)(t1^2)]/t1
                double y1 = aim.y - start.y;
                double g = 0.05;//dagger.getGravity();
                double verticalSpeed = (y1 + 0.5 * g * ticks * ticks) / ticks;
                Vec3 trajectory = horizontal.normalize().scale(horizontalSpeed).add(0, verticalSpeed, 0);
                dagger.setDeltaMovement(trajectory);
                level.addFreshEntity(dagger);
            }
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Nullable
    private LivingEntity findTarget(LivingEntity caster) {
        var target = RaycastBuilder.begin(caster.level(), caster)
                .range(32)
                .checkForBlocks(true)
                .bbInflation(0.35f)
                .build();
        if (target instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity livingTarget) {
            return livingTarget;
        } else {
            return null;
        }
    }

    public int getDuration(int spellLevel, LivingEntity caster) {
        return 30;
    }
}
