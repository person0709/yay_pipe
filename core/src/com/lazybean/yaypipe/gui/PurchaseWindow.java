package com.lazybean.yaypipe.gui;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gamehelper.FontType;

public class PurchaseWindow extends Dialog {
    public TextButton yes;

    public PurchaseWindow(AssetLoader assetLoader, WindowStyle windowStyle) {
        super("", windowStyle);

        setSize(YayPipe.SCREEN_WIDTH * 0.6f, YayPipe.SCREEN_WIDTH * 0.3f);

        TextButton.TextButtonStyle yesStyle = new TextButton.TextButtonStyle();
        yesStyle.font = assetLoader.getFont(FontType.ANJA_MEDIUM);
        yesStyle.fontColor = CustomColor.TURQUOISE.getColor();
        yesStyle.pressedOffsetY = -10;

        TextButton.TextButtonStyle noStyle = new TextButton.TextButtonStyle(yesStyle);
        noStyle.fontColor = CustomColor.RED.getColor();

        yes = new TextButton("YAY", yesStyle);
        setObject(yes, 0);
        TextButton no = new TextButton("NAY", noStyle);
        setObject(no, false);
        key(Input.Keys.BACK, false);

        setMovable(false);
        setResizable(false);
        getTitleLabel().setAlignment(Align.center);
        padTop(Value.percentHeight(0.5f));

        Table buttonTable = getButtonTable();

        buttonTable.add(no).padRight(Value.percentWidth(0.5f)).padTop(Value.percentHeight(0.2f));
        buttonTable.add(yes).padTop(Value.percentHeight(0.2f));
    }
}