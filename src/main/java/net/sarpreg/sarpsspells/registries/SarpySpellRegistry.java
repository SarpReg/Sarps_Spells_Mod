package net.sarpreg.sarpsspells.registries;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.sarpreg.sarpsspells.SarpsSpellsMod;
import net.sarpreg.sarpsspells.spells.ender.LevitateSpell;
import net.sarpreg.sarpsspells.spells.evocation.UpdraftSpell;
import net.sarpreg.sarpsspells.spells.holy.OldHolyDaggerRainSpell;
import net.sarpreg.sarpsspells.spells.holy.SunlightLanceSpell;
import net.sarpreg.sarpsspells.spells.ice.SnowgraveSpell;
import net.sarpreg.sarpsspells.spells.lightning.LaserEyesSpell;
import net.sarpreg.sarpsspells.spells.melee.basic.LionsClawCombatArt;

import java.util.function.Supplier;

public class SarpySpellRegistry {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, SarpsSpellsMod.MODID);

    public static Supplier<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    // SPELL REGISTRY BELOW

    public static final Supplier<AbstractSpell> UPDRAFT_SPELL = registerSpell(new UpdraftSpell());
    public static final Supplier<AbstractSpell> LEVITATE_SPELL = registerSpell(new LevitateSpell());
    //public static final Supplier<AbstractSpell> EMERGING_TENTACLES_SPELL = registerSpell(new EmergingTentaclesSpell());
    public static final Supplier<AbstractSpell> SUNLIGHT_LANCE_SPELL = registerSpell(new SunlightLanceSpell());
    public static final Supplier<AbstractSpell> HOLY_DAGGER_RAIN_SPELL = registerSpell(new OldHolyDaggerRainSpell());
    public static final Supplier<AbstractSpell> LASER_EYES_SPELL = registerSpell(new LaserEyesSpell());
    public static final Supplier<AbstractSpell> SNOWGRAVE_SPELL = registerSpell(new SnowgraveSpell());

    // ASH OF WAR REGISTRY BELOW

    public static final Supplier<AbstractSpell> LIONS_CLAW_COMBAT_ART = registerSpell(new LionsClawCombatArt());
}
