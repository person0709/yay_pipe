package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gamehelper.FontType;
import com.lazybean.yaypipe.gamehelper.IconType;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;

public class GameSettingWindow extends GameWindow{
    private Icon quitIcon;
    private TextButton googlePlayButton;

    public GameSettingWindow(AssetLoader assetLoader) {
        super(assetLoader.uiSkin);

        Label.LabelStyle titleStyle = new Label.LabelStyle(assetLoader.getFont(FontType.ANJA_MEDIUM), Color.BLACK);
        Label title = new Label("SETTINGS", titleStyle);

        getContentTable().add(title).padBottom(Value.percentHeight(0.5f)).row();

        Table table = new Table();
        table.defaults().height(Value.percentHeight(2f, title));

        Label.LabelStyle settingStyle = new Label.LabelStyle(assetLoader.getFont(FontType.ANJA_MEDIUM_SMALL), Color.BLACK);
        Label sound = new Label("Sound", settingStyle);
        sound.setAlignment(Align.center, Align.center);

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

        table.add(sound).width(sound.getWidth() * 1.2f).align(Align.center);
        table.add(soundSlider).width(YayPipe.SCREEN_WIDTH * 0.6f).padLeft(Value.percentWidth(0.25f, sound)).row();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = assetLoader.getFont(FontType.ANJA_MEDIUM_SMALL);
        buttonStyle.fontColor = Color.BLACK;
        buttonStyle.up = assetLoader.button;

        googlePlayButton = new TextButton("Disconnected", buttonStyle);

        Image googlePlayIcon = new Image(assetLoader.getIconTexture(IconType.GOOGLE_PLAY));
        googlePlayIcon.setColor(Color.BLACK);
        googlePlayIcon.setScaling(Scaling.fillY);

        googlePlayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (YayPipe.playService.isSignedIn()){
                    YayPipe.playService.signOut();
                    googlePlayButton.setText("Disconnected");
                } else {
                    YayPipe.playService.startSignInIntent();
                    googlePlayButton.setText("Connected");
                }
            }
        });

        table.add(googlePlayIcon);
        table.add(googlePlayButton).size(googlePlayButton.getWidth() * 1.2f, googlePlayButton.getHeight() * 1.4f)
                .padLeft(Value.percentWidth(0.25f, sound));

        getContentTable().add(table);


        pack();

//        debugAll();

        //prevents quitIcon from clipping
        setClip(false);

        quitIcon = new Icon(assetLoader, IconType.CROSS, Icon.MENU_DIAMETER);
        quitIcon.setDiameter(YayPipe.SCREEN_WIDTH * 0.1f);
        quitIcon.setColor(CustomColor.RED.getColor());
        quitIcon.setPosition(getWidth() - quitIcon.getWidth() * 1.2f,
                getHeight() - quitIcon.getHeight() * 1.2f);

        addActor(quitIcon);
    }

    @Override
    public Dialog show(Stage stage) {
        if (YayPipe.playService.isSignedIn()){
            googlePlayButton.setText("Connected");
        } else {
            googlePlayButton.setText("Disconnected");
        }
        return super.show(stage);
    }

    @Override
    public float getPrefWidth() {
        return YayPipe.SCREEN_WIDTH * 0.95f;
    }

    @Override
    public float getPrefHeight() {
        return getPrefWidth() * 0.65f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (quitIcon.isTouched()){
            quitIcon.setTouched(false);
            hide();
        }
    }
}
