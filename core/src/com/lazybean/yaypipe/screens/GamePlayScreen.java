package com.lazybean.yaypipe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Difficulty;
import com.lazybean.yaypipe.gamehelper.GridSize;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gui.Gui;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Quart;

public class GamePlayScreen extends GameScreen {
    private AssetLoader assetLoader;

    private GameWorld gameWorld;
    private Gui gui;

    private Stage gameWorldStage;
    private Stage guiStage;

    private TweenManager tweenManager;


    public GamePlayScreen(YayPipe game, GridSize gridSize, Difficulty difficulty) {
        super(game);
        assetLoader = game.assetLoader;
        tweenManager = new TweenManager();

        //loading ads
        if (game.replayCount >= 3){
            YayPipe.adHelper.showAd();
            game.replayCount = 0;
        }
        else {
            game.replayCount++;
        }

        gameWorldStage = new Stage(new ScreenViewport());
        guiStage = new Stage(new ScreenViewport(), game.batch);

        InputMultiplexer im = new InputMultiplexer(guiStage, gameWorldStage);
        Gdx.input.setInputProcessor(im);

        gameWorld = new GameWorld(gameWorldStage, assetLoader, gridSize, difficulty);

    }



    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {
//        Gdx.app.log("FPS", String.valueOf(Gdx.graphics.getFramesPerSecond()));
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);
        gameWorld.update(delta);

        gameWorldStage.act();
        gameWorldStage.draw();

        gameUI.act();
        gameUI.draw();

        if (gameWorld.isRestart() || gameWorld.isQuit()) {
            gameUI.addActor(fadeInOut);
            Tween.to(fadeInOut, SpriteAccessor.ALPHA, 0.5f).target(1f).ease(Quart.OUT)
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            if (gameWorld.isRestart()) {
                                AssetLoader.stats.incrementValue("totalRestartPerformed");
                                dispose();
                                game.setScreen(new GamePlayScreen(game, assetLoader));
                            }
                            else {
                                dispose();
                                game.setScreen(new MainMenuScreen(game, assetLoader));
                            }
                        }
                    }).start(tweenManager);
        }
    }


    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        Gdx.app.log("GamePlayScreen","disposed");
        gameWorld.dispose();
        if (AssetLoader.stats.isTimerOn()){
            AssetLoader.stats.stopTimer();
        }
        AssetLoader.stats.saveData();
    }
}