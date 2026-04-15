package com.swastik.seasonsmod.client;


import com.swastik.seasonsmod.season.Season;
import com.swastik.seasonsmod.season.SeasonRegistry;
import net.minecraft.client.Minecraft;  
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.swastik.seasonsmod.SeasonsMod;
import java.util.Random;

@Mod.EventBusSubscriber(
    modid=SeasonsMod.MOD_ID,
    bus=Mod.EventBusSubscriber.Bus.FORGE,
    value=Dist.CLIENT
)

public class SeasonPartciles{
    
    private static final Random random = new Random();
    private static int particleTick=0;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc=Minecraft.getInstance();
        if(mc.player==null) return;
        if (mc.level == null) return;
        if (mc.isPaused()) return;

        Season season = SeasonRegistry.getClientSeason();
        if (season != Season.WINTER && season != Season.AUTMN) return;

        particleTick++;
        if (particleTick <3) return;
        particleTick=0;

        BlockPos playerPos = mc.player.blockPosition();

        for (int i=0; i<40; i++) {
            double x = playerPos.getX() + (random.nextDouble()-0.5) *30;
            double y = playerPos.getY() + 5 + random.nextDouble() *5;
            double z = playerPos.getZ() + (random.nextDouble()-0.5) *30;

            BlockPos checkPos=new BlockPos((int)x, (int)y, (int)z);
            if (!mc.level.canSeeSky(checkPos)) continue;

            if (season==Season.WINTER) {
                mc.level.addParticle(
                    ParticleTypes.SNOWFLAKE,
                    x,y,z,
                    (random.nextDouble() -0.5) *0.08,
                    -0.15,
                    (random.nextDouble() -0.5) *0.08
                );
            } else if (season == Season.AUTMN) {
                mc.level.addParticle(
                    ParticleTypes.CHERRY_LEAVES,
                    x,y,z,
                    (random.nextDouble() -0.5) *0.01,
                    -0.05,
                    (random.nextDouble() -0.5) *0.1
                );
            }
        }
    }
}
