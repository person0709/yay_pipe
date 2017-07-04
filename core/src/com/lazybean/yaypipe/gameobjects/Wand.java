package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Colour;
import com.lazybean.yaypipe.gui.Background;
import com.lazybean.yaypipe.gui.Icon;

public class Wand extends Group{
    public static final int PRICE = 100;

    private boolean isAvailable = false;
    private boolean isOpen = false;
    private boolean isSelected = false;
    private boolean isCancelled = false;

    private int stock = 0;
    private int pipeSelected = 0;

    private Array<Block> blockArray;
    private Icon cancel;
    private Group drawer;
    private Background fadeInOut;

    public Wand(AssetLoader assetLoader, int stock){
        this.stock = stock;

        blockArray = new Array<Block>(7);

        drawer = new Group();

        fadeInOut = new Background(assetLoader.background);
        fadeInOut.setColor(Color.BLACK);
        fadeInOut.readyFadeIn();
        addActor(fadeInOut);

        if (stock > 0){
            setAvailable(true);
        }

        //radius of the circle drawer
        setSize(Gdx.graphics.getWidth()/3, Gdx.graphics.getWidth()/3);

        setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        setOrigin(getWidth()/2, getHeight()/2);
        for (int i = 0; i < 7; i++){
            final Block block = new Block(assetLoader, i+1);
            block.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    setSelected(true);
                    pipeSelected = block.getPipeImage();
                }
            });
            blockArray.add(block);
            drawer.addActor(block);
        }
        addActor(drawer);

        cancel = new Icon(assetLoader.circle, assetLoader.cross);
        cancel.setPosition(0,0,Align.center);
        cancel.setColor(Colour.RED);
        cancel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setCancelled(true);
            }
        });
        drawer.addActor(cancel);

        drawer.setScale(0);

        //move fade in to stage origin position
        fadeInOut.setPosition(-getX(), -getY());
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isSelected(){
        return isSelected;
    }

    public void setSelected(boolean selected){
        isSelected = selected;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public void setStock(int stock){
        this.stock = stock;
        if (this.stock > 0){
            setAvailable(true);
        }
        else{
            setAvailable(false);
        }
    }

    public int getStock(){
        return stock;
    }

    public Block getPipeSelected(){
        return blockArray.get(pipeSelected-1);
    }

    public Group getDrawer(){
        return drawer;
    }

    public Background getFadeInOut(){
        return fadeInOut;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isOpen()) {
            for (int i = 0; i < 7; i++) {
                // calculate position of each pipe block in the circle drawer
                blockArray.get(i).setPosition(MathUtils.cos(i * (MathUtils.PI2 / 7) + MathUtils.PI / 2) * getWidth(),
                        MathUtils.sin(i * (MathUtils.PI2 / 7) + MathUtils.PI / 2) * getHeight(), Align.center);
            }
        }
    }
}
