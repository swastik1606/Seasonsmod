package com.swastik.seasonsmod.season;

public enum Season {
    SPRING("Spring"),
    SUMMER("Summer"),
    AUTMN("Autumn"),
    WINTER("winter");


    private final String displayName;

    Season(String displayName){
        this.displayName=displayName;

    }

    public String getDisplayName() {
        return displayName;
    }

    public Season next() {
        Season[] values=Season.values();
        return values[(this.ordinal()+1)% values.length];
    }

}

