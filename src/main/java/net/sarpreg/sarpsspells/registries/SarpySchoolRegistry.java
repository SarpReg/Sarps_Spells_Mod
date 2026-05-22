package net.sarpreg.sarpsspells.registries;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import net.sarpreg.sarpsspells.SarpsSpellsMod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class SarpySchoolRegistry {
    public static final ResourceKey<Registry<SchoolType>> SCHOOL_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(SarpsSpellsMod.MODID, "sarpschools"));
    private static final DeferredRegister<SchoolType> SCHOOLS = DeferredRegister.create(SCHOOL_REGISTRY_KEY, IronsSpellbooks.MODID);
    public static final Supplier<IForgeRegistry<SchoolType>> REGISTRY = SCHOOLS.makeRegistry(() -> new RegistryBuilder<SchoolType>().disableSaving().disableOverrides());

    /**
     * Register registry objects
     */
    public static void register(IEventBus eventBus) {
        SCHOOLS.register(eventBus);
    }

//    public static void registerRegistry(NewRegistryEvent event) {
//        IronsSpellbooks.LOGGER.debug("SchoolRegistry.registerRegistry");
//        event.register(REGISTRY);
//    }

    private static RegistryObject<SchoolType> registerSchool(SchoolType schoolType) {
        return SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
    }

    public static SchoolType getSchool(ResourceLocation resourceLocation) {
        return REGISTRY.get().getValue(resourceLocation);
    }

    public static final ResourceLocation SCULK_RESOURCE = SarpsSpellsMod.id("sculk");

    public static final RegistryObject<SchoolType> SCULK = registerSchool(new SchoolType(
            SCULK_RESOURCE,
            ModTags.ELDRITCH_FOCUS,
            Component.translatable("school.sarps_spells.sculk").withStyle(Style.EMPTY.withColor(0x15646f)),
            SarpttributeRegistry.SCULK_SPELL_POWER,
            SarpttributeRegistry.SCULK_MAGIC_RESIST,
            SoundRegistry.EVOCATION_CAST,
            ISSDamageTypes.ELDRITCH_MAGIC
    ));

    @Nullable
    public static SchoolType getSchoolFromFocus(ItemStack focusStack) {
        for (SchoolType school : REGISTRY.get()) {
            if (school.isFocus(focusStack)) {
                return school;
            }
        }
        return null;
    }

    public static List<SchoolType> getSchoolsFromFocus(ItemStack focusStack) {
        return REGISTRY.get().getValues().stream().filter(school -> school.isFocus(focusStack)).toList();
    }
}
