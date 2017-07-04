package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class Snail extends Actor{
    public static final int PRICE = 500;
    private final float ACTIVE_TIME = 5f;

    private ShapeRenderer shapeRenderer;

    private boolean isActive = false;
    private boolean isAvailable = false;
    private boolean isDeactivated = false;
    private float timePassed = 0;

    public Snail(boolean isAvailable){
        shapeRenderer = new ShapeRenderer();
        this.isAvailable = isAvailable;

        setSize(Gdx.graphics.getHeight() * 1.5f, Gdx.graphics.getHeight() * 1.5f);
        setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        setOrigin(getWidth()/2, getHeight()/2);
        setScale(0);
        setTouchable(Touchable.disabled);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isDeactivated(){
        return isDeactivated;
    }

    public void setDeactivated(boolean deactivated){
        isDeactivated = deactivated;
    }

    public void update(float delta) {
        if (isActive){
            timePassed += delta;
            if (timePassed > ACTIVE_TIME){
                setActive(false);
                setDeactivated(true);
                timePassed = 0;
            }
        }
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
