package com.swastik.seasonsmod.client;

import com.swastik.seasonsmod.season.Season;
import com.swastik.seasonsmod.season.SeasonRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.level.block.Blocks;
import com.swastik.seasonsmod.SeasonsMod;

@Mod.EventBusSubscriber(
    modid=SeasonsMod.MOD_ID,
    bus=Mod.EventBusSubscriber.Bus.MOD,
    value=Dist.CLIENT
)

public class SeasonLeafColors {
    
    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        BlockColor autumnLeafColor=(state,level,pos,tintIndex) -> {
            Season season = SeasonRegistry.getClientSeason();
            if (season==Season.AUTMN) {
                int[] autumnColors={
                    0xD4520A,
                    0xC43A0A,
                    0xE8720A,
                    0xB84A00,
                    0xFF6600
                };
                if (pos==null) return autumnColors[0];
                int index= (int)(Math.abs(pos.getX()*3+pos.getZ()*7)) % autumnColors.length;
                return autumnColors[index];
            } else if (season == Season.WINTER) {
                return 0x4EC44E;
            } else if (season == Season.SPRING) {
                return 0x4EC44e;
            } else {
                return 0x3A9A2A;
            }
        };

        event.register(autumnLeafColor,
            Blocks.OAK_LEAVES,
            Blocks.BIRCH_LEAVES,
            Blocks.JUNGLE_LEAVES,
            Blocks.ACACIA_LEAVES,
            Blocks.DARK_OAK_LEAVES
        );
    }
}
