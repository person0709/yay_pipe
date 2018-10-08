package com.lazybean.yaypipe.guiobjects;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.CustomColor;

public abstract class UpperBarUI extends Table {
    protected AssetLoader assetLoader;
    protected Skin skin;
    private TextureRegion shadow;

    protected boolean isBack = false;

    public UpperBarUI(AssetLoader assetLoader){
        //setDebug(true);
        this.assetLoader = assetLoader;
        this.skin = assetLoader.uiSkin;

        setBounds(0, YayPipe.SCREEN_HEIGHT - YayPipe.SCREEN_HEIGHT * 0.09f,
                YayPipe.SCREEN_WIDTH, YayPipe.SCREEN_HEIGHT * 0.09f);

        Background background = new Background();
        background.setColor(CustomColor.YELLOW.getColor());
        background.setHeight(getHeight());
        addActor(background);

        shadow = assetLoader.shadow;
    }


    public void setBack(boolean back) {
        isBack = back;
    }

    public boolean isBack() {
        return isBack;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(shadow, getX(), getY() - shadow.getRegionHeight(), YayPipe.SCREEN_WIDTH, shadow.getRegionHeight());
    }
}
