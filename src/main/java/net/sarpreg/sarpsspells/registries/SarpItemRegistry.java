package net.sarpreg.sarpsspells.registries;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.block.statue.tyros_statue.TyrosStatueBlockItem;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.*;
import io.redspace.ironsspellbooks.item.armor.*;
import io.redspace.ironsspellbooks.item.consumables.FireAleItem;
import io.redspace.ironsspellbooks.item.consumables.NetherwardTinctureItem;
import io.redspace.ironsspellbooks.item.consumables.SimpleElixir;
import io.redspace.ironsspellbooks.item.curios.*;
import io.redspace.ironsspellbooks.item.weapons.*;
import io.redspace.ironsspellbooks.item.weapons.pyrium_staff.PyriumStaffItem;
import io.redspace.ironsspellbooks.registries.*;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.render.CinderousRarity;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sarpreg.sarpsspells.SarpsSpellsMod;
import net.sarpreg.sarpsspells.item.weapons.SanguineTridentItem;
import net.sarpreg.sarpsspells.item.weapons.SarpExtendedWeaponTier;

import java.util.Collection;
import java.util.Optional;

public class SarpItemRegistry {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, SarpsSpellsMod.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    //public static final RegistryObject<Item> SPELL_BOOK = ITEMS.register("spell_book", SpellBook::new);
    /**
     * Spell items
     */

    public static final RegistryObject<Item> SANGUINE_TRIDENT = ITEMS.register("sanguine_halberd", () -> new SanguineTridentItem(SarpExtendedWeaponTier.SANGUINE_TRIDENT, ItemPropertiesHelper.equipment().rarity(Rarity.EPIC)/*.attributes(ExtendedSwordItem.createAttributes(ExtendedWeaponTier.SPELLBREAKER))*/, SpellDataRegistryHolder.of(new SpellDataRegistryHolder((RegistryObject<AbstractSpell>) SarpySpellRegistry.NIHIL_COMBAT_ART, 1))));


    public static Collection<RegistryObject<Item>> getSarpsItems() {
        return ITEMS.getEntries();
    }
}
