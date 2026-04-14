package com.swastik.seasonsmod.network;

import com.swastik.seasonsmod.season.Season;
import com.swastik.seasonsmod.season.SeasonRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class SeasonSyncPacket {

    private final Season season;

    public SeasonSyncPacket(Season season) {
        this.season = season;
    }

    public static void encode(SeasonSyncPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.season.ordinal());
    }

    public static SeasonSyncPacket decode(FriendlyByteBuf buf) {
        return new SeasonSyncPacket(Season.values()[buf.readInt()]);
    }

    public static void handle(SeasonSyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->{
            SeasonRegistry.setClientSeason(packet.season);
        });
        ctx.get().setPacketHandled(true);
    }
}

