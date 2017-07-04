package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Colour;
import com.lazybean.yaypipe.gamehelper.GridSize;

public class ZoomUI extends Table {
    private Icon zoomIn, zoomOut;
    private boolean isZoomIn = false;
    private boolean isZoomOut = false;

    public ZoomUI(AssetLoader assetLoader){
        if (AssetLoader.prefs.getInteger("gridSize") < GridSize.LARGE){
            return;
        }
        align(Align.bottomRight);
        //setDebug(true);

        setWidth(Gdx.graphics.getWidth());
        zoomIn = new Icon(assetLoader.circle, assetLoader.zoomIn);
        Color color = new Color(Colour.BLUEGREY);
        zoomIn.setColor(color.r, color.g, color.b, 0.7f);
        zoomIn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isZoomIn = true;
            }
        });

        zoomOut = new Icon(assetLoader.circle, assetLoader.zoomOut);
        zoomOut.setColor(color.r, color.g, color.b, 0.7f);
        zoomOut.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isZoomOut = true;
            }
        });

        add(zoomIn).padRight(Value.percentWidth(0.1f));
        add(zoomOut).padRight(Gdx.graphics.getWidth()*0.09f);
    }

    public void setZoomIn(boolean bool){
        isZoomIn = bool;
    }

    public boolean isZoomIn(){
        return isZoomIn;
    }

    public void setZoomOut(boolean bool){
        isZoomOut = bool;
    }

    public boolean isZoomOut(){
        return isZoomOut;
    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        batch.enableBlending();
//        super.draw(batch, parentAlpha);
//        batch.disableBlending();
//    }
}
