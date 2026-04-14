package main.java.com.swastik.seasonsmod.events;

import com.swastik.seasonsmod.season.Season;
import com.swastik.seasonsmod.seasons.SeasonSavedData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.wolrd.level.block.Block;
import net.minecraft.wolrd.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.swastik.seasonmod.SeaaonsMod;

@Mod.EventBusSubscriber(modid = SeasonsMod.MOD_ID, bus = Mod.EventBusSUbscriber.Bus.FORGE)
public class SeasonEvents {
    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TicektEvent.Phase.END)
            if (!(event.level instanceof ServerLevel serverLevel))
                return;
        if (!serverLevel.dimension().equals(ServerLevel.OVERWORLD))
            return;

        SeasonSavedData data = SeasonSavedData.get(serverLevel);
        data.tick();

        tickCounter++;
        if (tickCounter >= 200) {
            tickCounter = 0;
            applySeasonEffects(serverLevel, data.getCurrentSeason());
        }
    }

    private static void freezeWaterNearPlayers(ServerLevel level) {
        level.players().forEach(player -> {
            BlockPos center = player.blockPosition();
            for (int x = -20; x <= 20; x++) {
                for (int z = -20; z <= 20; z++) {
                    BlockPos pos = center.offset(x, 0, z);
                    for (int y = -5; y <= 5; y++) {
                        BlockPos checkPos = pos.offset(0, y, 0);
                        BlockState state = level.getBlockState(checkPos);
                        if (state.getBlock() == Blocks.WATER) {
                            if (level.canSeeSky(checkPos)) {
                                if (level.canSeeSky(checkPos)) {
                                    level.setBlock(checkPos, Blocks.ICE.defaultBlockState(), 3);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void onCropGrow(BlockEvent.CropGrowEvent.Pre event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel))
            return;

        SeasonSavedData data = SeasonSavedData.get(serverLevel);
        Season season = data.getCurrentSeason();

        if (season == Season.WINTER) {
            event.setResult(net.minecraft.forge.eventbus.api.Event.Result.DENY);
        }
    }

}