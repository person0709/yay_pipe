package com.lazybean.yaypipe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.FontType;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;
import com.lazybean.yaypipe.gui.DifficultySelector;
import com.lazybean.yaypipe.gui.GridSizeSelector;
import com.lazybean.yaypipe.gui.ItemSelector;
import com.lazybean.yaypipe.gui.PageScrollPane;
import com.lazybean.yaypipe.gui.PurchaseWindow;
import com.lazybean.yaypipe.gui.SetupUpperBarUI;

import aurelienribon.tweenengine.TweenManager;

public class GameSetupScreen extends GameScreen{
    private AssetLoader assetLoader;
    private Table table;

    private PageScrollPane pageScrollPane;
    public DifficultySelector difficultySelector;
    public GridSizeSelector gridSizeSelector;
    public ItemSelector itemSelector;

    public TextButton next, back, start;

    public PurchaseWindow purchaseWindow;

    public GameSetupScreen(YayPipe passedGame){
        super(passedGame, YayPipe.BACKGROUND_COLOUR);

        assetLoader = game.assetLoader;

        table = new Table();
        table.align(Align.center);
        table.setFillParent(true);

        SetupUpperBarUI upperBarUI = new SetupUpperBarUI(assetLoader, game);
        stage.addActor(upperBarUI);

        Label setupLabel = new Label("SETUP", assetLoader.uiSkin, "setupTitle");

        table.add(setupLabel).padBottom(YayPipe.SCREEN_HEIGHT * 0.1f).row();

        difficultySelector = new DifficultySelector(this, assetLoader);

        gridSizeSelector = new GridSizeSelector(this, assetLoader);

        itemSelector = new ItemSelector(assetLoader,this);
        pageScrollPane = new PageScrollPane();

        pageScrollPane.addPage(difficultySelector);
        pageScrollPane.addPage(gridSizeSelector);
        pageScrollPane.addPage(itemSelector);

        Window.WindowStyle windowStyle = new Window.WindowStyle(assetLoader.getFont(FontType.NOTO_MEDIUM_LARGE), Color.BLACK, assetLoader.window);
        purchaseWindow = new PurchaseWindow(assetLoader, windowStyle){
            @Override
            protected void result(Object object) {
                if (object instanceof Integer){
                    if (GameData.getInstance().coinBank.getBalance() >= (int)object) {
                        GameData.getInstance().coinBank.addBalance(-(Integer) object);
                        GameData.getInstance().unlock.setUnlock(difficultySelector.getDifficulty(), gridSizeSelector.getGridSize(), true);
                        gridSizeSelector.getLock().setVisible(false);
                        gridSizeSelector.getLock().remove();
                        next.setVisible(true);
                }
                    else{
                        addMessage("Not enough coins");
                    }
                    hide();
                }
                else{
                    hide();
                }
            }
        };


        table.add(pageScrollPane).padBottom(YayPipe.SCREEN_HEIGHT * 0.04f).expandX().row();


        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = assetLoader.getFont(FontType.ANJA_MEDIUM);
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.up = assetLoader.button;
        textButtonStyle.pressedOffsetY = -10;
        textButtonStyle.pressedOffsetX = 10;

        Table navigateTable = new Table();
        next = new TextButton("NEXT", textButtonStyle);
        next.pad(Value.percentWidth(0.1f));
        next.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pageScrollPane.nextPage();
                if (pageScrollPane.getScrollX() == pageScrollPane.getMaxX()){
                    next.setVisible(false);
                    start.setVisible(true);
                }
                back.setVisible(true);
            }
        });

        back = new TextButton("BACK", textButtonStyle);
        back.pad(Value.percentWidth(0.1f));
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pageScrollPane.backPage();
                if (pageScrollPane.getScrollX() == 0){
                    back.setVisible(false);
                }
                next.setVisible(true);
                start.setVisible(false);
            }
        });
        back.setVisible(false);

        navigateTable.add(back).padRight(YayPipe.SCREEN_WIDTH * 0.5f);
        navigateTable.add(next);

        table.add(navigateTable).padBottom(YayPipe.SCREEN_HEIGHT * 0.1f).expandX().row();


        start = new TextButton("START!", textButtonStyle);
        start.pad(Value.percentWidth(0.1f));
        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GamePlayScreen(game, difficultySelector.getDifficulty(), gridSizeSelector.getGridSize()));
            }
        });

        stage.addActor(table);

        table.add(start);

    }

    @Override
    public void show() {
        // reset page scroll pane to the first page
        pageScrollPane.firstPage();
        back.setVisible(false);
        next.setVisible(true);
        start.setVisible(false);

        itemSelector.update();

        Gdx.input.setCatchBackKey(true);
        InputProcessor inputProcessor = new InputAdapter(){
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.BACK){
                    game.setScreen(new MainMenuScreen(game));
                }
                return true;
            }
        };

        InputMultiplexer multiplexer = new InputMultiplexer(stage, inputProcessor);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        tweenManager.update(delta);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
