package com.lazybean.yaypipe.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;

import aurelienribon.tweenengine.Tween;

public class SplashScreen extends GameScreen{
    private AssetLoader assetLoader;

    private Stage stage;

    public SplashScreen(YayPipe game){
        super(game, Color.BLACK);
        assetLoader = game.assetLoader;
        stage = new Stage(new ScreenViewport(), game.stage.getBatch());

        Tween.registerAccessor(Actor.class, new SpriteAccessor());
        Tween.registerAccessor(Group.class, new SpriteAccessor());
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        Texture texture = assetLoader.manager.get("splash_logo.png");
        Image logo = new Image(texture);

        Container<Image> container = new Container<>(logo);
        container.setFillParent(true);
        container.align(Align.center);
        container.prefSize(YayPipe.SCREEN_WIDTH * 0.7f, YayPipe.SCREEN_WIDTH * 0.7f);

        stage.addActor(container);
        assetLoader.loadManager();
        assetLoader.manager.finishLoading();
        assetLoader.assignAssets();

        YayPipe.playService.startSignInIntent();
    }

    @Override
    public void show() {
        YayPipe.androidHelper.hideProgressBar();
        stage.addAction(Actions.delay(3, Actions.run(new Runnable() {
            @Override
            public void run() {
                GameData.getInstance().loadFromLocal();
                game.screenManager.getGameSetUpScreen();
                game.setScreenWithFadeInOut(game.screenManager.getMainMenuScreen());
            }
        })));

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act();
        stage.draw();
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
}
