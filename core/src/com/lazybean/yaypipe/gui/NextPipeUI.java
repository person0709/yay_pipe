package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Colour;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.gameobjects.NextBlockQueue;
import com.lazybean.yaypipe.gameobjects.RedBlock;

import aurelienribon.tweenengine.Tween;

public class NextPipeUI extends Group {
    private RedBlock redBlock;

    public NextPipeUI(AssetLoader assetLoader){
        setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * 0.12f);

        Background background = new Background();
        background.setColor(Colour.YELLOW);
        background.setHeight(getHeight());

        addActor(background);

        rb = new RedBlock(assetLoader.redBlock);
        nextPipeUI.addActor(rb);
        rb.setPosition(grid.getRefLeft(), nextPipeUI.getHeight() / 2 - rb.getHeight() / 2);

        nextBlockQueue = new NextBlockQueue(assetLoader);
        nextBlockQueue.setPosition(grid.getRefRight() - nextBlockQueue.getWidth() * 1.2f,
                nextPipeUI.getHeight()/2, Align.left);
        Tween.set(nextBlockQueue, SpriteAccessor.ALPHA).target(0f).start(tweenManager);
        nextPipeUI.addActor(nextBlockQueue);
    }
}
