package com.lazybean.yaypipe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lazybean.yaypipe.gamehelper.AchievementType;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Difficulty;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;
import com.lazybean.yaypipe.gamehelper.GameState;
import com.lazybean.yaypipe.gamehelper.GridSize;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.StatisticsType;
import com.lazybean.yaypipe.gui.Gui;

public class GamePlayScreen extends GameScreen {
    private AssetLoader assetLoader;

    private GameWorld gameWorld;
    private Gui gui;

    private Stage gameWorldStage;
    private Stage guiStage;

    public GamePlayScreen(YayPipe game, Difficulty gridSize, GridSize difficulty) {
        super(game, YayPipe.BACKGROUND_COLOUR);
        assetLoader = game.assetLoader;

        //loading ads
        if (YayPipe.replayCount >= 3){
            YayPipe.adHelper.showAd();
            YayPipe.replayCount = 0;
        }
        else {
            YayPipe.replayCount++;
        }

        gameWorldStage = stage;

        guiStage = new Stage(new ScreenViewport(), gameWorldStage.getBatch());

        InputMultiplexer im = new InputMultiplexer(guiStage, gameWorldStage);
        Gdx.input.setInputProcessor(im);

        gameWorld = new GameWorld(gameWorldStage, assetLoader, difficulty, gridSize);
        gui = new Gui(guiStage, gameWorld, assetLoader);
        
        gameWorld.attachGui(gui);

    }



    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {
//        Gdx.app.log("FPS", String.valueOf(Gdx.graphics.getFramesPerSecond()));
        super.render(delta);
        gameWorld.update(delta);
        gui.update(delta);

        gameWorldStage.draw();
        guiStage.draw();

        if (gameWorld.getState() == GameState.RESTART || gameWorld.getState() == GameState.QUIT) {
            // consecutive count reset;
            YayPipe.consecutiveClearCount = 0;

            //coward! achievement
            YayPipe.consecutiveRestartCount++;
            if (YayPipe.consecutiveRestartCount == 5){
                YayPipe.playService.unlockAchievement(AchievementType.COWARD);
            }

            if (gameWorld.isRestart()) {
                GameData.getInstance().statistics.incrementValue(StatisticsType.TOTAL_RESTART, 1);
                game.setScreen(new GamePlayScreen(game, gameWorld.difficulty, gameWorld.gridSize));
                dispose();
            }
            else {
                game.setScreen(game.screenManager.getMainMenuScreen());
                dispose();
            }

            gameWorld.setState(GameState.IDLE);
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
        super.dispose();
        Gdx.app.log("GamePlayScreen","disposed");
        gameWorld.dispose();
        gui.dispose();
        if (GameData.getInstance().statistics.isTimerOn()){
            GameData.getInstance().statistics.stopTimer();
        }
    }
}