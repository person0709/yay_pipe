package com.lazybean.yaypipe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.gui.Background;
import com.lazybean.yaypipe.gui.Gui;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Linear;

public abstract class GameScreen implements Screen {
    protected YayPipe game;
    protected Stage stage;
    protected Background background;
    protected TweenManager tweenManager;

    private Label message;
    private Color backgroundColour;

    public GameScreen(YayPipe game, Color backgroundColour){
        this.game = game;
        this.backgroundColour = backgroundColour;

        stage = new Stage(new ScreenViewport(), game.stage.getBatch());

        tweenManager = new TweenManager();
    }

    public void addMessage(String string){
        if (message == null) {
            message = new Label(string, game.assetLoader.uiSkin, "message");
        }
        else {
            message.remove();
            message.setText(string);
            tweenManager.update(3f);
        }
        message.setAlignment(Align.center);
        message.setPosition(YayPipe.SCREEN_WIDTH / 2, YayPipe.SCREEN_HEIGHT / 2, Align.center);
        message.setTouchable(Touchable.disabled);
        Timeline.createParallel()
                .beginSequence()
                .push(Tween.set(message, SpriteAccessor.ALPHA).target(0f).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        game.stage.addActor(message);
                    }
                }))
                .push(Tween.to(message, SpriteAccessor.ALPHA,0.25f).target(1f))
                .pushPause(1.5f)
                .push(Tween.to(message, SpriteAccessor.ALPHA,0.25f).target(0f))
                .end()
                .push(Tween.to(message, SpriteAccessor.POSITION, 2f).targetRelative(0, message.getHeight() / 2).ease(Linear.INOUT))
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        message.remove();
                    }
                })
                .start(tweenManager);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(backgroundColour.r, backgroundColour.g, backgroundColour.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);
    }

    @Override
    public void dispose() {
        stage.dispose();
        tweenManager.killAll();
    }

    public Stage getStage(){
        return stage;
    }
}
