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
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gamehelper.FontType;
import com.lazybean.yaypipe.gamehelper.Stopwatch;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;
import com.lazybean.yaypipe.gamehelper.StatisticsType;
import com.lazybean.yaypipe.gui.Background;
import com.lazybean.yaypipe.gui.StatisticsUpperBarUI;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.gamehelper.gamedata.Statistics;
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
    private StatisticsUpperBarUI upperBarUI;
    private ScrollPane scrollPane;
    private Table table;
    private Statistics statistics;

    private Background background;

    private TweenManager tweenManager;

    public StatisticsScreen(YayPipe game, AssetLoader assetLoader){
        this.game = game;
        this.assetLoader = assetLoader;
        this.tweenManager = new TweenManager();
        statistics = GameData.getInstance().statistics;

        stage = new Stage(new FitViewport(YayPipe.SCREEN_WIDTH, YayPipe.SCREEN_HEIGHT));
        Background background = new Background();
        background.setColor(CustomColor.YELLOW.getColor());

        upperBarUI = new StatisticsUpperBarUI(assetLoader);

        this.background = new Background();
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
        table.setSize(YayPipe.SCREEN_WIDTH, YayPipe.SCREEN_HEIGHT*2);

        Label.LabelStyle labelStyle = new Label.LabelStyle(assetLoader.getFont(FontType.NOTO_EXTRA_SMALL), Color.BLACK);

        Label totalPlayTime_label = new Label("Total Play Time",labelStyle);
        Label totalPlayTime_value = new Label(Stopwatch.convertSecondToHour(statistics.get(StatisticsType.TOTAL_PLAYTIME)), labelStyle);

        Label totalPipePlaced_label = new Label("Total PipeType Placed", labelStyle);
        Label totalPipePlaced_value = new Label(String.valueOf(statistics.get(StatisticsType.TOTAL_PIPE)), labelStyle);

        Label totalUndo_label = new Label("Total Undo Performed", labelStyle);
        Label totalUndo_value = new Label(String.valueOf(statistics.get(StatisticsType.TOTAL_UNDO)), labelStyle);

        Label totalTryCount_label = new Label("Game played", labelStyle);
        Label totalTryCount_value = new Label(String.valueOf(statistics.get(StatisticsType.TOTAL_TRY)), labelStyle);

        Label totalRestart_label = new Label("Restarted", labelStyle);
        Label totalRestart_value = new Label(String.valueOf(statistics.get(StatisticsType.TOTAL_RESTART)), labelStyle);

        Label totalFailCount_label = new Label("Failed", labelStyle);
        Label totalFailCount_value = new Label(String.valueOf(statistics.get(StatisticsType.TOTAL_FAIL)), labelStyle);

        Label clear_label = new Label("Cleared", labelStyle);

        Label easyClear_label = new Label("Easy", labelStyle);
        Label easyClear_value = new Label(String.valueOf(statistics.get(StatisticsType.CLEAR_EASY)), labelStyle);

        Label normalClear_label = new Label("Normal", labelStyle);
        Label normalClear_value = new Label(String.valueOf(statistics.get(StatisticsType.CLEAR_NORMAL)), labelStyle);

        Label hardClear_label = new Label("Hard", labelStyle);
        Label hardClear_value = new Label(String.valueOf(statistics.get(StatisticsType.CLEAR_HARD)), labelStyle);

        Label proClear_label = new Label("Professional", labelStyle);
        Label proClear_value = new Label(String.valueOf(statistics.get(StatisticsType.CLEAR_EXTREME)), labelStyle);

        Label masterClear_label = new Label("Master", labelStyle);
        Label masterClear_value = new Label(String.valueOf(statistics.get(StatisticsType.CLEAR_MASTER)), labelStyle);


        Label highScore_label = new Label("Highest Score", labelStyle);

        Label easyHigh_label = new Label("Easy", labelStyle);
        Label easyHigh_value = new Label(String.valueOf(statistics.get(StatisticsType.HIGHSCORE_EASY)), labelStyle);

        Label normalHigh_label = new Label("Normal", labelStyle);
        Label normalHigh_value = new Label(String.valueOf(statistics.get(StatisticsType.HIGHSCORE_NORMAL)), labelStyle);

        Label hardHigh_label = new Label("Hard", labelStyle);
        Label hardHigh_value = new Label(String.valueOf(statistics.get(StatisticsType.HIGHSCORE_HARD)), labelStyle);

        Label proHigh_label = new Label("Professional", labelStyle);
        Label proHigh_value = new Label(String.valueOf(statistics.get(StatisticsType.HIGHSCORE_EXTREME)), labelStyle);

        Label masterHigh_label = new Label("Master", labelStyle);
        Label masterHigh_value = new Label(String.valueOf(statistics.get(StatisticsType.HIGHSCORE_MASTER)), labelStyle);


        Label clearTime_label = new Label("Best Time", labelStyle);

        Label easyTime_label = new Label("Easy", labelStyle);
        int value = statistics.get(StatisticsType.BEST_TIME_EASY);
        Label easyTime_value;
        if (value == 0){
            easyTime_value = new Label("N/A", labelStyle);
        }
        else {
            easyTime_value = new Label(Stopwatch.convertSecondToMinute(value), labelStyle);
        }

        Label normalTime_label = new Label("Normal", labelStyle);
        value = statistics.get(StatisticsType.BEST_TIME_NORMAL);
        Label normalTime_value;
        if (value == 0){
            normalTime_value = new Label("N/A", labelStyle);
        }
        else {
            normalTime_value = new Label(Stopwatch.convertSecondToMinute(value), labelStyle);
        }

        Label hardTime_label = new Label("Hard", labelStyle);
        value = statistics.get(StatisticsType.BEST_TIME_HARD);
        Label hardTime_value;
        if (value == 0){
            hardTime_value = new Label("N/A", labelStyle);
        }
        else {
            hardTime_value = new Label(Stopwatch.convertSecondToMinute(value), labelStyle);
        }

        Label proTime_label = new Label("Extreme", labelStyle);
        value = statistics.get(StatisticsType.BEST_TIME_EXTREME);
        Label proTime_value;
        if (value == 0){
            proTime_value = new Label("N/A", labelStyle);
        }
        else {
            proTime_value = new Label(Stopwatch.convertSecondToMinute(value), labelStyle);
        }

        Label masterTime_label = new Label("Master", labelStyle);
        value = statistics.get(StatisticsType.BEST_TIME_MASTER);
        Label masterTime_value;
        if (value == 0){
            masterTime_value = new Label("N/A", labelStyle);
        }
        else {
            masterTime_value = new Label(Stopwatch.convertSecondToMinute(value), labelStyle);
        }

        Label mostPipeConnected_label = new Label("Most Pipes Connected", labelStyle);
        Label mostPipeConnected_value = new Label(String.valueOf(statistics.get(StatisticsType.MOST_PIPE_CONNECTED)), labelStyle);

        Label mostPipeChanged_label = new Label("Most Pipes Changed", labelStyle);
        Label mostPipeChanged_value = new Label(String.valueOf(statistics.get(StatisticsType.MOST_PIPE_CHANGED)), labelStyle);

        Label mostUndo_label = new Label("Most Undos Performed", labelStyle);
        Label mostUndo_value = new Label(String.valueOf(statistics.get(StatisticsType.MOST_UNDO)), labelStyle);


        Label pipeUsed_label = new Label("PipeType Used", labelStyle);

        Label pipe1_label = new Label("Left_right", labelStyle);
        Label pipe1_value = new Label(String.valueOf(statistics.get(StatisticsType.PIPE_LEFT_RIGHT_USE)), labelStyle);

        Label pipe2_label = new Label("Top_bottom", labelStyle);
        Label pipe2_value = new Label(String.valueOf(statistics.get(StatisticsType.PIPE_TOP_BOTTOM_USE)), labelStyle);

        Label pipe3_label = new Label("Left_top", labelStyle);
        Label pipe3_value = new Label(String.valueOf(statistics.get(StatisticsType.PIPE_LEFT_TOP_USE)), labelStyle);

        Label pipe4_label = new Label("Left_bottom", labelStyle);
        Label pipe4_value = new Label(String.valueOf(statistics.get(StatisticsType.PIPE_LEFT_BOTTOM_USE)), labelStyle);

        Label pipe5_label = new Label("Right_top", labelStyle);
        Label pipe5_value = new Label(String.valueOf(statistics.get(StatisticsType.PIPE_RIGHT_TOP_USE)), labelStyle);

        Label pipe6_label = new Label("Right_bottom", labelStyle);
        Label pipe6_value = new Label(String.valueOf(statistics.get(StatisticsType.PIPE_RIGHT_BOTTOM_USE)), labelStyle);

        Label pipe7_label = new Label("All_direction", labelStyle);
        Label pipe7_value = new Label(String.valueOf(statistics.get(StatisticsType.PIPE_ALL_DIRECTION_USE)), labelStyle);

        Label pipe7Full_label = new Label("Full_all_direction", labelStyle);
        Label pipe7Full_value = new Label(String.valueOf(statistics.get(StatisticsType.PIPE_ALL_DIRECTION_FULL_USE)), labelStyle);

        table.padTop(100);
        table.add(totalPlayTime_label).padLeft(YayPipe.SCREEN_WIDTH * 0.1f).expandX().align(Align.left);
        table.add(totalPlayTime_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(totalPipePlaced_label).padLeft(YayPipe.SCREEN_WIDTH * 0.1f).expandX().align(Align.left);
        table.add(totalPipePlaced_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(totalUndo_label).padLeft(YayPipe.SCREEN_WIDTH * 0.1f).expandX().align(Align.left);
        table.add(totalUndo_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(totalTryCount_label).padLeft(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.left);
        table.add(totalTryCount_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(totalRestart_label).padLeft(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.left);
        table.add(totalRestart_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(totalFailCount_label).padLeft(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.left);
        table.add(totalFailCount_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(clear_label).colspan(2).padTop(YayPipe.SCREEN_HEIGHT * 0.01f).padLeft(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.left);
        table.row();

        table.add(easyClear_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(easyClear_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(normalClear_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(normalClear_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(hardClear_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(hardClear_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(proClear_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(proClear_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(masterClear_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(masterClear_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();


        table.add(highScore_label).colspan(2).padTop(YayPipe.SCREEN_HEIGHT * 0.01f).padLeft(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.left);
        table.row();

        table.add(easyHigh_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(easyHigh_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(normalHigh_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(normalHigh_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(hardHigh_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(hardHigh_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(proHigh_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(proHigh_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(masterHigh_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(masterHigh_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();


        table.add(clearTime_label).colspan(2).padTop(YayPipe.SCREEN_HEIGHT * 0.01f).padLeft(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.left);
        table.row();

        table.add(easyTime_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(easyTime_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(normalTime_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(normalTime_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(hardTime_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(hardTime_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(proTime_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(proTime_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(masterTime_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(masterTime_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();


        table.add(mostPipeConnected_label).padTop(YayPipe.SCREEN_HEIGHT * 0.01f).padLeft(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.left);
        table.add(mostPipeConnected_value).padTop(YayPipe.SCREEN_HEIGHT * 0.01f).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(mostPipeChanged_label).padLeft(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.left);
        table.add(mostPipeChanged_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(mostUndo_label).padLeft(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.left);
        table.add(mostUndo_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();


        table.add(pipeUsed_label).colspan(2).padTop(YayPipe.SCREEN_HEIGHT * 0.01f).padLeft(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.left);
        table.row();

        table.add(pipe1_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(pipe1_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(pipe2_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(pipe2_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(pipe3_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(pipe3_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(pipe4_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(pipe4_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(pipe5_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(pipe5_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(pipe6_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(pipe6_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(pipe7_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(pipe7_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.row();

        table.add(pipe7Full_label).padLeft(YayPipe.SCREEN_WIDTH * 0.15f).align(Align.left);
        table.add(pipe7Full_value).padRight(YayPipe.SCREEN_WIDTH * 0.1f).align(Align.right);
        table.padBottom(100);

        scrollPane = new ScrollPane(table);
        scrollPane.setSize(YayPipe.SCREEN_WIDTH, YayPipe.SCREEN_HEIGHT - upperBarUI.getHeight());
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
                            game.setScreenWithFadeInOut(new MainMenuScreen(game));
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
