package net.sarpreg.sarpsspells.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sarpreg.sarpsspells.SarpsSpellsMod;
import net.sarpreg.sarpsspells.client.ModModelLayers;
import net.sarpreg.sarpsspells.entity.spells.sculk_tendril.SculkTendrilModel;

@Mod.EventBusSubscriber(modid = SarpsSpellsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.SCULK_TENDRIL_LAYER, SculkTendrilModel::createBodyLayer);
    }
}
