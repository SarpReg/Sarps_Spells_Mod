package net.sarpreg.sarpsspells.registries;

import com.mojang.serialization.Codec;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.particle.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sarpreg.sarpsspells.SarpsSpellsMod;
import net.sarpreg.sarpsspells.particle.MeleeStrikeParticleOptions;

import java.util.function.Supplier;


public class SarparticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, SarpsSpellsMod.MODID);

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }

    /*
    To Create Particle:
    - textures + json
    - particle class
    - register it here
    - add it to particle helper
    - register it in client setup
     */

    public static final Supplier<SimpleParticleType> SUNSPARK_PARTICLE = PARTICLE_TYPES.register("sunspark", () -> new SimpleParticleType(false));
    public static final Supplier<ParticleType<MeleeStrikeParticleOptions>> MELEE_STRIKE_PARTICLE = PARTICLE_TYPES.register("melee_strike", () -> new ParticleType<>(true, MeleeStrikeParticleOptions.DESERIALIZER) {
        public Codec<MeleeStrikeParticleOptions> codec() {
            return MeleeStrikeParticleOptions.CODEC;
        }
    });

}
