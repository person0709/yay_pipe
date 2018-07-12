package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.GridSize;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.gameobjects.NextBlock;
import com.lazybean.yaypipe.gameobjects.WandBlock;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Elastic;

public class Gui {
    private GameWorld gameWorld;

    private TweenManager tweenManager;
    private AssetLoader assetLoader;
    private Stage guiStage;

    public GamePlayUpperBarUI upperBarUI;
    public NextPipeUI nextPipeUI;
    public ItemTray itemTray;
    public BadgeIndicator badgeIndicator;
    public ZoomUI zoomUI;

    private GamePausedWindow pausedWindow;
    public GameOverWindow gameOverWindow;

    public Gui(Stage guiStage, GameWorld gameWorld, AssetLoader assetLoader){
        this.assetLoader = assetLoader;
        this.guiStage = guiStage;
        this.gameWorld = gameWorld;

        tweenManager = new TweenManager();

        itemTrayInit();
        upperBarUIInit();
        nextPipeUIInit();
        badgeIndicatorInit();
        zoomUIInit();
        gamePausedWindowInit();

        ready();
    }

    private void itemTrayInit() {
//        if (GameData.getInstance().unlock.isItemUnlocked()) {
            itemTray = new ItemTray(assetLoader, gameWorld);
            guiStage.addActor(itemTray);
//        }
    }

    private void nextPipeUIInit() {
        nextPipeUI = new NextPipeUI(gameWorld, assetLoader);
        guiStage.addActor(nextPipeUI);
    }

    private void upperBarUIInit() {
        upperBarUI = new GamePlayUpperBarUI(assetLoader, gameWorld);
        guiStage.addActor(upperBarUI);
    }

    private void badgeIndicatorInit() {
        badgeIndicator = new BadgeIndicator(assetLoader, gameWorld.difficulty.stopNum);
        badgeIndicator.setBounds(0, upperBarUI.getY() - upperBarUI.getHeight()*0.5f ,
                YayPipe.SCREEN_WIDTH, upperBarUI.getHeight() * 0.5f);
        guiStage.addActor(badgeIndicator);
    }

    private void zoomUIInit() {
        zoomUI = new ZoomUI(assetLoader, gameWorld);
        if (gameWorld.gridSize == GridSize.TINY || gameWorld.gridSize == GridSize.SMALL){
            return;
        }
        guiStage.addActor(zoomUI);
    }

    private void gamePausedWindowInit(){
        pausedWindow = new GamePausedWindow(assetLoader, gameWorld);
    }

    public void showPausedWindow(){
        pausedWindow.show(guiStage);
    }

    public void showGameOverWindow(boolean isClear) {
        gameOverWindow = new GameOverWindow(assetLoader, isClear, gameWorld);
        upperBarUI.remove();
        nextPipeUI.remove();
        badgeIndicator.remove();
        zoomUI.remove();
        if (itemTray != null){
            itemTray.remove();
        }
        gameWorld.returnToDefaultView();
        gameOverWindow.show(guiStage);
    }

    public void update(float delta) {
        guiStage.act(delta);
        tweenManager.update(delta);
    }

    private void ready(){
        TapToStart tapToStart = new TapToStart(assetLoader, gameWorld, this);
        guiStage.addActor(tapToStart);
    }

    public void start(){
        nextPipeUI.start();
        Timeline.createParallel()
                .push(Tween.to(gameWorld.getGrid().getStartBlock().getPipe(), SpriteAccessor.SCALE, 0.5f).target(1f).ease(Elastic.OUT))
                .push(Tween.to(gameWorld.getGrid().getFinishBlock().getPipe(), SpriteAccessor.SCALE, 0.5f).target(1f).ease(Elastic.OUT))
                .start(tweenManager);
    }

    public void useWand(WandDrawer drawer, final WandBlock movingBlock){
        //decrease stock by 1
        int wandStock = GameData.getInstance().getWandStock() - 1;
        GameData.getInstance().setWandStock(wandStock);
        if (wandStock == 0){
            itemTray.disableWand();
        }
        else {
            itemTray.wandStock.setBadgeLabel("x" + String.valueOf(wandStock));
        }

        final NextBlock movingBlockClone = assetLoader.blockFactory.obtainNextBlock();
        movingBlockClone.setPipe(assetLoader.getPipeImage(movingBlock.getPipe().getType()), movingBlock.getPipe().getType());

        Vector2 srcPos = movingBlock.localToStageCoordinates(new Vector2());
        movingBlockClone.setPosition(srcPos.x, srcPos.y);
        guiStage.addActor(movingBlockClone);

        Timeline.createParallel()
                .push(Tween.to(drawer.drawerCircle, SpriteAccessor.SCALE, 0.2f).target(0f))
                .push(Tween.to(drawer.fadeInOut, SpriteAccessor.ALPHA, 0.2f).target(0f))
                .push(Tween.to(movingBlockClone, SpriteAccessor.POSITION, 0.2f).target(nextPipeUI.redBlock.getX() + NextPipeUI.RED_BLOCK_LINE_LENGTH,
                        nextPipeUI.redBlock.getY() + NextPipeUI.RED_BLOCK_LINE_LENGTH))
                .push(Tween.to(movingBlockClone, SpriteAccessor.SCALE, 0.2f).target(0.8f)
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                gameWorld.getGrid().setTouchable(Touchable.enabled);
                                nextPipeUI.set(0, movingBlockClone.getPipe());
                                movingBlockClone.remove();
                            }
                        }).setCallbackTriggers(TweenCallback.END))
                .start(tweenManager);
    }

    public Stage getGuiStage() {
        return guiStage;
    }

    public void dispose(){
        guiStage.dispose();
    }
}
