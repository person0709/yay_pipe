package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pools;
import com.lazybean.yaypipe.gamehelper.PipeType;

/**
 * Block for wand item
 */

public class WandBlock extends Block{
    public static final float BLOCK_LENGTH = GridBlock.BLOCK_LENGTH * 1.5f;

    public WandBlock(TextureRegion blockTexture){
        super(blockTexture, BLOCK_LENGTH);
        setBounds(0, 0, BLOCK_LENGTH, BLOCK_LENGTH);
        setOrigin(getWidth()/2, getHeight()/2);
    }

    public void init(TextureRegion pipeImage, PipeType pipeType){
        pipe = Pools.obtain(Pipe.class);
        pipe.init(pipeImage, pipeType);
    }
}
