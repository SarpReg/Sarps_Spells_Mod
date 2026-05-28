package net.sarpreg.sarpsspells.util;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import net.minecraft.resources.ResourceLocation;
import net.sarpreg.sarpsspells.SarpsSpellsMod;

public class SarpSpellAnims {
    public static ResourceLocation ANIMATION_RESOURCE = ResourceLocation.fromNamespaceAndPath(SarpsSpellsMod.MODID, "animation");

    public static final AnimationHolder ANIMATION_LIONS_CLAW = new AnimationHolder(SarpsSpellsMod.id("lionsclaw"), true);
    public static final AnimationHolder ANIMATION_LIONS_CLAW_END = new AnimationHolder(SarpsSpellsMod.id("lionsclaw_end"), true);


}
