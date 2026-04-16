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
import com.swastik.seasonsmod.common.SeasonsConfig;

@Mod.EventBusSubscriber(modid = SeasonsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SeasonEvents {

    private static int tickCounter = 0;
    private static Season lastKnownSeason = null;

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;
        if (!(event.level instanceof ServerLevel serverLevel))
            return;
        if (!serverLevel.dimension().equals(ServerLevel.OVERWORLD))
            return;

        SeasonSavedData data = SeasonSavedData.get(serverLevel);
        Season seasonBefore = data.getCurrentSeason();
        data.tick(serverLevel);
        Season seasonAfter = data.getCurrentSeason();

        if (seasonBefore != seasonAfter) {
            lastKnownSeason = seasonAfter;
            ModNetwork.sendToAll(
                    serverLevel.getServer(),
                    new SeasonSyncPacket(seasonAfter)
            );
            
            int color;
            if (seasonAfter == Season.SPRING) color=0x00FF7F;
            else if (seasonAfter == Season.SUMMER) color=0xFFD700;
            else if (seasonAfter == Season.AUTMN) color=0xFF6600;
            else color=0x00BFFF;
            
            serverLevel.players().forEach(player -> {
                player.sendSystemMessage(
                    Component.literal("► ")
                        .append(Component.literal(seasonAfter.getDisplayName()+ " has arrived!")
                        .withStyle(style -> style.withColor(color).withBold(true)))
                );
            });

            if (seasonAfter == Season.SPRING) {
                meltIceNearPlayers(serverLevel);
                meltSnowNearPlayers(serverLevel);
            }
        }

        tickCounter++;
        if (tickCounter >= 30) {
            tickCounter = 0;
            SeasonsMod.LOGGER.info("Tick fired, season is: ", data.getCurrentSeason());
            applySeasonEffects(serverLevel, data.getCurrentSeason());

        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player))
            return;
        ServerLevel level = (ServerLevel) player.level();
        SeasonSavedData data = SeasonSavedData.get(level);
        ModNetwork.sendToPlayer(player, new SeasonSyncPacket(data.getCurrentSeason()));
    }

    private static void applySeasonEffects(ServerLevel level, Season season) {
        if (season == Season.WINTER && SeasonsConfig.FREEZE_WATER.get()) {
            freezeWaterNearPlayers(level);
            placeSnowNearPlayers(level);
        }
    }

    private static void freezeWaterNearPlayers(ServerLevel level) {
        SeasonsMod.LOGGER.info("Freeze method running, players: " + level.players().size());
        level.players().forEach(player -> {
            BlockPos center = player.blockPosition();
            for (int x = -20; x <= 20; x++) {
                for (int z = -20; z <= 20; z++) {
                    BlockPos pos = center.offset(x, 0, z);
                    BlockPos surface=level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos
                    ).below();
                    BlockState state=level.getBlockState(surface);
                    if(state.getBlock() == Blocks.WATER) {
                        level.setBlock(surface, Blocks.PACKED_ICE.defaultBlockState(), 3);
                    }
                }
            }
        });
    }

    private static void placeSnowNearPlayers(ServerLevel level) {
        level.players().forEach(player -> {
            BlockPos center = player.blockPosition();
            for (int x=-30; x<=30; x++) {
                for (int z=-30; z <=30; z++) {
                    if (level.random.nextFloat()>0.3f) continue;
                    BlockPos surface = level.getHeightmapPos(
                        net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                        center.offset(x,0,z)
                    );
                    BlockPos below=surface.below();
                    BlockState belowState=level.getBlockState(below);
                    BlockState surfaceState=level.getBlockState(surface);

                    if (!surfaceState.isAir()) continue;
                    if (!level.canSeeSky(surface)) continue;

                    BlockState snowState = net.minecraft.world.level.block.Blocks.SNOW.defaultBlockState();
                    if (snowState.canSurvive(level, surface)) {
                        level.setBlock(surface, snowState, 3);
                    }
                }
            }
        });
    }

    private static void meltIceNearPlayers(ServerLevel level){
        level.players().forEach(player -> {
            BlockPos center = player.blockPosition();
            for (int x=-20; x<=20; x++) {
                for(int z=-20; z<=20; z++) {
                    BlockPos pos=center.offset(x,0,z);
                    BlockPos surface=level.getHeightmapPos(
                        net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                        pos
                    ).below();
                    BlockState state=level.getBlockState(surface);
                    if (state.getBlock() == Blocks.PACKED_ICE) {
                        level.setBlock(surface, Blocks.WATER.defaultBlockState(), 3);
                    }
                }
            }
        });
    }

    private static void meltSnowNearPlayers(ServerLevel level) {
        level.players().forEach(player ->{
            BlockPos center=player.blockPosition();
            for (int x=-30; x<=30; x++) {
                for (int z=-30; z<=30; z++) {
                    BlockPos surface=level.getHeightmapPos(
                        net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                        center.offset(x,0,z));

                    BlockState state=level.getBlockState(surface);
                    if (state.getBlock() == Blocks.SNOW) {
                        level.setBlock(surface, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void onCropGrow(BlockEvent.CropGrowEvent.Pre event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;

        SeasonSavedData data = SeasonSavedData.get(serverLevel);
        Season season = data.getCurrentSeason();

        if (season == Season.WINTER) {
            event.setResult(net.minecraftforge.eventbus.api.Event.Result.DENY);
            return;
        }

        if (season == Season.AUTMN) {
            if (serverLevel.random.nextFloat() < 0.6f) {
                event.setResult(net.minecraftforge.eventbus.api.Event.Result.DENY);
            }
            return;
        }

        if (season == Season.SUMMER) {
            if (serverLevel.random.nextFloat() < 0.5f) {
                net.minecraft.world.level.block.Block block=event.getState().getBlock();
                if( block instanceof CropBlock crop) {
                    net.minecraft.world.level.block.state.BlockState current=event.getState();
                    net.minecraft.world.level.block.state.BlockState grown= crop.getStateForAge(
                        Math.min(crop.getAge(current) + 1, crop.getMaxAge())
                    );
                    serverLevel.setBlock(event.getPos(), grown, 3);
                }
            }
        }
    }

}