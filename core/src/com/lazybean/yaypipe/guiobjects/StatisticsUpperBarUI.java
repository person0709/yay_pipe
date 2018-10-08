package com.lazybean.yaypipe.guiobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.IconType;

public class StatisticsUpperBarUI extends UpperBarUI {
    public StatisticsUpperBarUI(AssetLoader assetLoader) {
        super(assetLoader);

        align(Align.left);
        Table controlIcons = new Table();
        controlIcons.align(Align.left);
        add(controlIcons);

        Icon back = new Icon(assetLoader, IconType.UNDO, Icon.MENU_DIAMETER);
        back.setColor(Color.BLACK);

        controlIcons.add(back).padLeft(YayPipe.SCREEN_WIDTH * 0.07f);
    }
}
