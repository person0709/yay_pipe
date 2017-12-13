package com.lazybean.yaypipe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lazybean.yaypipe.gamehelper.AdHelper;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gamehelper.ScreenManager;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;
import com.lazybean.yaypipe.gamehelper.PathLoader;
import com.lazybean.yaypipe.gamehelper.PlayService;
import com.lazybean.yaypipe.gui.Background;
import com.lazybean.yaypipe.screens.SplashScreen;

import aurelienribon.tweenengine.Tween;

public class YayPipe extends Game {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static final Color BACKGROUND_COLOUR = CustomColor.YELLOW.getColor();

    public static int replayCount = 0;
    public static int consecutiveClearCount = 0;
    public static int consecutiveRestartCount = 0;
    public static int consecutiveFailCount = 0;

    public AssetLoader assetLoader;
    public static PlayService playService;
    public static AdHelper adHelper;

    public Stage stage;
    private Background fadeInOut;

    public ScreenManager screenManager;

    public YayPipe(PlayService playService, AdHelper adHelper) {
        YayPipe.playService = playService;
        YayPipe.adHelper = adHelper;
    }

    @Override
	public void create () {
        Gdx.app.log("Game","create");

        Tween.registerAccessor(Group.class, new SpriteAccessor());
        Tween.registerAccessor(Actor.class, new SpriteAccessor());

        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();

        stage = new Stage(new ScreenViewport());

        fadeInOut = new Background();
        fadeInOut.setColor(Color.BLACK);
        fadeInOut.setAlpha(0f);

        stage.addActor(fadeInOut);

        assetLoader = new AssetLoader();

        PathLoader.load();
        assetLoader.manager.load("splash_logo.png", Texture.class);
        assetLoader.manager.finishLoading();
        this.setScreen(new SplashScreen(this));
	}

	@Override
	public void render () {
        super.render();
        stage.act();
        stage.draw();
	}

    @Override
    public void setScreen(final Screen screen) {
        fadeInOut.addAction(Actions.sequence(
                Actions.alpha(1, 0.5f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        YayPipe.super.setScreen(screen);
                    }
                }),
                Actions.alpha(0, 0.5f)
        ));
    }

    @Override
    public void pause() {
        Gdx.app.log("Game","paused");

        GameData.getInstance().saveLocal();
        super.pause();
    }

    public void dispose(){
        Gdx.app.log("Game","disposed");
        assetLoader.dispose();
        PathLoader.dispose();
        GameData.getInstance().saveLocal();
        super.dispose();
    }
}
