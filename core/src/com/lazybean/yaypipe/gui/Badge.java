package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gameobjects.GridBlock;

public class Badge extends Group {
    public static final Array<CustomColor> BADGE_COLORS = new Array<>(new CustomColor[]
            {CustomColor.PINK, CustomColor.ORANGE, CustomColor.GREEN, CustomColor.BLUE, CustomColor.PURPLE});

    private static final float DIAMETER = GridBlock.BLOCK_LENGTH * 0.4f;

    private TextureRegion background;
    private Label badgeLabel;

    public Badge(AssetLoader assetLoader, String string){
        this.background = assetLoader.circle;

        setBounds(0,0,DIAMETER, DIAMETER);
        setOrigin(getWidth()/2, getHeight()/2);

        badgeLabel = new Label(string, assetLoader.uiSkin, "badge");

        badgeLabel.setBounds(getX(), getY(), getWidth(), getHeight());
        badgeLabel.setAlignment(Align.center);
        addActor(badgeLabel);
    }

    public void setBadgeLabel(String newString){
        badgeLabel.setText(newString);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(background, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());

        batch.setColor(1,1,1, color.a * parentAlpha);
        super.draw(batch, parentAlpha);

        batch.setColor(Color.WHITE);
    }
}
