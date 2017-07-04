package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Colour;

public class GridSizeSelector {
    private Slider gridSizeSlider;

    public GridSizeSelector(AssetLoader assetLoader){
        Array<String> gridList = new Array<>();
        gridList.add("Tiny");
        gridList.add("Small");
        gridList.add("Regular");
        gridList.add("Large");
        gridList.add("Extra Large");

        Label.LabelStyle labelStyle = new Label.LabelStyle(assetLoader.mediumFont_anja, Color.BLACK);
        Label.LabelStyle valueStyle = new Label.LabelStyle(assetLoader.smallMediumFont_anja, Color.BLACK);

        //settings for grid size slider
        Label gridSizeLabel = new Label("GAMEBOARD SIZE", labelStyle);
        gridSizeLabel.setAlignment(Align.center);

        Label gridSize = new Label(gridList.get(0), valueStyle);
        gridSize.setAlignment(Align.center);

        Slider.SliderStyle gridSizeSliderStyle = new Slider.SliderStyle();
        gridSizeSliderStyle.background = new TextureRegionDrawable(assetLoader.slider);
        gridSizeSliderStyle.background.setMinHeight(50f);
        gridSizeSliderStyle.knob = new TextureRegionDrawable(assetLoader.circle).tint(Colour.TURQUOISE);
        gridSizeSliderStyle.knob.setMinHeight(100f);
        gridSizeSliderStyle.knob.setMinWidth(100f);

        gridSizeSlider = new Slider(0f, 4f, 1f, false, gridSizeSliderStyle);
    }

    public Slider getSlider() {
        return gridSizeSlider;
    }
}
