package net.sarpreg.sarpsspells.setup;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.network.*;
import io.redspace.ironsspellbooks.network.casting.*;
import io.redspace.ironsspellbooks.network.gui.SelectSpellPacket;
import io.redspace.ironsspellbooks.network.particles.*;
import io.redspace.ironsspellbooks.network.spells.GuidingBoltManagerStartTrackingPacket;
import io.redspace.ironsspellbooks.network.spells.GuidingBoltManagerStopTrackingPacket;
import io.redspace.ironsspellbooks.network.spells.LearnSpellPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.sarpreg.sarpsspells.SarpsSpellsMod;

/**
 * mirrors native 1.21+ PacketDistributor, acts as in-stead replacement
 */
//@Deprecated(forRemoval = true)
public class SPacketDistributor {

    private static SimpleChannel INSTANCE;

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(ResourceLocation.fromNamespaceAndPath(SarpsSpellsMod.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        /*
        1.20.1 Special Packets
         */net.messageBuilder(OpenHeldBookPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(OpenHeldBookPacket::new)
                .encoder(OpenHeldBookPacket::toBytes)
                .consumerMainThread(OpenHeldBookPacket::handle)
                .add();

        net.messageBuilder(SyncUpgradeOrbTypes.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncUpgradeOrbTypes::new)
                .encoder(SyncUpgradeOrbTypes::toBytes)
                .consumerMainThread(SyncUpgradeOrbTypes::handle)
                .add();
        /*
        End 1.20.1 Special Packets
         */

        net.messageBuilder(BloodSiphonParticlesPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(BloodSiphonParticlesPacket::new)
                .encoder(BloodSiphonParticlesPacket::toBytes)
                .consumerMainThread(BloodSiphonParticlesPacket::handle)
                .add();

    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(ServerPlayer player, MSG message) {
        INSTANCE.send(net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> player), message);

    }

    public static <MSG> void sendToAllPlayers(MSG message) {
        INSTANCE.send(net.minecraftforge.network.PacketDistributor.ALL.noArg(), message);
    }

    public static <MSG> void sendToPlayersTrackingEntity(Entity entity, MSG message) {
        INSTANCE.send(net.minecraftforge.network.PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }

    public static <MSG> void sendToPlayersTrackingEntityAndSelf(Entity entity, MSG message) {
        sendToPlayersTrackingEntity(entity, message);
        if (entity instanceof ServerPlayer serverPlayer) {
            sendToPlayer(serverPlayer, message);
        }
    }
}