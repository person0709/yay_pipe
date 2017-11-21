package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.lazybean.yaypipe.gamehelper.PipeType;

public class NextBlock extends Block {
    public static final float BLOCK_LENGTH = GridBlock.BLOCK_LENGTH * 1.2f;
    public static final float BLOCK_GAP = BLOCK_LENGTH * 0.04f;

    public NextBlock(TextureRegion blockTexture) {
        super(blockTexture, BLOCK_LENGTH);
        setBounds(0,0, BLOCK_LENGTH, BLOCK_LENGTH);
        setOrigin(getWidth()/2, getHeight()/2);
    }
}
