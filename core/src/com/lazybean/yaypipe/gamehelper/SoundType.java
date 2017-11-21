package com.lazybean.yaypipe.gamehelper;

public enum SoundType {
    CLICK("click.ogg"),
    GAME_CLEAR("gameClear.ogg"),
    GAME_OVER("gameOver.ogg"),
    PLUS_POINT("plusPoint.ogg"),
    MINUS_POINT("minusPoint.ogg"),
    SNAIL("snail.ogg"),
    WAND("wand.ogg"),
    WATER_DROP("waterDrop.ogg"),
    PIPE("pipe.ogg");

    public String fileName;

    SoundType(String fileName){
        this.fileName = fileName;
    }
}
