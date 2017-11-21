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
import com.lazybean.yaypipe.gamehelper.IconType;
import com.lazybean.yaypipe.screens.GameSetupScreen;

public class DifficultySelector extends Table{
    private final int EASY = 0;
    private final int NORMAL = 1;
    private final int HARD = 2;
    private final int EXTREME = 3;
    private final int MASTER = 4;

    private Slider difficultySlider;
    private int difficultySelect;

    private Image difficultyLock;

    public DifficultySelector(final GameSetupScreen screen, AssetLoader assetLoader){
        final Array<String> difficultyList = new Array<>();
        difficultyList.add("Easy");
        difficultyList.add("Normal");
        difficultyList.add("Hard");
        difficultyList.add("Professional");
        difficultyList.add("Master");

        final Table lockTable = new Table();
        lockTable.setTouchable(Touchable.enabled);

        difficultyLock = new Image(assetLoader.getIconTexture(IconType.LOCK));
        difficultyLock.setColor(Color.BLACK);
        difficultyLock.setVisible(false);

        //settings for difficulty slider
        Label difficultyLabel = new Label("DIFFICULTY", assetLoader.uiSkin, "setupSubtitle");
        difficultyLabel.setAlignment(Align.center);

        final Label difficulty = new Label(difficultyList.get(0), assetLoader.uiSkin, "setupValue");
        difficulty.setAlignment(Align.center);

        Slider.SliderStyle difficultySliderStyle = new Slider.SliderStyle();
        difficultySliderStyle.background = new TextureRegionDrawable(assetLoader.slider);
        difficultySliderStyle.background.setMinHeight(50f);
        difficultySliderStyle.knob = new TextureRegionDrawable(assetLoader.circle).tint(CustomColor.RED.getColor());
        difficultySliderStyle.knob.setMinHeight(100f);
        difficultySliderStyle.knob.setMinWidth(100f);

        difficultySlider = new Slider(0f, 4f, 1f, false, difficultySliderStyle);

        difficultySlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                difficultySelect = (int) difficultySlider.getValue();
                difficulty.setText(difficultyList.get(difficultySelect));
                screen.gridSizeSelector.setRange(getDifficulty());

                if (GameData.getInstance().unlock.isUnlocked(getDifficulty())){
                    difficultyLock.setVisible(false);
                    difficultyLock.remove();
                    screen.next.setVisible(true);
                } else{
                    difficultyLock.setVisible(true);
                    lockTable.clearChildren();
                    lockTable.add(difficultyLock);
                    lockTable.add(difficulty);
                    screen.next.setVisible(false);
                }
            }
        });

        add(difficultyLabel).padBottom(30).expandX().row();

        add(difficultySlider).width(YayPipe.SCREEN_WIDTH * 0.8f).height(100).padBottom(30).row();

        lockTable.add(difficulty);

        add(lockTable).height(difficultyLock.getHeight());


        lockTable.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (difficultyLock.isVisible()) {
                    switch (difficultySelect) {
                        case NORMAL:
                            screen.addMessage("Score 3000 points \nto unlock");
                            break;

                        case HARD:
                            screen.addMessage("Score 5000 points \nto unlock");
                            break;

                        case EXTREME:
                            screen.addMessage("Score 10000 points \nto unlock");
                            break;

                        case MASTER:
                            screen.addMessage("Score 25000 points \nto unlock");
                            break;
                    }
                }
            }
        });
    }

    public Difficulty getDifficulty() {
        switch (difficultySelect){
            case EASY:
                return Difficulty.EASY;

            case NORMAL:
                return Difficulty.NORMAL;

            case HARD:
                return Difficulty.HARD;

            case EXTREME:
                return Difficulty.EXTREME;

            case MASTER:
                return Difficulty.MASTER;

            default:
                return null;
        }
    }

    public Image getLock(){
        return difficultyLock;
    }
}
