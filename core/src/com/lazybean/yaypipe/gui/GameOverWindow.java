package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.GameState;
import com.lazybean.yaypipe.gamehelper.NumberAnimator;
import com.lazybean.yaypipe.gamehelper.Stopwatch;
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gamehelper.FontType;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;
import com.lazybean.yaypipe.gamehelper.IconType;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.gamehelper.StatisticsType;
import com.lazybean.yaypipe.gameobjects.Coin;
import com.lazybean.yaypipe.gamehelper.Score;

import java.text.DecimalFormat;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Quart;
import aurelienribon.tweenengine.equations.Sine;

public class GameOverWindow extends GameWindow{
    private Container<Label> container;
    private Label finalScore_label, finalScore_value;
    private Label coin_value;

    private Icon retry_icon, quit_icon, view_icon;

    private Score score;

    private Array<Coin> coinArray = new Array<>();

    private NumberAnimator coinNumberAnimator;
    private TweenManager tweenManager;

    public GameOverWindow(AssetLoader assetLoader, final boolean isClear, GameWorld gameWorld) {
        super(assetLoader.uiSkin, gameWorld);

        score = gameWorld.getScore();
        score.getNumberAnimator().setStartNum(0);
        score.getNumberAnimator().stop();
        tweenManager = new TweenManager();
        coinNumberAnimator = new NumberAnimator();
        coinNumberAnimator.setRange(GameData.getInstance().getCoin(), GameData.getInstance().getCoin());

        Color iconColor = CustomColor.INDIGO.getColor();
        Label.LabelStyle style = new Label.LabelStyle(assetLoader.getFont(FontType.ANJA_EXTRA_LARGE), Color.BLACK);
        if (isClear){
            Label label = new Label("YAY!\nCLEAR!", style);
            label.setAlignment(Align.center);
            getContentTable().add(label).padBottom(Value.percentHeight(0.2f)).colspan(2).row();
        }
        else{
            Label label = new Label("GAME\nOVER", style);
            label.setAlignment(Align.center);
            getContentTable().add(label).padBottom(Value.percentHeight(0.2f)).colspan(2).row();
        }

        Label clearTime = new Label(Stopwatch.convertSecondToMinute(Stopwatch.getInstance().getIntTime()), getSkin(), "gameOverTime");

        // TODO: 24/08/2016 time bonus
//        Label timeBonus_label = new Label("TIME BONUS", detailStyle);
//        Label timeBonus_value = new Label("1000", detailStyle);

        Label pipeConnected_label = new Label("PIPE CONNECTED", getSkin(), "gameOverText");
        Label pipeConnected_value = new Label(String.valueOf(score.getPipeConnectScore()), getSkin(), "gameOverText");

        Label stopCleared_label = new Label("STOPS CLEARED", getSkin(), "gameOverText");
        Label stopCleared_value = new Label(String.valueOf(score.getStopPassScore()), getSkin(), "gameOverText");

        Label pipeChange_label = new Label("PIPE CHANGE PENALTY", getSkin(), "gameOverText");
        Label pipeChange_value = new Label(String.valueOf(score.getPipeChangeScore()), getSkin(), "gameOverText");

        Label crossPipeUse_label = new Label("CROSS PIPE BONUS", getSkin(), "gameOverText");
        Label crossPipeUse_value = new Label('x' + String.valueOf(new DecimalFormat("#.#").format(1.0f + 0.1f * GameData.getInstance().statistics.getFullPipeCount())), getSkin(), "gameOverText");

        Image line = new Image(assetLoader.line);
        line.setScaling(Scaling.fillX);
        line.setColor(Color.BLACK);

        finalScore_label = new Label("SCORE", getSkin(), "gameOverFinalScore");
        finalScore_label.setColor(Color.BLACK);
        finalScore_label.setAlignment(Align.center);

        container = new Container<>(finalScore_label);
        container.setTransform(true);
        container.setOrigin(finalScore_label.getWidth()/2, finalScore_label.getHeight());

        finalScore_value = new Label("0", getSkin(), "gameOverFinalScore");
        finalScore_value.setColor(Color.BLACK);

        Image piggy = new Image(assetLoader.piggy);
        piggy.setScaling(Scaling.fillY);

        Image coin = new Image(assetLoader.coin);
        coin.setScaling(Scaling.fillX);

        coin_value = new Label("0", getSkin(), "gameOverFinalScore");
        coin_value.setColor(Color.BLACK);

        retry_icon = new Icon(assetLoader, IconType.RESTART, Icon.MENU_DIAMETER);
        retry_icon.setColor(iconColor);

        Label retry_label = new Label("RESTART", getSkin(), "gameOverText");


        quit_icon = new Icon(assetLoader, IconType.HOME, Icon.MENU_DIAMETER);
        quit_icon.setColor(iconColor);

        Label quit_label = new Label("HOME", getSkin(), "gameOverText");


        view_icon = new Icon(assetLoader, IconType.ZOOM_IN, Icon.MENU_DIAMETER);
        view_icon.setColor(iconColor);

        Label view_label = new Label("VIEW", getSkin(), "gameOverText");


        Table contentTable = getContentTable();
        contentTable.add(clearTime).colspan(2).padBottom(Value.percentHeight(0.5f));
        contentTable.row();

//        window.add(timeBonus_label).align(Align.left).padLeft(Value.percentWidth(0.2f, window));
//        window.add(timeBonus_value).align(Align.right).padRight(Value.percentWidth(0.2f, window));
//        window.row();

        contentTable.add(pipeConnected_label).align(Align.left).padLeft(Value.percentWidth(0.2f, this));
        contentTable.add(pipeConnected_value).align(Align.right).padRight(Value.percentWidth(0.2f, this));
        contentTable.row();

        contentTable.add(stopCleared_label).align(Align.left).padLeft(Value.percentWidth(0.2f, this));
        contentTable.add(stopCleared_value).align(Align.right).padRight(Value.percentWidth(0.2f, this));
        contentTable.row();

        contentTable.add(pipeChange_label).align(Align.left).padLeft(Value.percentWidth(0.2f, this));
        contentTable.add(pipeChange_value).align(Align.right).padRight(Value.percentWidth(0.2f, this));
        contentTable.row();


        // cross pipe bonus only given when cleared
        if (isClear) {
            contentTable.add(crossPipeUse_label).align(Align.left).padLeft(Value.percentWidth(0.2f, this));
            contentTable.add(crossPipeUse_value).align(Align.right).padRight(Value.percentWidth(0.2f, this));
            contentTable.row();
        }

        contentTable.add(line).height(stopCleared_label.getHeight()).padLeft(Value.percentWidth(0.1f, this))
                .padRight(Value.percentWidth(0.1f, this)).colspan(2);
        contentTable.row();

        GlyphLayout scoreLayout = new GlyphLayout(finalScore_value.getStyle().font, String.valueOf(score.getTotalScore()));
        contentTable.add(container).height(finalScore_label.getHeight() * 2.1f).align(Align.left).padLeft(Value.percentWidth(0.2f, this));
        contentTable.add(finalScore_value).width(scoreLayout.width).align(Align.right).padRight(Value.percentWidth(0.2f, this));
        contentTable.row();

        contentTable.add(piggy).height(Value.percentWidth(0.3f, this)).colspan(2).padTop(Value.percentHeight(0.2f));
        contentTable.row();

        Table coinGroup = new Table();
        coinGroup.add(coin).width(coin_value.getHeight());
        coinGroup.add(coin_value);
        contentTable.add(coinGroup).colspan(2);
        contentTable.row();
        
        Table iconTable = getButtonTable();

        Table quit = new Table();
        quit.add(quit_icon).size(Value.percentWidth(0.17f, this)).row();
        quit.add(quit_label);

        iconTable.add(quit).padLeft(Value.percentWidth(0.2f, this))
                .padRight(Value.percentWidth(0.1f, this));

        Table retry = new Table();
        retry.add(retry_icon).size(Value.percentWidth(0.17f, this)).row();
        retry.add(retry_label);

        iconTable.add(retry);

        Table view = new Table();
        view.add(view_icon).size(Value.percentWidth(0.17f, this)).row();
        view.add(view_label);

        iconTable.add(view).padLeft(Value.percentWidth(0.1f, this))
                .padRight(Value.percentWidth(0.2f, this));

        iconTable.padTop(coin_value.getHeight()/2);

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
                .push(Tween.to(pipeConnected_label, SpriteAccessor.ALPHA, 0.3f).target(1f))
                .push(Tween.to(pipeConnected_value, SpriteAccessor.ALPHA, 0.3f).target(1f))
                .push(Tween.to(stopCleared_label, SpriteAccessor.ALPHA, 0.3f).target(1f))
                .push(Tween.to(stopCleared_value, SpriteAccessor.ALPHA, 0.3f).target(1f))
                .push(Tween.to(pipeChange_label, SpriteAccessor.ALPHA, 0.3f).target(1f))
                .push(Tween.to(pipeChange_value, SpriteAccessor.ALPHA, 0.3f).target(1f))
                .push(Tween.to(crossPipeUse_label, SpriteAccessor.ALPHA, 0.3f).target(1f))
                .push(Tween.to(crossPipeUse_value, SpriteAccessor.ALPHA, 0.3f).target(1f))
                .push(Tween.to(line, SpriteAccessor.ALPHA, 0.3f).target(1f))
                .push(Tween.to(finalScore_label, SpriteAccessor.ALPHA, 0.3f).target(1f))
                .push(Tween.to(finalScore_value, SpriteAccessor.ALPHA, 0.3f).target(1f).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        score.getNumberAnimator().start();
                    }
                }))
                .end()
                .start(tweenManager);

        pack();

        //generate coins for piggy bank animation
        Vector2 scoreLoc = finalScore_value.localToStageCoordinates(new Vector2());

        Vector2 piggyLoc = piggy.localToStageCoordinates(new Vector2());

        for (int i = 0; i < score.getTotalScore() / 200; i++){
            Coin coinImage = new Coin(assetLoader);
            coinImage.setPosition(scoreLoc.x + finalScore_value.getWidth() / 2, scoreLoc.y);
            Tween.set(coinImage, SpriteAccessor.ALPHA).target(0f).start(tweenManager);
            coinArray.add(coinImage);

            addActor(coinImage);
        }

        if (coinArray.size > 0) {
            int coinToAdd;

            //1% of total score for coins when cleared
            if (isClear) {
                coinToAdd = (int) ((score.getTotalScore() * 0.01f));
            }
            //0.5% of total score for coins when failed
            else {
                coinToAdd = (int) ((score.getTotalScore() * 0.005f));
            }

            GameData.getInstance().addCoin(coinToAdd);
            coinNumberAnimator.setTargetNum(coinNumberAnimator.getCurrentNum() + coinToAdd);
            coinNumberAnimator.setChangeConstant(coinToAdd / coinArray.size);
            coinNumberAnimator.setDelay(0.2f);
        }


        // coin animation
        float timeDelay = 1f;
        for (int i = 0; i < coinArray.size; i++){
            Coin temp = coinArray.get(i);
            Timeline.createParallel()
                    .delay(3f)
                    .push(Tween.to(temp, SpriteAccessor.ALPHA, 0.2f).target(1f))
                    .push(Tween.to(temp, SpriteAccessor.POSITION, 1f)
                            .target(piggyLoc.x + piggy.getWidth() / 2 - temp.getWidth()/2, piggyLoc.y + piggy.getHeight() * 0.6f)
                            .ease(Sine.INOUT))
                    .beginSequence()
                    .pushPause(0.8f)
                    .push(Tween.to(temp, SpriteAccessor.ALPHA, 0.2f).target(0f))
                    .end()
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            coinNumberAnimator.start();
                        }
                    })
                    .delay(timeDelay)
                    .start(tweenManager);

            timeDelay+= 0.2f;
        }

        //highscore event
        if (score.getTotalScore() > GameData.getInstance().statistics.get(StatisticsType.HIGHSCORE_ALL)){
            Timeline.createSequence()
                    .push(Tween.to(container, SpriteAccessor.SCALE, 0.5f).target(0f).ease(Quart.IN)
                            .setCallback(new TweenCallback() {
                                @Override
                                public void onEvent(int type, BaseTween<?> source) {
                                    finalScore_label.setText("HIGH\nSCORE");
                                    finalScore_label.setColor(CustomColor.RED.getColor());
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
        }
    }

    @Override
    public float getWidth() {
        return getPrefWidth();
    }

    @Override
    public float getPrefWidth() {
        return YayPipe.SCREEN_WIDTH * 0.85f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        score.update(delta);
        coinNumberAnimator.update(delta);

        if (quit_icon.isTouched()){
            quit_icon.setTouched(false);
            gameWorld.setState(GameState.QUIT);
        } else if (retry_icon.isTouched()){
            retry_icon.setTouched(false);
            gameWorld.setState(GameState.RESTART);
        } else if (view_icon.isTouched()){
            view_icon.setTouched(false);
            gameWorld.setState(GameState.VIEW);
            hide();
        }

        int currentScore = score.getCurrentScore();

        finalScore_value.setText(String.valueOf(currentScore));
        finalScore_value.setAlignment(Align.center);

        coin_value.setText(String.valueOf(coinNumberAnimator.getCurrentNum()));
        coin_value.setAlignment(Align.center);

        //skip score increasing animation
        if (Gdx.input.justTouched()) {
            tweenManager.update(100f);
            coinNumberAnimator.skip();
            score.skip();
        }

        tweenManager.update(delta);
    }
}