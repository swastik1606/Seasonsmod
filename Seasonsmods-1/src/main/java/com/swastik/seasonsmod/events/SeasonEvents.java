package com.swastik.seasonsmod.events;

import com.swastik.seasonsmod.network.ModNetwork;
import com.swastik.seasonsmod.network.SeasonSyncPacket;
import com.swastik.seasonsmod.season.Season;
import com.swastik.seasonsmod.season.SeasonSavedData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.swastik.seasonsmod.SeasonsMod;

@Mod.EventBusSubscriber(modid = SeasonsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SeasonEvents {

    private static int tickCounter = 0;
    private static Season lastKnownSeason=null;

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.level instanceof ServerLevel serverLevel)) return;
        if (!serverLevel.dimension().equals(ServerLevel.OVERWORLD))return;

        SeasonSavedData data = SeasonSavedData.get(serverLevel);
        Season seasonBefore=data.getCurrentSeason();
        data.tick();
        Season seasonAfter=data.getCurrentSeason();

        if (seasonBefore != seasonAfter) {
            lastKnownSeason=seasonAfter;
            ModNetwork.sendToAll(
                serverLevel.getServer(),
                new SeasonSyncPacket(seasonAfter)
            );
            serverLevel.players().forEach(player -> {
                player.sendSystemMessage(
                    Component.literal("The season has change to " + seasonAfter.getDisplayName() + "!")
                );
            });
        }

        tickCounter++;
        if (tickCounter >= 200) {
            tickCounter = 0;
            applySeasonEffects(serverLevel, data.getCurrentSeason());
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ServerLevel level= (ServerLevel) player.level();
        SeasonSavedData data= SeasonSavedData.get(level);
        ModNetwork.sendToPlayer(player, new SeasonSyncPacket(data.getCurrentSeason()));
    }

    private static void applySeasonEffects(ServerLevel level, Season season) {
        if (season==Season.WINTER) {
            freezeWaterNearPlayers(level);
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
                                level.setBlock(checkPos, Blocks.ICE.defaultBlockState(), 3);
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
            event.setResult(net.minecraftforge.eventbus.api.Event.Result.DENY);
        }
    }

}