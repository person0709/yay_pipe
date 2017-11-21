package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.gamehelper.AssetLoader;

public class GameWindow extends Dialog{
    protected GameWorld gameWorld;

    public GameWindow(String title, Skin skin, GameWorld gameWorld) {
        super(title, skin, "default");
        getTitleLabel().setAlignment(Align.center, Align.center);
        this.gameWorld = gameWorld;
    }
}
