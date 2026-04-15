package com.swastik.seasonsmod.client;

import com.swastik.seasonsmod.season.Season;
import com.swastik.seasonsmod.season.SeasonRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.swastik.seasonsmod.SeasonsMod;

@Mod.EventBusSubscriber(
    modid=SeasonsMod.MOD_ID,
    bus=Mod.EventBusSubscriber.Bus.FORGE,
    value=Dist.CLIENT
)

public class SeasonSkyRenderer {

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        Minecraft mc=Minecraft.getInstance();
        if (!(mc.level instanceof ClientLevel)) return;

        Season season=SeasonRegistry.getClientSeason();
        float r=event.getRed();
        float g=event.getGreen();
        float b=event.getBlue();

        if (season == Season.WINTER){
            event.setRed(r*0.85f);
            event.setGreen(g*0.92f);
            event.setBlue(b*1.1f);
        } else if (season == Season.AUTMN) {
            event.setRed(r*1.1f);
            event.setGreen(g*0.95f);
            event.setBlue(b*0.85f);
        } else if (season == Season.SUMMER) {
            event.setRed(r*0.95f);
            event.setGreen(g*1.0f);
            event.setBlue(b*1.05f);
        } else if (season == Season.SPRING) {
            event.setRed(r*0.5f);
            event.setGreen(g*1.05f);
            event.setBlue(b*1.0f);
        }
    }
}

