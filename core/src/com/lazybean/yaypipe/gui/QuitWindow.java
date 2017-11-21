package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gamehelper.FontType;

public class QuitWindow extends Dialog{
    public QuitWindow(AssetLoader assetLoader) {
        super("QUIT?", assetLoader.uiSkin);

        TextButton.TextButtonStyle yesStyle = new TextButton.TextButtonStyle();
        yesStyle.font = assetLoader.getFont(FontType.ANJA_MEDIUM);
        yesStyle.fontColor = CustomColor.TURQUOISE.getColor();
        yesStyle.pressedOffsetY = -10;

        TextButton.TextButtonStyle noStyle = new TextButton.TextButtonStyle(yesStyle);
        noStyle.fontColor = CustomColor.RED.getColor();

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

        setSize(YayPipe.SCREEN_WIDTH * 0.6f, YayPipe.SCREEN_WIDTH * 0.3f);
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
