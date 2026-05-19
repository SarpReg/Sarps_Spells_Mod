package net.sarpreg.sarpsspells.registries;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sarpreg.sarpsspells.SarpsSpellsMod;
import net.sarpreg.sarpsspells.effect.UpdraftEffect;

public class SarpMobEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, SarpsSpellsMod.MODID);

    public static void register(IEventBus eventBus) {
        MOB_EFFECT_DEFERRED_REGISTER.register(eventBus);
    }
    public static final RegistryObject<MobEffect> UPDRAFT = MOB_EFFECT_DEFERRED_REGISTER.register("updraft", () -> new UpdraftEffect(MobEffectCategory.BENEFICIAL, 0xecf0ef));
}
