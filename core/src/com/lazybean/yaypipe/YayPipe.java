package com.lazybean.yaypipe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lazybean.yaypipe.gamehelper.AdHelper;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.PathLoader;
import com.lazybean.yaypipe.gamehelper.PlayService;
import com.lazybean.yaypipe.gui.Background;
import com.lazybean.yaypipe.screens.SplashScreen;

public class YayPipe extends Game {
    public static float SCREEN_WIDTH;
    public static float SCREEN_HEIGHT;
    public static float BLOCK_LENGTH;
    public static float BLOCK_GAP;

    public static int replayCount = 0;
    public static int consecutiveClearCount = 0;
    public static int consecutiveRestartCount = 0;
    public static int consecutiveFailCount = 0;

    public AssetLoader assetLoader;
    public static PlayService playService;
    public static AdHelper adHelper;

    public SpriteBatch batch;
    private Stage stage;
    private Background fadeInOut;

    public YayPipe(PlayService playService, AdHelper adHelper) {
        YayPipe.playService = playService;
        YayPipe.adHelper = adHelper;
    }

    @Override
	public void create () {
        Gdx.app.log("Game","create");

        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
        BLOCK_LENGTH = SCREEN_WIDTH * 0.1f;
        BLOCK_GAP = BLOCK_LENGTH * 0.04f;

        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport(), batch);
        fadeInOut = new Background();
        fadeInOut.setColor(1,1,1,0);

        stage.addActor(fadeInOut);

        assetLoader = new AssetLoader();

        PathLoader.load();
        assetLoader.manager.load("logo.png", Texture.class);
        assetLoader.manager.finishLoading();
        this.setScreen(new SplashScreen(this));
	}

	@Override
	public void render () {
        stage.act();
        stage.draw();
        super.render();
	}

    @Override
    public void setScreen(Screen screen) {
        fadeInOut.addAction(Actions.sequence(
                Actions.fadeIn(0.3f),
                Actions.run(() -> YayPipe.super.setScreen(screen)),
                Actions.fadeOut(0.3f)
        ));
    }

    public void dispose(){
        Gdx.app.log("Game","disposed");
        assetLoader.dispose();
        PathLoader.dispose();
        super.dispose();
    }
}
