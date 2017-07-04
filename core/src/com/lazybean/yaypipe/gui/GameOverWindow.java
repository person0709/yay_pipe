package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Colour;
import com.lazybean.yaypipe.gamehelper.Difficulty;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.gameobjects.Coin;
import com.lazybean.yaypipe.gameobjects.Score;

import java.text.DecimalFormat;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Quart;
import aurelienribon.tweenengine.equations.Sine;

public class GameOverWindow extends Group {
    public Background fadeInOut;
    public Table window;
    private Container<Label> container;
    private Label finalScore_label, finalScore_value;
    private Label coin_value;

    private Score score;
    private boolean isClear;
    private boolean isRestart = false;
    private boolean isQuit = false;
    private boolean isView = false;

    private boolean scoreUpdateStart = false;

    private Array<Coin> coinArray = new Array<>();

    private TweenManager tweenManager;

    public GameOverWindow(AssetLoader assetLoader, boolean isClear, TweenManager tweenManager) {
        score = GameWorld.score;
        this.isClear = isClear;
        this.tweenManager = tweenManager;

        window = new Table();
//        window.setDebug(true);
        window.setTransform(true);
        window.setWidth(Gdx.graphics.getWidth() * 0.85f);
        window.setHeight(window.getWidth() * 1.7f);
        window.setPosition(Gdx.graphics.getWidth() / 2 - window.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - window.getHeight() / 2);
        window.setOrigin(window.getWidth()/2, window.getHeight()/2);
        window.setBackground(new NinePatchDrawable(assetLoader.window));

        String string;
        Color stringColor = new Color(Color.BLACK);
        Color iconColor = new Color(Colour.INDIGO);
        if (isClear){
            string = "YAY!\nCLEAR!";
        }
        else{
            string = "GAME\nOVER";
        }

        LabelStyle gameOverStyle = new LabelStyle(assetLoader.extraLargeFont_anja, stringColor);
        LabelStyle timeStyle = new LabelStyle(assetLoader.mediumFont_noto, stringColor);
        LabelStyle detailStyle = new LabelStyle(assetLoader.doubleExtraSmallFont_noto, stringColor);
        LabelStyle finalScoreStyle = new LabelStyle(assetLoader.mediumFontShadow_anja,Color.WHITE);

        Label gameOver_label = new Label(string, gameOverStyle);
        gameOver_label.setAlignment(Align.center, Align.center);

        Label clearTime = new Label(AssetLoader.stats.convertSecondToMinute(AssetLoader.stats.getStageTimeSum()), timeStyle);

        // TODO: 24/08/2016 time bonus
//        Label timeBonus_label = new Label("TIME BONUS", detailStyle);
//        Label timeBonus_value = new Label("1000", detailStyle);

        Label pipeConnected_label = new Label("PIPE CONNECTED", detailStyle);
        Label pipeConnected_value = new Label(String.valueOf(score.getPipeConnectScore()), detailStyle);

        Label stopCleared_label = new Label("STOPS CLEARED", detailStyle);
        Label stopCleared_value = new Label(String.valueOf(score.getStopPassScore()), detailStyle);

        Label pipeChange_label = new Label("PIPE CHANGE PENALTY", detailStyle);
        Label pipeChange_value = new Label(String.valueOf(score.getPipeChangeScore()), detailStyle);

        Label crossPipeUse_label = new Label("CROSS PIPE BONUS", detailStyle);
        Label crossPipeUse_value = new Label('x' + String.valueOf(new DecimalFormat("#.#").format(1.0f + 0.1f * AssetLoader.stats.getCrossPipeUse())), detailStyle);

        Image line = new Image(assetLoader.line);
        line.setScaling(Scaling.fillX);
        line.setColor(Color.BLACK);

        finalScore_label = new Label("SCORE", finalScoreStyle);
        finalScore_label.setColor(Color.BLACK);
        finalScore_label.setAlignment(Align.center);

        container = new Container<>(finalScore_label);
        container.setTransform(true);
        container.setOrigin(finalScore_label.getWidth()/2, finalScore_label.getHeight());

        finalScore_value = new Label("0", finalScoreStyle);
        finalScore_value.setColor(Color.BLACK);

        Image piggy = new Image(assetLoader.piggy);
        piggy.setScaling(Scaling.fillY);

        Image coin = new Image(assetLoader.coin);
        coin.setScaling(Scaling.fillX);

        coin_value = new Label("0", finalScoreStyle);
        coin_value.setColor(Color.BLACK);


        Icon retry_icon = new Icon(assetLoader.circle, assetLoader.restart);
        retry_icon.setColor(iconColor);
        retry_icon.setDiameter(window.getWidth() * 0.15f);
        retry_icon.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isRestart = true;
            }
        });
        
        Label retry_label = new Label("RESTART", detailStyle);


        Icon quit_icon = new Icon(assetLoader.circle, assetLoader.home);
        quit_icon.setColor(iconColor);
        quit_icon.setDiameter(window.getWidth() * 0.15f);
        quit_icon.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isQuit = true;
            }
        });
        
        Label quit_label = new Label("HOME", detailStyle);


        Icon view_icon = new Icon(assetLoader.circle, assetLoader.zoomIn);
        view_icon.setColor(iconColor);
        view_icon.setDiameter(window.getWidth() * 0.15f);
        view_icon.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isView = true;
            }
        });

        Label view_label = new Label("VIEW", detailStyle);


        window.add(gameOver_label).colspan(2).expandX();
        window.row();

        window.add(clearTime).colspan(2).height(gameOver_label.getHeight()/3).padBottom(clearTime.getHeight()/2);
        window.row();

//        window.add(timeBonus_label).align(Align.left).padLeft(Value.percentWidth(0.2f, window));
//        window.add(timeBonus_value).align(Align.right).padRight(Value.percentWidth(0.2f, window));
//        window.row();

        window.add(pipeConnected_label).align(Align.left).padLeft(Value.percentWidth(0.2f, window));
        window.add(pipeConnected_value).align(Align.right).padRight(Value.percentWidth(0.2f, window));
        window.row();

        window.add(stopCleared_label).align(Align.left).padLeft(Value.percentWidth(0.2f, window));
        window.add(stopCleared_value).align(Align.right).padRight(Value.percentWidth(0.2f, window));
        window.row();

        window.add(pipeChange_label).align(Align.left).padLeft(Value.percentWidth(0.2f, window));
        window.add(pipeChange_value).align(Align.right).padRight(Value.percentWidth(0.2f, window));
        window.row();


        // cross pipe bonus only given when cleared
        if (isClear) {
            window.add(crossPipeUse_label).align(Align.left).padLeft(Value.percentWidth(0.2f, window));
            window.add(crossPipeUse_value).align(Align.right).padRight(Value.percentWidth(0.2f, window));
            window.row();
        }

        window.add(line).height(stopCleared_label.getHeight()).padLeft(Value.percentWidth(0.1f, window))
                .padRight(Value.percentWidth(0.1f, window)).colspan(2);
        window.row();

        window.add(container).height(finalScore_label.getHeight() * 2).align(Align.left).padLeft(Value.percentWidth(0.2f, window));
        window.add(finalScore_value).align(Align.right).padRight(Value.percentWidth(0.2f, window));
        window.row();

        window.add(piggy).height(Value.percentWidth(0.3f, window)).colspan(2).padTop(finalScore_value.getHeight() / 2);
        window.row();

        Table coinGroup = new Table();
        coinGroup.add(coin).width(coin_value.getHeight());
        coinGroup.add(coin_value);
        window.add(coinGroup).colspan(2);
        window.row();
        
        Table iconGroup = new Table();

        Table quit = new Table();
        quit.add(quit_icon).row();
        quit.add(quit_label);

        iconGroup.add(quit).width(Value.percentWidth(0.15f, window)).padLeft(Value.percentWidth(0.2f, window))
                .padRight(Value.percentWidth(0.1f, window));


        if ( > Difficulty.TUTORIAL_BASIC) {
            Table retry = new Table();
            retry.add(retry_icon).row();
            retry.add(retry_label);

            iconGroup.add(retry);
        }


        Table view = new Table();
        view.add(view_icon).row();
        view.add(view_label);

        iconGroup.add(view).width(Value.percentWidth(0.15f, window)).padLeft(Value.percentWidth(0.1f, window))
                .padRight(Value.percentWidth(0.2f, window));

        window.add(iconGroup).padTop(coin_value.getHeight()/2).colspan(2);

        addActor(window);


        Timeline.createSequence()
                .beginParallel()
                .push(Tween.set(pipeConnected_label, SpriteAccessor.ALPHA).target(0f))
                .push(Tween.set(pipeConnected_value, SpriteAccessor.ALPHA).target(0f))
                .push(Tween.set(stopCleared_label, SpriteAccessor.ALPHA).target(0f))
                .push(Tween.set(stopCleared_value, SpriteAccessor.ALPHA).target(0f))
                .push(Tween.set(pipeChange_label, SpriteAccessor.ALPHA).target(0f))
                .push(Tween.set(pipeChange_value, SpriteAccessor.ALPHA).target(0f))
                .push(Tween.set(crossPipeUse_label, SpriteAccessor.ALPHA).target(0f))
                .push(Tween.set(crossPipeUse_value, SpriteAccessor.ALPHA).target(0f))
                .push(Tween.set(line, SpriteAccessor.ALPHA).target(0f))
                .push(Tween.set(finalScore_label, SpriteAccessor.ALPHA).target(0f))
                .push(Tween.set(finalScore_value, SpriteAccessor.ALPHA).target(0f))
                .end()
                .beginSequence()
                .push(Tween.to(pipeConnected_label, SpriteAccessor.ALPHA, 0.5f).target(1f))
                .push(Tween.to(pipeConnected_value, SpriteAccessor.ALPHA, 0.5f).target(1f))
                .push(Tween.to(stopCleared_label, SpriteAccessor.ALPHA, 0.5f).target(1f))
                .push(Tween.to(stopCleared_value, SpriteAccessor.ALPHA, 0.5f).target(1f))
                .push(Tween.to(pipeChange_label, SpriteAccessor.ALPHA, 0.5f).target(1f))
                .push(Tween.to(pipeChange_value, SpriteAccessor.ALPHA, 0.5f).target(1f))
                .push(Tween.to(crossPipeUse_label, SpriteAccessor.ALPHA, 0.5f).target(1f))
                .push(Tween.to(crossPipeUse_value, SpriteAccessor.ALPHA, 0.5f).target(1f))
                .push(Tween.to(line, SpriteAccessor.ALPHA, 0.5f).target(1f))
                .push(Tween.to(finalScore_label, SpriteAccessor.ALPHA, 0.5f).target(1f))
                .push(Tween.to(finalScore_value, SpriteAccessor.ALPHA, 0.5f).target(1f))
//                .push(Tween.to(piggy, SpriteAccessor.SCALE, 1f).target(0.5f))
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        scoreUpdateStart = true;
                    }
                })
                .end()
                .start(tweenManager);

        window.validate();
        //generate coins for piggy bank animation
        Vector2 score_stageLoc = finalScore_value.localToStageCoordinates(new Vector2());

        Vector2 piggy_stageLoc = piggy.localToStageCoordinates(new Vector2());

        for (int i = 0; i < score.getTotalScore() / 200; i++){
            Coin coinImage = new Coin(assetLoader);
            coinImage.setPosition(score_stageLoc.x - finalScore_value.getWidth(), score_stageLoc.y);
            Tween.set(coinImage, SpriteAccessor.ALPHA).target(0f).start(tweenManager);
            coinArray.add(coinImage);

            addActor(coinImage);
        }

        // coin animation
        float timeDelay = 1f;
        for (int i = 0; i < coinArray.size; i++){
            Coin temp = coinArray.get(i);
            Timeline.createParallel()
                    .delay(5f)
                    .push(Tween.to(temp, SpriteAccessor.ALPHA, 0.2f).target(1f))
                    .push(Tween.to(temp, SpriteAccessor.POSITION, 1f)
                            .target(piggy_stageLoc.x + piggy.getWidth() / 2 - temp.getWidth()/2, piggy_stageLoc.y + piggy.getHeight() * 0.6f)
                            .ease(Sine.INOUT))
                    .beginSequence()
                    .pushPause(0.8f)
                    .push(Tween.to(temp, SpriteAccessor.ALPHA, 0.2f).target(0f))
                    .end()
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            //1% of total score when cleared
                            if (isClear()) {
                                AssetLoader.coinBank.addBalance((int) ((score.getTotalScore() * 0.01f) / coinArray.size));
                            }
                            //0.5% of total score when failed
                            else{
                                AssetLoader.coinBank.addBalance((int) ((score.getTotalScore() * 0.005f) / coinArray.size));
                            }
                            AssetLoader.coinBank.saveData();
                        }
                    })
                    .delay(timeDelay)
                    .start(tweenManager);

            timeDelay+= 0.2f;
        }

    }

    public boolean isClear(){
        return isClear;
    }

    public boolean isRestart() {
        return isRestart;
    }

    public void setRestart(boolean bool) {
        isRestart = bool;
    }

    public boolean isQuit() {
        return isQuit;
    }

    public void setQuit(boolean bool) {
        isQuit = bool;
    }

    public boolean isView() {
        return isView;
    }

    public void setView(boolean bool) {
        isView = bool;
    }

    private boolean firstLoop = true;
    @Override
    public void act(float delta) {
        int currentScore = score.getCurrentScore();
        if (currentScore > score.getHighScore() && firstLoop && isClear()){
            Timeline.createSequence()
                    .push(Tween.to(container, SpriteAccessor.SCALE, 0.5f).target(0f).ease(Quart.IN)
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            finalScore_label.setText("HIGH\nSCORE");
                            finalScore_label.setColor(Colour.RED);
                        }
                    }))
                    .push(Tween.to(container, SpriteAccessor.SCALE, 0.5f).target(1.1f).ease(Quart.OUT))
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            Tween.to(container, SpriteAccessor.SCALE, 0.8f).target(1f)
                                    .repeatYoyo(Tween.INFINITY, 0)
                                    .start(tweenManager);
                        }
                    })
                    .start(tweenManager);

            firstLoop = false;
        }
        finalScore_value.setText(String.valueOf(currentScore));
        finalScore_value.setAlignment(Align.center);

        int coin = AssetLoader.coinBank.getCurrentBalance();
        coin_value.setText(String.valueOf(coin));
        coin_value.setAlignment(Align.center);

        if (scoreUpdateStart) {
            score.update(5, true);
        }
        tweenManager.update(delta);
        AssetLoader.coinBank.update(1);
    }
}