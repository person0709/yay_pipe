package com.lazybean.yaypipe.gamehelper;

public enum Difficulty {

    TUTORIAL_ADVANCED(-1, 10, 2),
    TUTORIAL_BASIC(-1, 10, 0),
    EASY(1, 30, 0),
    NORMAL(2, 35, 2),
    HARD(3, 40, 3),
    EXTREME(4, 45, 4),
    MASTER(5, 50, 5);

    public int difficultyLevel;
    public float waterSpeedLimit;
    public int stopNum;

    Difficulty(int difficultyLevel, float waterSpeedLimit, int stopNum){
        this.difficultyLevel = difficultyLevel;
        this.waterSpeedLimit = waterSpeedLimit;
        this.stopNum = stopNum;
    }

    public boolean isHigherLevelComparedTo(Difficulty difficulty) {
        return this.difficultyLevel > difficulty.difficultyLevel;
    }
}
