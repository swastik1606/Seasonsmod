package com.swastik.seasonsmod;

import com.swastik.seasonsmod.network.ModNetwork;
import com.swastik.seasonsmod.season.SeasonRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.config.ModConfig;

import com.swastik.seasonsmod.common.ModParticles;
import com.swastik.seasonsmod.common.SeasonsConfig;

@Mod("seasonsmod")
public class SeasonsMod {
    
    public static final String MOD_ID = "seasonsmod";
    public static final Logger LOGGER = LogManager.getLogger();

    public SeasonsMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SeasonsConfig.SPEC);

        FMLJavaModLoadingContext.get().getModEventBus()
            .addListener(this::setup);
            ModParticles.PARTICLES.register(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Seasons Mod initializing...");
        
        SeasonRegistry.init();
        ModNetwork.register();
    }
}