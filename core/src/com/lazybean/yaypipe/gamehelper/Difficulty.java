package com.lazybean.yaypipe.gamehelper;

public enum Difficulty {

    TUTORIAL_ADVANCED(-1, 10, 2),
    TUTORIAL_BASIC(-1, 10, 0),
    EASY(1, 15, 0),
    NORMAL(2, 20, 2),
    HARD(3, 25, 3),
    PROFESSIONAL(4, 30, 4),
    MASTER(5, 40, 5);

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
