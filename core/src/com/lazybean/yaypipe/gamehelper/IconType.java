package com.lazybean.yaypipe.gamehelper;

public enum IconType {
    PAUSE("pause"),
    RESTART("restart"),
    RESUME("resume"),
    ITEM("item"),
    CROSS("cross"),
    SHARE("share"),
    SOUND("sound"),
    SOUND_OFF("soundOff"),
    UNDO("undo"),
    FAST_FORWARD("fastForward"),
    BADGE_ARROW("badgeArrow"),
    ZOOM_IN("zoomIn"),
    ZOOM_OUT("zoomOut"),
    HOME("home"),
    SNAIL("snail"),
    WAND("wand"),
    SETTINGS("settings"),
    LOCK("lock");

    public String fileName;

    IconType(String fileName){
        this.fileName = fileName;
    }
}
