package com.lazybean.yaypipe.gamehelper;

public enum PipeType {
    TOP_BOTTOM(1),
    LEFT_RIGHT(2),
    LEFT_TOP(3),
    LEFT_BOTTOM(4),
    RIGHT_TOP(5),
    RIGHT_BOTTOM(6),
    ALL_DIRECTION(7),
    BOTTOM_END(8),
    LEFT_END(9),
    TOP_END(10),
    RIGHT_END(11);

    private int pipeNum;

    PipeType(int pipeNum){
        this.pipeNum = pipeNum;
    }

    public int getPipeNum(){
        return pipeNum;
    }
}
