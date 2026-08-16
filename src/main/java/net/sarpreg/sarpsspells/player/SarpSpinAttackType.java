package net.sarpreg.sarpsspells.player;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.resources.ResourceLocation;

public record SarpSpinAttackType(ResourceLocation textureId, boolean fullbright){
    public static final SarpSpinAttackType SCULK = new SarpSpinAttackType(ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "textures/entity/sculk_riptide.png"), true);
}
