package main.java.com.swastik.seasonsmod.season;

public class SeasonRegistry {
    private static Season clientSeason= Season.SPRING;

    public static void init() {
    }

    public static Season getClientSeason() {
        return clientSeason;
    }

    public static void setClientSeason(Season season) {
        clientSeason= season;
    }
}