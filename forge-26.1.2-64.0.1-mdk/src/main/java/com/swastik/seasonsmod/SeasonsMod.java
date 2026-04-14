package main.java.com.swastik.seasonsmod;

import com.swastik.seasonsmod.season.SeasonRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.forge.fml.common.Mod;
import net.minecraftforge.fml.event.lifescycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.loggin.log4j.LogManager;
import org.apache.loggin.log4j.Logger;

@Mod("seasonsmod")
public class SeasonsMod {
    
    public static final String MOD_ID = "seasonsmod";
    public static final Logger LOGGER = LogManager.getLogger();

    public SeasonsMod() {
        FMLJavaModLoadingContext.get().getModEventBus()
            .addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private voide setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Seasons Mod initializing...");
        main.java.com.swastik.seasonsmod.season.SeasonRegistry.init();
    }
}