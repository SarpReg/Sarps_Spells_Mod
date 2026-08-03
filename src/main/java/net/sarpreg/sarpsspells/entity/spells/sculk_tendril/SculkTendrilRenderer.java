package net.sarpreg.sarpsspells.entity.spells.sculk_tendril;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sarpreg.sarpsspells.SarpsSpellsMod;
import net.sarpreg.sarpsspells.client.ModModelLayers;

public class SculkTendrilRenderer extends LivingEntityRenderer<SculkTendril, SculkTendrilModel<SculkTendril>> {
    public SculkTendrilRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SculkTendrilModel<>(pContext.bakeLayer(ModModelLayers.SCULK_TENDRIL_LAYER)), 0.2f);
    }

    @Override
    public ResourceLocation getTextureLocation(SculkTendril pEntity) {
        return ResourceLocation.fromNamespaceAndPath(SarpsSpellsMod.MODID, "textures/entity/sculk_tendril/sculk_tendril.png");
    }

    @Override
    public void render(SculkTendril pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight) {


        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
