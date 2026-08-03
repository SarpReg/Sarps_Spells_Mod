package net.sarpreg.sarpsspells.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sarpreg.sarpsspells.SarpsSpellsMod;
import net.sarpreg.sarpsspells.client.ModModelLayers;
import net.sarpreg.sarpsspells.entity.spells.sculk_tendril.SculkTendril;
import net.sarpreg.sarpsspells.entity.spells.sculk_tendril.SculkTendrilModel;
import net.sarpreg.sarpsspells.registries.EntityRegistry;

@Mod.EventBusSubscriber(modid = SarpsSpellsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.SCULK_TENDRIL.get(), SculkTendril.createAttributes().build());
    }
}
