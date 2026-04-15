package com.swastik.seasonsmod.client;

import com.swastik.seasonsmod.common.ModParticles;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.swastik.seasonsmod.SeasonsMod;

@Mod.EventBusSubscriber(
    modid= SeasonsMod.MOD_ID,
    bus=Mod.EventBusSubscriber.Bus.MOD,
    value=Dist.CLIENT
)

public class ClientSetup {

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(
            ModParticles.AUTUMN_LEAF.get(),
            AutumnLeafParticle.Provider::new
        );
    }
}