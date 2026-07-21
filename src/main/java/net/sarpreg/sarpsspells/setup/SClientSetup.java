package net.sarpreg.sarpsspells.setup;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.backwards_compat.ClothingVariantHelper;
import io.redspace.ironsspellbooks.api.backwards_compat.blocks.trial_spawner.TrialSpawnerRenderer;
import io.redspace.ironsspellbooks.api.backwards_compat.blocks.vault.VaultRenderer;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronRenderer;
import io.redspace.ironsspellbooks.block.pedestal.PedestalRenderer;
import io.redspace.ironsspellbooks.block.portal_frame.PortalFrameRenderer;
import io.redspace.ironsspellbooks.block.scroll_forge.ScrollForgeRenderer;
import io.redspace.ironsspellbooks.block.statue.tyros_statue.TyrosStatueBlockRenderer;
import io.redspace.ironsspellbooks.effect.PlanarSightEffect;
import io.redspace.ironsspellbooks.entity.VisualFallingBlockRenderer;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingRenderer;
import io.redspace.ironsspellbooks.entity.mobs.debug_wizard.DebugWizardRenderer;
import io.redspace.ironsspellbooks.entity.mobs.frozen_humanoid.FrozenHumanoidRenderer;
import io.redspace.ironsspellbooks.entity.mobs.horse.SpectralSteedRenderer;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.IceSpiderRenderer;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperRenderer;
import io.redspace.ironsspellbooks.entity.mobs.necromancer.NecromancerRenderer;
import io.redspace.ironsspellbooks.entity.mobs.raise_dead_summons.SummonedSkeletonMultiRenderer;
import io.redspace.ironsspellbooks.entity.mobs.raise_dead_summons.SummonedZombieMultiRenderer;
import io.redspace.ironsspellbooks.entity.mobs.wizards.alchemist.ApothecaristRenderer;
import io.redspace.ironsspellbooks.entity.mobs.wizards.archevoker.ArchevokerRenderer;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cryomancer.CryomancerRenderer;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cultist.CultistRenderer;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cursed_armor_stand.CursedArmorStandRenderer;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossRenderer;
import io.redspace.ironsspellbooks.entity.mobs.wizards.priest.PriestRenderer;
import io.redspace.ironsspellbooks.entity.mobs.wizards.pyromancer.PyromancerRenderer;
import io.redspace.ironsspellbooks.entity.spells.acid_orb.AcidOrbRenderer;
import io.redspace.ironsspellbooks.entity.spells.ball_lightning.BallLightningRenderer;
import io.redspace.ironsspellbooks.entity.spells.black_hole.BlackHoleRenderer;
import io.redspace.ironsspellbooks.entity.spells.blood_needle.BloodNeedleRenderer;
import io.redspace.ironsspellbooks.entity.spells.blood_slash.BloodSlashRenderer;
import io.redspace.ironsspellbooks.entity.spells.comet.CometRenderer;
import io.redspace.ironsspellbooks.entity.spells.devour_jaw.DevourJawRenderer;
import io.redspace.ironsspellbooks.entity.spells.eldritch_blast.EldritchBlastRenderer;
import io.redspace.ironsspellbooks.entity.spells.electrocute.ElectrocuteRenderer;
import io.redspace.ironsspellbooks.entity.spells.fiery_dagger.FieryDaggerRenderer;
import io.redspace.ironsspellbooks.entity.spells.fire_arrow.FireArrowRenderer;
import io.redspace.ironsspellbooks.entity.spells.fireball.FireballRenderer;
import io.redspace.ironsspellbooks.entity.spells.firebolt.FireboltRenderer;
import io.redspace.ironsspellbooks.entity.spells.guiding_bolt.GuidingBoltRenderer;
import io.redspace.ironsspellbooks.entity.spells.gust.GustRenderer;
import io.redspace.ironsspellbooks.entity.spells.ice_block.IceBlockRenderer;
import io.redspace.ironsspellbooks.entity.spells.ice_spike.IceSpikeRenderer;
import io.redspace.ironsspellbooks.entity.spells.ice_tomb.IceTombRenderer;
import io.redspace.ironsspellbooks.entity.spells.icicle.IcicleRenderer;
import io.redspace.ironsspellbooks.entity.spells.lightning_lance.LightningLanceRenderer;
import io.redspace.ironsspellbooks.entity.spells.magic_arrow.MagicArrowRenderer;
import io.redspace.ironsspellbooks.entity.spells.magic_missile.MagicMissileRenderer;
import io.redspace.ironsspellbooks.entity.spells.magma_ball.MagmaBallRenderer;
import io.redspace.ironsspellbooks.entity.spells.poison_arrow.PoisonArrowRenderer;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalRenderer;
import io.redspace.ironsspellbooks.entity.spells.ray_of_frost.RayOfFrostRenderer;
import io.redspace.ironsspellbooks.entity.spells.root.RootRenderer;
import io.redspace.ironsspellbooks.entity.spells.shield.ShieldModel;
import io.redspace.ironsspellbooks.entity.spells.shield.ShieldRenderer;
import io.redspace.ironsspellbooks.entity.spells.shield.ShieldTrimModel;
import io.redspace.ironsspellbooks.entity.spells.skull_projectile.SkullProjectileRenderer;
import io.redspace.ironsspellbooks.entity.spells.small_magic_arrow.SmallMagicArrowRenderer;
import io.redspace.ironsspellbooks.entity.spells.snowball.SnowballRenderer;
import io.redspace.ironsspellbooks.entity.spells.spectral_hammer.SpectralHammerRenderer;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedClaymoreModel;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedRapierModel;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedSwordModel;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedSwordRenderer;
import io.redspace.ironsspellbooks.entity.spells.sunbeam.SunbeamRenderer;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetAreaRenderer;
import io.redspace.ironsspellbooks.entity.spells.thrown_item.ThrownItemRenderer;
import io.redspace.ironsspellbooks.entity.spells.thrown_spear.ThrownSpearRenderer;
import io.redspace.ironsspellbooks.entity.spells.thunderstep.ThunderstepProjectileRenderer;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacleRenderer;
import io.redspace.ironsspellbooks.entity.spells.wisp.WispRenderer;
import io.redspace.ironsspellbooks.gui.arcane_anvil.ArcaneAnvilScreen;
import io.redspace.ironsspellbooks.gui.inscription_table.InscriptionTableScreen;
import io.redspace.ironsspellbooks.gui.scroll_forge.ScrollForgeScreen;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.WaywardCompass;
import io.redspace.ironsspellbooks.item.weapons.AutoloaderCrossbow;
import io.redspace.ironsspellbooks.item.weapons.pyrium_staff.PyriumStaffHeadModel;
import io.redspace.ironsspellbooks.item.weapons.pyrium_staff.PyriumStaffOrbModel;
import io.redspace.ironsspellbooks.particle.*;
import io.redspace.ironsspellbooks.registries.*;
import io.redspace.ironsspellbooks.render.*;
import io.redspace.ironsspellbooks.render.animation.AnimationHelper;
import io.redspace.ironsspellbooks.util.IMinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.sarpreg.sarpsspells.SarpsSpellsMod;
import net.sarpreg.sarpsspells.entity.spells.sunlight_lance.SunlightLanceProjectile;
import net.sarpreg.sarpsspells.entity.spells.sunlight_lance.SunlightLanceRenderer;
import net.sarpreg.sarpsspells.particle.SunsparkParticle;
import net.sarpreg.sarpsspells.registries.EntityRegistry;
import net.sarpreg.sarpsspells.registries.SarpMobEffectRegistry;
import net.sarpreg.sarpsspells.registries.SarparticleRegistry;
import net.sarpreg.sarpsspells.render.SChargeSpellLayer;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import java.util.Map;
import java.util.function.BiFunction;

import static io.redspace.ironsspellbooks.render.EnergySwirlLayer.CHARGE_TEXTURE;
import static io.redspace.ironsspellbooks.render.EnergySwirlLayer.EVASION_TEXTURE;

import static net.sarpreg.sarpsspells.effect.AscendanceEffect.ASCENDANCE_TEXTURE;

@EventBusSubscriber(modid = SarpsSpellsMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SClientSetup {
    @SubscribeEvent
    public static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
        //event.register(SarpsSpellsMod.id("pocket_dimension"), new PocketDimensionEffects());
    }

    @SubscribeEvent
    public static void registerDyeables(RegisterColorHandlersEvent.Item event) {
        /*// Wizard Armor (default blue)
        BiFunction<ItemStack, Integer, Integer> imlazy = (stack, defaultColor) -> {
            CompoundTag compoundtag = stack.hasTag() ? stack.getTagElement("display") : null;
            return compoundtag != null && compoundtag.contains("color", 99) ? compoundtag.getInt("color") : defaultColor;
        };
        event.register(
                (stack, layer) -> layer > 0 ? -1 : imlazy.apply(stack, 0xFFb8e5f3),
                ItemRegistry.WIZARD_BOOTS.get(), ItemRegistry.WIZARD_LEGGINGS.get(), ItemRegistry.WIZARD_CHESTPLATE.get(), ItemRegistry.WIZARD_HELMET.get(), ItemRegistry.WIZARD_HAT.get());
        // Netherite Armor (default red)
        event.register(
                (stack, layer) -> layer > 0 ? -1 : imlazy.apply(stack, 0xFF8c4141),
                ItemRegistry.NETHERITE_MAGE_BOOTS.get(), ItemRegistry.NETHERITE_MAGE_LEGGINGS.get(), ItemRegistry.NETHERITE_MAGE_CHESTPLATE.get(), ItemRegistry.NETHERITE_MAGE_HELMET.get());

         */
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
       /* //LayerDefinition basicHumanLayer = LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64);

        //See LayerDefinitions.createRoots
        LayerDefinition energyOverlayLayer = LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.5F), 0.0F), 64, 64);
        LayerDefinition outerLayer = LayerDefinition.create(HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32);
        LayerDefinition innerLayer = LayerDefinition.create(HumanoidModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F), 64, 32);

        event.registerLayerDefinition(ShieldModel.LAYER_LOCATION, ShieldModel::createBodyLayer);
        event.registerLayerDefinition(AcidOrbRenderer.MODEL_LAYER_LOCATION, AcidOrbRenderer::createBodyLayer);
        event.registerLayerDefinition(GustRenderer.MODEL_LAYER_LOCATION, GustRenderer::createBodyLayer);
        event.registerLayerDefinition(RayOfFrostRenderer.MODEL_LAYER_LOCATION, RayOfFrostRenderer::createBodyLayer);
        event.registerLayerDefinition(EldritchBlastRenderer.MODEL_LAYER_LOCATION, EldritchBlastRenderer::createBodyLayer);
        event.registerLayerDefinition(FireballRenderer.MODEL_LAYER_LOCATION, FireballRenderer::createBodyLayer);
        event.registerLayerDefinition(FireboltRenderer.MODEL_LAYER_LOCATION, FireboltRenderer::createBodyLayer);
        event.registerLayerDefinition(GuidingBoltRenderer.MODEL_LAYER_LOCATION, GuidingBoltRenderer::createBodyLayer);
        event.registerLayerDefinition(IcicleRenderer.MODEL_LAYER_LOCATION, IcicleRenderer::createBodyLayer);
        event.registerLayerDefinition(ShieldTrimModel.LAYER_LOCATION, ShieldTrimModel::createBodyLayer);
        event.registerLayerDefinition(AngelWingsModel.ANGEL_WINGS_LAYER, AngelWingsModel::createLayer);
        event.registerLayerDefinition(EnergySwirlLayer.Vanilla.ENERGY_LAYER, () -> energyOverlayLayer);
        event.registerLayerDefinition(BallLightningRenderer.MODEL_LAYER_LOCATION, BallLightningRenderer::createBodyLayer);
        event.registerLayerDefinition(SkullProjectileRenderer.MODEL_LAYER_LOCATION, SkullProjectileRenderer::createBodyLayer);
        event.registerLayerDefinition(ArmorCapeLayer.ARMOR_CAPE_LAYER, ArmorCapeLayer::createBodyLayer);
        event.registerLayerDefinition(IceSpikeRenderer.IceSpikeModel.LAYER_LOCATION, IceSpikeRenderer.IceSpikeModel::createBodyLayer);
        event.registerLayerDefinition(IceTombRenderer.IceTombModel.LAYER_LOCATION, IceTombRenderer.IceTombModel::createBodyLayer);
        event.registerLayerDefinition(PyriumStaffHeadModel.LAYER_LOCATION, PyriumStaffHeadModel::createBodyLayer);
        event.registerLayerDefinition(PyriumStaffOrbModel.LAYER_LOCATION, PyriumStaffOrbModel::createBodyLayer);*/
    }

    @SubscribeEvent
    public static void registerMenuScreen(FMLClientSetupEvent event) {
        /*MenuScreens.register(MenuRegistry.INSCRIPTION_TABLE_MENU.get(), InscriptionTableScreen::new);
        MenuScreens.register(MenuRegistry.SCROLL_FORGE_MENU.get(), ScrollForgeScreen::new);
        MenuScreens.register(MenuRegistry.ARCANE_ANVIL_MENU.get(), ArcaneAnvilScreen::new);*/
    }

    @SubscribeEvent
    public static void replaceRenderers(EntityRenderersEvent.RegisterRenderers event) {
       /* event.registerEntityRenderer(EntityType.SMALL_FIREBALL, (context) -> new ReplacedFireballRenderer(context, 0.75f, .75f));
        event.registerEntityRenderer(EntityType.FIREBALL, (context) -> new ReplacedFireballRenderer(context, 1.25f, 3f));*/
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.AddLayers event) {
        /*addLayerToPlayerSkin(event, "default");
        addLayerToPlayerSkin(event, "slim");
        for (Map.Entry<EntityType<?>, EntityRenderer<?>> entry : Minecraft.getInstance().getEntityRenderDispatcher().renderers.entrySet()) {
            EntityRenderer<?> livingEntityRendererTest = entry.getValue();
            if (livingEntityRendererTest instanceof LivingEntityRenderer) {
                EntityType<?> entityType = entry.getKey();
                var renderer = event.getRenderer((EntityType) entityType);
                if (renderer != null) {
                    renderer.addLayer(new SpellTargetingLayer.Vanilla<>(renderer));
                }
            }
        }*/
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void addLayerToPlayerSkin(EntityRenderersEvent.AddLayers event, String skinName) {
        EntityRenderer<? extends Player> render = event.getSkin(skinName);
        if (render instanceof LivingEntityRenderer livingRenderer) {
            livingRenderer.addLayer(new EnergySwirlLayer.Vanilla(livingRenderer, ASCENDANCE_TEXTURE, SarpMobEffectRegistry.ASCENDANCE));
            livingRenderer.addLayer(new SChargeSpellLayer.Vanilla<>(livingRenderer));
        }


    /*.valu((entityType)->{
            if(entityType. instanceof EntityType<LivingEntity> livingType)
            if(event.getRenderer(entityType) instanceof EntityRenderer<? extends LivingEntity> livingRenderer)
                livingRenderer.addlayer
        });*/
//        EntityRenderer<? extends LivingEntity> genericRender = event.getRenderer()
        //EntityRenderer<? extends AbstractSpellCastingMob> renderer = event.getRenderer(EntityRegistry.PYROMANCER.get());
    }

    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.SUNLIGHT_LANCE_PROJECTILE.get(), SunlightLanceRenderer::new);
        event.registerEntityRenderer(io.redspace.ironsspellbooks.registries.EntityRegistry.THROWN_SPEAR.get(), ThrownSpearRenderer::new);

    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(SarparticleRegistry.SUNSPARK_PARTICLE.get(), SunsparkParticle.Provider::new);
    }

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent e) {
        //Item Properties
       /* e.enqueueWork(() -> {
            // Allow use of attack damage attribute in client spell tooltips
            Attributes.ATTACK_DAMAGE.setSyncable(true);
            // Instantiate client side abstraction
            MinecraftInstanceHelper.instance = new IMinecraftInstanceHelper() {
                @Nullable
                @Override
                public Player player() {
                    return Minecraft.getInstance().player;
                }
            };
            // Register Item Properties
            ItemProperties.register(ItemRegistry.WAYWARD_COMPASS.get(), ResourceLocation.withDefaultNamespace("angle"),
                    new CompassItemPropertyFunction((level, itemStack, entity) -> WaywardCompass.getCatacombsLocation(entity, itemStack.getOrCreateTag())));
            ItemProperties.register(ItemRegistry.AUTOLOADER_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("pull"), (itemStack, clientLevel, livingEntity, i) -> CrossbowItem.isCharged(itemStack) ? 0.0F : AutoloaderCrossbow.getLoadingTicks(itemStack) / (float) AutoloaderCrossbow.getChargeDuration(itemStack));
            ItemProperties.register(ItemRegistry.AUTOLOADER_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("pulling"), (itemStack, clientLevel, livingEntity, i) -> AutoloaderCrossbow.isLoading(itemStack) && !CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F);
            ItemProperties.register(ItemRegistry.AUTOLOADER_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("charged"), (itemStack, clientLevel, livingEntity, i) -> livingEntity != null && CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F);
            ItemProperties.register(ItemRegistry.AUTOLOADER_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("firework"), (itemStack, clientLevel, livingEntity, i) -> livingEntity != null && CrossbowItem.isCharged(itemStack) && CrossbowItem.containsChargedProjectile(itemStack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F);
            ItemProperties.register(ItemRegistry.WIZARD_HELMET.get(), IronsSpellbooks.id("hat"), (itemStack, clientLevel, livingEntity, i)
                    -> ClothingVariantHelper.getClothingVariantOrElse(itemStack, "").equals("hat") ? 1.0f : 0f);

            ItemProperties.register(ItemRegistry.TWILIGHT_GALE.get(), ResourceLocation.withDefaultNamespace("throwing"), (p_234996_, p_234997_, p_234998_, p_234999_) -> p_234998_ != null && p_234998_.isUsingItem() && p_234998_.getUseItem() == p_234996_ ? 1.0F : 0.0F
            );
            // Register Fog mob effects
            FogRenderer.MOB_EFFECT_FOG.add(new PlanarSightEffect.EcholocationBlindnessFogFunction());
            // Register Curio renderers for vanilla spellbooks
            ItemRegistry.getIronsItems().stream().filter(item -> item.get() instanceof SpellBook).forEach((item) -> CuriosRendererRegistry.register(item.get(), SpellBookCurioRenderer::new));
            // Init player animation stuff
            AnimationHelper.initializePlayerAnimationFactory();
        });*/


    }

    @SubscribeEvent
    public static void registerSpecialModels(ModelEvent.RegisterAdditional event) {
       /* for (SchoolType schoolType : SchoolRegistry.REGISTRY.get().getValues()) {
            event.register((AffinityRingRenderer.getAffinityRingModelLocation(schoolType)));
            event.register((ScrollModel.getScrollModelLocation(schoolType)));
        }
        event.register((IronsSpellbooks.id("item/template_open_spell_book_model")));
        event.register((IronsSpellbooks.id("item/pyrium_staff_haft")));
        event.register((IronsSpellbooks.id("item/fiery_dagger")));
        event.register((IronsSpellbooks.id("item/stone_scythe")));*/
    }

    @SubscribeEvent
    public static void replaceItemModels(ModelEvent.ModifyBakingResult event) {
        /*var key = new ModelResourceLocation(IronsSpellbooks.id("scroll"), "inventory");
        BakedModel model = event.getModels().get(key);
        IronsSpellbooks.LOGGER.debug("replaceItemModels {}: {}", key, model.getClass());
        event.getModels().computeIfPresent(key, (k, oldModel) -> new ScrollModel(oldModel, event.getModelBakery()));
        event.getModels().computeIfPresent(IronsSpellbooks.id("item/fiery_dagger"), (k, oldModel) -> new SpectralItemModel(oldModel));*/
    }
}

