package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;
import com.lazybean.yaypipe.gamehelper.IconType;
import com.lazybean.yaypipe.gamehelper.Score;
import com.lazybean.yaypipe.gamehelper.StatisticsType;


public class GamePlayUpperBarUI extends UpperBarUI {
    private GameWorld gameWorld;
    private Score score;

    private Icon pauseIcon, undoIcon, fastForwardIcon;
    private Label scoreNum, highScoreNum, scoreText, highScoreText;

    public GamePlayUpperBarUI(AssetLoader assetLoader, GameWorld gameWorld) {
        super(assetLoader);

        this.gameWorld = gameWorld;
        this.score = gameWorld.getScore();

        Table controlIcons = new Table();
        controlIcons.align(Align.left);
        add(controlIcons);

        pauseIcon = new Icon(assetLoader, IconType.PAUSE, Icon.MENU_DIAMETER);
        pauseIcon.setColor(CustomColor.RED.getColor());
        controlIcons.add(pauseIcon).padLeft(YayPipe.SCREEN_WIDTH * 0.07f);

        undoIcon = new Icon(assetLoader, IconType.UNDO, Icon.MENU_DIAMETER);
        undoIcon.setColor(CustomColor.RED.getColor());
        undoIcon.setDim(true);
        controlIcons.add(undoIcon).padLeft(YayPipe.SCREEN_WIDTH * 0.017f);

        fastForwardIcon = new Icon(assetLoader, IconType.FAST_FORWARD, Icon.MENU_DIAMETER);
        fastForwardIcon.setColor(CustomColor.RED.getColor());
        controlIcons.add(fastForwardIcon).padLeft(YayPipe.SCREEN_WIDTH * 0.017f);


        Table scoreDisplayGroup = new Table();
        //scoreDisplayGroup.setDebug(true);
        add(scoreDisplayGroup).expandX().align(Align.bottomRight);

        Table currentScore = new Table();
        //currentScore.setDebug(true);
        scoreText = new Label("score", skin, "upperBarHighScore");
        scoreText.setColor(Color.BLACK);
        scoreText.setAlignment(Align.center);

        scoreNum = new Label("0", skin, "upperBarHighScore");
        scoreNum.setColor(Color.BLACK);

        currentScore.add(scoreText).size(YayPipe.SCREEN_WIDTH * 0.22f, pauseIcon.getHeight()/2).row();
        currentScore.add(scoreNum).height(pauseIcon.getHeight()/2);

        scoreDisplayGroup.add(currentScore).align(Align.center).padRight(YayPipe.SCREEN_WIDTH*0.02f);


        Table highScore = new Table();
        //highScore.setDebug(true);

        highScoreText = new Label("HIGH", skin, "upperBarHighScore");
        highScoreText.setColor(CustomColor.TURQUOISE.getColor());
        highScoreText.setHeight(pauseIcon.getHeight()/2);

        highScoreNum = new Label(String.valueOf(GameData.getInstance().statistics.get(StatisticsType.HIGHSCORE_ALL)), skin, "upperBarHighScore");
        highScoreNum.setColor(CustomColor.TURQUOISE.getColor());

        highScore.add(highScoreText).height(pauseIcon.getHeight()/2 - 15).padTop(15).align(Align.bottom).row();
        highScore.add(highScoreNum).height(pauseIcon.getHeight()/2);

        scoreDisplayGroup.add(highScore).align(Align.center).padRight(YayPipe.SCREEN_WIDTH*0.07f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        score.update(delta);
        int currentScore = score.getCurrentScore();
        scoreNum.setText(String.valueOf(currentScore));
        if (currentScore > GameData.getInstance().statistics.get(StatisticsType.HIGHSCORE_ALL)) {
            setHighScoreText();
        }

        if (pauseIcon.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            pauseIcon.setTouched(false);
            gameWorld.pause();
        }
        if (fastForwardIcon.isTouched()){
            fastForwardIcon.setTouched(false);
            fastForwardIcon.setDim(true);
            gameWorld.getGrid().getWater().skip();
            gameWorld.getGrid().setTouchable(Touchable.disabled);
        }
        if (undoIcon.isTouched()){
            undoIcon.setTouched(false);

            undoIcon.setDim(true);

            gameWorld.undo();
        }
    }

    public void setUndoIcon(boolean able){
        undoIcon.setDim(!able);
        undoIcon.setAble(able);
    }

    private void setHighScoreText(){
        scoreText.setColor(CustomColor.RED.getColor());
        scoreNum.setColor(CustomColor.RED.getColor());
    }
}
