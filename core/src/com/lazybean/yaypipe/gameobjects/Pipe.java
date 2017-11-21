package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.PipeType;

public class Pipe extends Actor implements Pool.Poolable {
    private PipeType pipeType;
    private TextureRegion pipeImage;

    public void init(TextureRegion pipeImage, PipeType pipeType) {
        this.pipeType = pipeType;
        this.pipeImage = pipeImage;
    }

    public PipeType getType() {
        return pipeType;
    }

    public TextureRegion getImage(){
        return pipeImage;
    }

    @Override
    public void reset() {
        pipeType = null;
        pipeImage = null;
    }
}
