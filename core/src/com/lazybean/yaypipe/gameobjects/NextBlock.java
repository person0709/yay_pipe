package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.lazybean.yaypipe.gamehelper.PipeHashMap;

public class NextBlock extends Actor{
    public static final float LENGTH = Block.LENGTH * 1.2f;
    public static final float GAP =  LENGTH * 0.04f;

    private TextureRegion gridBlock;
    private TextureRegion pipeBlock;

    public NextBlock(TextureRegion block){
        gridBlock = block;
        setBounds(0, 0, LENGTH, LENGTH);
        setOrigin(getWidth()/2, getHeight()/2);
    }

    public void setPipe(int pipeNum){
        pipeBlock = PipeHashMap.numToPipe.get(pipeNum);
    }

    public int getPipe(){
        return PipeHashMap.pipeToNum.get(pipeBlock);
    }

    public float getLength(){
        return LENGTH;
    }

    public float getGap(){
        return GAP;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = this.getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(gridBlock,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
        batch.draw(pipeBlock,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
        batch.setColor(color.r, color.g, color.b, 1f);
    }

    public void nullify(){
        gridBlock = null;
        pipeBlock = null;
        setVisible(false);
    }
}
