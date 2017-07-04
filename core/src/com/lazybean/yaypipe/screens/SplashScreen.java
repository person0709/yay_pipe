package com.lazybean.yaypipe.screens;

import com.badlogic.gdx.Gdx;
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

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class SplashScreen extends GameScreen{
    private AssetLoader assetLoader;
    private Stage stage;

    private TweenManager tweenManager;

    public SplashScreen(YayPipe game){
        super(game);
        assetLoader = game.assetLoader;

        this.tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new SpriteAccessor());
        Tween.registerAccessor(Group.class, new SpriteAccessor());
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        stage = new Stage(new ScreenViewport());
    }
    @Override
    public void show() {
        Texture texture = assetLoader.manager.get("logo.png");
        Image logo = new Image(texture);

        Container<Image> container = new Container<>(logo);
        container.setFillParent(true);
        container.align(Align.center);
        container.prefSize(Gdx.graphics.getWidth() * 0.7f, Gdx.graphics.getWidth() * 0.7f);

        stage.addActor(container);
        assetLoader.loadManager();
        assetLoader.manager.finishLoading();
        assetLoader.assignAssets();

        stage.addAction(Actions.delay(3, Actions.run(new Runnable() {
            @Override
            public void run() {
                dispose();
                game.setScreen(new MainMenuScreen(game));
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

    @Override
    public void dispose() {
        stage.dispose();
        tweenManager.killAll();
    }
}
