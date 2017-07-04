package com.lazybean.yaypipe.gui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Colour;
import com.lazybean.yaypipe.GameWorld;

public class UpperBarUI extends Table {
    public Background background;
    public TextureRegion shadow;

    private Icon undo, fastForward;
    public Label scoreNum, highScoreNum, scoreText, highScoreText, balance;
    private AssetLoader assetLoader;

    private boolean isPaused = false;
    private boolean isSkip = false;
    private boolean isUndo = false;
    private boolean isBack = false;

    public UpperBarUI(AssetLoader assetLoader){
        //setDebug(true);

        this.assetLoader = assetLoader;
        setBounds(0, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * 0.09f,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * 0.09f);

        this.background = new Background();
        background.setColor(Colour.YELLOW);
        background.setHeight(getHeight());
        addActor(background);

        shadow = new TextureRegion(assetLoader.shadow);
    }

    public void setForStatisticsScreen(){
        align(Align.left);
        Table controlIcons = new Table();
        controlIcons.align(Align.left);
        add(controlIcons);

        Icon back = new Icon(assetLoader.circle, assetLoader.undo);
        back.setColor(Color.BLACK);
        back.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                assetLoader.click.play(AssetLoader.prefs.getFloat("soundVolume"));
                isBack = true;
            }
        });
        controlIcons.add(back).padLeft(Gdx.graphics.getWidth() * 0.07f);
    }

    public void setForSettingScreen(){
        Table controlIcons = new Table();
        controlIcons.align(Align.left);
        add(controlIcons);

        Icon back = new Icon(assetLoader.circle, assetLoader.undo);
        back.setColor(Color.BLACK);
        back.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                assetLoader.click.play(AssetLoader.prefs.getFloat("soundVolume"));
                isBack = true;
            }
        });
        controlIcons.add(back).padLeft(Gdx.graphics.getWidth() * 0.07f);


        Table highScore = new Table();
        add(highScore).align(Align.right).padRight(Gdx.graphics.getWidth()*0.07f).expandX();

        Label.LabelStyle labelStyle = new Label.LabelStyle(assetLoader.extraSmallFont_noto, Color.WHITE);
        Label.LabelStyle valueStyle = new Label.LabelStyle(assetLoader.smallFont_noto, Color.WHITE);

        Label label = new Label("HIGH SCORE",labelStyle);
        label.setColor(Color.BLACK);
        label.setAlignment(Align.right);

        Label value = new Label(String.valueOf(AssetLoader.prefs.getInteger("highScore")),valueStyle);
        value.setColor(Color.BLACK);
        value.setAlignment(Align.right);

        highScore.add(label).height(label.getHeight()*0.6f).padTop(label.getHeight()*0.5f).row();
        highScore.add(value).align(Align.right);

        Table coinBalance = new Table();
        //scoreDisplayGroup.setDebug(true);
        add(coinBalance).align(Align.right);

        Image coin = new Image(assetLoader.coin);
        coin.setScaling(Scaling.fillX);

        Label coinLabel = new Label("BALANCE", labelStyle);
        coinLabel.setColor(Color.BLACK);
        coinLabel.setAlignment(Align.right);

        balance = new Label(String.valueOf(AssetLoader.coinBank.getBalance()), valueStyle);
        balance.setColor(Color.BLACK);
        balance.setAlignment(Align.right);

        coinBalance.padRight(Gdx.graphics.getWidth()*0.07f);
        coinBalance.add(coinLabel).align(Align.right).colspan(2).height(coinLabel.getHeight()*0.6f).padTop(coinLabel.getHeight()*0.5f).row();
        coinBalance.add(coin).size(balance.getHeight() * 0.8f, balance.getHeight()).align(Align.right).padRight(Value.percentWidth(0.1f)).expandX();
        coinBalance.add(balance).align(Align.right);
    }

    public void setForGamePlayScreen(){
        Table controlIcons = new Table();
        controlIcons.align(Align.left);
        add(controlIcons);

        Icon pause = new Icon(assetLoader.circle, assetLoader.pause);
        pause.setColor(Colour.RED);
        pause.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                assetLoader.click.play(AssetLoader.prefs.getFloat("soundVolume"));
                isPaused = true;
            }
        });
        controlIcons.add(pause).padLeft(Gdx.graphics.getWidth() * 0.07f);

        undo = new Icon(assetLoader.circle, assetLoader.undo);
        undo.setColor(Colour.RED);
        undo.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isUndo = true;
            }
        });
        undo.setDisable();
        controlIcons.add(undo).padLeft(Gdx.graphics.getWidth() * 0.017f);

        fastForward = new Icon(assetLoader.circle, assetLoader.fastForward);
        fastForward.setColor(Colour.RED);
        fastForward.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                assetLoader.click.play(AssetLoader.prefs.getFloat("soundVolume"));
                isSkip = true;
            }
        });
        controlIcons.add(fastForward).padLeft(Gdx.graphics.getWidth() * 0.017f);


        Table scoreDisplayGroup = new Table();
        //scoreDisplayGroup.setDebug(true);
        add(scoreDisplayGroup).expandX().align(Align.bottomRight);

        Table currentScore = new Table();
        //currentScore.setDebug(true);
        Label.LabelStyle labelStyle = new Label.LabelStyle(assetLoader.smallMediumFont_noto, Color.WHITE);
        Label.LabelStyle labelStyle2 = new Label.LabelStyle(assetLoader.smallFont_noto, Color.WHITE);
        scoreText = new Label("score", labelStyle);
        scoreText.setColor(Color.BLACK);
        scoreText.setAlignment(Align.center);

        scoreNum = new Label("0", labelStyle);
        scoreNum.setColor(Color.BLACK);

        currentScore.add(scoreText).size(Gdx.graphics.getWidth() * 0.22f, pause.getHeight()/2).row();
        currentScore.add(scoreNum).height(pause.getHeight()/2);

        scoreDisplayGroup.add(currentScore).align(Align.center).padRight(Gdx.graphics.getWidth()*0.02f);


        Table highScore = new Table();
        //highScore.setDebug(true);

        highScoreText = new Label("HIGH", labelStyle2);
        highScoreText.setColor(Colour.TURQUOISE);
        highScoreText.setHeight(pause.getHeight()/2);

        highScoreNum = new Label(String.valueOf(GameWorld.score.getHighScore()), labelStyle);
        highScoreNum.setColor(Colour.TURQUOISE);

        highScore.add(highScoreText).height(pause.getHeight()/2 - 15).padTop(15).align(Align.bottom).row();
        highScore.add(highScoreNum).height(pause.getHeight()/2);

        scoreDisplayGroup.add(highScore).align(Align.center).padRight(Gdx.graphics.getWidth()*0.07f);
    }

    public void setHighScoreText(){
        scoreText.setColor(Colour.RED);
        scoreNum.setColor(Colour.RED);
    }

    public boolean isPaused(){
        return isPaused;
    }

    public void setPaused(boolean bool){
        isPaused = bool;
    }

    public boolean isUndo() {
        return isUndo;
    }

    public void setUndo(boolean bool){
        isUndo = bool;
    }

    public boolean isSkip(){
        return isSkip;
    }

    public void setBack(boolean bool) {
        isBack = bool;
    }

    public boolean isBack() {
        return isBack;
    }

    public void setSkip(boolean bool){
        isPaused = bool;
    }

    public void setDisable(int icon){
        switch (icon){
            case Icon.UNDO:
                undo.setDisable();
                undo.setTouchable(Touchable.disabled);
                break;

            case Icon.FAST_FORWARD:
                fastForward.setDisable();
                fastForward.setTouchable(Touchable.disabled);
                break;
        }
    }

    public void setAble(int icon){
        switch (icon){
            case Icon.UNDO:
                undo.setAble();
                undo.setTouchable(Touchable.enabled);
                break;

            case Icon.FAST_FORWARD:
                fastForward.setAble();
                fastForward.setTouchable(Touchable.enabled);
                break;
        }
    }

    public Icon getUndo(){
        return undo;
    }

    public Icon getFastForward(){
        return fastForward;
    }

    @Override
    public void act(float delta) {
        if (scoreNum != null) {
            int score = GameWorld.score.getCurrentScore();
            scoreNum.setText(String.valueOf(score));
            if (GameWorld.score.isHighScore()) {
                setHighScoreText();
            }
        }
        if (balance != null){
            AssetLoader.coinBank.update(10);
            balance.setText(String.valueOf(AssetLoader.coinBank.getCurrentBalance()));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(shadow, getX(), getY() - shadow.getRegionHeight(), Gdx.graphics.getWidth(), shadow.getRegionHeight());
    }
}
