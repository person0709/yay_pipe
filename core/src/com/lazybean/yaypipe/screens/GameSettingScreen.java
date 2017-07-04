package com.lazybean.yaypipe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Colour;
import com.lazybean.yaypipe.gamehelper.Difficulty;
import com.lazybean.yaypipe.gamehelper.Unlock;
import com.lazybean.yaypipe.gui.Background;
import com.lazybean.yaypipe.gui.Icon;
import com.lazybean.yaypipe.gameobjects.Snail;
import com.lazybean.yaypipe.gameobjects.Wand;
import com.lazybean.yaypipe.gamehelper.GridSize;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.YayPipe;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quart;

public class GameSettingScreen implements Screen{
    private YayPipe game;
    private AssetLoader assetLoader;
    private Stage stage;

    private int gridSelect = 0, difficultySelect = 0;
    private Image gridSizeLock;
    private Dialog purchaseDialog;
    private TextButton yes;

    private Label message;
    private LabelStyle messageStyle;

    private boolean isSnailPurchased = false;
    private int wandStock = 0;

    private TweenManager tweenManager;
    private Background fadeInOut;

    public GameSettingScreen(YayPipe game, AssetLoader assetLoader){
        Gdx.input.setCatchBackKey(true);

        this.game = game;
        this.assetLoader = assetLoader;

        messageStyle = new LabelStyle(assetLoader.messageFont, Color.WHITE);

        isSnailPurchased = AssetLoader.prefs.getBoolean("snail");
        wandStock = AssetLoader.prefs.getInteger("wandStock");

        this.tweenManager = new TweenManager();

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        final Background background = new Background();
        background.setColor(Colour.YELLOW);

        this.fadeInOut = new Background();
        this.fadeInOut.setColor(Color.BLACK);

        stage.addActor(background);

        Tween.to(this.fadeInOut, SpriteAccessor.ALPHA, 0.5f).target(0f).ease(Quart.OUT)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        GameSettingScreen.this.fadeInOut.remove();
                    }
                })
                .start(tweenManager);

        Window.WindowStyle windowStyle = new Window.WindowStyle(assetLoader.mediumFont_noto, Color.BLACK, new NinePatchDrawable(assetLoader.window));
        windowStyle.stageBackground = new TextureRegionDrawable(new TextureRegion(assetLoader.background)).tint(new Color(0,0,0,0.5f));
        purchaseDialog = new Dialog("", windowStyle){
            @Override
            protected void result(Object object) {
                if (object instanceof Integer){
                    if (AssetLoader.coinBank.getBalance() >= (Integer) object) {
                        AssetLoader.coinBank.addBalance(-(Integer) object);
                        Unlock.setUnlock(difficultySelect, gridSelect, true);
                        gridSizeLock.setVisible(false);
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

        purchaseDialog.setSize(Gdx.graphics.getWidth() * 0.6f, Gdx.graphics.getWidth() * 0.3f);

        TextButton.TextButtonStyle yesStyle = new TextButton.TextButtonStyle();
        yesStyle.font = assetLoader.mediumFont_anja;
        yesStyle.fontColor = Colour.TURQUOISE;
        yesStyle.pressedOffsetY = -10;

        TextButton.TextButtonStyle noStyle = new TextButton.TextButtonStyle(yesStyle);
        noStyle.fontColor = Colour.RED;

        yes = new TextButton("YAY", yesStyle);
        purchaseDialog.setObject(yes, 0);
        TextButton no = new TextButton("NAY", noStyle);
        purchaseDialog.setObject(no, false);
        purchaseDialog.key(Input.Keys.BACK, false);

        purchaseDialog.setMovable(false);
        purchaseDialog.setResizable(false);
        purchaseDialog.getTitleLabel().setAlignment(Align.center);
        purchaseDialog.padTop(Value.percentHeight(0.5f));

        Table buttonTable = purchaseDialog.getButtonTable();

        buttonTable.add(no).padRight(Value.percentWidth(0.5f)).padTop(Value.percentHeight(0.2f));
        buttonTable.add(yes).padTop(Value.percentHeight(0.2f));

        gridSizeLock = new Image(assetLoader.lock);
        gridSizeLock.setScaling(Scaling.fillY);
        gridSizeLock.setAlign(Align.left);
        gridSizeLock.setColor(Color.BLACK);
        gridSizeLock.setHeight(gridSize.getHeight() * 1.3f);
        gridSizeLock.setWidth(Gdx.graphics.getWidth() / 2);
        gridSizeLock.setVisible(false);
    }

    @Override
    public void show() {
        Table table = new Table();
        table.align(Align.center);
        table.setFillParent(true);
//        table.setDebug(true);


        gridSizeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gridSelect = (int) gridSizeSlider.getValue();
                gridSize.setText(gridList.get(gridSelect));
                if (Unlock.isUnlocked(difficultySelect, gridSelect)){
                    gridSizeLock.setVisible(false);
                } else {
                    gridSizeLock.setVisible(true);
                }
            }
        });


        LabelStyle labelStyle = new LabelStyle(assetLoader.mediumFont_anja, Color.BLACK);

        Label itemLabel = new Label("ITEMS", labelStyle);

        Table itemTable = new Table();

        Table snailTable = new Table();
        itemTable.add(snailTable);

        final Icon snailIcon = new Icon(assetLoader.circle, assetLoader.snail);
        snailIcon.setDiameter(Gdx.graphics.getHeight()*0.08f);
        snailIcon.setColor(Colour.RED);
        if (!isSnailPurchased){
            snailIcon.setDisable();
        }
        snailIcon.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                assetLoader.click.play();
                if (!isSnailPurchased && AssetLoader.coinBank.getBalance() >= Snail.PRICE){
                    snailIcon.setAble();
                    AssetLoader.coinBank.addBalance(-Snail.PRICE);
                    isSnailPurchased = true;
                }
                else if (isSnailPurchased){
                    snailIcon.setDisable();
                    AssetLoader.coinBank.addBalance(Snail.PRICE);
                    isSnailPurchased = false;
                }
                else{
                    addMessage("Not enough coins");
                }
            }
        });

        Image coin = new Image(assetLoader.coin);
        coin.setScaling(Scaling.fillX);
        LabelStyle itemLabelStyle = new LabelStyle(assetLoader.smallFont_anja, Color.BLACK);
        Label snailLabel = new Label(String.valueOf(Snail.PRICE), itemLabelStyle);

        snailTable.add(snailIcon).colspan(2).row();
        snailTable.add(coin).width(snailLabel.getHeight()).padRight(10);
        snailTable.add(snailLabel);


        Table wandTable = new Table();
        itemTable.add(wandTable).padLeft(Gdx.graphics.getWidth() * 0.2f);

        final Group wandIconGroup = new Group();
        wandIconGroup.setSize(Gdx.graphics.getHeight()*0.08f, Gdx.graphics.getHeight()*0.08f);

        Label.LabelStyle wandIconStyle = new Label.LabelStyle(assetLoader.extraSmallFont_anja, Color.WHITE);
        final Label stockBadgeLabel = new Label("x" + String.valueOf(wandStock), wandIconStyle);
        final Icon wandStockBadge = new Icon(assetLoader.circle, stockBadgeLabel);
        wandStockBadge.setPosition(wandIconGroup.getRight(),0, Align.bottomRight);
        wandStockBadge.setColor(Color.BLACK);

        final Icon wandIcon = new Icon(assetLoader.circle, assetLoader.wand);
        wandIcon.setDiameter(Gdx.graphics.getHeight()*0.08f);
        wandIcon.setColor(Colour.RED);
        if (wandStock == 0){
            wandIcon.setDisable();
        }
        wandIcon.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (AssetLoader.coinBank.getBalance() >= Wand.PRICE && wandStock < 5){
                    wandIcon.setAble();
                    wandStockBadge.setAble();
                    wandStock++;
                    AssetLoader.coinBank.addBalance(-Wand.PRICE);

                    stockBadgeLabel.setText("x" + String.valueOf(wandStock));
                    wandIconGroup.addActor(wandStockBadge);
                }
                else if (wandStock >= 5){
                    addMessage("Don't be greedy!\nYou can only have 5!");
                }
                else{
                    addMessage("Not enough coins");
                }
            }
        });

        wandIconGroup.addActor(wandIcon);
        if (wandStock > 0) {
            wandIconGroup.addActor(wandStockBadge);
        }

        Image coin2 = new Image(assetLoader.coin);
        coin2.setScaling(Scaling.fillX);
        Label wandLabel = new Label(String.valueOf(Wand.PRICE), itemLabelStyle);

        wandTable.add(wandIconGroup).colspan(2).row();
        wandTable.add(coin2).width(wandLabel.getHeight()).padRight(10);
        wandTable.add(wandLabel);


        TextButtonStyle startStyle = new TextButtonStyle();
        startStyle.font = assetLoader.mediumFont_anja;
        startStyle.fontColor = Color.BLACK;
        startStyle.pressedOffsetY = -10;

        TextButton start = new TextButton("START!", startStyle);
        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                assetLoader.click.play(AssetLoader.prefs.getFloat("soundVolume"));
                if (!difficultyLock.isVisible() && !gridSizeLock.isVisible()) {
                    AssetLoader.prefs.putInteger("gridSize", gridSelect);
                    AssetLoader.prefs.putInteger("difficulty", difficultySelect);

                    //advance tutorial flag
                    if (difficultySelect >= Difficulty.NORMAL && AssetLoader.prefs.getBoolean("adTutFlag")){
                        AssetLoader.prefs.putInteger("gridSize", GridSize.TUTORIAL);
                        AssetLoader.prefs.putInteger("difficulty", Difficulty.TUTORIAL_ADVANCED);
                    }
                    AssetLoader.prefs.flush();

                    stage.addActor(fadeInOut);
                    Timeline.createSequence()
                            .delay(0.1f)
                            .push(Tween.to(fadeInOut, SpriteAccessor.ALPHA, 0.5f).target(1f).ease(Quart.OUT))
                            .setCallback(new TweenCallback() {
                                @Override
                                public void onEvent(int type, BaseTween<?> source) {
                                    dispose();
                                    game.setScreen(new GamePlayScreen(game, assetLoader));
                                }
                            })
                            .start(tweenManager);
                }
                else {
                    addMessage("No, can't do!");
                }
            }
        });

        table.add(difficultyLabel).expandX().row();

        table.add(difficultySlider).width(Gdx.graphics.getWidth() * 0.8f).height(100).row();

        table.add(difficulty).padBottom(100).row();

        table.add(gridSizeLabel).expandX().row();

        table.add(gridSizeSlider).width(Gdx.graphics.getWidth() * 0.8f).height(100).row();

        table.add(gridSize).padBottom(150).row();

        if (AssetLoader.prefs.getBoolean("itemUnlocked")) {
            table.add(itemLabel).padBottom(50).row();
            table.add(itemTable);
        }

        stage.addActor(table);

        table.validate();


        difficultyLock.setPosition(Gdx.graphics.getWidth() * 0.22f,
                difficulty.localToStageCoordinates(new Vector2(0,difficulty.getHeight()/2)).y, Align.left);
        table.addActor(difficultyLock);

        difficultyLock.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                switch (difficultySelect){
                    case Difficulty.NORMAL:
                        addMessage("Score 3000 points \nto unlock");
                        break;

                    case Difficulty.HARD:
                        addMessage("Score 5000 points \nto unlock");
                        break;

                    case Difficulty.EXTREME:
                        addMessage("Score 10000 points \nto unlock");
                        break;

                    case Difficulty.MASTER:
                        addMessage("Score 25000 points \nto unlock");
                        break;
                }
            }
        });


        gridSizeLock.setPosition(Gdx.graphics.getWidth() * 0.22f,
                gridSize.localToStageCoordinates(new Vector2(0,gridSize.getHeight()/2)).y, Align.left);
        table.addActor(gridSizeLock);

        gridSizeLock.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (Unlock.isUnlocked(difficultySelect)) {
                    int price = (difficultySelect) * (gridSelect - 1) * 1000 + (difficultySelect + 1) * 500;
                    purchaseDialog.getTitleLabel().setText("Unlock for " + price + " coins?");
                    purchaseDialog.setObject(yes, price);
                    purchaseDialog.show(stage);
                } else{
                    addMessage("Unlock the difficulty first");
                }
            }
        });


        start.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight() * 0.15f, Align.center);
        stage.addActor(start);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        tweenManager.update(delta);
        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.BACK) || upperBarUI.isBack()){
            stage.addActor(fadeInOut);
            Timeline.createSequence()
                    .delay(0.1f)
                    .push(Tween.to(fadeInOut, SpriteAccessor.ALPHA, 0.5f).target(1f).ease(Quart.OUT))
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            dispose();
                            game.setScreen(new MainMenuScreen(game, assetLoader));
                        }
                    })
                    .start(tweenManager);
        }
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

    @Override
    public void dispose() {
        if (isSnailPurchased){
            AssetLoader.prefs.putBoolean("snail", true);
        } else{
            AssetLoader.prefs.putBoolean("snail", false);
        }
        AssetLoader.prefs.putInteger("wandStock", wandStock);
        AssetLoader.prefs.flush();
        AssetLoader.coinBank.saveData();
//        stage.dispose();
    }


    private void addMessage(String string){
        if (message == null) {
            message = new Label(string, messageStyle);
        }
        else {
            message.remove();
            message.setText(string);
            tweenManager.update(3f);
        }
//        label.setColor(Color.WHITE);
        message.setAlignment(Align.center);
        message.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, Align.center);
        message.setTouchable(Touchable.disabled);
        Timeline.createParallel()
                .beginSequence()
                .push(Tween.set(message, SpriteAccessor.ALPHA).target(0f).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        stage.addActor(message);
                    }
                }))
                .push(Tween.to(message, SpriteAccessor.ALPHA,0.25f).target(1f))
                .pushPause(1.5f)
                .push(Tween.to(message, SpriteAccessor.ALPHA,0.25f).target(0f))
                .end()
                .push(Tween.to(message, SpriteAccessor.POSITION, 2f).targetRelative(0, message.getHeight() / 2).ease(Linear.INOUT))
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        message.remove();
                    }
                })
                .start(tweenManager);
    }
}
