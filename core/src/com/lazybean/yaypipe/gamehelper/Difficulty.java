package com.lazybean.yaypipe.gamehelper;

public enum Difficulty {

    TUTORIAL_ADVANCED(-1, 0, 10, 2),
    TUTORIAL_BASIC(-1, 0, 10, 0),
    EASY(1, 1, 30, 0),
    NORMAL(2, 2, 35, 2),
    HARD(3, 3, 40, 3),
    EXTREME(4, 4, 45, 4),
    MASTER(5, 5, 50, 5);

    public int difficultyLevel;
    public float waterSpeedIncrease;
    public float waterSpeedLimit;
    public int stopNum;

    Difficulty(int difficultyLevel, float waterSpeedIncrease, float waterSpeedLimit, int stopNum){
        this.difficultyLevel = difficultyLevel;
        this.waterSpeedIncrease = waterSpeedIncrease;
        this.waterSpeedLimit = waterSpeedLimit;
        this.stopNum = stopNum;
    }

    public boolean isHigherLevelComparedTo(Difficulty difficulty) {
        if (this.difficultyLevel > difficulty.difficultyLevel) {
            return true;
        }
        else{
            return false;
        }
    }
}
