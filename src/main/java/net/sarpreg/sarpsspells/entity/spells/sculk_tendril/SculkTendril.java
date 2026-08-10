package net.sarpreg.sarpsspells.entity.spells.sculk_tendril;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sarpreg.sarpsspells.registries.EntityRegistry;
import net.sarpreg.sarpsspells.util.SarparticleHelper;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.UUID;

public class SculkTendril extends LivingEntity implements GeoEntity, AntiMagicSusceptible{

    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    private float damage;
    private int age;

    public final AnimationState flailAnimationState = new AnimationState();
    public final AnimationState emergeAnimationState = new AnimationState();
    public final AnimationState submergeAnimationState = new AnimationState();

    public SculkTendril(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SculkTendril(Level level, LivingEntity owner, float damage) {
        this(EntityRegistry.SCULK_TENDRIL.get(), level);
        setOwner(owner);
        setDamage(damage);
        this.setCustomNameVisible(false);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setOwner(@Nullable LivingEntity pOwner) {
        this.owner = pOwner;
        this.ownerUUID = pOwner == null ? null : pOwner.getUUID();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.KNOCKBACK_RESISTANCE, 1f)
                .add(Attributes.MAX_HEALTH, 60);
    }

    @Override
    public void tick() {
        if (!level().isClientSide) {
            if (age > 300) {
                //IronsSpellbooks.LOGGER.debug("Discarding void Tentacle (age:{})", age);
                this.discard();
            } else {
                if (age < 280 && (age) % 20 == 0) {
                    level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.5)).forEach(this::dealDamage);
                    if (Utils.random.nextFloat() < .15f) {
                        playSound(SoundRegistry.VOID_TENTACLES_AMBIENT.get(), 1.5f, .5f + Utils.random.nextFloat() * .65f);
                    }
                }
            }
            if (age == 260 && Utils.random.nextFloat() < .3f) {
                playSound(SoundRegistry.VOID_TENTACLES_LEAVE.get(), 2, 1);
            }
        } else {
            if (age < 280) {
                if (Utils.random.nextFloat() < .15f) {
                    level().addParticle(SarparticleHelper.SCULK_TENTACLE_FOG, getX() + Utils.getRandomScaled(.5f), getY() + Utils.getRandomScaled(.5f) + .2f, getZ() + Utils.getRandomScaled(.5f), Utils.getRandomScaled(2f), -random.nextFloat() * .5f, Utils.getRandomScaled(2f));
                }
            }
        }
        age++;
    }

    public boolean dealDamage(LivingEntity target) {
        if (target != getOwner())
            if (DamageSources.applyDamage(target, damage, SpellRegistry.SCULK_TENTACLES_SPELL.get().getDamageSource(this, getOwner()))) {
                target.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20));
                return true;
            }
        return false;
    }




    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.level()).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
            }
        }

        return this.owner;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {

    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return false;
        return super.hurt(pSource, pAmount);
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        MagicManager.spawnParticles(level(), ParticleTypes.SMOKE, getX(), getY() + 1, getZ(), 50, .2, 1.25, .2, .08, false);
        this.discard();
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
    }
}
