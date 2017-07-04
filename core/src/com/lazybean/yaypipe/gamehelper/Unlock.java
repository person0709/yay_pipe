package com.lazybean.yaypipe.gamehelper;

public class Unlock {

    public static boolean isUnlocked(Difficulty difficulty, GridSize gridSize){
        return AssetLoader.prefs.getBoolean(createString(difficulty, gridSize) + "Unlocked", false);
    }

    public static boolean isUnlocked(Difficulty difficulty){
        return AssetLoader.prefs.getBoolean(createString(difficulty, null) + "Unlocked", false);
    }

    public static void setUnlock(Difficulty difficulty, GridSize gridSize, boolean unlock){
        AssetLoader.prefs.putBoolean(createString(difficulty, gridSize) + "Unlocked", unlock);
        AssetLoader.prefs.flush();
    }

    public static void setUnlock(Difficulty difficulty, boolean unlock){
        AssetLoader.prefs.putBoolean(createString(difficulty, null) + "Unlocked", unlock);
        AssetLoader.prefs.flush();
    }

    private static String createString(Difficulty difficulty, GridSize gridSize){
        String difficultyString = "";
        switch (difficulty){
            case EASY:
                difficultyString = "easy";
                break;

            case NORMAL:
                difficultyString = "normal";
                break;

            case HARD:
                difficultyString = "hard";
                break;

            case EXTREME:
                difficultyString = "extreme";
                break;

            case MASTER:
                difficultyString = "master";
                break;
        }

        String gridSizeString = "";
        switch (gridSize){
            case SMALL:
                gridSizeString = "Small";
                break;

            case REGULAR:
                gridSizeString = "Regular";
                break;

            case LARGE:
                gridSizeString = "Large";
                break;

            case EXTRA_LARGE:
                gridSizeString = "ExtraLarge";
                break;
        }

        return difficultyString + gridSizeString;
    }
}
