package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Colour;

public class GamePausedWindow extends Group {
    public Background background;
    public Table window;

    private boolean isResume = false;
    private boolean isSound = true;
    private boolean isRestart = false;
    private boolean isQuit = false;

    public GamePausedWindow(final AssetLoader assetLoader){
        background = new Background();
        background.setColor(0, 0, 0, 0f);
        addActor(background);

        if (AssetLoader.prefs.getFloat("soundVolume") == 0){
            isSound = false;
        }

        LabelStyle labelStyle = new LabelStyle(assetLoader.extraSmallFont_noto, Color.BLACK);

        window = new Table();
//        window.setDebug(true);
        window.setTransform(true);
        window.setWidth(Gdx.graphics.getWidth() * 0.65f);
        window.setHeight(window.getWidth() * 0.7f);
        window.setBackground(new NinePatchDrawable(assetLoader.window));
        window.setPosition(Gdx.graphics.getWidth() / 2 - window.getWidth() /2,
                Gdx.graphics.getHeight() /2 - window.getHeight() / 2);
        window.setOrigin(window.getWidth()/2, window.getHeight()/2);

        LabelStyle style = new LabelStyle(assetLoader.mediumLargeFont_anja, Color.WHITE);

        Label paused = new Label("PAUSE", style);
        paused.setColor(Color.BLACK);
        window.add(paused).padBottom(Value.percentHeight(0.5f)).colspan(3);
        window.row();

        Table quit = new Table();
        Icon quit_icon = new Icon(assetLoader.circle, assetLoader.home);
        quit_icon.setDiameter(window.getWidth() * 0.23f);
        quit_icon.setColor(Colour.INDIGO);
        quit_icon.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                assetLoader.click.play(AssetLoader.prefs.getFloat("soundVolume"));
                isQuit = true;
            }
        });

        Label quit_label = new Label("MAIN", labelStyle);

        quit.add(quit_icon).row();
        quit.add(quit_label);
        window.add(quit).expandY().width(Value.percentWidth(0.23f, window));


        Table restart = new Table();
        Icon restart_icon = new Icon(assetLoader.circle, assetLoader.restart);
        restart_icon.setDiameter(window.getWidth() * 0.23f);
        restart_icon.setColor(Colour.INDIGO);
        restart_icon.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                assetLoader.click.play(AssetLoader.prefs.getFloat("soundVolume"));
                isRestart = true;
            }
        });

        Label restart_label = new Label("RESTART", labelStyle);

        restart.add(restart_icon).row();
        restart.add(restart_label);

        window.add(restart).expandX();

        Table sound = new Table();
        final Icon sound_icon = new Icon(assetLoader.circle, assetLoader.sound);
        if (!isSound){
            sound_icon.setIconImage(assetLoader.soundOff);
        }
        sound_icon.setDiameter(window.getWidth() * 0.23f);
        sound_icon.setColor(Colour.INDIGO);
        sound_icon.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                assetLoader.click.play(AssetLoader.prefs.getFloat("soundVolume"));
                if (isSound){
                    sound_icon.setIconImage(assetLoader.soundOff);
                    isSound = false;
                    AssetLoader.prefs.putFloat("soundVolume",0);
                    AssetLoader.prefs.flush();
                }
                else{
                    sound_icon.setIconImage(assetLoader.sound);
                    isSound = true;
                    AssetLoader.prefs.putFloat("soundVolume",1f);
                    AssetLoader.prefs.flush();
                }
            }
        });

        Label sound_label = new Label("SOUND", labelStyle);

        sound.add(sound_icon).row();
        sound.add(sound_label);

        window.add(sound);


        Icon resume = new Icon(assetLoader.circle, assetLoader.cross);
        resume.setDiameter(window.getWidth() * 0.15f);
        resume.setColor(Colour.RED);
        resume.setPosition(window.getWidth() - resume.getWidth() * 1.2f,
                window.getHeight() - resume.getHeight() * 1.2f);
        resume.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                assetLoader.click.play(AssetLoader.prefs.getFloat("soundVolume"));
                isResume = true;
            }
        });

        window.addActor(resume);

        addActor(window);
    }

    public boolean isResume() {
        return isResume;
    }

    public void setResume(boolean bool) {
        isResume = bool;
    }

    public boolean getSound(){
        return isSound;
    }

    public void setSound(boolean bool){
        isSound = bool;
    }

    public boolean isRestart(){
        return isRestart;
    }

    public void setRestart(boolean bool){
        isRestart = bool;
    }

    public boolean isQuit(){
        return isQuit;
    }

    public void setQuit(boolean bool){
        isQuit = bool;
    }
}
