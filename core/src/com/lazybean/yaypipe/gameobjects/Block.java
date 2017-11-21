package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.lazybean.yaypipe.gamehelper.PipeType;

public abstract class Block extends Group implements Pool.Poolable {
    private TextureRegion blockTexture;
    protected Pipe pipe;

    public Block(TextureRegion blockTexture, float length){
        this.blockTexture = blockTexture;

        setBounds(0,0,length, length);
        setOrigin(getWidth()/2, getHeight()/2);
    }

//    public void init(TextureRegion blockTexture){
//        this.blockTexture = blockTexture;
//    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        batch.draw(blockTexture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        if (pipe != null) {
            if (pipe.getImage() != null) {
                batch.draw(pipe.getImage(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), pipe.getScaleX(), pipe.getScaleY(), getRotation());
            }
        }

        batch.setColor(Color.WHITE);

        super.draw(batch, parentAlpha);
    }

    public void setPipe(TextureRegion pipeImage, PipeType pipeType) {
        if (pipe == null) {
            pipe = Pools.obtain(Pipe.class);
        }
        pipe.init(pipeImage, pipeType);
    }

    public void setPipe(Pipe pipe){
        if (pipe != null) {
            setPipe(pipe.getImage(), pipe.getType());
        } else{
            this.pipe = null;
        }
    }

    public Pipe getPipe(){
        return pipe;
    }

    @Override
    public void reset() {
        if (pipe != null) {
            Pools.free(pipe);
            pipe = null;
        }
    }
}
