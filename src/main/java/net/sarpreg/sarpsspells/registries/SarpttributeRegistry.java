package net.sarpreg.sarpsspells.registries;

import io.redspace.ironsspellbooks.api.attribute.MagicPercentAttribute;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sarpreg.sarpsspells.SarpsSpellsMod;


@Mod.EventBusSubscriber(modid = SarpsSpellsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SarpttributeRegistry {

    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, SarpsSpellsMod.MODID);

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    public static final RegistryObject<Attribute> SCULK_MAGIC_RESIST = newResistanceAttribute("sculk");
    public static final RegistryObject<Attribute> COMBATART_MAGIC_RESIST = newResistanceAttribute("combatart");

    public static final RegistryObject<Attribute> SCULK_SPELL_POWER = newPowerAttribute("sculk");
    public static final RegistryObject<Attribute> COMBATART_SPELL_POWER = newPowerAttribute("combatart");

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(entity -> ATTRIBUTES.getEntries().forEach(attribute -> e.add(entity, attribute.get())));
    }

    private static RegistryObject<Attribute> newResistanceAttribute(String id) {
        return (RegistryObject<Attribute>) ATTRIBUTES.register(id + "_magic_resist", () -> (new MagicPercentAttribute("attribute.sarps_spells." + id + "_magic_resist", 1.0D, -100, 100).setSyncable(true)));
    }

    private static RegistryObject<Attribute> newPowerAttribute(String id) {
        return ATTRIBUTES.register(id + "_spell_power", () -> (new MagicPercentAttribute("attribute.sarps_spells." + id + "_spell_power", 1.0D, -100, 100).setSyncable(true)));
    }
}
