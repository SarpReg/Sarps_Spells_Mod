package net.sarpreg.sarpsspells.registries;

import io.redspace.ironsspellbooks.entity.mobs.*;
import io.redspace.ironsspellbooks.entity.spells.*;
import io.redspace.ironsspellbooks.entity.spells.lightning_lance.LightningLanceProjectile;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sarpreg.sarpsspells.SarpsSpellsMod;
import net.sarpreg.sarpsspells.entity.spells.sunlight_lance.SunlightLanceProjectile;


public class EntityRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, SarpsSpellsMod.MODID);

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    public static final RegistryObject<EntityType<SunlightLanceProjectile>> SUNLIGHT_LANCE_PROJECTILE =
            ENTITIES.register("sunlight_lance", () -> EntityType.Builder.<SunlightLanceProjectile>of(SunlightLanceProjectile::new, MobCategory.MISC)
                    .sized(1.25f, 1.25f)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(SarpsSpellsMod.MODID, "sunlight_lance").toString()));
}

