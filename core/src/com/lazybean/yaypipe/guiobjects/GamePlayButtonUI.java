package com.lazybean.yaypipe.guiobjects;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gamehelper.IconType;

public class GamePlayButtonUI extends Table{
    private AssetLoader assetLoader;
    private GameWorld gameWorld;

    public TextButton undo;

    public GamePlayButtonUI(AssetLoader assetLoader, GameWorld gameWorld){
        this.assetLoader = assetLoader;
        this.gameWorld = gameWorld;

        setBounds(YayPipe.SCREEN_WIDTH * 0.2f, 0, YayPipe.SCREEN_WIDTH * 0.8f, YayPipe.SCREEN_HEIGHT * 0.11f);

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = assetLoader.buttonUp;
        style.down = assetLoader.buttonDown;
        style.disabled = assetLoader.buttonDisabled;

        Button fastForward = new Button(style);
        Image icon = new Image(assetLoader.getIconTexture(IconType.FAST_FORWARD));
        icon.setScaling(Scaling.fillX);

        fastForward.add(icon).width(getWidth() * 0.2f);

        fastForward.setColor(CustomColor.RED.getColor());
        fastForward.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameWorld.getGrid().getWater().skip();
                gameWorld.getGrid().setTouchable(Touchable.disabled);
                fastForward.setDisabled(true);
                undo.setDisabled(true);
            }
        });
        add(fastForward).height(getHeight() * 0.85f).width(getWidth() * 0.2f).padRight(Value.percentWidth(0.02f, this)).padLeft(Value.percentWidth(0.05f, this));

        undo = new TextButton("UNDO", assetLoader.uiSkin, "mainMenu");
        undo.setDisabled(true);
        undo.pad(Value.percentHeight(0.3f, this), Value.percentWidth(0.7f, this),
                Value.percentHeight(0.3f, this), Value.percentWidth(0.7f, this));
        add(undo).padRight(getWidth() * 0.05f).height(getHeight() * 0.85f).width(getWidth() * 0.7f);

        undo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameWorld.undo();
                undo.setDisabled(true);
            }
        });
    }
}
