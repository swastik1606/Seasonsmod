package com.swastik.seasonsmod.season;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class SeasonSavedData extends SavedData {

    private static final String DATA_NAME = "seasonmod_season";
    private static final int SEASON_LENGTH = 24000*7;
    
    private Season currentSeason=Season.SPRING;
    private int dayTimer=0;

    public SeasonSavedData() {}

    public static SeasonSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
            SeasonSavedData::load,
            SeasonSavedData::new,
            DATA_NAME
        );
    }

    public static SeasonSavedData load(CompoundTag tag) {
    SeasonSavedData data=new SeasonSavedData();
    data.currentSeason=Season.values()[tag.getInt("season")];
    data.dayTimer=tag.getInt("dayTimer");
    return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
    tag.putInt("season", currentSeason.ordinal());
    tag.putInt("dayTimer", dayTimer);
    return tag;
   }

   public Season getCurrentSeason(){
    return currentSeason;
   }

   public int getDayTimer() {
    return dayTimer;
   }

   public void tick() {
        dayTimer++;
        if (dayTimer >= SEASON_LENGTH) {
            dayTimer =0;
             int nextIndex= (currentSeason.ordinal() +1) % Season.values().length;
             currentSeason= Season.values()[nextIndex];
        }

        setDirty();
    }

}



