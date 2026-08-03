package net.sarpreg.sarpsspells.entity.spells.sculk_tendril;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

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

    public SculkTendril(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SculkTendril(Level level, LivingEntity owner, float damage) {
        this(EntityRegistry.SCULK_TENTACLE.get(), level);
        setOwner(owner);
        setDamage(damage);
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
                .add(Attributes.MAX_HEALTH, 60);
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

    @Override
    public void onAntiMagic(MagicData playerMagicData) {

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
