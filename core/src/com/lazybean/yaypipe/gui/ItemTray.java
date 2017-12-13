package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gamehelper.IconType;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;

public class ItemTray extends Table {
    private GameWorld gameWorld;

    private float tweenOffset;

    public SnailEffect snailEffect;
    private WandDrawer wandDrawer;

    private Icon snailIcon, wandIcon;
    private Badge snailStock;
    public Badge wandStock;

    public ItemTray(AssetLoader assetLoader, GameWorld gameWorld){
        this.gameWorld = gameWorld;
        this.snailEffect = new SnailEffect(gameWorld);
        this.wandDrawer = new WandDrawer(assetLoader, gameWorld);

        setBackground(new NinePatchDrawable(assetLoader.itemTray));
        setSize(YayPipe.SCREEN_WIDTH * 0.25f, YayPipe.SCREEN_WIDTH * 0.6f);
        setPosition(YayPipe.SCREEN_WIDTH * 0.7f, YayPipe.SCREEN_HEIGHT * 0.87f);

        tweenOffset = -getHeight() * 0.7f;

        Group snailGroup = new Group();
        snailGroup.setSize(YayPipe.SCREEN_WIDTH * 0.15f, YayPipe.SCREEN_WIDTH * 0.15f);

        snailIcon = new Icon(assetLoader, IconType.SNAIL, Icon.ITEM_DIAMETER);
        snailIcon.setDiameter(YayPipe.SCREEN_WIDTH * 0.15f);
        snailIcon.setColor(CustomColor.RED.getColor());

        snailStock = new Badge(assetLoader, "x" + String.valueOf(GameData.getInstance().getSnailStock()));
        snailStock.setPosition(snailGroup.getRight(),0, Align.bottomRight);
        snailStock.setColor(Color.BLACK);

        snailGroup.addActor(snailIcon);
        snailGroup.addActor(snailStock);
        add(snailGroup).padBottom(Value.percentHeight(0.3f)).padTop(Value.percentHeight(0.4f)).row();

        Group wandGroup = new Group();
        wandGroup.setSize(YayPipe.SCREEN_WIDTH * 0.15f, YayPipe.SCREEN_WIDTH * 0.15f);

        wandIcon = new Icon(assetLoader, IconType.WAND, Icon.ITEM_DIAMETER);
        wandIcon.setDiameter(YayPipe.SCREEN_WIDTH * 0.15f);
        wandIcon.setColor(CustomColor.RED.getColor());

        wandStock = new Badge(assetLoader, "x" + String.valueOf(GameData.getInstance().getWandStock()));
        wandStock.setPosition(wandGroup.getRight(),0, Align.bottomRight);
        wandStock.setColor(Color.BLACK);

        wandGroup.addActor(wandIcon);
        wandGroup.addActor(wandStock);
        add(wandGroup);

        setTouchable(Touchable.enabled);
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                addAction(Actions.sequence(
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                setTouchable(Touchable.disabled);
                            }
                        }),
                        Actions.moveBy(0,tweenOffset, 0.3f, Interpolation.swingOut),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                setTouchable(Touchable.enabled);
                            }
                        })));
                tweenOffset = -tweenOffset;
            }
        });

        if (GameData.getInstance().getSnailStock() == 0) {
            disableSnail();
        }
        if (GameData.getInstance().getWandStock() == 0) {
            disableWand();
        }
    }

    @Override
    public void act(float delta) {
        if (wandIcon.isTouched()){
            wandIcon.setTouched(false);
            wandDrawer.expand();
        }

        if (snailIcon.isTouched()){
            snailIcon.setTouched(false);
            snailEffect.activate();
            if (GameData.getInstance().getSnailStock() == 0){
                disableSnail();
            }
            else {
                snailStock.setBadgeLabel("x" + String.valueOf(GameData.getInstance().getSnailStock()));
            }
        }
        super.act(delta);
    }

    public void disableSnail(){
        snailIcon.setAble(false);
        snailIcon.setDim(true);
        snailStock.remove();
    }

    public void disableWand(){
        wandIcon.setAble(false);
        wandIcon.setDim(true);
        wandStock.remove();
    }
}
