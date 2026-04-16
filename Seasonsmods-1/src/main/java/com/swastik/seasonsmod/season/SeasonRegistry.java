package com.swastik.seasonsmod.season;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.core.BlockPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.antlr.v4.parse.ANTLRParser.prequelConstruct_return;

import com.swastik.seasonsmod.SeasonsMod;


@Mod.EventBusSubscriber(
    modid=SeasonsMod.MOD_ID,
    bus=Mod.EventBusSubscriber.Bus.FORGE
)

public class SeasonRegistry {
    
    private static Season clientSeason= Season.SPRING;
    private static boolean transitionPending= false;
    private static int transitionTick=0;
    private static int transitionRadius=0;
    private static final int MAX_RADIUS=128;
    private static final int TICKS_PER_STEP=4;

    public static void init() {
    }

    public static Season getClientSeason() {
        return clientSeason;
    }

    public static void setClientSeason(Season season) {
        if(clientSeason == season) return;
        clientSeason= season;
        transitionPending=true;
        transitionTick=0;
        transitionRadius=0;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!transitionPending) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.levelRenderer == null) return;

        transitionTick++;
        if (transitionTick % TICKS_PER_STEP !=0) return;

        BlockPos center=mc.player.blockPosition();
        int r= transitionRadius;

        mc.levelRenderer.setSectionDirtyWithNeighbors(
            (center.getX() + r) >> 4,
            center.getY() >> 4,
            (center.getZ() + r) >> 4
        );

        mc.levelRenderer.setSectionDirtyWithNeighbors(
            (center.getX() - r) >> 4,
            center.getY() >> 4,
            (center.getZ() - r) >> 4
        );

        mc.levelRenderer.setSectionDirtyWithNeighbors(
            center.getX() >>4,
            center.getY() >> 4,
            (center.getZ() + r) >> 4
        );

        mc.levelRenderer.setSectionDirtyWithNeighbors(
            center.getX() >> 4,
            center.getY() >> 4,
            (center.getZ() -r) >> 4
        );

        transitionRadius +=8;
        if (transitionRadius>MAX_RADIUS) {
            transitionPending = false;
            transitionRadius = 0;
        }
    }
}