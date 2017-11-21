package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gamehelper.FontType;
import com.lazybean.yaypipe.gamehelper.IconType;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;
import com.lazybean.yaypipe.gameobjects.Snail;
import com.lazybean.yaypipe.gameobjects.Wand;
import com.lazybean.yaypipe.screens.GameScreen;

public class ItemSelector extends Table{
    private GameScreen screen;

    private int snailStock;
    private int wandStock;

    private Icon wandIcon;
    private Badge wandStockBadge;
    private Icon snailIcon;
    private Badge snailStockBadge;

    public ItemSelector(AssetLoader assetLoader, GameScreen screen){
        this.screen = screen;

        Label itemLabel = new Label("ITEMS", assetLoader.uiSkin, "setupSubtitle");

        snailStock = GameData.getInstance().getSnailStock();
        wandStock = GameData.getInstance().getWandStock();

        Table itemTable = new Table();

        Table snailTable = new Table();
        itemTable.add(snailTable);

        Image coin = new Image(assetLoader.coin);
        coin.setScaling(Scaling.fillX);
        Label.LabelStyle itemLabelStyle = new Label.LabelStyle(assetLoader.getFont(FontType.ANJA_SMALL), Color.BLACK);
        Label snailLabel = new Label(String.valueOf(Snail.PRICE), itemLabelStyle);

        Group snailIconGroup = new Group();
        snailIconGroup.setSize(YayPipe.SCREEN_HEIGHT * 0.08f, YayPipe.SCREEN_HEIGHT * 0.08f);

        snailIcon = new Icon(assetLoader, IconType.SNAIL, Icon.ITEM_DIAMETER);
        snailIcon.setDiameter(YayPipe.SCREEN_HEIGHT*0.08f);
        snailIcon.setColor(CustomColor.RED.getColor());
        snailIconGroup.addActor(snailIcon);

        snailStockBadge = new Badge(assetLoader, "x" + String.valueOf(snailStock));
        snailStockBadge.setPosition(snailIconGroup.getRight(),0, Align.bottomRight);
        snailStockBadge.setColor(Color.BLACK);
        snailIconGroup.addActor(snailStockBadge);

        snailTable.add(snailIconGroup).colspan(2).row();
        snailTable.add(coin).width(snailLabel.getHeight()).padRight(10);
        snailTable.add(snailLabel);

        if (snailStock == 0){
            snailIcon.setDim(true);
            snailStockBadge.setVisible(false);
        }


        Table wandTable = new Table();
        itemTable.add(wandTable).padLeft(YayPipe.SCREEN_WIDTH * 0.2f);

        Group wandIconGroup = new Group();
        wandIconGroup.setSize(YayPipe.SCREEN_HEIGHT * 0.08f, YayPipe.SCREEN_HEIGHT * 0.08f);

        wandIcon = new Icon(assetLoader, IconType.WAND, Icon.ITEM_DIAMETER);
        wandIcon.setDiameter(YayPipe.SCREEN_HEIGHT*0.08f);
        wandIcon.setColor(CustomColor.RED.getColor());
        wandIconGroup.addActor(wandIcon);

        wandStockBadge = new Badge(assetLoader, "x" + String.valueOf(wandStock));
        wandStockBadge.setPosition(wandIconGroup.getRight(),0, Align.bottomRight);
        wandStockBadge.setColor(Color.BLACK);
        wandIconGroup.addActor(wandStockBadge);

        Image coin2 = new Image(assetLoader.coin);
        coin2.setScaling(Scaling.fillX);
        Label wandLabel = new Label(String.valueOf(Wand.PRICE), itemLabelStyle);

        wandTable.add(wandIconGroup).colspan(2).row();
        wandTable.add(coin2).width(wandLabel.getHeight()).padRight(10);
        wandTable.add(wandLabel);

        if (wandStock == 0){
            wandIcon.setDim(true);
            wandStockBadge.setVisible(false);
        }

        add(itemLabel).padBottom(30).row();
        add(itemTable);
    }

    @Override
    public void act(float delta) {
        if (wandIcon.isTouched()){
            wandIcon.setTouched(false);
            if (wandStock < Wand.MAX_STOCK && GameData.getInstance().coinBank.getBalance() >= Wand.PRICE){
                wandIcon.setDim(false);
                wandStock++;
                GameData.getInstance().setWandStock(wandStock);
                GameData.getInstance().coinBank.addBalance(-Wand.PRICE);

                wandStockBadge.setBadgeLabel("x" + String.valueOf(wandStock));
                wandStockBadge.setVisible(true);
            }
            else if (wandStock >= Wand.MAX_STOCK){
                screen.addMessage("Don't be greedy!\nYou can only have " + Wand.MAX_STOCK + "!");
            }
            else{
                screen.addMessage("Not enough coins");
            }
        }

        if (snailIcon.isTouched()){
            snailIcon.setTouched(false);
            if (snailStock < Snail.MAX_STOCK && GameData.getInstance().coinBank.getBalance() >= Snail.PRICE){
                snailIcon.setDim(false);
                snailStock++;
                GameData.getInstance().setSnailStock(snailStock);
                GameData.getInstance().coinBank.addBalance(-Snail.PRICE);

                snailStockBadge.setBadgeLabel("x" + String.valueOf(snailStock));
                snailStockBadge.setVisible(true);
            }

            else if (snailStock >= Snail.MAX_STOCK){
                screen.addMessage("Don't be greedy!\nYou can only have " + Snail.MAX_STOCK + "!");
            }

            else{
                screen.addMessage("Not enough coins");
            }
        }
    }

    /**
     * update snail and wand stock
     */
    public void update() {
        snailStock = GameData.getInstance().getSnailStock();
        wandStock = GameData.getInstance().getWandStock();

        snailStockBadge.setBadgeLabel("x" + String.valueOf(snailStock));
        if (snailStock == 0){
            snailIcon.setDim(true);
            snailStockBadge.setVisible(false);
        }

        wandStockBadge.setBadgeLabel("x" + String.valueOf(wandStock));
        if (wandStock == 0){
            wandIcon.setDim(true);
            wandStockBadge.setVisible(false);
        }
    }
}
