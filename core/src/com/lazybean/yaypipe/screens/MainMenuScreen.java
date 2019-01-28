package com.lazybean.yaypipe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.IconType;
import com.lazybean.yaypipe.gamehelper.SoundType;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;
import com.lazybean.yaypipe.gamehelper.StatisticsType;
import com.lazybean.yaypipe.guiobjects.GameSettingWindow;
import com.lazybean.yaypipe.guiobjects.Icon;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.guiobjects.QuitWindow;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Sine;

public class MainMenuScreen extends GameScreen{
    private AssetLoader assetLoader;

    private Stage stage;

    private QuitWindow quitWindow;

    private Icon settings;
    private Label highScoreLabel;

    private Image y_1, a, y_2, ex_mark, p_1, i, p_2, e;
    private Vector2 dropStartPos;
    private Actor drop;
    private Actor inPipeActor;
    private Actor splashActor;
    private Animation<TextureRegion> inPipe, splash;
    private float animStateTime = 0;

    private boolean animPhaseOne = false; // water accumulating at the pipe
    private boolean animPhaseTwo = false; // waterdrop falling
    private boolean animPhaseThree = false; // waterdrop splashing

    private TweenCallback animPhaseOneStart;
    private TweenCallback animPhaseTwoStart;
    private TweenCallback animPhaseThreeStart;

//    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public MainMenuScreen(YayPipe passedGame) {
        super(passedGame, YayPipe.BACKGROUND_COLOUR);

        assetLoader = passedGame.assetLoader;
        stage = new Stage(new ScreenViewport(), game.stage.getBatch());

        quitWindow = new QuitWindow(assetLoader);

        final GameSettingWindow settingWindow = new GameSettingWindow(game, stage);

        settings = new Icon(assetLoader, IconType.SETTINGS, Icon.MENU_DIAMETER);
        settings.setColor(Color.BLACK);
        settings.setDiameter(settings.getHeight() * 0.8f);
        settings.setPosition(YayPipe.SCREEN_WIDTH - settings.getWidth() * 1.2f, YayPipe.SCREEN_HEIGHT - settings.getHeight() * 1.2f);
        settings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settingWindow.show(stage);
            }
        });

        stage.addActor(settings);

        Group yay = new Group();
        yay.setWidth(Gdx.graphics.getWidth() * 0.6f);
        yay.setHeight(Gdx.graphics.getHeight() * 0.14f);

        y_1 = new Image(assetLoader.y_1);
        y_1.setWidth(yay.getWidth() * 0.33f);
        y_1.setAlign(Align.bottom);
        y_1.setScaling(Scaling.fillX);
        y_1.setScale(0f);
        y_1.setOrigin(y_1.getWidth() / 2, yay.getHeight()/2);

        a = new Image(assetLoader.a);
        a.setWidth(yay.getWidth() * 0.35f);
        a.setAlign(Align.bottom);
        a.setScaling(Scaling.fillX);
        a.setScale(0f);
        a.setOrigin(a.getWidth() / 2, yay.getHeight()/2);

        y_2 = new Image(assetLoader.y_2);
        y_2.setWidth(yay.getWidth() * 0.32f);
        y_2.setAlign(Align.bottom);
        y_2.setScaling(Scaling.fillX);
        y_2.setScale(0f);
        y_2.setOrigin(y_2.getWidth() / 2, yay.getHeight()/2);

        ex_mark = new Image(assetLoader.ex_mark);
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


        final Table pipe = new Table();
        pipe.setWidth(YayPipe.SCREEN_WIDTH * 0.6f);

        p_1 = new Image(assetLoader.p_1);
        p_1.setScaling(Scaling.fillX);
        p_1.setScale(0f);
        p_1.setOrigin(p_1.getWidth() / 2, p_1.getHeight() / 2);

        i = new Image(assetLoader.i);
        i.setScaling(Scaling.fillX);
        i.setScale(0f);
        i.setOrigin(i.getWidth() / 2, i.getHeight() / 2);

        p_2 = new Image(assetLoader.p_2);
        p_2.setScaling(Scaling.fillX);
        p_2.setScale(0f);
        p_2.setOrigin(p_2.getWidth() / 2, p_2.getHeight() / 2);

        e = new Image(assetLoader.e);
        e.setWidth(pipe.getWidth() * 0.26f);
        e.setScaling(Scaling.fillX);
        e.setScale(0f);
        e.setOrigin(e.getWidth() / 2, e.getHeight() / 2);
        e.layout();

        pipe.add(p_1).width(Value.percentWidth(0.3f, pipe)).padRight(Value.percentWidth(0.05f)).align(Align.topLeft);
        pipe.add(i).width(Value.percentWidth(0.14f, pipe)).padRight(Value.percentWidth(0.05f)).align(Align.topLeft);
        pipe.add(p_2).width(Value.percentWidth(0.29f, pipe)).padRight(Value.percentWidth(0.05f)).align(Align.topLeft);
        pipe.add(e).width(Value.percentWidth(0.26f, pipe)).align(Align.topLeft);


        TextButton newGame = new TextButton("NEW GAME", assetLoader.uiSkin, "mainMenu");
        newGame.pad(Value.percentHeight(0.1f));
        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("NewGame", "Touched");
                assetLoader.getSound(SoundType.CLICK).play(GameData.getInstance().getSoundVolume());
                game.setScreenWithFadeInOut(game.screenManager.getGameSetUpScreen());

            }
        });

        TextButton achievement = new TextButton("ACHIEVEMENT", assetLoader.uiSkin, "mainMenu");
        achievement.pad(Value.percentHeight(0.1f));
        achievement.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                assetLoader.getSound(SoundType.CLICK).play(GameData.getInstance().getSoundVolume());
                YayPipe.playService.showAchievement();
            }
        });
//
//        TextButton statistics = new TextButton("STATISTICS", assetLoader.uiSkin, "mainMenu");
//        statistics.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                assetLoader.click.play(AssetLoader.prefs.getFloat("soundVolume"));
//                dispose();
//                game.setScreenWithFadeInOut(new StatisticsScreen(game, assetLoader));
//            }
//        });

        highScoreLabel = new Label("", assetLoader.uiSkin, "mainMenuHighScore");
        highScoreLabel.setAlignment(Align.center);

        Table table = new Table();
        table.setFillParent(true);
        table.add(yay).size(yay.getWidth(), yay.getHeight()).padTop(YayPipe.SCREEN_HEIGHT * 0.2f).align(Align.center);
        table.row();
        table.add(pipe).size(YayPipe.SCREEN_WIDTH * 0.6f, e.getImageHeight()).padTop(yay.getHeight() * 0.5f).align(Align.top);
        table.row();
        table.add(newGame).padTop(YayPipe.SCREEN_HEIGHT * 0.1f).padBottom(Value.percentHeight(0.1f));
        table.row();
//        table.add(statistics);
//        table.row();
        table.add(achievement).row();
        table.add(highScoreLabel).align(Align.bottom).padBottom(YayPipe.SCREEN_HEIGHT * 0.1f).expandY();

//        table.debug();

        stage.addActor(table);

        table.validate();

        dropStartPos = new Vector2(pipe.getX() + pipe.getWidth() * 0.95f, pipe.getTop() - e.getImageHeight());

        inPipeActor = new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                if (animPhaseOne) {
                    batch.draw(inPipe.getKeyFrame(animStateTime, false), getX(), getY(), getWidth(), getHeight());
                }
            }
        };
        inPipeActor.setSize(e.getImageWidth() * 0.4f, e.getImageWidth() * 0.4f);
        stage.addActor(inPipeActor);


        drop = new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                Color batchColor = batch.getColor();
                Color color = getColor();
                batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
                batch.draw(assetLoader.waterDrop, getX() - getWidth() / 2, getY() - getHeight(),getOriginX(), getOriginY(), getWidth(), getHeight(),
                        getScaleX(), getScaleY(), getRotation());
                batch.setColor(batchColor);
            }
        };
        drop.setSize(e.getImageHeight() * 0.4f, e.getImageHeight() * 0.4f);
        drop.setOrigin(Align.top);

        stage.addActor(drop);


        splashActor = new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                if (animPhaseThree)
                    batch.draw(splash.getKeyFrame(animStateTime, false),
                            getX(), getY(), getWidth(), getHeight());
            }
        };
        splashActor.setSize(e.getImageHeight() * 0.4f, e.getImageHeight() * 0.4f);

        splashActor.setPosition(dropStartPos.x, 0, Align.bottom);
        inPipeActor.setPosition(dropStartPos.x, dropStartPos.y, Align.top);

        stage.addActor(splashActor);

        inPipe = new Animation<>(0.7f / 9f, assetLoader.manager.get("pipe_drop_anim.atlas", TextureAtlas.class).findRegions("drop"));
        splash = new Animation<>(0.05f, assetLoader.manager.get("splash_anim.atlas", TextureAtlas.class).findRegions("splash"));


        animPhaseOneStart = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
//                Gdx.app.log("anim", "phaseOneStart");

                animPhaseOne = true;
                animStateTime = 0;
            }
        };

        animPhaseTwoStart = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
//                Gdx.app.log("anim", "phaseTwoStart");

                Timeline.createSequence()
                        .push(Tween.set(drop, SpriteAccessor.POSITION).target(dropStartPos.x, dropStartPos.y - drop.getHeight() * 0.15f))
                        .push(Tween.to(drop, SpriteAccessor.SCALE, 0.1f).target(1f))
                        .push(Tween.to(drop, SpriteAccessor.POSITION, 1f).target(dropStartPos.x, 0).ease(Sine.IN))
                        .push(Tween.set(drop, SpriteAccessor.SCALE).target(0f))
                        .setCallback(animPhaseThreeStart).setCallbackTriggers(TweenCallback.END).start(tweenManager);
            }
        };

        animPhaseThreeStart = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
//                Gdx.app.log("anim", "phaseThreeStart");

                animPhaseThree = true;
                animStateTime = 0;
                assetLoader.getSound(SoundType.WATER_DROP).play(GameData.getInstance().getSoundVolume());
            }
        };

    }

    public void show() {
        Gdx.input.setCatchBackKey(true);
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
                .push(Tween.call(animPhaseOneStart))
                .pushPause(0.35f)
                .push(Tween.call(animPhaseTwoStart))
                .start(tweenManager);

        int highScore = GameData.getInstance().statistics.get(StatisticsType.HIGHSCORE_ALL);
        highScoreLabel.setText("High Score\n" + String.valueOf(highScore));
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        stage.act(delta);
        stage.draw();

        if(animPhaseOne) {
            animStateTime += delta;
            if (inPipe.isAnimationFinished(animStateTime)) {
                animPhaseOne = false;
            }
        } else if (animPhaseThree){
            animStateTime += delta;
            if (splash.isAnimationFinished(animStateTime)){
                animPhaseThree = false;
                Timeline.createSequence()
                        .push(Tween.call(animPhaseOneStart))
                        .pushPause(0.35f)
                        .push(Tween.call(animPhaseTwoStart))
                        .start(tweenManager);

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
        y_1.setScale(0f);
        a.setScale(0f);
        y_2.setScale(0f);
        ex_mark.setScale(0f);
        p_1.setScale(0f);
        i.setScale(0f);
        p_2.setScale(0f);
        e.setScale(0f);
        drop.setScale(0f);
        tweenManager.killAll();

        animStateTime = 0;
        animPhaseOne = false;
        animPhaseTwo = false;
        animPhaseThree = false;
    }
}