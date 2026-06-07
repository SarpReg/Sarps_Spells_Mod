package net.sarpreg.sarpsspells.setup;

import io.redspace.ironsspellbooks.capabilities.magic.MagicEvents;
import io.redspace.ironsspellbooks.compat.CompatHandler;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


public class SModSetup {

    public static void setup() {
        IEventBus bus = MinecraftForge.EVENT_BUS;

        SPacketDistributor.register();

        //bus.addListener(MagicEvents::onWorldTick);

        //SPELLBOOKS
        //bus.addGenericListener(ItemStack.class, SpellBookDataEvents::onAttachCapabilities);
        //bus.addListener(SpellBookDataEvents::onRegisterCapabilities);

        //SCROLLS
        //bus.addListener(ScrollDataEvents::onRegisterCapabilities);
        //bus.addGenericListener(ItemStack.class, ScrollDataEvents::onAttachCapabilitiesItemStack);

    }
}