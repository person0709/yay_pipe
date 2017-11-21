package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;

public class Coin extends Actor{
    private TextureRegion image;

    public Coin(AssetLoader assetLoader){
        image = assetLoader.coin;
        setSize(YayPipe.SCREEN_WIDTH * 0.05f, YayPipe.SCREEN_WIDTH * 0.05f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(image, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

        batch.setColor(Color.WHITE);
    }
}
