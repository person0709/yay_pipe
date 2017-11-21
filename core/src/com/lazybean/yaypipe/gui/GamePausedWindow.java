package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gamehelper.FontType;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;
import com.lazybean.yaypipe.gamehelper.GameState;
import com.lazybean.yaypipe.gamehelper.IconType;

public class GamePausedWindow extends GameWindow {
    private final float ICON_DIAMETER = YayPipe.SCREEN_WIDTH * 0.15f;

    public Icon resumeIcon, soundIcon, restartIcon, quitIcon;

    public GamePausedWindow(final AssetLoader assetLoader, GameWorld gameWorld){
        super("", assetLoader.uiSkin, gameWorld);

        setColor(getColor().r, getColor().g, getColor().b, 0f);

        LabelStyle titleStyle = new LabelStyle(assetLoader.getFont(FontType.ANJA_MEDIUM), Color.BLACK);
        Label title = new Label("PAUSED", titleStyle);

        getContentTable().add(title);

        LabelStyle labelStyle = new LabelStyle(assetLoader.getFont(FontType.NOTO_EXTRA_SMALL), Color.BLACK);

        Table quit = new Table();
        quitIcon = new Icon(assetLoader, IconType.HOME, Icon.MENU_DIAMETER);
        quitIcon.setColor(CustomColor.INDIGO.getColor());

        Label quit_label = new Label("MAIN", labelStyle);

        quit.add(quitIcon).size(ICON_DIAMETER).row();
        quit.add(quit_label);
        getButtonTable().add(quit).padLeft(Value.percentWidth(0.1f));


        Table restart = new Table();
        restartIcon = new Icon(assetLoader, IconType.RESTART, Icon.MENU_DIAMETER);
        restartIcon.setColor(CustomColor.INDIGO.getColor());

        Label restart_label = new Label("RESTART", labelStyle);

        restart.add(restartIcon).size(ICON_DIAMETER).row();
        restart.add(restart_label);

        getButtonTable().add(restart).expandX();

        Table sound = new Table();
        soundIcon = new Icon(assetLoader, IconType.SOUND, Icon.MENU_DIAMETER);
        if (GameData.getInstance().isSoundOn()){
            soundIcon.setIconImage(assetLoader.getIconTexture(IconType.SOUND));
        }
        soundIcon.setColor(CustomColor.INDIGO.getColor());
        soundIcon.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (GameData.getInstance().isSoundOn()){
                    soundIcon.setIconImage(assetLoader.getIconTexture(IconType.SOUND_OFF));
                    GameData.getInstance().setSoundOn(false);
                }
                else{
                    soundIcon.setIconImage(assetLoader.getIconTexture(IconType.SOUND));
                    GameData.getInstance().setSoundOn(true);
                }
            }
        });

        Label sound_label = new Label("SOUND", labelStyle);

        sound.add(soundIcon).size(ICON_DIAMETER).row();
        sound.add(sound_label);

        getButtonTable().add(sound).padRight(Value.percentWidth(0.1f));

        pack();

        //prevents resumeIcon from clipping
        setClip(false);

        resumeIcon = new Icon(assetLoader, IconType.CROSS, Icon.MENU_DIAMETER);
        resumeIcon.setDiameter(YayPipe.SCREEN_WIDTH * 0.1f);
        resumeIcon.setColor(CustomColor.RED.getColor());
        resumeIcon.setPosition(getWidth() - resumeIcon.getWidth() * 1.2f,
                getHeight() - resumeIcon.getHeight() * 1.2f);

        addActor(resumeIcon);

//        debugAll();
    }

    @Override
    public float getPrefWidth() {
        return YayPipe.SCREEN_WIDTH * 0.65f;
    }

    @Override
    public float getPrefHeight() {
        return getPrefWidth() * 0.7f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (quitIcon.isTouched()){
            quitIcon.setTouched(false);
            gameWorld.setState(GameState.QUIT);
            hide();
        }

        else if (restartIcon.isTouched()){
            restartIcon.setTouched(false);
            gameWorld.setState(GameState.RESTART);
            hide();
        }
        else if (resumeIcon.isTouched()){
            resumeIcon.setTouched(false);
            gameWorld.resume();
            hide();
        }
    }
}
