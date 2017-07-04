package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.PipeHashMap;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.PipeType;
import com.lazybean.yaypipe.gui.Icon;

public class Block extends Group {
    private float length;

    private AssetLoader assetLoader;
    private Pipe pipe;

    private int posX,posY;

    private boolean isEntered = false;
    private boolean isItem = false;

    private int stopNum = 0;
    private int flowCount = 0;

    private float pipePenalty = -30f;


    public Block(AssetLoader assetLoader, PipeType pipeType){
        isItem = true;

        this.assetLoader = assetLoader;
        setSize(Gdx.graphics.getWidth() * 0.15f, Gdx.graphics.getWidth() * 0.15f);
        setOrigin(getWidth()/2, getHeight()/2);

        pipe = new Pipe(assetLoader, pipeType);
        pipe.setScale(1f);
        pipe.setBounds(0,0,getWidth(),getHeight());
        pipe.setOrigin(getWidth()/2, getHeight()/2);
        addActor(pipe);

        addInputListener();
    }

    public Block(AssetLoader assetLoader, int x, int y) {
        this.assetLoader = assetLoader;

        posX = x;
        posY = y;

        setBounds(0,0, YayPipe.BLOCK_LENGTH, YayPipe.BLOCK_LENGTH);
        setOrigin(getWidth()/2, getHeight()/2);

//        pipe.setTouchable(Touchable.disabled);
//        pipe.setBounds(0,0,LENGTH,LENGTH);
//        pipe.setOrigin(LENGTH/2, LENGTH/2);
//        addActor(pipe);

        addInputListener();
    }

    public void addInputListener(){
        this.addListener(new InputListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                isEntered = true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                isEntered = false;
            }
        });
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getPipePenalty(){
        float tmp = pipePenalty;
        pipePenalty = tmp * 1.2f;
        return Math.round(tmp) ;
    }

    public void setStopNum(int num){
        stopNum = num;
        if (num == 0){
            return;
        }

        setZIndex(getParent().getChildren().size);
        LabelStyle labelStyle = new LabelStyle(assetLoader.extraSmallFont_anja, Color.WHITE);
        Label stopNumLabel = new Label(String.valueOf(num), labelStyle);
        Icon badge = new Icon(assetLoader.circle, stopNumLabel);
        badge.setColor(assetLoader.badgeColor.get(num-1));
        badge.setPosition(0, getHeight() - badge.getHeight());
        badge.setScale(0f);
        addActor(badge);

        MainGrid grid = (MainGrid) getParent();
        grid.getBadgeArray().add(badge);
    }

    public Actor getPipe(){
        return pipe;
    }

    public int getStopNum(){
        return stopNum;
    }

    //number of water flow passed through the block(vertical or horizontal or curved = 1, vertical and horizontal = 2)
    public void addFlowCount(){
        flowCount++;

        //be water my friend achievement
        if (flowCount == 2){
            AssetLoader.stats.addCrossPipeUse();
            YayPipe.playService.incrementAchievement(1);
        }
    }

    public int getFlowCount(){
        return flowCount;
    }

    @Override
    public void act(float delta) {
        if (isEntered){
            setScale(1.2f);
        }
        else {
            setScale(1f);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(assetLoader.gridBlock, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        super.draw(batch, parentAlpha);
    }
}
