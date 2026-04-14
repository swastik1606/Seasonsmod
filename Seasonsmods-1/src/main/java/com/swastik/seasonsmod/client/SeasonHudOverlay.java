package com.swastik.seasonsmod.client;

import com.swastik.seasonsmod.season.SeasonRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.swastik.seasonsmod.SeasonsMod;

@Mod.EventBusSubscriber(modid=SeasonsMod.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class SeasonHudOverlay {
    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft mc= Minecraft.getInstance();
        if (mc.player==null) return;
        if (mc.options.hideGui) return;

        String seasonText="Season: " + SeasonRegistry.getClientSeason().getDisplayName();
        GuiGraphics graphics = event.getGuiGraphics();
        graphics.drawString(mc.font, seasonText, 5, 5, 0xFFFFFF, true);
    }
}