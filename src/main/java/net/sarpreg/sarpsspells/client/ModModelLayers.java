package net.sarpreg.sarpsspells.client;

import com.ibm.icu.text.Normalizer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.sarpreg.sarpsspells.SarpsSpellsMod;

public class ModModelLayers {
    public static ModelLayerLocation SCULK_TENDRIL_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(SarpsSpellsMod.MODID, "sculk_tendril_layer"), "main");
}
