package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class RedBlock extends Actor {
    public final float LENGTH = Block.LENGTH * 1.57f;
    public final float LINE_LENGTH = LENGTH * 0.11f;

    private TextureRegion redBlock;

    public RedBlock(TextureRegion redBlock){
        this.redBlock = redBlock;
        setWidth(LENGTH);
        setHeight(LENGTH);
        setOrigin(getWidth()/2, getHeight()/2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Color.WHITE);
        batch.draw(redBlock,getX(),getY(),getWidth(),getHeight());
    }
}
