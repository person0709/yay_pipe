package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.guiobjects.Badge;

public class GridBlock extends Block{
    public static final float BLOCK_LENGTH = YayPipe.SCREEN_WIDTH * 0.1f;
    public static final float BLOCK_GAP = BLOCK_LENGTH * 0.04f;

    private Badge badge;

    private int posX,posY;

    private int stopNum = 0;
    private int flowCount = 0;

    private int pipeChangeCount = 0;

    public GridBlock(TextureRegion blockTexture) {
        super(blockTexture, BLOCK_LENGTH);

        addListener(new InputListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                addAction(Actions.scaleTo(1.2f, 1.2f, 0.1f));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                addAction(Actions.scaleTo(1f, 1f, 0.1f));
            }
        });
    }

    /**
     * set position on the grid (y-up coordination)
     */
    public void setGridPos(int x, int y){
        posX = x;
        posY = y;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void addStopBadge(Badge badge, int stopNum){
        this.badge = badge;
        this.stopNum = stopNum;

        setZIndex(getParent().getChildren().size);
        badge.setColor(Badge.BADGE_COLORS.get(stopNum-1).getColor());
        badge.setPosition(0, getHeight() - badge.getHeight());
        badge.setScale(0f);
        addActor(badge);
    }

    public void removeBadge(){
        this.stopNum = 0;
        badge.remove();
    }

    public Badge getBadge() {
        return badge;
    }

    public int getStopNum(){
        return stopNum;
    }

    //number of water flow passed through the block(vertical or horizontal or curved = 1, vertical and horizontal = 2)
    public void addFlowCount(){
        flowCount++;
    }

    public int getFlowCount(){
        return flowCount;
    }

    public int getPipeChangeCount() {
        return pipeChangeCount;
    }

    public void setPipeChangeCount(int pipeChangeCount) {
        this.pipeChangeCount = pipeChangeCount;
    }

    @Override
    public void reset() {
        super.reset();
        pipeChangeCount = 0;
        flowCount = 0;
        stopNum = 0;
    }
}
