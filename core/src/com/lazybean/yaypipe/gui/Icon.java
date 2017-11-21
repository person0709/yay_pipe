package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;
import com.lazybean.yaypipe.gamehelper.IconType;
import com.lazybean.yaypipe.gamehelper.SoundType;

public class Icon extends Actor {
    public static final float MENU_DIAMETER = YayPipe.SCREEN_WIDTH * 0.11f;
    public static final float ITEM_DIAMETER = YayPipe.SCREEN_WIDTH * 0.15f;

    private TextureRegion background, iconTexture;

    private boolean isAble = true;
    private boolean isTouched;

    public Icon(final AssetLoader assetLoader, IconType iconType, float diameter){
        this.background = assetLoader.circle;
        this.iconTexture = assetLoader.getIconTexture(iconType);

        setBounds(0,0, diameter, diameter);
        setOrigin(getWidth()/2, getHeight()/2);

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

        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isAble) {
                    isTouched = true;
                    if (GameData.getInstance().isSoundOn()) {
                        assetLoader.getSound(SoundType.CLICK).play(GameData.getInstance().getSoundVolume());
                    }
                }
            }
        });
    }

    public void setIconImage(TextureRegion iconTexture){
        this.iconTexture = iconTexture;
    }

    public boolean isTouched() {
        return isTouched;
    }

    public void setTouched(boolean touched) {
        isTouched = touched;
    }

    public void setDiameter(float diameter){
        setBounds(0,0,diameter, diameter);
        setOrigin(getWidth()/2, getHeight()/2);
    }

    public void setDim(boolean dim) {
        if (dim) {
            Color color = getColor();
            setColor(color.r, color.g, color.b, 0.5f);
        }
        else{
            Color color = getColor();
            setColor(color.r, color.g, color.b, 1f);
        }
    }

    public void setAble(boolean able){
        isAble = able;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(background, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());

        batch.setColor(1,1,1, color.a * parentAlpha);
        batch.draw(iconTexture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                    getScaleX(), getScaleY(), getRotation());

        batch.setColor(Color.WHITE);
    }
}