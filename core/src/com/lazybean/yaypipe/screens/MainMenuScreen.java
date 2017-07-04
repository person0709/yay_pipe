package com.lazybean.yaypipe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Colour;
import com.lazybean.yaypipe.gamehelper.Difficulty;
import com.lazybean.yaypipe.gamehelper.GridSize;
import com.lazybean.yaypipe.gui.Background;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.gui.Gui;
import com.lazybean.yaypipe.gui.Icon;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gui.QuitWindow;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Quart;
import aurelienribon.tweenengine.equations.Sine;

public class MainMenuScreen extends GameScreen{
    private Stage stage;
    private AssetLoader assetLoader;
    private TweenManager tweenManager;

    private QuitWindow quitWindow;

    private Icon settings;

    private Image e;
    private Actor drop;
    private Vector2 pipePos = new Vector2();
    private Animation<TextureRegion> inPipe, splash;
    private float pipeAnimationStateTime = 0;
    private float splashAnimationStateTime = 0;
    private boolean isAnimationStart = false;
    private boolean isDropFinished = false;
    private boolean splashAnimationStart = false;

    private Sound waterDropSound = Gdx.audio.newSound(Gdx.files.internal("waterDrop.ogg"));
    public MainMenuScreen(YayPipe game) {
        super(game);
        stage = new Stage(new ScreenViewport());
        assetLoader = game.assetLoader;

        Gdx.input.setCatchBackKey(true);
        this.tweenManager = new TweenManager();

        quitWindow = new QuitWindow(assetLoader);

        InputProcessor inputProcessor = new InputAdapter(){
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.BACK){
                    quitWindow.show(stage);
                }
                return true;
            }
        };

        InputMultiplexer multiplexer = new InputMultiplexer(stage, inputProcessor);
        Gdx.input.setInputProcessor(multiplexer);

        settings = new Icon(assetLoader.circle, assetLoader.settings);
        settings.setColor(Color.BLACK);
        settings.setDiameter(settings.getHeight() * 0.8f);
        settings.setPosition(Gdx.graphics.getWidth() - settings.getWidth() * 1.2f, Gdx.graphics.getHeight() - settings.getHeight() * 1.2f);

        stage.addActor(settings);
    }

    public void show() {
        Group yay = new Group();
        yay.setWidth(Gdx.graphics.getWidth() * 0.6f);
        yay.setHeight(Gdx.graphics.getHeight() * 0.14f);

        Image y_1 = new Image(assetLoader.y_1);
        y_1.setWidth(yay.getWidth() * 0.33f);
        y_1.setAlign(Align.bottom);
        y_1.setScaling(Scaling.fillX);
        y_1.setScale(0f);
        y_1.setOrigin(y_1.getWidth() / 2, yay.getHeight()/2);

        Image a = new Image(assetLoader.a);
        a.setWidth(yay.getWidth() * 0.35f);
        a.setAlign(Align.bottom);
        a.setScaling(Scaling.fillX);
        a.setScale(0f);
        a.setOrigin(a.getWidth() / 2, yay.getHeight()/2);

        Image y_2 = new Image(assetLoader.y_2);
        y_2.setWidth(yay.getWidth() * 0.32f);
        y_2.setAlign(Align.bottom);
        y_2.setScaling(Scaling.fillX);
        y_2.setScale(0f);
        y_2.setOrigin(y_2.getWidth() / 2, yay.getHeight()/2);

        Image ex_mark = new Image(assetLoader.ex_mark);
        ex_mark.setWidth(yay.getWidth() * 0.117f);
        ex_mark.setAlign(Align.bottom);
        ex_mark.setScaling(Scaling.fillX);
        ex_mark.setScale(0f);
        ex_mark.setOrigin(ex_mark.getWidth() / 2, yay.getHeight()/2);

        yay.addActor(y_1);
        yay.addActor(a);
        a.setPosition(y_1.getX() + y_1.getWidth() * 0.85f, 0);
        yay.addActor(y_2);
        y_2.setPosition(a.getX() + a.getWidth() * 0.85f, 0);
        yay.addActor(ex_mark);
        ex_mark.setPosition(y_2.getRight() + ex_mark.getWidth() * 0.1f, 0);


        Group pipe = new Group();
        pipe.setWidth(Gdx.graphics.getWidth() * 0.6f);
        pipe.setHeight(Gdx.graphics.getHeight() * 0.14f);

        Image p_1 = new Image(assetLoader.p_1);
        p_1.setWidth(pipe.getWidth() * 0.3f);
        p_1.setAlign(Align.top);
        p_1.setScaling(Scaling.fillX);
        p_1.setScale(0f);
        p_1.setOrigin(p_1.getWidth() / 2, 0);

        Image i = new Image(assetLoader.i);
        i.setWidth(pipe.getWidth() * 0.14f);
        i.setAlign(Align.top);
        i.setScaling(Scaling.fillX);
        i.setScale(0f);
        i.setOrigin(i.getWidth() / 2, 0);

        Image p_2 = new Image(assetLoader.p_2);
        p_2.setWidth(pipe.getWidth() * 0.29f);
        p_2.setAlign(Align.top);
        p_2.setScaling(Scaling.fillX);
        p_2.setScale(0f);
        p_2.setOrigin(p_2.getWidth() / 2, 0);

        e = new Image(assetLoader.e);
        e.setWidth(pipe.getWidth() * 0.26f);
        e.setAlign(Align.top);
        e.setScaling(Scaling.fillX);
        e.setScale(0f);
        e.setOrigin(e.getWidth() / 2, 0);

        pipe.addActor(p_1);
        p_1.setPosition(p_1.getWidth() * 0.05f, pipe.getTop() - p_1.getHeight());
        pipe.addActor(i);
        i.setPosition(p_1.getRight() + p_1.getWidth() * 0.05f, pipe.getTop() - i.getHeight());
        pipe.addActor(p_2);
        p_2.setPosition(i.getRight() + i.getWidth() * 0.07f, pipe.getTop() - p_2.getHeight());
        pipe.addActor(e);
        e.setPosition(p_2.getRight() + p_2.getWidth() * 0.03f, pipe.getTop() - e.getHeight());

        Array<Image> titleArray = new Array<>(8);
        titleArray.addAll(y_1, a, y_2, ex_mark, p_1, i, p_2, e);
        titleArray.shuffle();

        Timeline.createParallel()
                .push(Tween.to(titleArray.get(0), SpriteAccessor.SCALE, 0.25f).target(1f).ease(Back.OUT).delay(0.05f))
                .push(Tween.to(titleArray.get(1), SpriteAccessor.SCALE, 0.25f).target(1f).ease(Back.OUT).delay(0.1f))
                .push(Tween.to(titleArray.get(2), SpriteAccessor.SCALE, 0.25f).target(1f).ease(Back.OUT).delay(0.15f))
                .push(Tween.to(titleArray.get(3), SpriteAccessor.SCALE, 0.25f).target(1f).ease(Back.OUT).delay(0.2f))
                .push(Tween.to(titleArray.get(4), SpriteAccessor.SCALE, 0.25f).target(1f).ease(Back.OUT).delay(0.25f))
                .push(Tween.to(titleArray.get(5), SpriteAccessor.SCALE, 0.25f).target(1f).ease(Back.OUT).delay(0.3f))
                .push(Tween.to(titleArray.get(6), SpriteAccessor.SCALE, 0.25f).target(1f).ease(Back.OUT).delay(0.35f))
                .push(Tween.to(titleArray.get(7), SpriteAccessor.SCALE, 0.25f).target(1f).ease(Back.OUT).delay(0.4f))
                .beginSequence()
                .pushPause(0.8f)
                .push(Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        isAnimationStart = true;
                    }
                }))
                .start(tweenManager);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = assetLoader.mediumFont_anja;
        style.fontColor = Color.BLACK;
        style.pressedOffsetY = -10;

        TextButton newGame = new TextButton("NEW GAME", style);
        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                assetLoader.click.play(AssetLoader.prefs.getFloat("soundVolume"));
                if (AssetLoader.prefs.getBoolean("firstRun", true)){
                    dispose();
                    game.setScreen(new GamePlayScreen(game, GridSize.TUTORIAL, Difficulty.TUTORIAL_BASIC));
                } else {
                    dispose();
                    game.setScreen(new GameSettingScreen(game, assetLoader));
                }
            }
        });

        TextButton achievement = new TextButton("ACHIEVEMENT", style);
        achievement.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                assetLoader.click.play(AssetLoader.prefs.getFloat("soundVolume"));
                YayPipe.playService.showAchievement();
            }
        });

        TextButton statistics = new TextButton("STATISTICS", style);
        statistics.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                assetLoader.click.play(AssetLoader.prefs.getFloat("soundVolume"));
                dispose();
                game.setScreen(new StatisticsScreen(game, assetLoader));
            }
        });

        // TODO: 11/09/2016 purchase test
        TextButton test = new TextButton("TEST", style);
        test.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                YayPipe.playService.removeAds();
            }
        });

        int highScore = AssetLoader.prefs.getInteger("highScore");
        Label.LabelStyle labelStyle = new Label.LabelStyle(assetLoader.smallFont_anja, Color.BLACK);
        Label highScoreLabel = new Label("High Score\n" + String.valueOf(highScore), labelStyle);
        highScoreLabel.setAlignment(Align.top);

        Table table = new Table();
        table.setFillParent(true);
        table.add(yay).padTop(Gdx.graphics.getHeight() * 0.2f).expandX().height(yay.getHeight()).align(Align.center);
        table.row();
        table.add(pipe).padTop(pipe.getHeight()*0.15f).expandX().height(pipe.getHeight()).align(Align.center).expandY();
        table.row();
        table.add(newGame).padTop(Gdx.graphics.getHeight() * 0.1f);
        table.row();
        table.add(statistics);
        table.row();
        table.add(achievement);
        table.row();
        table.add(test).padBottom(Value.percentHeight(0.5f));
        table.row();
        table.add(highScoreLabel).height(Gdx.graphics.getHeight() * 0.2f);

        stage.addActor(table);


        table.validate();
        pipePos = e.localToStageCoordinates(new Vector2());

        drop = new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                Color color = new Color(batch.getColor());
                batch.setColor(drop.getColor());
                batch.draw(assetLoader.waterDrop, drop.getX(), drop.getY(), e.getWidth() * 0.5f, e.getWidth() * 0.5f);
                batch.setColor(color);
            }
        };

        Tween.set(drop, SpriteAccessor.ALPHA).target(0f).start(tweenManager);
        stage.addActor(drop);

        Actor inPipeActor = new Actor(){
            @Override
            public void act(float delta) {
                if (isAnimationStart) {
                    pipeAnimationStateTime += delta;
                }
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                if (isAnimationStart) {
                    batch.draw((TextureRegion) inPipe.getKeyFrame(pipeAnimationStateTime, false),
                            pipePos.x + e.getWidth() * 0.1f, pipePos.y - e.getHeight() * 1.46f, e.getWidth() * 0.4f, e.getWidth() * 0.4f);
                }
            }
        };

        stage.addActor(inPipeActor);

        Actor splashActor = new Actor(){
            @Override
            public void act(float delta) {
                if (isAnimationStart && splashAnimationStart) {
                    splashAnimationStateTime += delta;
                }
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                if (isAnimationStart && splashAnimationStart)
                batch.draw(splash.getKeyFrame(splashAnimationStateTime, false),
                        pipePos.x + e.getWidth() * 0.1f, 0, e.getWidth() * 0.5f, e.getWidth() * 0.5f);
            }
        };

        stage.addActor(splashActor);

        inPipe = new Animation(0.7f / 9f, assetLoader.uiSkin.getAtlas().findRegions("drop"));
        splash = new Animation(0.05f, assetLoader.uiSkin.getAtlas().findRegions("splash"));


    }

    private boolean firstLoop = true;

    @Override
    public void render(float delta) {
        super.render(delta);
        tweenManager.update(delta);

        stage.act(delta);
        stage.draw();

//        stage.getBatch().begin();
//        stage.getBatch().draw(assetLoader.water_lb, 100 ,100);
//        stage.getBatch().end();

        if(isAnimationStart) {
            if (firstLoop) {
                Timeline.createSequence()
                        .pushPause(0.45f)
                        .push(Tween.set(drop, SpriteAccessor.POSITION).target(pipePos.x + e.getWidth() * 0.03f, pipePos.y - e.getHeight() * 1.7f))
                        .push(Tween.to(drop, SpriteAccessor.ALPHA, 0.1f).target(1f))
                        .push(Tween.to(drop, SpriteAccessor.POSITION, 1f).target(pipePos.x + e.getWidth() * 0.03f, 0).ease(Sine.IN))
                        .push(Tween.set(drop, SpriteAccessor.ALPHA).target(0f))
                        .repeat(Tween.INFINITY,0f).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        splashAnimationStart = true;
                        isDropFinished = true;
                        waterDropSound.play(AssetLoader.prefs.getFloat("soundVolume"));
                    }
                }).setCallbackTriggers(TweenCallback.END)
                        .start(tweenManager);
                firstLoop = false;
            }

            if (inPipe.isAnimationFinished(pipeAnimationStateTime)){
                if (isDropFinished){
                    isDropFinished = false;
                    pipeAnimationStateTime = 0;
                }
            }

            if (splash.isAnimationFinished(splashAnimationStateTime)){
                splashAnimationStart = false;
                splashAnimationStateTime = 0;
            }
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
        stage.dispose();
        tweenManager.killAll();
    }
}