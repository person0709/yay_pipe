package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gamehelper.Difficulty;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;
import com.lazybean.yaypipe.gamehelper.GridSize;
import com.lazybean.yaypipe.gamehelper.IconType;
import com.lazybean.yaypipe.screens.GameSetupScreen;

public class GridSizeSelector extends Table{
    private final int TINY = 0;
    private final int SMALL = 1;
    private final int REGULAR = 2;
    private final int LARGE = 3;
    private final int EXTRA_LARGE = 4;

    private Slider gridSizeSlider;
    private int gridSelect;

    private Image gridSizeLock;

    public GridSizeSelector(final GameSetupScreen screen, AssetLoader assetLoader){
        final Array<String> gridList = new Array<>();
        gridList.add("Tiny");
        gridList.add("Small");
        gridList.add("Regular");
        gridList.add("Large");
        gridList.add("Extra Large");

        final Table lockTable = new Table();
        lockTable.setTouchable(Touchable.enabled);

        gridSizeLock = new Image(assetLoader.getIconTexture(IconType.LOCK));
        gridSizeLock.setColor(Color.BLACK);
        gridSizeLock.setVisible(false);

        //settings for grid size slider
        Label gridSizeLabel = new Label("GAMEBOARD SIZE", assetLoader.uiSkin, "setupSubtitle");
        gridSizeLabel.setAlignment(Align.center);

        final Label gridSize = new Label(gridList.get(0), assetLoader.uiSkin, "setupValue");
        gridSize.setAlignment(Align.center);

        Slider.SliderStyle gridSizeSliderStyle = new Slider.SliderStyle();
        gridSizeSliderStyle.background = new TextureRegionDrawable(assetLoader.slider);
        gridSizeSliderStyle.background.setMinHeight(50f);
        gridSizeSliderStyle.knob = new TextureRegionDrawable(assetLoader.circle).tint(CustomColor.TURQUOISE.getColor());
        gridSizeSliderStyle.knob.setMinHeight(100f);
        gridSizeSliderStyle.knob.setMinWidth(100f);

        gridSizeSlider = new Slider(0f, 4f, 1f, false, gridSizeSliderStyle);

        gridSizeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gridSelect = (int) gridSizeSlider.getValue();
                gridSize.setText(gridList.get(gridSelect));

                if (GameData.getInstance().isUnlocked(screen.difficultySelector.getDifficulty(), getGridSize())){
                    gridSizeLock.setVisible(false);
                    gridSizeLock.remove();
                    screen.next.setVisible(true);
                } else{
                    gridSizeLock.setVisible(true);
                    lockTable.clearChildren();
                    lockTable.add(gridSizeLock);
                    lockTable.add(gridSize);
                    screen.next.setVisible(false);
                }
            }
        });

        add(gridSizeLabel).padBottom(30).expandX().row();

        add(gridSizeSlider).width(YayPipe.SCREEN_WIDTH * 0.8f).height(100).padBottom(30).row();

        lockTable.add(gridSize);

        add(lockTable).height(gridSizeLock.getHeight());

        lockTable.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (gridSizeLock.isVisible()) {
                    int price = (screen.difficultySelector.getDifficulty().difficultyLevel - 1) * 1000 +
                            (getGridSize().gridSizeLevel) * 500;
                    screen.promptWindow.getTitleLabel().setText("Unlock for " + price + " coins?");
                    screen.promptWindow.setObject(screen.promptWindow.yes, price);
                    screen.promptWindow.show(screen.stage);
                }
            }
        });
    }

    public void setRange(Difficulty difficulty){
        switch (difficulty){
            case EASY:
                gridSizeSlider.setRange(TINY, EXTRA_LARGE);
                gridSizeSlider.setValue(TINY);
                break;

            case NORMAL:
                gridSizeSlider.setRange(SMALL, EXTRA_LARGE);
                gridSizeSlider.setValue(SMALL);
                break;

            case HARD:
                gridSizeSlider.setRange(REGULAR, EXTRA_LARGE);
                gridSizeSlider.setValue(REGULAR);
                break;

            case EXTREME:
                gridSizeSlider.setRange(REGULAR, EXTRA_LARGE);
                gridSizeSlider.setValue(REGULAR);
                break;

            case MASTER:
                gridSizeSlider.setRange(LARGE, EXTRA_LARGE);
                gridSizeSlider.setValue(LARGE);
        }
    }

    public GridSize getGridSize(){
        switch(gridSelect){
            case 0:
                return GridSize.TINY;

            case 1:
                return GridSize.SMALL;

            case 2:
                return GridSize.REGULAR;

            case 3:
                return GridSize.LARGE;

            case 4:
                return GridSize.EXTRA_LARGE;
        }
        return null;
    }

    public Image getLock(){
        return gridSizeLock;
    }
}
