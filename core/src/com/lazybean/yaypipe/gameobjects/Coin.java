package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lazybean.yaypipe.gamehelper.AssetLoader;

public class Coin extends Actor{
    private TextureRegion image;

    public Coin(AssetLoader assetLoader){
        image = assetLoader.coin;
        setSize(Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getWidth() * 0.05f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = new Color(batch.getColor());
        batch.setColor(getColor());
        batch.draw(image, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        batch.setColor(color);
    }
}
