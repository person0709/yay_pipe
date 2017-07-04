package com.lazybean.yaypipe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Difficulty;
import com.lazybean.yaypipe.gamehelper.GridSize;
import com.lazybean.yaypipe.gamehelper.PipeHashMap;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.gamehelper.Unlock;
import com.lazybean.yaypipe.gui.BadgeIndicator;
import com.lazybean.yaypipe.gui.GameOverWindow;
import com.lazybean.yaypipe.gui.GamePausedWindow;
import com.lazybean.yaypipe.gui.Icon;
import com.lazybean.yaypipe.gameobjects.MainGrid;
import com.lazybean.yaypipe.gameobjects.Block;
import com.lazybean.yaypipe.gameobjects.NextBlock;
import com.lazybean.yaypipe.gameobjects.Score;
import com.lazybean.yaypipe.gameobjects.Snail;
import com.lazybean.yaypipe.gui.UpperBarUI;
import com.lazybean.yaypipe.gameobjects.Wand;
import com.lazybean.yaypipe.gameobjects.Water;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Circ;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Quad;
import aurelienribon.tweenengine.equations.Quart;

public class GameWorld {
    public enum GameState {Ready, Running, Paused, GameOver, Clear, Restart, Quit}

    public GameState state = GameState.Ready;
    public Difficulty difficulty;
    public GridSize gridSize;

    private PipeHashMap pipeHashMap;

    private AssetLoader assetLoader;

    public static Score score;
    public static boolean isBlockChanged = false;

    private MainGrid grid;

    private GamePausedWindow gamePausedWindow;
    private GameOverWindow gameOverWindow;

    private LabelStyle effectStyle;

    private Stage gameWorldStage;

    private Sound snailSound = Gdx.audio.newSound(Gdx.files.internal("snail.ogg"));
    private Sound wandSound = Gdx.audio.newSound(Gdx.files.internal("wand.ogg"));
    private Sound gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameOver.ogg"));
    private Sound gameClearSound = Gdx.audio.newSound(Gdx.files.internal("gameClear.ogg"));

    private TweenManager tweenManager;
    public GameWorld(Stage gameWorldStage, AssetLoader assetLoader, GridSize gridSize, Difficulty difficulty) {
        this.gameWorldStage = gameWorldStage;
        this.assetLoader = assetLoader;
        this.gridSize = gridSize;
        this.difficulty = difficulty;

        this.tweenManager = new TweenManager();

        pipeHashMap = new PipeHashMap(assetLoader);

        score = new Score();

        mainGridInit();

        ((OrthographicCamera) gameWorldStage.getCamera()).zoom = grid.getWidth() / grid.getRefWidth();
        ((OrthographicCamera) gameWorldStage.getCamera()).position.set(grid.getOriginX() + grid.getX(),grid.getOriginY() + grid.getY(),0);

//        if (difficulty == Difficulty.TUTORIAL_BASIC || difficulty == Difficulty.TUTORIAL_ADVANCED){
//            tutorialInit();
//        }

        effectStyle = new LabelStyle(assetLoader.smallFont_anja, Color.WHITE);
    }

    private void mainGridInit() {
        grid = new MainGrid(this, assetLoader, gridSize, difficulty);

        grid.setTouchable(Touchable.disabled);
        gameWorldStage.addActor(grid);
        grid.addGesture();
    }



//    private void tutorialInit(){
//        Window.WindowStyle windowStyle = new Window.WindowStyle(assetLoader.mediumFont_noto, Color.BLACK, new NinePatchDrawable(assetLoader.window));
//        windowStyle.stageBackground = new TextureRegionDrawable(new TextureRegion(assetLoader.background)).tint(new Color(0,0,0,0.5f));
//        tutorial = new Tutorial("Tutorial", windowStyle, this, assetLoader);
//        tutorial.init(difficulty);
//        tutorialDialog.debug();
//    }

    private void gameOverUIInit(boolean isClear) {
        gameOverWindow = new GameOverWindow(assetLoader, isClear, tweenManager);
        gameOverWindow.window.setScale(0f);
        gameOverWindow.setVisible(false);
    }

    //first time access flag
    private int flag = 0;
    public void update(float delta) {
        tweenManager.update(delta);

        switch (state) {
            case Ready:
                updateReady(delta);
                break;

            case Running:
                updateRunning(delta);
                break;

            case Paused:
                updatePaused(delta);
                break;

            case GameOver:
                if (flag == 0) {
                    gameOverSound.play(AssetLoader.prefs.getFloat("soundVolume"));
                    AssetLoader.stats.stopTimer();

                    gameOverUIInit(false);
                    grid.getWater().setClear(true);
                    flag = 1;
                }
                updateGameOver(delta);
                break;

            case Clear:
                if (flag == 0){
                    gameClearSound.play(AssetLoader.prefs.getFloat("soundVolume"));
                    AssetLoader.stats.stopTimer();

                    score.applyMultiplier((float) (1.0 + 0.1 * AssetLoader.stats.getCrossPipeUse()));
                    gameOverUIInit(true);
                    grid.getWater().setClear(true);
                    flag = 1;
                }
                updateGameOver(delta);
                break;
        }

    private void updateReady(float delta) {
        if (tts.isTouched()){
            Tween.to(tts, SpriteAccessor.ALPHA, 0.1f).target(0f)
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            tts.setVisible(false);
                            tts.remove();
                        }
                    })
            .start(tweenManager);
            tts.setTouched(false);
        }

        if (!tts.isVisible()) {
            //start timer for stat recording
            AssetLoader.stats.reset();
            AssetLoader.stats.startTimer();

            Timeline.createParallel()
                    .push(Tween.to(nextBlockQueue, SpriteAccessor.ALPHA, 0.3f).target(1f))
                    .push(Tween.from(nextBlockQueue, SpriteAccessor.POSITION, 0.3f).targetRelative(nextPipeUI.getRight(), 0)
                            .ease(Quad.OUT))
                    .push(Tween.to(grid.getStartBlock().getPipe(), SpriteAccessor.SCALE, 0.5f).target(1f).ease(Elastic.OUT))
                    .push(Tween.to(grid.getFinishBlock().getPipe(), SpriteAccessor.SCALE, 0.5f).target(1f).ease(Elastic.OUT))
                    .beginSequence()
                    .push(Tween.to(nextBlockQueue.get(0), SpriteAccessor.POSITION, 0.3f).targetRelative(rb.getX() - nextBlockQueue.getX() + rb.LINE_LENGTH, 0))
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            grid.setTouchable(Touchable.enabled);
                        }
                    })
                    .end()
                    .start(tweenManager);

            grid.getFinishIndicator().setVisible(true);
            Timeline.createParallel()
                    .push(Tween.to(grid.getFinishIndicator(), SpriteAccessor.POSITION, 1f).targetRelative(0, grid.getFinishIndicator().getHeight() * 0.3f)
                            .ease(Quart.OUT))
                    .push(Tween.to(grid.getFinishIndicator(), SpriteAccessor.ALPHA, 1f).target(0.3f).ease(Quart.OUT))
                    .repeatYoyo(Tween.INFINITY,0f)
                    .start(tweenManager);

            //stop badge pop out animation
            float delay = 0.1f;
            for (int i = 0; i < grid.getBadgeArray().size; i++){
                grid.getBadgeArray().get(i).setVisible(true);
            }

            for (int i = 0; i< grid.getBadgeArray().size; i++){
                Tween.to(grid.getBadgeArray().get(i), SpriteAccessor.SCALE, 0.5f).target(1f).ease(Elastic.OUT).delay(delay).start(tweenManager);
                delay += 0.1f;
            }

            grid.addWater();
            Tween.to(grid.getWater(), SpriteAccessor.SCALE, 0.3f).target(1.4f).ease(Circ.OUT)
            .repeatYoyo(Tween.INFINITY, 0).start(tweenManager);

            state = GameState.Running;
        }
    }

    private void updateRunning(float delta) {
        if (isBlockChanged) {
            //statistics handle
            AssetLoader.stats.incrementValue("totalPipePlaced");
            YayPipe.playService.incrementAchievement(0);

            //disable grid touchable until the next block is in moved in place
            grid.setTouchable(Touchable.disabled);
            setNextPipe();
            upperBarUI.setAble(Icon.UNDO);
            isBlockChanged = false;
                upperBarUI.setDisable(Icon.UNDO);
            }
        }

        score.update(2,false);

        //snail item only gets updated during Running State
        itemTray.getSnail().update(delta);

        //handles score changes eg. stop pass points, undo
        if (score.isScoreChanged()){
            addScoreEffect(score.getEffectBlock(), score.getScoreChange());
            score.setScoreChange(false);
        }

        //handles when the water reaches the finish pipe
        if (grid.getWater().isStop()){
            state = GameState.Clear;
        }


        if (zoomUI.isZoomIn()){
            zoomUI.setZoomIn(false);
            if (((OrthographicCamera) gameWorldStage.getCamera()).zoom >= 1) {
                ((OrthographicCamera) gameWorldStage.getCamera()).zoom -= 0.1f;
            }
        }

        if (zoomUI.isZoomOut()){
            zoomUI.setZoomOut(false);
            float scale = grid.getWidth() / grid.getRefWidth();
            ((OrthographicCamera) gameWorldStage.getCamera()).zoom = scale;
            ((OrthographicCamera) gameWorldStage.getCamera()).position.set(grid.getOriginX() + grid.getX(), grid.getOriginY() + grid.getY(),0);
        }


        if (upperBarUI.isPaused() || Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            state = GameState.Paused;
            upperBarUI.setPaused(false);
        }
        if (upperBarUI.isSkip()){
            upperBarUI.setDisable(Icon.FAST_FORWARD);
            grid.getWater().skip();
            grid.setTouchable(Touchable.disabled);
        }
        if (upperBarUI.isUndo()){
            if (nextBlockQueue.isUndoAble() && grid.getUndoBlock().getFlowCount() == 0) {
                assetLoader.click.play(AssetLoader.prefs.getFloat("soundVolume"));

                if (tutorial != null){
                    tutorial.nextStep();
                }

                //statistics handle
                AssetLoader.stats.incrementValue("totalUndoPerformed");
                AssetLoader.stats.addUndoCount();

                Block block = grid.getUndoBlock();
                block.setPipe(grid.getUndoPipe());

                //pipe popout animation
                Tween.to(block.getPipe(), SpriteAccessor.SCALE,0.15f).target(1f).ease(Quart.OUT)
                        .start(tweenManager);

                score.undoScore(block);
                undoNextPipe();
                upperBarUI.setDisable(Icon.UNDO);
            }
            upperBarUI.setUndo(false);
        }


        //handles item tray input
        if (itemTray.isClicked()){
            itemTray.setTouchable(Touchable.disabled);
            float offset;
            if (itemTray.isOpen()) {
                offset = itemTray.getHeight() * 0.7f;
                itemTray.setOpen(false);

                if (itemTray.isSnailClicked()){
                    Snail snail = itemTray.getSnail();
                    if (snail.isAvailable()) {
                        snailSound.play(AssetLoader.prefs.getFloat("soundVolume"));

                        gameWorldStage.addActor(snail);
                        snail.setActive(true);

                        //puts the effect behind the grid
                        snail.setZIndex(0);

                        snail.setAvailable(false);
                        AssetLoader.prefs.putBoolean("snail", false);
                        AssetLoader.prefs.flush();

                        itemTray.disableSnail();
                        Tween.to(snail, SpriteAccessor.SCALE, 1f).target(1f).start(tweenManager);
                    }
                    itemTray.setSnailClicked(false);
                }

                if (itemTray.isWandClicked()){
                    Wand wand = itemTray.getWand();
                    if (wand.isAvailable()){
                        gameUI.addActor(wand);
                        wand.setOpen(true);
                        grid.setTouchable(Touchable.disabled);

                        Timeline.createParallel()
                                .push(Tween.to(wand.getDrawer(), SpriteAccessor.SCALE, 0.1f).target(1f))
                                .push(Tween.to(wand.getFadeInOut(), SpriteAccessor.ALPHA, 0.1f).target(0.5f))
                                .start(tweenManager);
                    }
                    itemTray.setWandClicked(false);
                }
            }
            else{
                offset = -itemTray.getHeight() * 0.7f;
                itemTray.setOpen(true);
            }
            Tween.to(itemTray, SpriteAccessor.POSITION, 0.3f).targetRelative(0, offset)
            .setCallback(new TweenCallback() {
                @Override
                public void onEvent(int type, BaseTween<?> source) {
                    itemTray.setTouchable(Touchable.enabled);
                }
            }).setCallbackTriggers(TweenCallback.END)
            .start(tweenManager);
            itemTray.setClicked(false);
        }

        //when snail item is activated, speed reduced to the starting speed
        if (itemTray.getSnail().isActive()){
            grid.getWater().setSpeed(5f);
        }

        //when snail item activation time ends
        if (itemTray.getSnail().isDeactivated()){
            final Snail snail = itemTray.getSnail();
            snail.setDeactivated(false);
            Tween.to(snail,SpriteAccessor.SCALE, 1f).target(0f)
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            snail.remove();
                        }
                    }).setCallbackTriggers(TweenCallback.END)
            .start(tweenManager);
        }

        if (itemTray.getWand().isOpen()) {
            Wand wand = itemTray.getWand();
            if (wand.isSelected()) {
                wandSound.play(AssetLoader.prefs.getFloat("soundVolume"));

                wand.setSelected(false);
                itemTray.useWand();
                AssetLoader.prefs.putInteger("wandStock", itemTray.getWand().getStock());
                AssetLoader.prefs.flush();

                final Block movingBlock = new Block(assetLoader, wand.getPipeSelected().getPipeImage());
                movingBlock.setOrigin(0, 0);
                Vector2 srcPos = wand.getPipeSelected().localToStageCoordinates(new Vector2());
                movingBlock.setPosition(srcPos.x, srcPos.y);
                gameUI.addActor(movingBlock);

                Timeline.createParallel()
                        .push(Tween.to(wand.getDrawer(), SpriteAccessor.SCALE, 0.2f).target(0f))
                        .push(Tween.to(wand.getFadeInOut(), SpriteAccessor.ALPHA, 0.2f).target(0f))
                        .push(Tween.to(movingBlock, SpriteAccessor.POSITION, 0.2f).target(rb.getX() + rb.LINE_LENGTH, rb.getY() + rb.LINE_LENGTH))
                        .push(Tween.to(movingBlock, SpriteAccessor.SCALE, 0.2f).target(0.8f)
                                .setCallback(new TweenCallback() {
                                    @Override
                                    public void onEvent(int type, BaseTween<?> source) {
                                        grid.setTouchable(Touchable.enabled);
                                        nextBlockQueue.set(0, movingBlock.getPipeImage());
                                        movingBlock.remove();
                                    }
                                }).setCallbackTriggers(TweenCallback.END))
                        .start(tweenManager);
                wand.setOpen(false);
            }

            if (wand.isCancelled()) {
                wand.setCancelled(false);
                Timeline.createParallel()
                        .push(Tween.to(wand.getDrawer(), SpriteAccessor.SCALE, 0.2f).target(0f))
                        .push(Tween.to(wand.getFadeInOut(), SpriteAccessor.ALPHA, 0.2f).target(0f))
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                grid.setTouchable(Touchable.enabled);
                            }
                        }).setCallbackTriggers(TweenCallback.END)
                        .start(tweenManager);

                wand.setOpen(false);
            }
        }

        Water water = grid.getWater();
        water.setSpeed(water.getSpeed() + water.getSpeedIncrease());
    }

    private void setNextPipe() {
        Timeline.createParallel()
                .push(Tween.to(nextBlockQueue.get(0), SpriteAccessor.ALPHA, 0.2f).target(0f))
                .push(Tween.to(nextBlockQueue.get(1), SpriteAccessor.POSITION, 0.2f).targetRelative(rb.getX() - nextBlockQueue.getX() - NextBlock.LENGTH - NextBlock.GAP + rb.LINE_LENGTH, 0))
                .push(Tween.to(nextBlockQueue.get(2), SpriteAccessor.POSITION, 0.2f).targetRelative(-NextBlock.LENGTH - NextBlock.GAP, 0))
                .push(Tween.to(nextBlockQueue.get(3), SpriteAccessor.POSITION, 0.2f).targetRelative(-NextBlock.LENGTH - NextBlock.GAP, 0))
                .push(Tween.to(nextBlockQueue.get(4), SpriteAccessor.POSITION, 0.2f).targetRelative(-NextBlock.LENGTH - NextBlock.GAP, 0))
                .push(Tween.to(nextBlockQueue.get(5), SpriteAccessor.POSITION, 0.2f).targetRelative(-NextBlock.LENGTH - NextBlock.GAP, 0))
                .push(Tween.to(nextBlockQueue.get(6), SpriteAccessor.POSITION, 0.2f).targetRelative(-NextBlock.LENGTH - NextBlock.GAP, 0))
                .push(Tween.to(nextBlockQueue.get(6), SpriteAccessor.ALPHA, 0.2f).target(1f))
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        grid.setTouchable(Touchable.enabled);
                        nextBlockQueue.addNewBlock();
                    }
                })
                .start(tweenManager);
    }

    private void undoNextPipe(){
        nextBlockQueue.undo();

        Timeline.createParallel()
                .push(Tween.to(nextBlockQueue.get(0), SpriteAccessor.ALPHA, 0.2f).target(1f))
                .push(Tween.to(nextBlockQueue.get(1), SpriteAccessor.POSITION, 0.2f).targetRelative(-(rb.getX() - nextBlockQueue.getX() - NextBlock.LENGTH - NextBlock.GAP + rb.LINE_LENGTH), 0))
                .push(Tween.to(nextBlockQueue.get(2), SpriteAccessor.POSITION, 0.2f).targetRelative(NextBlock.LENGTH + NextBlock.GAP, 0))
                .push(Tween.to(nextBlockQueue.get(3), SpriteAccessor.POSITION, 0.2f).targetRelative(NextBlock.LENGTH + NextBlock.GAP, 0))
                .push(Tween.to(nextBlockQueue.get(4), SpriteAccessor.POSITION, 0.2f).targetRelative(NextBlock.LENGTH + NextBlock.GAP, 0))
                .push(Tween.to(nextBlockQueue.get(5), SpriteAccessor.POSITION, 0.2f).targetRelative(NextBlock.LENGTH + NextBlock.GAP, 0))
                .push(Tween.to(nextBlockQueue.get(6), SpriteAccessor.POSITION, 0.2f).targetRelative(NextBlock.LENGTH + NextBlock.GAP, 0))
                .push(Tween.to(nextBlockQueue.get(6), SpriteAccessor.ALPHA, 0.2f).target(0f))
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        grid.setTouchable(Touchable.enabled);
                    }
                })
                .start(tweenManager);
    }

    private void updatePaused(float delta){
        if (!gamePausedWindow.isVisible()){
            AssetLoader.stats.stopTimer();

            grid.getWater().stop();
            grid.removeGesture();
            grid.setTouchable(Touchable.disabled);

            gameUI.addActor(gamePausedWindow);
            gamePausedWindow.setVisible(true);
            Timeline.createParallel()
                    .push(Tween.to(gamePausedWindow.background, SpriteAccessor.ALPHA, 0.2f).target(0.5f))
                    .push(Tween.to(gamePausedWindow.window, SpriteAccessor.SCALE, 0.2f).target(1f).ease(Back.OUT))
                    .start(tweenManager);

        }

        if (gamePausedWindow.isResume()){
            AssetLoader.stats.startTimer();

            gamePausedWindow.setResume(false);
            Timeline.createParallel()
                    .push(Tween.to(gamePausedWindow.background, SpriteAccessor.ALPHA, 0.2f).target(0f))
                    .push(Tween.to(gamePausedWindow.window, SpriteAccessor.SCALE, 0.2f).target(0f).ease(Back.IN))
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            state = GameState.Running;
                            gamePausedWindow.setVisible(false);
                            gamePausedWindow.remove();
                            grid.addGesture();
                            grid.setTouchable(Touchable.enabled);
                            grid.getWater().resume();
                        }
                    }).start(tweenManager);
        }

        if (gamePausedWindow.isRestart() || gamePausedWindow.isQuit()) {
            if (gamePausedWindow.isRestart()) {
                state = GameState.Restart;
                gamePausedWindow.setRestart(false);

                // consecutive count reset;
                YayPipe.consecutiveClearCount = 0;

                //coward! achievement
                YayPipe.consecutiveRestartCount++;
                if (YayPipe.consecutiveRestartCount == 5){
                    YayPipe.playService.unlockAchievement(11);
                }
            }
              else {
                state = GameState.Quit;
                gamePausedWindow.setQuit(false);
            }
          }
    }

    private void updateGameOver(float delta){
        if (!gameOverWindow.isVisible()) {
            if (state == GameState.Clear && difficulty != Difficulty.TUTORIAL_BASIC) {
                //in-game high score check
                score.checkHighScore();

                //high score/best time check for statistics
                AssetLoader.stats.checkHighScore(score.getTotalScore());
                AssetLoader.stats.checkBestTime();

                //statistics clear increment
                AssetLoader.stats.incrementClear();

                //consecutive count reset
                YayPipe.consecutiveFailCount = 0;
                YayPipe.consecutiveRestartCount = 0;

                //score achievement
                if (score.getTotalScore() >= 3000){
                    YayPipe.playService.unlockAchievement(6);
                    Unlock.setUnlock(Difficulty.NORMAL, true);
                    AssetLoader.prefs.putBoolean("itemUnlocked", true);
                    if (AssetLoader.prefs.getBoolean("adTutFlag", true)){
                        AssetLoader.prefs.putBoolean("adTutFlag", true);
                    }
                }
                if (score.getTotalScore() >= 5000){
                    YayPipe.playService.unlockAchievement(7);
                    Unlock.setUnlock(Difficulty.HARD, true);
                }
                if (score.getTotalScore() >= 10000){
                    YayPipe.playService.unlockAchievement(8);
                    Unlock.setUnlock(Difficulty.EXTREME, true);
                }
                if (score.getTotalScore() >= 25000){
                    YayPipe.playService.unlockAchievement(9);
                    Unlock.setUnlock(Difficulty.MASTER, true);
                }
                if (score.getTotalScore() >= 50000){
                    YayPipe.playService.unlockAchievement(10);
                }

                //very resourceful achievement
                if (grid.getGridBlockArray().get(0).get(0).getFlowCount() > 0 &&
                        grid.getGridBlockArray().get(0).peek().getFlowCount() > 0 &&
                        grid.getGridBlockArray().peek().get(0).getFlowCount() > 0 &&
                        grid.getGridBlockArray().peek().peek().getFlowCount() > 0){
                    YayPipe.playService.unlockAchievement(14);
                }

                //difficulty clear achievement
                YayPipe.playService.unlockAchievement(1 + DIFFICULTY);

                //you're on fire achievement
                YayPipe.consecutiveClearCount++;
                if (YayPipe.consecutiveClearCount == 3){
                    YayPipe.playService.unlockAchievement(13);
                }

                // careful planner achievement
                if (AssetLoader.stats.getPipeChangeCount() == 0 && difficulty >= Difficulty.NORMAL){
                    YayPipe.playService.unlockAchievement(15);
                }
            }
            else {
                AssetLoader.stats.incrementValue("totalFailNumber");

                // consecutive count reset
                YayPipe.consecutiveClearCount = 0;
                YayPipe.consecutiveRestartCount = 0;

                // it's aul good man achievement
                YayPipe.consecutiveFailCount++;
                if (YayPipe.consecutiveFailCount == 3){
                    YayPipe.playService.unlockAchievement(12);
                }
            }
            //reset score counter to 0
            score.reset();

            //statistics count pipe used
            AssetLoader.stats.countPipeUse(grid.getGridBlockArray());

            //most undo performed / most pipe change count check
            AssetLoader.stats.checkCount();

            //increment game play count (clear or minimum 30 seconds of gameplay is required to be qualified as a try)
            if (gameOverWindow.isClear() || AssetLoader.stats.getStageTimeSum() > 30) {
                AssetLoader.stats.incrementValue("totalTryNumber");
                YayPipe.playService.incrementAchievement(2);
            }

            //remove grid touchable/panning
            grid.removeGesture();
            grid.setTouchable(Touchable.disabled);
            grid.getWater().stop();

            //move grid to the centre, zoom out.
            float scale = grid.getWidth() / grid.getRefWidth();
            ((OrthographicCamera) gameWorldStage.getCamera()).zoom = scale;
            ((OrthographicCamera) gameWorldStage.getCamera()).position.set(grid.getOriginX() + grid.getX(), grid.getOriginY() + grid.getY(),0);

            //display game over screen
            gameUI.addActor(gameOverWindow);
            gameOverWindow.setVisible(true);

            Timeline.createSequence()
                    .push(Tween.to(gameOverWindow.fadeInOut, SpriteAccessor.ALPHA, 0.3f).target(0.5f))
                    .push(Tween.to(gameOverWindow.window, SpriteAccessor.SCALE, 0.3f).target(1f).ease(Back.OUT))
                    .start(tweenManager);
        }

        //skip score increasing animation
        if (Gdx.input.justTouched()){
            tweenManager.update(100f);
            score.skip();
        }


        //handle restart and quit button
        if (gameOverWindow.isRestart() || gameOverWindow.isQuit() || Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            if (gameOverWindow.isRestart()) {
                state = GameState.Restart;
                gameOverWindow.setRestart(false);
            } else {
                state = GameState.Quit;
                gameOverWindow.setQuit(false);
            }
        }


        //handle view button
        if (gameOverWindow.isView()){
            gameOverWindow.setView(false);
            Timeline.createParallel()
                    .push(Tween.to(gameOverWindow.fadeInOut, SpriteAccessor.ALPHA, 0.3f).target(0f))
                    .push(Tween.to(gameOverWindow.window, SpriteAccessor.SCALE, 0.3f).target(0f).ease(Back.IN))
                    .start(tweenManager);
        }


        //return from view
        if (Gdx.input.justTouched() && gameOverWindow.window.getScaleX() == 0){
            Timeline.createSequence()
                    .push(Tween.to(gameOverWindow.fadeInOut, SpriteAccessor.ALPHA, 0.3f).target(0.5f))
                    .push(Tween.to(gameOverWindow.window, SpriteAccessor.SCALE, 0.3f).target(1f).ease(Back.OUT))
                    .start(tweenManager);
        }
    }

    private void addScoreEffect(Actor actor, int score){
        final Label label = new Label(String.valueOf(score), effectStyle);
        if (score > 0){
            label.setColor(Color.FOREST);
            label.setText("+" + String.valueOf(score));
        } else{
            label.setColor(Color.ORANGE);
        }
        label.setPosition(actor.getX() + actor.getWidth(), actor.getY() + actor.getHeight() / 2, Align.center);
        label.setTouchable(Touchable.disabled);
        Timeline.createParallel()
                .beginSequence()
                .push(Tween.set(label, SpriteAccessor.ALPHA).target(0f).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        grid.addActor(label);
                    }
                }))
                .push(Tween.to(label, SpriteAccessor.ALPHA,0.25f).target(1f))
                .push(Tween.to(label, SpriteAccessor.ALPHA,0.25f).target(0f))
                .end()
                .push(Tween.to(label, SpriteAccessor.POSITION, 0.5f).targetRelative(0,actor.getHeight() * 0.7f))
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        label.remove();
                    }
                })
                .start(tweenManager);
    }

    public UpperBarUI getUpperBarUI(){
        return upperBarUI;
    }

    public BadgeIndicator getBadgeIndicator(){
        return badgeIndicator;
    }

    public Stage getGameUI(){
        return gameUI;
    }

    public Stage getGameWorldStage(){
        return gameWorldStage;
    }

    public MainGrid getGrid(){
        return grid;
    }

    public TweenManager getTweenManager(){
        return tweenManager;
    }

    public boolean isRestart() {
        return state == GameState.Restart;
    }

    public boolean isQuit(){
        return state == GameState.Quit;
    }

    public void dispose() {
        Gdx.app.log("GameWorld","disposed");
        tweenManager.killAll();
    }
}
