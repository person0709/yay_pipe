package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Colour;

public class QuitWindow extends GameWindow{
    public QuitWindow(AssetLoader assetLoader) {
        super("QUIT?", new WindowStyle(assetLoader.mediumLargeFont_anja, Color.BLACK, assetLoader.uiSkin.getDrawable("window")));
        getStyle().stageBackground = new TextureRegionDrawable(assetLoader.uiSkin.getRegion("white")).tint(new Color(0,0,0,0.5f));

        TextButton.TextButtonStyle yesStyle = new TextButton.TextButtonStyle();
        yesStyle.font = assetLoader.mediumFont_anja;
        yesStyle.fontColor = Colour.TURQUOISE;
        yesStyle.pressedOffsetY = -10;

        TextButton.TextButtonStyle noStyle = new TextButton.TextButtonStyle(yesStyle);
        noStyle.fontColor = Colour.RED;

        TextButton yes = new TextButton("YAY", yesStyle);
        setObject(yes, true);
        TextButton no = new TextButton("NAY", noStyle);
        setObject(no, false);
        key(Input.Keys.BACK, false);

        setMovable(false);
        setResizable(false);
        getTitleLabel().setAlignment(Align.center);
        padTop(Value.percentHeight(0.5f));

        getButtonTable().add(no).padRight(Value.percentWidth(0.5f));
        getButtonTable().add(yes);

        setSize(Gdx.graphics.getWidth() * 0.6f, Gdx.graphics.getWidth() * 0.3f);
    }

    @Override
    protected void result(Object object) {
        if (object.equals(true)){
            Gdx.app.exit();
        }
        else{
            hide();
        }
    }
}
