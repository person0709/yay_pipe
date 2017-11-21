package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.gamedata.CoinBank;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;
import com.lazybean.yaypipe.gamehelper.IconType;
import com.lazybean.yaypipe.gamehelper.StatisticsType;
import com.lazybean.yaypipe.screens.MainMenuScreen;


public class SetupUpperBarUI extends UpperBarUI {
    private YayPipe game;

    private Label balance;
    private CoinBank coinBank;

    public Icon backIcon;

    public SetupUpperBarUI(AssetLoader assetLoader, YayPipe game) {
        super(assetLoader);
        this.game = game;

        coinBank = GameData.getInstance().coinBank;

        Table controlIcons = new Table();
        controlIcons.align(Align.left);
        add(controlIcons);

        backIcon = new Icon(assetLoader, IconType.UNDO, Icon.MENU_DIAMETER);
        backIcon.setColor(Color.BLACK);
        controlIcons.add(backIcon).padLeft(YayPipe.SCREEN_WIDTH * 0.07f);


        Table highScore = new Table();
        add(highScore).align(Align.right).padRight(YayPipe.SCREEN_WIDTH * 0.07f).expandX();

        Label label = new Label("HIGH SCORE", assetLoader.uiSkin, "setupUpperUIChar");
        label.setColor(Color.BLACK);
        label.setAlignment(Align.right);

        Label value = new Label(String.valueOf(GameData.getInstance().statistics.get(StatisticsType.HIGHSCORE_ALL)),
                assetLoader.uiSkin, "setupUpperUIValue");
        value.setColor(Color.BLACK);
        value.setAlignment(Align.right);

        highScore.add(label).height(label.getHeight()*0.6f).padTop(label.getHeight()*0.5f).row();
        highScore.add(value).align(Align.right);

        Table coinBalance = new Table();
        //scoreDisplayGroup.setDebug(true);
        add(coinBalance).align(Align.right);

        Image coin = new Image(assetLoader.coin);
        coin.setScaling(Scaling.fillX);

        /*
          test purpose
         */
        coin.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameData.getInstance().coinBank.addBalance(1000);
            }
        });

        Label coinLabel = new Label("BALANCE", assetLoader.uiSkin, "setupUpperUIChar");
        coinLabel.setColor(Color.BLACK);
        coinLabel.setAlignment(Align.right);

        balance = new Label(String.valueOf(coinBank.getBalance()), assetLoader.uiSkin, "setupUpperUIValue");
        balance.setColor(Color.BLACK);
        balance.setAlignment(Align.right);

        coinBalance.padRight(YayPipe.SCREEN_WIDTH * 0.07f);
        coinBalance.add(coinLabel).align(Align.right).colspan(2).height(coinLabel.getHeight()*0.6f).padTop(coinLabel.getHeight()*0.5f).row();
        coinBalance.add(coin).size(balance.getHeight() * 0.8f, balance.getHeight()).align(Align.right).padRight(Value.percentWidth(0.1f)).expandX();
        coinBalance.add(balance).align(Align.right);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        coinBank.update(50);
        balance.setText(String.valueOf(coinBank.getCurrentBalance()));

        if (backIcon.isTouched()){
            backIcon.setTouched(false);
            game.setScreen(new MainMenuScreen(game));
        }
    }
}
