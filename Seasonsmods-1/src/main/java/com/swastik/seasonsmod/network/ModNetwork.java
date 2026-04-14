package com.swastik.seasonsmod.network;

import com.swastik.seasonsmod.SeasonsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {

    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel CHANNEL;
    public static void register() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(SeasonsMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
        );

        CHANNEL.registerMessage(
            0,
            SeasonSyncPacket.class,
            SeasonSyncPacket::encode,
            SeasonSyncPacket::decode,
            SeasonSyncPacket::handle
        );
    }

    public static void sendToPlayer(ServerPlayer player, SeasonSyncPacket packet) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static void sendToAll(net.minecraft.server.MinecraftServer server, SeasonSyncPacket packet) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
    }
}
