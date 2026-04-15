package com.swastik.seasonsmod.common;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class SeasonsConfig {
    
    public static ForgeConfigSpec SPEC;
    public static ForgeConfigSpec.IntValue SEASON_LENGTH_DAYS;
    public static ForgeConfigSpec.BooleanValue FREEZE_WATER;
    public static ForgeConfigSpec.BooleanValue SHOW_HUD;

    static {
        ForgeConfigSpec.Builder builder=new ForgeConfigSpec.Builder();

        builder.comment("Seasons and Weather Overhaul Configuration")
            .push("Seasons");
        
        SEASON_LENGTH_DAYS=builder
            .comment("How many in-game days each season lasts (default 7)")
            .defineInRange("seasonLengthDays", 7, 1, 100);

        FREEZE_WATER=builder
            .comment("Wether water freezes during Winter (default true)")
            .define("showHud", true);
        
        builder.pop();
        SPEC=builder.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC);
    }

    public static int getSeasonLengthTicks() {
        return SEASON_LENGTH_DAYS.get() *24000;
    }
}