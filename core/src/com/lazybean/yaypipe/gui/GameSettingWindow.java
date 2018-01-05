package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gamehelper.FontType;
import com.lazybean.yaypipe.gamehelper.IconType;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;
import com.lazybean.yaypipe.screens.GameScreen;

public class GameSettingWindow extends GameWindow{
    private final int REFRESH_RATE = 2;
    private float refreshTimer = 0;

    private AssetLoader assetLoader;

    private PromptWindow promptWindow;

    private Icon quitIcon;
    private TextButton googlePlayButton, save, load;

    public GameSettingWindow(YayPipe game, Stage stage) {
        super(game.assetLoader.uiSkin);

        this.assetLoader = game.assetLoader;

        getTitleTable().remove();

        Label.LabelStyle titleStyle = new Label.LabelStyle(assetLoader.getFont(FontType.ANJA_MEDIUM), Color.BLACK);
        Label title = new Label("SETTINGS", titleStyle);

        getContentTable().add(title).padBottom(Value.percentHeight(0.5f)).row();

        Table table = new Table();
        table.defaults().height(Value.percentHeight(2f, title));

        Image soundIcon = new Image(assetLoader.getIconTexture(IconType.SOUND));
        soundIcon.setColor(Color.BLACK);
        soundIcon.setScaling(Scaling.fillY);

        Slider.SliderStyle soundSliderStyle = new Slider.SliderStyle();
        soundSliderStyle.background = new TextureRegionDrawable(assetLoader.slider);
        soundSliderStyle.background.setMinHeight(30f);
        soundSliderStyle.knob = new TextureRegionDrawable(assetLoader.circle).tint(Color.BLACK);
        soundSliderStyle.knob.setMinHeight(70f);
        soundSliderStyle.knob.setMinWidth(70f);

        final Slider soundSlider = new Slider(0f, 1f, 0.1f, false, soundSliderStyle);
        soundSlider.setValue(GameData.getInstance().getSoundVolume());

        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameData.getInstance().setSoundVolume(soundSlider.getValue());
            }
        });

        table.add(soundIcon).fillX().padLeft(getPrefWidth() * 0.05f);
        table.add(soundSlider).width(this.getPrefWidth() * 0.65f).align(Align.right).row();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = assetLoader.getFont(FontType.ANJA_MEDIUM_SMALL);
        buttonStyle.fontColor = Color.BLACK;
        buttonStyle.up = assetLoader.button;
        buttonStyle.pressedOffsetY = -5f;

        Window.WindowStyle windowStyle = new Window.WindowStyle(assetLoader.getFont(FontType.ANJA_SMALL), Color.BLACK, assetLoader.window);
        promptWindow = new PromptWindow(assetLoader, windowStyle){
            @Override
            protected void result(Object object) {
                if (object instanceof Runnable){
                    ((Runnable) object).run();
                }
                promptWindow.hide();
            }
        };
        promptWindow.setObject(promptWindow.yes, true);


        googlePlayButton = new TextButton("Disconnected", buttonStyle);

        save = new TextButton("Save to Cloud", buttonStyle);
        save.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                promptWindow.getTitleLabel().setText("This will overwrite your Cloud data.\nContinue?");
                promptWindow.setObject(promptWindow.yes, new Runnable() {
                    @Override
                    public void run() {
                        GameData.getInstance().saveLocalToCloud();
                    }
                });
                promptWindow.show(stage);
            }
        });

        load = new TextButton("Load from Cloud", buttonStyle);
        load.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switch (GameData.getInstance().loadFromCloud()){
                    case -1: {
                        ((GameScreen) game.getScreen()).addMessage("No Cloud data");
                        break;
                    }

                    case -2: {
                        ((GameScreen) game.getScreen()).addMessage("Not connected to Internet");
                        break;
                    }

                    case 0: {
                        promptWindow.getTitleLabel().setText("This will overwrite your save data.\nContinue?");
                        promptWindow.setObject(promptWindow.yes, new Runnable() {
                            @Override
                            public void run() {
                                game.setScreenWithFadeInOut(game.screenManager.getSplashScreen());
                                hide();
                            }
                        });
                        promptWindow.show(stage);
                        break;
                    }
                }
            }
        });

        Image googlePlayIcon = new Image(assetLoader.getIconTexture(IconType.GOOGLE_PLAY));
        googlePlayIcon.setColor(Color.BLACK);
        googlePlayIcon.setScaling(Scaling.fillY);

        googlePlayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (YayPipe.playService.isSignedIn()) {
                    YayPipe.playService.signOut();
                    googlePlayButton.setText("Disconnected");
                } else {
                    YayPipe.androidHelper.showProgressBar();
                    YayPipe.playService.startSignInIntent();
                    refresh();
                }
            }
        });

        table.add(googlePlayIcon).fillX().padLeft(getPrefWidth() * 0.05f);
        table.add(googlePlayButton).size(this.getPrefWidth() * 0.65f, googlePlayButton.getHeight() * 1.4f).align(Align.right).fillX().row();
        table.add(save).colspan(2).size(this.getPrefWidth() * 0.9f, save.getHeight() * 1.4f).padBottom(Value.percentHeight(0.1f)).row();
        table.add(load).colspan(2).size(this.getPrefWidth() * 0.9f, load.getHeight() * 1.4f);

        getContentTable().add(table);

        pack();

        //prevents quitIcon from clipping
        setClip(false);

        quitIcon = new Icon(assetLoader, IconType.CROSS, Icon.MENU_DIAMETER);
        quitIcon.setDiameter(YayPipe.SCREEN_WIDTH * 0.1f);
        quitIcon.setColor(CustomColor.RED.getColor());
        quitIcon.setPosition(getWidth() - quitIcon.getWidth() * 1.2f,
                getHeight() - quitIcon.getHeight() * 1.2f);

        addActor(quitIcon);

//        debugAll();
    }

    public void refresh(){
        Gdx.app.log("GameSettingsWindow", "Refreshed");
        if (YayPipe.playService.isConnectedToInternet()) {
            if (YayPipe.playService.isSignedIn()) {
                googlePlayButton.setText("Connected");
                save.setTouchable(Touchable.enabled);
                save.setColor(0,0,0,1f);
                load.setTouchable(Touchable.enabled);
                load.setColor(0,0,0,1f);
            } else {
                googlePlayButton.setText("Disconnected");
                save.setTouchable(Touchable.disabled);
                save.setColor(0,0,0,0.5f);
                load.setTouchable(Touchable.disabled);
                load.setColor(0,0,0,0.5f);
            }
        } else {
            save.setTouchable(Touchable.disabled);
            save.setColor(0,0,0,0.5f);
            load.setTouchable(Touchable.disabled);
            load.setColor(0,0,0,0.5f);
        }
    }

    @Override
    public Dialog show(Stage stage) {
        refresh();
        return super.show(stage);
    }

    @Override
    public float getPrefWidth() {
        return YayPipe.SCREEN_WIDTH * 0.95f;
    }

    @Override
    public float getPrefHeight() {
        return getPrefWidth() * 0.85f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        refreshTimer += delta;
        if (refreshTimer > REFRESH_RATE){
            refresh();
            refreshTimer = 0;
        }

        if (quitIcon.isTouched() || Gdx.input.isKeyPressed(Input.Keys.BACK)){
            quitIcon.setTouched(false);
            hide();
        }
    }
}
