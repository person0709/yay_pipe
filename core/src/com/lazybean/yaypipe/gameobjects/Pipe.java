package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.PipeType;

public class Pipe extends Actor{
    private PipeType pipeType;
    private TextureRegion pipeImage;

    public Pipe(AssetLoader assetLoader, PipeType pipeType){
        this.pipeType = pipeType;
        pipeImage = assetLoader.getPipeImage(pipeType);
    }

    public PipeType getPipeType() {
        return pipeType;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(pipeImage, getX(), getY(), getWidth(), getHeight());
    }
}
