package com.lazybean.yaypipe.guiobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.Gui;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.FontType;

public class TapToStart extends Table{
    private Background background;

    public TapToStart(AssetLoader assetLoader, final GameWorld gameWorld, final Gui gui){
        this.setTouchable(Touchable.enabled);
        this.background = new Background();
        background.setColor(1,1,1,0.5f);
        addActor(background);

        setFillParent(true);
        //setDebug(true);
        Image hand = new Image(assetLoader.tapToStart);
        hand.setScaling(Scaling.fillY);

        Label.LabelStyle labelStyle = new Label.LabelStyle(assetLoader.getFont(FontType.ANJA_MEDIUM), Color.BLACK);
        Label label = new Label("TAP TO START!", labelStyle);

        add(hand).height(YayPipe.SCREEN_HEIGHT * 0.2f);
        row();

        add(label);


        this.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                addAction(Actions.sequence(Actions.fadeOut(0.1f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        remove();
                        gameWorld.start();
                        gui.start();
                    }
                })));
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        batch.setColor(Color.WHITE);
    }
}
