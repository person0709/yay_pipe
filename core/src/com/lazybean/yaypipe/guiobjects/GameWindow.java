package com.lazybean.yaypipe.guiobjects;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.lazybean.yaypipe.GameWorld;

public class GameWindow extends Dialog{
    protected GameWorld gameWorld;

    public GameWindow(Skin skin, GameWorld gameWorld) {
        super("", skin, "default");
        this.gameWorld = gameWorld;
    }

    public GameWindow(Skin skin){
        super("", skin, "default");
    }
}
