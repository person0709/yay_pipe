package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Colour;
import com.lazybean.yaypipe.gui.Icon;

public class ItemTray extends Table {
    private Snail snail;
    private Wand wand;

    private Icon snailIcon, wandIcon, wandStock;
    private Label wandStockLabel;

    private boolean isOpen = false;
    private boolean isClicked = false;

    private boolean isSnailClicked = false;
    private boolean isWandClicked = false;

    public ItemTray(AssetLoader assetLoader, Snail snail, Wand wand){
        this.wand = wand;
        this.snail = snail;

        this.setBackground(new TextureRegionDrawable(assetLoader.itemTray));
        this.setSize(Gdx.graphics.getWidth() * 0.25f, Gdx.graphics.getWidth() * 0.6f);
        this.setPosition(Gdx.graphics.getWidth() * 0.7f, Gdx.graphics.getHeight() * 0.87f);

        snailIcon = new Icon(assetLoader.circle, assetLoader.snail);
        snailIcon.setDiameter(Gdx.graphics.getWidth() * 0.15f);
        snailIcon.setColor(Colour.RED);
        snailIcon.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isSnailClicked = true;
            }
        });
        add(snailIcon).padBottom(Value.percentHeight(0.3f)).padTop(Value.percentHeight(0.4f)).row();

        Group wandGroup = new Group();
        wandGroup.setSize(Gdx.graphics.getWidth() * 0.15f, Gdx.graphics.getWidth() * 0.15f);

        wandIcon = new Icon(assetLoader.circle, assetLoader.wand);
        wandIcon.setDiameter(Gdx.graphics.getWidth() * 0.15f);
        wandIcon.setColor(Colour.RED);
        wandIcon.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isWandClicked = true;
            }
        });

        Label.LabelStyle labelStyle = new Label.LabelStyle(assetLoader.extraSmallFont_anja, Color.WHITE);
        wandStockLabel = new Label("x" + String.valueOf(AssetLoader.prefs.getInteger("wandStock")), labelStyle);
        wandStock = new Icon(assetLoader.circle, wandStockLabel);
        wandStock.setPosition(wandGroup.getRight(),0, Align.bottomRight);
        wandStock.setColor(Color.BLACK);

        wandGroup.addActor(wandIcon);
        wandGroup.addActor(wandStock);
        add(wandGroup);

        this.setTouchable(Touchable.enabled);
        this.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isClicked = true;
            }
        });
    }

    public boolean isClicked(){
        return isClicked;
    }

    public void setClicked(boolean clicked){
        isClicked = clicked;
    }

    public boolean isOpen(){
        return isOpen;
    }

    public void setOpen(boolean open){
        isOpen = open;
    }

    public boolean isSnailClicked() {
        return isSnailClicked;
    }

    public void setSnailClicked(boolean snailClicked) {
        isSnailClicked = snailClicked;
    }

    public void disableSnail(){
        snailIcon.setDisable();
    }

    public boolean isWandClicked() {
        return isWandClicked;
    }

    public void setWandClicked(boolean wandClicked) {
        isWandClicked = wandClicked;
    }

    public void disableWand(){
        wandIcon.setDisable();
        wandStock.remove();
    }

    public Snail getSnail() {
        return snail;
    }

    public Wand getWand() {
        return wand;
    }

    public void useWand(){
        wand.setStock(wand.getStock() - 1);
        wandStockLabel.setText("x" + String.valueOf(wand.getStock()));
        if (!wand.isAvailable()){
            disableWand();
        }
    }
}
