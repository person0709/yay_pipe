package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gui.Background;

public class TapToStart extends Table{
    private boolean isTouched = false;

    private Background background;

    public TapToStart(AssetLoader assetLoader){
        this.setTouchable(Touchable.enabled);
        this.background = new Background(assetLoader.background);
        background.setColor(1,1,1,0.5f);
        addActor(background);

        setFillParent(true);
        //setDebug(true);
        Image hand = new Image(assetLoader.tapToStart);
        hand.setScaling(Scaling.fillY);

        Label.LabelStyle labelStyle = new Label.LabelStyle(assetLoader.mediumFont_anja, Color.BLACK);
        Label label = new Label("TAP TO START!", labelStyle);

        add(hand).height(Gdx.graphics.getHeight() * 0.2f);
        row();

        add(label);


        this.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
               isTouched = true;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = this.getColor();
        background.setColor(1,1,1,0.5f * color.a);
        super.draw(batch, parentAlpha);
    }

    public boolean isTouched(){
        return isTouched;
    }

    public void setTouched(boolean touched){
        isTouched = touched;
    }
}
