package net.sarpreg.sarpsspells.registries;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageTypes;
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
    public static final ResourceKey<Registry<SchoolType>> SCHOOL_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(SarpsSpellsMod.MODID, "schools"));
    private static final DeferredRegister<SchoolType> SCHOOLS = DeferredRegister.create(SCHOOL_REGISTRY_KEY, IronsSpellbooks.MODID);
    public static final Supplier<IForgeRegistry<SchoolType>> REGISTRY = SCHOOLS.makeRegistry(() -> new RegistryBuilder<SchoolType>().disableSaving().disableOverrides());

    /**
     * Register registry objects
     */
    public static void register(IEventBus eventBus) {
        SCHOOLS.register(eventBus);
    }

    private static RegistryObject<SchoolType> registerSchool(SchoolType schoolType) {
        return SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
    }

    public static SchoolType getSchool(ResourceLocation resourceLocation) {
        return REGISTRY.get().getValue(resourceLocation);
    }

    public static final ResourceLocation SCULK_RESOURCE = IronsSpellbooks.id("sculk");
    public static final ResourceLocation COMBATART_RESOURCE = IronsSpellbooks.id("combatart");
    public static final ResourceLocation FIRE_COMBATART_RESOURCE = IronsSpellbooks.id("fire_combatart");
    public static final ResourceLocation ICE_COMBATART_RESOURCE = IronsSpellbooks.id("ice_combatart");
    public static final ResourceLocation LIGHTNING_COMBATART_RESOURCE = IronsSpellbooks.id("lightning_combatart");
    public static final ResourceLocation HOLY_COMBATART_RESOURCE = IronsSpellbooks.id("holy_combatart");
    public static final ResourceLocation ENDER_COMBATART_RESOURCE = IronsSpellbooks.id("ender_combatart");
    public static final ResourceLocation BLOOD_COMBATART_RESOURCE = IronsSpellbooks.id("blood_combatart");
    public static final ResourceLocation EVOCATION_COMBATART_RESOURCE = IronsSpellbooks.id("evocation_combatart");
    public static final ResourceLocation NATURE_COMBATART_RESOURCE = IronsSpellbooks.id("nature_combatart");
    public static final ResourceLocation ELDRITCH_COMBATART_RESOURCE = IronsSpellbooks.id("eldritch_combatart");



    public static final RegistryObject<SchoolType> SCULK = registerSchool(new SchoolType(
            SCULK_RESOURCE,
            ModTags.ELDRITCH_FOCUS,
            Component.translatable("school.sarps_spells.sculk").withStyle(Style.EMPTY.withColor(0x15646f)),
            SarpttributeRegistry.SCULK_SPELL_POWER,
            SarpttributeRegistry.SCULK_MAGIC_RESIST,
            SoundRegistry.EVOCATION_CAST,
            ISSDamageTypes.ELDRITCH_MAGIC
    ));

    public static final RegistryObject<SchoolType> COMBATART = registerSchool(new SchoolType(
            COMBATART_RESOURCE,
            ItemTags.SWORDS,
            Component.translatable("school.sarps_spells.combatart").withStyle(Style.EMPTY.withColor(0x858585)),
            SarpttributeRegistry.COMBATART_SPELL_POWER,
            SarpttributeRegistry.COMBATART_MAGIC_RESIST,
            SoundRegistry.SOULCALLER_TOLL_FAILURE,
            DamageTypes.PLAYER_ATTACK
    ));

    public static final RegistryObject<SchoolType> FIRE_COMBATART = registerSchool(new SchoolType(
            FIRE_COMBATART_RESOURCE,
            ModTags.FIRE_FOCUS,
            Component.translatable("school.sarps_spells.fire_combatart").withStyle(ChatFormatting.GOLD),
            AttributeRegistry.FIRE_SPELL_POWER,
            AttributeRegistry.FIRE_MAGIC_RESIST,
            SoundRegistry.FIRE_CAST,
            ISSDamageTypes.FIRE_MAGIC
    ));

    public static final RegistryObject<SchoolType> ICE_COMBATART = registerSchool(new SchoolType(
            ICE_COMBATART_RESOURCE,
            ModTags.ICE_FOCUS,
            Component.translatable("school.sarps_spells.ice_combatart").withStyle(Style.EMPTY.withColor(0xd0f9ff)),
            AttributeRegistry.ICE_SPELL_POWER,
            AttributeRegistry.ICE_MAGIC_RESIST,
            SoundRegistry.ICE_CAST,
            ISSDamageTypes.ICE_MAGIC
    ));

    public static final RegistryObject<SchoolType> LIGHTNING_COMBATART = registerSchool(new SchoolType(
            LIGHTNING_COMBATART_RESOURCE,
            ModTags.LIGHTNING_FOCUS,
            Component.translatable("school.sarps_spells.lightning_combatart").withStyle(ChatFormatting.AQUA),
            AttributeRegistry.LIGHTNING_SPELL_POWER,
            AttributeRegistry.LIGHTNING_MAGIC_RESIST,
            SoundRegistry.LIGHTNING_CAST,
            ISSDamageTypes.LIGHTNING_MAGIC
    ));

    public static final RegistryObject<SchoolType> HOLY_COMBATART = registerSchool(new SchoolType(
            HOLY_COMBATART_RESOURCE,
            ModTags.HOLY_FOCUS,
            Component.translatable("school.sarps_spells.holy_combatart").withStyle(Style.EMPTY.withColor(0xfff8d4)),
            AttributeRegistry.HOLY_SPELL_POWER,
            AttributeRegistry.HOLY_MAGIC_RESIST,
            SoundRegistry.HOLY_CAST,
            ISSDamageTypes.HOLY_MAGIC
    ));

    public static final RegistryObject<SchoolType> ENDER_COMBATART = registerSchool(new SchoolType(
            ENDER_COMBATART_RESOURCE,
            ModTags.ENDER_FOCUS,
            Component.translatable("school.sarps_spells.ender_combatart").withStyle(ChatFormatting.LIGHT_PURPLE),
            AttributeRegistry.ENDER_SPELL_POWER,
            AttributeRegistry.ENDER_MAGIC_RESIST,
            SoundRegistry.ENDER_CAST,
            ISSDamageTypes.ENDER_MAGIC
    ));

    public static final RegistryObject<SchoolType> BLOOD_COMBATART = registerSchool(new SchoolType(
            BLOOD_COMBATART_RESOURCE,
            ModTags.BLOOD_FOCUS,
            Component.translatable("school.sarps_spells.blood_combatart").withStyle(ChatFormatting.DARK_RED),
            AttributeRegistry.BLOOD_SPELL_POWER,
            AttributeRegistry.BLOOD_MAGIC_RESIST,
            SoundRegistry.BLOOD_CAST,
            ISSDamageTypes.BLOOD_MAGIC));

    public static final RegistryObject<SchoolType> EVOCATION_COMBATART = registerSchool(new SchoolType(
            EVOCATION_COMBATART_RESOURCE,
            ModTags.EVOCATION_FOCUS,
            Component.translatable("school.sarps_spells.evocation_combatart").withStyle(ChatFormatting.WHITE),
            AttributeRegistry.EVOCATION_SPELL_POWER,
            AttributeRegistry.EVOCATION_MAGIC_RESIST,
            SoundRegistry.EVOCATION_CAST,
            ISSDamageTypes.EVOCATION_MAGIC
    ));

    public static final RegistryObject<SchoolType> NATURE_COMBATART = registerSchool(new SchoolType(
            NATURE_COMBATART_RESOURCE,
            ModTags.NATURE_FOCUS,
            Component.translatable("school.irons_spellbooks.nature").withStyle(ChatFormatting.GREEN),
            AttributeRegistry.NATURE_SPELL_POWER,
            AttributeRegistry.NATURE_MAGIC_RESIST,
            SoundRegistry.NATURE_CAST,
            ISSDamageTypes.NATURE_MAGIC
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
