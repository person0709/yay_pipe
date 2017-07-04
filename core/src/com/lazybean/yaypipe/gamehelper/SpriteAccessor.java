package com.lazybean.yaypipe.gamehelper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;

import aurelienribon.tweenengine.TweenAccessor;

public class SpriteAccessor implements TweenAccessor<Actor> {

    public static final int POSITION = 0;
    public static final int ALPHA = 1;
    public static final int SCALE = 2;
    public static final int COLOUR = 3;

    @Override
    public int getValues(Actor target, int tweenType, float[] returnValues) {
        switch(tweenType){
            case POSITION:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                return 2;

            case ALPHA:
                returnValues[0] = target.getColor().a;
                return 1;

            case SCALE:
                returnValues[0] = target.getScaleX();
                return 1;

            case COLOUR:
                returnValues[0] = target.getColor().r;
                returnValues[1] = target.getColor().g;
                returnValues[2] = target.getColor().b;

            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Actor target, int tweenType, float[] newValues) {
        switch (tweenType){
            case POSITION:
                target.setPosition(newValues[0],newValues[1]);
                break;

            case ALPHA:
                Color color = new Color(target.getColor());
                target.setColor(color.r, color.g, color.b, newValues[0]);
                break;

            case SCALE:
                target.setScale(newValues[0]);
                break;

            case COLOUR:
                target.setColor(newValues[0], newValues[1], newValues[2], 1);
        }
    }
}
