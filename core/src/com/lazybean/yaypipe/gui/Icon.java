package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.lazybean.yaypipe.gameobjects.Block;

public class Icon extends Group {
    public static final int BACK = 0;
    public static final int PAUSE = 1;
    public static final int UNDO = 2;
    public static final int FAST_FORWARD = 3;

    private TextureRegion background, icon;
    private Label number;
    private float diameter;

    private ScaleToAction scaleToAction = new ScaleToAction();

    public Icon(TextureRegion background, TextureRegion icon){
        this.background = background;
        this.icon = icon;

        diameter = Gdx.graphics.getWidth() * 0.11f;
        setBounds(0,0,diameter, diameter);
        setOrigin(getWidth()/2, getHeight()/2);

        this.addListener(new InputListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                scaleToAction.reset();
                scaleToAction.setScale(0.9f);
                scaleToAction.setDuration(0.05f);

                addAction(scaleToAction);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                scaleToAction.reset();
                scaleToAction.setScale(1f);
                scaleToAction.setDuration(0.05f);

                addAction(scaleToAction);
            }
        });
    }

    public Icon (TextureRegion background, Label number){
        this.background = background;
        diameter = Block.LENGTH * 0.4f;
        setBounds(0,0,diameter, diameter);
        setOrigin(getWidth()/2, getHeight()/2);

        this.number = number;
        this.number.setBounds(getX(), getY(), getWidth(), getHeight());
        this.number.setAlignment(Align.center);
        addActor(this.number);
    }

    public Icon (Icon copy){
        this.background = copy.getBackground();
        diameter = Block.LENGTH * 0.35f;
        setBounds(0,0,diameter, diameter);
        setOrigin(getWidth()/2, getHeight()/2);

        Label.LabelStyle labelStyle = new Label.LabelStyle(copy.getNumber().getStyle());
        Label label = new Label(copy.getNumber().getText(),labelStyle);
        label.setBounds(getX(), getY(), getWidth(), getHeight());
        label.setFontScale(0.8f);
        label.setAlignment(Align.center);

        addActor(label);
    }

    public void setIconImage(TextureRegion icon){
        this.icon = icon;
    }

    public void setDiameter(float diameter){
        this.diameter = diameter;
        setBounds(0,0,diameter, diameter);
        setOrigin(getWidth()/2, getHeight()/2);
    }

    public void setDisable(){
        Color color = getColor();
        setColor(color.r, color.g, color.b, 0.5f);
    }

    public void setAble() {
        Color color = getColor();
        setColor(color.r, color.g, color.b, 1);
    }

    public TextureRegion getBackground(){
        return background;
    }

    public Label getNumber(){
        return number;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor());
        batch.draw(background, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());

        batch.setColor(1,1,1,getColor().a);
        if (icon != null) {
            batch.draw(icon, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                    getScaleX(), getScaleY(), getRotation());
        }
        super.draw(batch, parentAlpha);
        batch.setColor(Color.WHITE);
    }
}