package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gameobjects.ItemTray;
import com.lazybean.yaypipe.gameobjects.Snail;
import com.lazybean.yaypipe.gameobjects.Wand;

public class Gui {
    private AssetLoader assetLoader;
    private Stage guiStage;

    private Background background;

    private ItemTray itemTray;
    private BadgeIndicator badgeIndicator;
    private ZoomUI zoomUI;

    public Gui(Stage guiStage, AssetLoader assetLoader, SpriteBatch batch){
        this.assetLoader = assetLoader;
        this.guiStage = guiStage;
    }

    private void itemTrayInit() {
        Snail snail = new Snail(AssetLoader.prefs.getBoolean("snail"));

        Wand wand = new Wand(assetLoader, AssetLoader.prefs.getInteger("wandStock"));

        itemTray = new ItemTray(assetLoader, snail, wand);

        if (!snail.isAvailable()) {
            itemTray.disableSnail();
        }
        if (!wand.isAvailable()) {
            itemTray.disableWand();
        }

        if (AssetLoader.prefs.getBoolean("itemUnlocked")) {
            guiStage.addActor(itemTray);
        }
    }

    private void nextPipeUIInit() {
        nextPipeUI = new Group();

    }

    private void upperBarUIInit() {
        upperBarUI = new UpperBarUI(assetLoader);
        upperBarUI.setForGamePlayScreen();
        gameUI.addActor(upperBarUI);
    }

    private void badgeIndicatorInit(Array<Icon> badgeArray) {
        badgeIndicator = new BadgeIndicator(assetLoader, badgeArray);
        badgeIndicator.setBounds(0, upperBarUI.getY() - upperBarUI.getHeight()*0.5f ,
                Gdx.graphics.getWidth(), upperBarUI.getHeight() * 0.5f);
        gameUI.addActor(badgeIndicator);
    }

    private void zoomUIInit() {
        zoomUI = new ZoomUI(assetLoader);
        zoomUI.setPosition(0,nextPipeUI.getTop());
        gameUI.addActor(zoomUI);
    }

    private void gamePausedUIInit(){
        gamePausedUI = new GamePausedWindow(assetLoader);
        gamePausedUI.window.setScale(0f);
        gamePausedUI.setVisible(false);
    }

    public void render(){

    }
}
