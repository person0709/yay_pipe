package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gameobjects.Block;
import com.lazybean.yaypipe.gameobjects.GridBlock;

public class FinishIndicator extends Image{

    public FinishIndicator(AssetLoader assetLoader, Block finishBlock){
        super(assetLoader.finishIndicator);
        setScaling(Scaling.fillX);
        setWidth(GridBlock.BLOCK_LENGTH * 0.5f);
        setPosition(finishBlock.getX() + finishBlock.getWidth()/2 - getWidth()/2,
                finishBlock.getY() + finishBlock.getHeight() * 0.8f);
        setAlign(Align.bottom);
    }

}
