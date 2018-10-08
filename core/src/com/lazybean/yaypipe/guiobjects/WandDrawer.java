package com.lazybean.yaypipe.guiobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gamehelper.IconType;
import com.lazybean.yaypipe.gamehelper.PipeType;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.gameobjects.WandBlock;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;


public class WandDrawer extends Group implements Disposable {
    public static final int MAX_STOCK = 5;
    public static final int PRICE = 100;

    private GameWorld gameWorld;
    private AssetLoader assetLoader;

    public Group drawerCircle;

    private boolean isSelected = false;

    private WandBlock pipeSelected;

    private Array<WandBlock> blockArray;
    private Icon cancelIcon;

    public Background fadeInOut;
    private TweenManager tweenManager;

    public WandDrawer(final AssetLoader assetLoader, GameWorld gameWorld){
        this.gameWorld = gameWorld;
        this.assetLoader = assetLoader;
        blockArray = new Array<>(7);

        drawerCircle = new Group();

        fadeInOut = new Background();
        fadeInOut.setColor(Color.BLACK);
        fadeInOut.setAlpha(0f);

        //radius of the circle drawerCircle
        drawerCircle.setSize(YayPipe.SCREEN_WIDTH/3, YayPipe.SCREEN_WIDTH/3);
        drawerCircle.setPosition(YayPipe.SCREEN_WIDTH/2, YayPipe.SCREEN_HEIGHT/2);
        drawerCircle.setOrigin(getWidth()/2, getHeight()/2);

        for (int i = 0; i < 7; i++){
            final WandBlock block = assetLoader.blockFactory.obtainItemBlock();
            block.init(assetLoader.getPipeImage(PipeType.values()[i]), PipeType.values()[i]);
            block.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    isSelected = true;
                    pipeSelected = block;
                }
            });
            blockArray.add(block);
            drawerCircle.addActor(block);
        }

        cancelIcon = new Icon(assetLoader, IconType.CROSS, Icon.MENU_DIAMETER);
        cancelIcon.setPosition(0,0,Align.center);
        cancelIcon.setColor(CustomColor.RED.getColor());
        drawerCircle.addActor(cancelIcon);

        drawerCircle.setScale(0);

        addActor(fadeInOut);
        addActor(drawerCircle);

        tweenManager = new TweenManager();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        tweenManager.update(delta);

        for (int i = 0; i < 7; i++) {
            // calculate position of each pipe block in the circle drawerCircle
            blockArray.get(i).setPosition(MathUtils.cos(i * (MathUtils.PI2 / 7) + MathUtils.PI / 2) * (drawerCircle.getWidth() * drawerCircle.getScaleX()),
                    MathUtils.sin(i * (MathUtils.PI2 / 7) + MathUtils.PI / 2) * (drawerCircle.getHeight() * drawerCircle.getScaleY()), Align.center);
        }

        if (cancelIcon.isTouched()){
            cancelIcon.setTouched(false);
            collapse();
        }

        if (isSelected){
            isSelected = false;
            gameWorld.getGui().useWand(this, pipeSelected);
        }
    }

    public void expand(){
        Gdx.app.log("drawerCircle", "expanded");
        gameWorld.getGui().getGuiStage().addActor(this);

        gameWorld.getGrid().setTouchable(Touchable.disabled);

        Timeline.createParallel()
                .push(Tween.to(drawerCircle, SpriteAccessor.SCALE, 0.1f).target(1f))
                .push(Tween.to(fadeInOut, SpriteAccessor.ALPHA, 0.1f).target(0.5f))
                .start(tweenManager);
    }

    public void collapse(){
        Timeline.createParallel()
                .push(Tween.to(drawerCircle, SpriteAccessor.SCALE, 0.2f).target(0f))
                .push(Tween.to(fadeInOut, SpriteAccessor.ALPHA, 0.2f).target(0f))
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        gameWorld.getGrid().setTouchable(Touchable.enabled);
                    }
                }).setCallbackTriggers(TweenCallback.END)
                .start(tweenManager);
    }

    @Override
    public void dispose() {
        for (WandBlock block : blockArray){
            assetLoader.blockFactory.free(block);
        }
    }
}
