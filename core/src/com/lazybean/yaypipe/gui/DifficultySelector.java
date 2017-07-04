package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Colour;
import com.lazybean.yaypipe.gamehelper.Difficulty;
import com.lazybean.yaypipe.gamehelper.GridSize;
import com.lazybean.yaypipe.gamehelper.Unlock;

public class DifficultySelector {
    private final int EASY = 0;
    private final int NORMAL = 1;
    private final int HARD = 2;
    private final int EXTREME = 3;
    private final int MASTER = 4;

    private Slider difficultySlider;
    private int difficultySelect;

    private Image difficultyLock;

    public DifficultySelector(AssetLoader assetLoader){
        final Array<String> difficultyList = new Array<>();
        difficultyList.add("Easy");
        difficultyList.add("Normal");
        difficultyList.add("Hard");
        difficultyList.add("Professional");
        difficultyList.add("Master");

        Label.LabelStyle labelStyle = new Label.LabelStyle(assetLoader.mediumFont_anja, Color.BLACK);
        Label.LabelStyle valueStyle = new Label.LabelStyle(assetLoader.smallMediumFont_anja, Color.BLACK);

        //settings for difficulty slider
        Label difficultyLabel = new Label("DIFFICULTY", labelStyle);
        difficultyLabel.setAlignment(Align.center);

        final Label difficulty = new Label(difficultyList.get(0), valueStyle);
        difficulty.setAlignment(Align.center);

        difficultyLock = new Image(assetLoader.lock);
        difficultyLock.setScaling(Scaling.fillY);
        difficultyLock.setAlign(Align.left);
        difficultyLock.setColor(Color.BLACK);
        difficultyLock.setHeight(difficulty.getHeight() * 1.3f);
        difficultyLock.setWidth(Gdx.graphics.getWidth() / 2);
        difficultyLock.setVisible(false);

        Slider.SliderStyle difficultySliderStyle = new Slider.SliderStyle();
        difficultySliderStyle.background = new TextureRegionDrawable(assetLoader.slider);
        difficultySliderStyle.background.setMinHeight(50f);
        difficultySliderStyle.knob = new TextureRegionDrawable(assetLoader.circle).tint(Colour.RED);
        difficultySliderStyle.knob.setMinHeight(100f);
        difficultySliderStyle.knob.setMinWidth(100f);

        difficultySlider = new Slider(0f, 4f, 1f, false, difficultySliderStyle);

        difficultySlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                difficultySelect = (int) difficultySlider.getValue();
                if (difficultySelect == EASY){
                    gridSizeSlider.setRange(GridSize.TINY, GridSize.EXTRA_LARGE);
                }
                else if (difficultySelect == NORMAL){
                    gridSizeSlider.setRange(GridSize.SMALL, GridSize.EXTRA_LARGE);
                }
                else if (difficultySelect == HARD || difficultySelect == EXTREME){
                    gridSizeSlider.setRange(GridSize.REGULAR, GridSize.EXTRA_LARGE);
                }
                else{
                    gridSizeSlider.setRange(GridSize.LARGE, GridSize.EXTRA_LARGE);
                }
                difficulty.setText(difficultyList.get(difficultySelect));
                gridSize.setText(gridList.get((int) gridSizeSlider.getValue()));

                if (Unlock.isUnlocked(difficultySelect)){
                    difficultyLock.setVisible(false);
                } else {
                    difficultyLock.setVisible(true);
                }

                if (Unlock.isUnlocked(difficultySelect, gridSelect)){
                    gridSizeLock.setVisible(false);
                } else {
                    gridSizeLock.setVisible(true);
                }
            }
        });
    }
}
