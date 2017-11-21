package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.GameState;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;
import com.lazybean.yaypipe.gameobjects.Snail;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Quad;
import aurelienribon.tweenengine.equations.Quart;


public class SnailEffect extends Actor {
    private GameWorld gameWorld;

    private boolean isActive = false;
    private float timePassed = 0;

    private TweenManager tweenManager;
    private ShapeRenderer shapeRenderer;

    public SnailEffect(GameWorld gameWorld){
        this.gameWorld = gameWorld;
        gameWorld.getGameWorldStage().addActor(this);
        tweenManager = new TweenManager();
        shapeRenderer = new ShapeRenderer();

        setHeight(YayPipe.SCREEN_HEIGHT * 1.1f);
        setPosition(YayPipe.SCREEN_WIDTH/2, YayPipe.SCREEN_HEIGHT/2);
        setOrigin(getWidth()/2, getHeight()/2);
        setScale(0);
        setTouchable(Touchable.disabled);
    }

    public void activate() {
        isActive = true;
        gameWorld.getGrid().getWater().setSnail(true);

        gameWorld.getGameWorldStage().addActor(this);

        //puts the effect behind the grid
        setZIndex(0);

        gameWorld.snail.setStock(gameWorld.snail.getStock() - 1);

        Tween.to(this, SpriteAccessor.SCALE, 1.5f).target(1f).ease(Quad.OUT).start(tweenManager);
    }

    public void deactivate(){
        Tween.to(this, SpriteAccessor.SCALE, 1.5f).target(0f).ease(Quad.IN)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        SnailEffect.this.remove();
                        timePassed = 0;
                    }
                }).setCallbackTriggers(TweenCallback.COMPLETE).start(tweenManager);
    }

    @Override
    public void act(float delta) {
        tweenManager.update(delta);
        if (isActive && gameWorld.getState() == GameState.RUNNING){
            if (timePassed > Snail.ACTIVE_TIME){
                deactivate();
                isActive = false;
                gameWorld.getGrid().getWater().setSnail(false);
            }
            timePassed += delta;
        }
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.SKY);
        shapeRenderer.circle(getX(), getY(), getHeight() * getScaleX());
        shapeRenderer.end();
        batch.begin();
    }
}
