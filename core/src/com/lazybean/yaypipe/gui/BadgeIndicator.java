package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.gui.Icon;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Back;

public class BadgeIndicator extends Table {
    private Array<Icon> badgeArray;
    private Array<Image> arrowArray;
    private int count = 0;

    private AssetLoader assetLoader;
    private TweenManager tweenManager = new TweenManager();

    public BadgeIndicator(AssetLoader assetLoader, Array<Icon> badges){
        if (badges.size == 0){
            return;
        }

        this.assetLoader = assetLoader;

        badgeArray = new Array<>(badges.size);
        arrowArray = new Array<>(badges.size);

        for (int i = 0; i < badges.size; i++){
            Image arrow = new Image(assetLoader.badgeArrow);
            arrow.setScaling(Scaling.fit);
            arrow.setColor(Color.BLACK);
            arrowArray.add(arrow);

            badgeArray.add(new Icon(badges.get(i)));
            badgeArray.get(i).setColor(Color.GRAY);

            add(arrow);
            add(badgeArray.get(i));
        }

        Tween.registerAccessor(Group.class, new SpriteAccessor());
        Tween.registerAccessor(Actor.class, new SpriteAccessor());

        Timeline.createSequence()
                .push(Tween.to(arrowArray.get(0), SpriteAccessor.ALPHA, 0.1f).target(0f))
                .repeatYoyo(Tween.INFINITY, 0.5f)
                .start(tweenManager);
    }

    @Override
    public void act(float delta){
        tweenManager.update(delta);
    }

    public void toNextBadge(){
        tweenManager.killAll();
        arrowArray.get(count).setColor(Color.WHITE);

        Color color = assetLoader.badgeColor.get(count);
        Timeline.createSequence()
                .push(Tween.to(badgeArray.get(count), SpriteAccessor.SCALE, 0.2f).target(0f).ease(Back.IN))
                .push(Tween.set(badgeArray.get(count), SpriteAccessor.COLOUR).target(color.r, color.g, color.b))
                .push(Tween.to(badgeArray.get(count), SpriteAccessor.SCALE, 0.2f).target(1f).ease(Back.OUT))
                .start(tweenManager);

        try{
            Timeline.createSequence()
                    .push(Tween.to(arrowArray.get(count+1), SpriteAccessor.ALPHA, 0.1f).target(0f))
                    .repeatYoyo(Tween.INFINITY, 0.5f)
                    .start(tweenManager);
        }
        catch (Exception ignored){
        }

        count++;
    }
}
