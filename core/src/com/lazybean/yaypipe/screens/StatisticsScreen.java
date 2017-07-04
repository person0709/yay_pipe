package com.lazybean.yaypipe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Colour;
import com.lazybean.yaypipe.gui.Background;
import com.lazybean.yaypipe.gui.UpperBarUI;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.gamehelper.Statistics;
import com.lazybean.yaypipe.YayPipe;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Quart;

public class StatisticsScreen implements Screen {
    private YayPipe game;
    private AssetLoader assetLoader;

    private Stage stage;
    private UpperBarUI upperBarUI;
    private ScrollPane scrollPane;
    private Table table;
    private Statistics statistics;

    private Background background;

    private TweenManager tweenManager;

    public StatisticsScreen(YayPipe game, AssetLoader assetLoader){
        this.game = game;
        this.assetLoader = assetLoader;
        this.tweenManager = new TweenManager();
        statistics = AssetLoader.stats;

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Background background = new Background(assetLoader.background);
        background.setColor(Colour.YELLOW);

        upperBarUI = new UpperBarUI(assetLoader);
        upperBarUI.setForStatisticsScreen();

        this.background = new Background(assetLoader.background);
        this.background.setColor(Color.BLACK);

        stage.addActor(background);
        stage.addActor(upperBarUI);
        stage.addActor(this.background);

        Tween.to(this.background, SpriteAccessor.ALPHA, 0.5f).target(0f).ease(Quart.OUT)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        StatisticsScreen.this.background.remove();
                    }
                })
                .start(tweenManager);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);

        table = new Table();
//        table.setDebug(true);
        table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()*2);

        Label.LabelStyle labelStyle = new Label.LabelStyle(assetLoader.extraSmallFont_noto, Color.BLACK);

        Label totalPlayTime_label = new Label("Total Play Time",labelStyle);
        Label totalPlayTime_value = new Label(statistics.convertSecondToHour(statistics.get("totalPlayTime")), labelStyle);

        Label totalPipePlaced_label = new Label("Total PipeType Placed", labelStyle);
        Label totalPipePlaced_value = new Label(String.valueOf(statistics.get("totalPipePlaced")), labelStyle);

        Label totalUndo_label = new Label("Total Undo Performed", labelStyle);
        Label totalUndo_value = new Label(String.valueOf(statistics.get("totalUndoPerformed")), labelStyle);

        Label totalTryCount_label = new Label("Game played", labelStyle);
        Label totalTryCount_value = new Label(String.valueOf(statistics.get("totalTryNumber")), labelStyle);

        Label totalRestart_label = new Label("Restarted", labelStyle);
        Label totalRestart_value = new Label(String.valueOf(statistics.get("totalRestartPerformed")), labelStyle);

        Label totalFailCount_label = new Label("Failed", labelStyle);
        Label totalFailCount_value = new Label(String.valueOf(statistics.get("totalFailNumber")), labelStyle);

        Label clear_label = new Label("Cleared", labelStyle);

        Label easyClear_label = new Label("Easy", labelStyle);
        Label easyClear_value = new Label(String.valueOf(statistics.get("easyCleared")), labelStyle);

        Label normalClear_label = new Label("Normal", labelStyle);
        Label normalClear_value = new Label(String.valueOf(statistics.get("normalCleared")), labelStyle);

        Label hardClear_label = new Label("Hard", labelStyle);
        Label hardClear_value = new Label(String.valueOf(statistics.get("hardCleared")), labelStyle);

        Label proClear_label = new Label("Professional", labelStyle);
        Label proClear_value = new Label(String.valueOf(statistics.get("professionalCleared")), labelStyle);

        Label masterClear_label = new Label("Master", labelStyle);
        Label masterClear_value = new Label(String.valueOf(statistics.get("masterCleared")), labelStyle);


        Label highScore_label = new Label("Highest Score", labelStyle);

        Label easyHigh_label = new Label("Easy", labelStyle);
        Label easyHigh_value = new Label(String.valueOf(statistics.get("easyHighScore")), labelStyle);

        Label normalHigh_label = new Label("Normal", labelStyle);
        Label normalHigh_value = new Label(String.valueOf(statistics.get("normalHighScore")), labelStyle);

        Label hardHigh_label = new Label("Hard", labelStyle);
        Label hardHigh_value = new Label(String.valueOf(statistics.get("hardHighScore")), labelStyle);

        Label proHigh_label = new Label("Professional", labelStyle);
        Label proHigh_value = new Label(String.valueOf(statistics.get("professionalHighScore")), labelStyle);

        Label masterHigh_label = new Label("Master", labelStyle);
        Label masterHigh_value = new Label(String.valueOf(statistics.get("masterHighScore")), labelStyle);


        Label clearTime_label = new Label("Best Time", labelStyle);

        Label easyTime_label = new Label("Easy", labelStyle);
        int value = statistics.get("easyBestTime");
        Label easyTime_value;
        if (value == 0){
            easyTime_value = new Label("N/A", labelStyle);
        }
        else {
            easyTime_value = new Label(statistics.convertSecondToMinute(value), labelStyle);
        }

        Label normalTime_label = new Label("Normal", labelStyle);
        value = statistics.get("normalBestTime");
        Label normalTime_value;
        if (value == 0){
            normalTime_value = new Label("N/A", labelStyle);
        }
        else {
            normalTime_value = new Label(statistics.convertSecondToMinute(value), labelStyle);
        }

        Label hardTime_label = new Label("Hard", labelStyle);
        value = statistics.get("hardBestTime");
        Label hardTime_value;
        if (value == 0){
            hardTime_value = new Label("N/A", labelStyle);
        }
        else {
            hardTime_value = new Label(statistics.convertSecondToMinute(value), labelStyle);
        }

        Label proTime_label = new Label("Professional", labelStyle);
        value = statistics.get("professionalBestTime");
        Label proTime_value;
        if (value == 0){
            proTime_value = new Label("N/A", labelStyle);
        }
        else {
            proTime_value = new Label(statistics.convertSecondToMinute(value), labelStyle);
        }

        Label masterTime_label = new Label("Master", labelStyle);
        value = statistics.get("masterBestTime");
        Label masterTime_value;
        if (value == 0){
            masterTime_value = new Label("N/A", labelStyle);
        }
        else {
            masterTime_value = new Label(statistics.convertSecondToMinute(value), labelStyle);
        }

        Label mostPipeConnected_label = new Label("Most Pipes Connected", labelStyle);
        Label mostPipeConnected_value = new Label(String.valueOf(statistics.get("mostPipeConnected")), labelStyle);

        Label mostPipeChanged_label = new Label("Most Pipes Changed", labelStyle);
        Label mostPipeChanged_value = new Label(String.valueOf(statistics.get("mostPipeChanged")), labelStyle);

        Label mostUndo_label = new Label("Most Undos Performed", labelStyle);
        Label mostUndo_value = new Label(String.valueOf(statistics.get("mostUndoPerformed")), labelStyle);


        Label pipeUsed_label = new Label("PipeType Used", labelStyle);

        Label pipe1_label = new Label("1", labelStyle);
        Label pipe1_value = new Label(String.valueOf(statistics.get("pipe1Used")), labelStyle);

        Label pipe2_label = new Label("2", labelStyle);
        Label pipe2_value = new Label(String.valueOf(statistics.get("pipe2Used")), labelStyle);

        Label pipe3_label = new Label("3", labelStyle);
        Label pipe3_value = new Label(String.valueOf(statistics.get("pipe3Used")), labelStyle);

        Label pipe4_label = new Label("4", labelStyle);
        Label pipe4_value = new Label(String.valueOf(statistics.get("pipe4Used")), labelStyle);

        Label pipe5_label = new Label("5", labelStyle);
        Label pipe5_value = new Label(String.valueOf(statistics.get("pipe5Used")), labelStyle);

        Label pipe6_label = new Label("6", labelStyle);
        Label pipe6_value = new Label(String.valueOf(statistics.get("pipe6Used")), labelStyle);

        Label pipe7_label = new Label("7", labelStyle);
        Label pipe7_value = new Label(String.valueOf(statistics.get("pipe7Used")), labelStyle);

        Label pipe7Full_label = new Label("Full 7", labelStyle);
        Label pipe7Full_value = new Label(String.valueOf(statistics.get("pipe7UsedFull")), labelStyle);

        table.padTop(100);
        table.add(totalPlayTime_label).padLeft(Gdx.graphics.getWidth() * 0.1f).expandX().align(Align.left);
        table.add(totalPlayTime_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(totalPipePlaced_label).padLeft(Gdx.graphics.getWidth() * 0.1f).expandX().align(Align.left);
        table.add(totalPipePlaced_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(totalUndo_label).padLeft(Gdx.graphics.getWidth() * 0.1f).expandX().align(Align.left);
        table.add(totalUndo_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(totalTryCount_label).padLeft(Gdx.graphics.getWidth() * 0.1f).align(Align.left);
        table.add(totalTryCount_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(totalRestart_label).padLeft(Gdx.graphics.getWidth() * 0.1f).align(Align.left);
        table.add(totalRestart_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(totalFailCount_label).padLeft(Gdx.graphics.getWidth() * 0.1f).align(Align.left);
        table.add(totalFailCount_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(clear_label).colspan(2).padTop(Gdx.graphics.getHeight() * 0.01f).padLeft(Gdx.graphics.getWidth() * 0.1f).align(Align.left);
        table.row();

        table.add(easyClear_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(easyClear_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(normalClear_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(normalClear_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(hardClear_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(hardClear_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(proClear_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(proClear_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(masterClear_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(masterClear_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();


        table.add(highScore_label).colspan(2).padTop(Gdx.graphics.getHeight() * 0.01f).padLeft(Gdx.graphics.getWidth() * 0.1f).align(Align.left);
        table.row();

        table.add(easyHigh_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(easyHigh_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(normalHigh_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(normalHigh_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(hardHigh_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(hardHigh_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(proHigh_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(proHigh_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(masterHigh_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(masterHigh_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();


        table.add(clearTime_label).colspan(2).padTop(Gdx.graphics.getHeight() * 0.01f).padLeft(Gdx.graphics.getWidth() * 0.1f).align(Align.left);
        table.row();

        table.add(easyTime_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(easyTime_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(normalTime_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(normalTime_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(hardTime_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(hardTime_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(proTime_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(proTime_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(masterTime_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(masterTime_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();


        table.add(mostPipeConnected_label).padTop(Gdx.graphics.getHeight() * 0.01f).padLeft(Gdx.graphics.getWidth() * 0.1f).align(Align.left);
        table.add(mostPipeConnected_value).padTop(Gdx.graphics.getHeight() * 0.01f).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(mostPipeChanged_label).padLeft(Gdx.graphics.getWidth() * 0.1f).align(Align.left);
        table.add(mostPipeChanged_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(mostUndo_label).padLeft(Gdx.graphics.getWidth() * 0.1f).align(Align.left);
        table.add(mostUndo_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();


        table.add(pipeUsed_label).colspan(2).padTop(Gdx.graphics.getHeight() * 0.01f).padLeft(Gdx.graphics.getWidth() * 0.1f).align(Align.left);
        table.row();

        table.add(pipe1_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(pipe1_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(pipe2_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(pipe2_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(pipe3_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(pipe3_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(pipe4_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(pipe4_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(pipe5_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(pipe5_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(pipe6_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(pipe6_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(pipe7_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(pipe7_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.row();

        table.add(pipe7Full_label).padLeft(Gdx.graphics.getWidth() * 0.15f).align(Align.left);
        table.add(pipe7Full_value).padRight(Gdx.graphics.getWidth() * 0.1f).align(Align.right);
        table.padBottom(100);

        scrollPane = new ScrollPane(table);
        scrollPane.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - upperBarUI.getHeight());
        scrollPane.setDebug(true);

        stage.addActor(scrollPane);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        tweenManager.update(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.BACK) || upperBarUI.isBack()){
            stage.addActor(background);
            Timeline.createSequence()
                    .delay(0.1f)
                    .push(Tween.to(background, SpriteAccessor.ALPHA, 0.5f).target(1f).ease(Quart.OUT))
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            dispose();
                            game.setScreen(new MainMenuScreen(game, assetLoader));
                        }
                    })
                    .start(tweenManager);

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
    }
}
