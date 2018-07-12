package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gamehelper.GridSize;
import com.lazybean.yaypipe.gamehelper.IconType;

public class ZoomUI extends Table {
    private GameWorld gameWorld;
    private Icon zoomInIcon, zoomOutIcon;

    public ZoomUI(AssetLoader assetLoader, GameWorld gameWorld){
        this.gameWorld = gameWorld;

        align(Align.bottomRight);
        //setDebug(true);

        setWidth(YayPipe.SCREEN_WIDTH);
        zoomInIcon = new Icon(assetLoader, IconType.ZOOM_IN, Icon.ITEM_DIAMETER);
        Color color = new Color(CustomColor.BLUEGREY.getColor());
        zoomInIcon.setColor(color.r, color.g, color.b, 0.7f);

        zoomOutIcon = new Icon(assetLoader, IconType.ZOOM_OUT, Icon.ITEM_DIAMETER);
        zoomOutIcon.setColor(color.r, color.g, color.b, 0.7f);

        add(zoomInIcon).padRight(Value.percentWidth(0.1f)).padBottom(Value.percentHeight(0.1f));
        add(zoomOutIcon).padRight(Value.percentWidth(0.1f)).padBottom(Value.percentHeight(0.1f));
    }

    @Override
    public void act(float delta) {
        if (zoomInIcon.isTouched()){
            zoomInIcon.setTouched(false);
            if (((OrthographicCamera) gameWorld.getGameWorldStage().getCamera()).zoom >= 0.7f) {
                ((OrthographicCamera) gameWorld.getGameWorldStage().getCamera()).zoom -= 0.15f;
            }
        }

        if (zoomOutIcon.isTouched()){
            zoomOutIcon.setTouched(false);
            gameWorld.returnToDefaultView();
        }
    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        batch.enableBlending();
//        super.draw(batch, parentAlpha);
//        batch.disableBlending();
//    }
}
