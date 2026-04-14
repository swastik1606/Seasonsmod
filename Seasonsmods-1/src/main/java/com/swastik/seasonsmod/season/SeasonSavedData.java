package com.swastik.seasonsmod.season;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class SeasonSavedData extends SavedData {

    private static final String DATA_NAME = "seasonmod_season";

    private Season currentSeason = Season.SPRING;

    public SeasonSavedData() {
    }

    public static SeasonSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                SeasonSavedData::load,
                SeasonSavedData::new,
                DATA_NAME);
    }

    public static SeasonSavedData load(CompoundTag tag) {
        SeasonSavedData data = new SeasonSavedData();
        data.currentSeason = Season.values()[tag.getInt("season")];
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("season", currentSeason.ordinal());
        return tag;
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }

    public void tick(ServerLevel level) {
        long vanillaTime=level.getDayTime();

        int totalDaysPassed = (int) (vanillaTime/24000);
        int expectedSeasonIndex= (totalDaysPassed/7) % Season.values().length;
        Season expectedSeason=Season.values()[expectedSeasonIndex];
        
        if (this.currentSeason!=expectedSeason) {
            this.currentSeason=expectedSeason;
            this.setDirty();
        }

        setDirty();
    }

}
