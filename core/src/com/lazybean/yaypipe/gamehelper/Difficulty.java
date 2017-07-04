package com.lazybean.yaypipe.gamehelper;

public enum Difficulty {

    TUTORIAL_ADVANCED(0, 10, 2),
    TUTORIAL_BASIC(0, 10, 0),
    EASY(1, 50, 0),
    NORMAL(2, 60, 2),
    HARD(3, 60, 3),
    EXTREME(4, 60, 4),
    MASTER(5, 60, 5);

    public float waterSpeedIncrease;
    public float waterSpeedLimit;
    public int stopNum;

    Difficulty(float waterSpeedIncrease, float waterSpeedLimit, int stopNum){
        this.waterSpeedIncrease = waterSpeedIncrease;
        this.waterSpeedLimit = waterSpeedLimit;
        this.stopNum = stopNum;
    }
}
