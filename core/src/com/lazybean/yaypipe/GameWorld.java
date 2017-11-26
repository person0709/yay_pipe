package com.lazybean.yaypipe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.lazybean.yaypipe.gamehelper.AchievementType;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Difficulty;
import com.lazybean.yaypipe.gamehelper.PipeType;
import com.lazybean.yaypipe.gamehelper.SoundType;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;
import com.lazybean.yaypipe.gamehelper.GameState;
import com.lazybean.yaypipe.gamehelper.GridSize;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.gamehelper.gamedata.Statistics;
import com.lazybean.yaypipe.gamehelper.StatisticsType;
import com.lazybean.yaypipe.gameobjects.GridBlock;
import com.lazybean.yaypipe.gui.FinishIndicator;
import com.lazybean.yaypipe.gui.Gui;
import com.lazybean.yaypipe.gameobjects.MainGrid;
import com.lazybean.yaypipe.gamehelper.Score;
import com.lazybean.yaypipe.gameobjects.Snail;
import com.lazybean.yaypipe.gameobjects.Wand;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Circ;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Quart;

public class GameWorld {
    private GameState state;

    public Difficulty difficulty;
    public GridSize gridSize;

    private Gui gui;
    private AssetLoader assetLoader;

    private Score score;

    private MainGrid grid;
    private Vector2 undoBlockPos;
    private PipeType undoBlockPipeType;

    public Snail snail;
    public Wand wand;

    private Stage gameWorldStage;
    private boolean isPanning = false;

    private TweenManager tweenManager;
    public GameWorld(final Stage gameWorldStage, AssetLoader assetLoader, GridSize gridSize, Difficulty difficulty) {
        this.gameWorldStage = gameWorldStage;
        this.assetLoader = assetLoader;
        this.gridSize = gridSize;
        this.difficulty = difficulty;

        state = GameState.RUNNING;

        this.tweenManager = new TweenManager();

        score = new Score();

        wand = new Wand(GameData.getInstance().getWandStock());
        snail = new Snail(GameData.getInstance().getSnailStock());


        mainGridInit();

        returnToPresetView();

        gameWorldStage.addListener(new ActorGestureListener(){
            private Vector2 gridPos = new Vector2();
            private Vector2 deltaPos = new Vector2();

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0){
                    isPanning = false;
                }
            }

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 1){
                    gridPos.set(grid.getX(), grid.getY());
                    isPanning = true;
                }
            }

            @Override
            public void pinch(InputEvent event, Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
                deltaPos.set(((pointer1.x - initialPointer1.x) + (pointer2.x - initialPointer2.x)) / 2,
                        ((pointer1.y - initialPointer1.y) + (pointer2.y - initialPointer2.y)) / 2);

                grid.setPosition(gridPos.x + deltaPos.x, gridPos.y + deltaPos.y);
            }
        });
    }

    public void attachGui(Gui gui){
        this.gui = gui;
    }

    private void mainGridInit() {
        grid = new MainGrid(this, assetLoader);

        gameWorldStage.addActor(grid);
    }

    public void update(float delta) {
        gameWorldStage.act();
        tweenManager.update(delta);
        score.update(2, false);

        switch (state){
            case RUNNING:
                if (grid.getWater().isClear()){
                    setState(GameState.CLEAR);
                }
                break;

            case CLEAR:
                clear();
                break;

            case FAIL:
                fail();
                break;

            case VIEW:
                if (Gdx.input.isTouched()){
                    gui.gameOverWindow.show(gui.getGuiStage());
                    setState(GameState.IDLE);
                }
                break;
        }
    }

    public void start(){
        //start timer for stat recording
        GameData.getInstance().statistics.reset();
        GameData.getInstance().statistics.startTimer();

        //stop pass badge pop out animation
        float delay = 0.1f;
        for (int i = 0; i< grid.getBadgeArray().size; i++){
            grid.getBadgeArray().get(i).setVisible(true);
            Tween.to(grid.getBadgeArray().get(i), SpriteAccessor.SCALE, 0.5f).target(1f).ease(Elastic.OUT).delay(delay).start(tweenManager);
            delay += 0.1f;
        }

        Timeline.createParallel()
                .push(Tween.to(grid.getStartBlock().getPipe(), SpriteAccessor.SCALE, 0.1f).target(1f).ease(Quart.OUT))
                .push(Tween.to(grid.getFinishBlock().getPipe(), SpriteAccessor.SCALE, 0.1f).target(1f).ease(Quart.OUT))
                .start(tweenManager);

        FinishIndicator finishIndicator = new FinishIndicator(assetLoader, grid.getFinishBlock());
        grid.addActor(finishIndicator);

        Timeline.createParallel()
                .push(Tween.to(finishIndicator, SpriteAccessor.POSITION, 1f).targetRelative(0, finishIndicator.getHeight() * 0.3f)
                        .ease(Quart.OUT))
                .push(Tween.to(finishIndicator, SpriteAccessor.ALPHA, 1f).target(0.3f).ease(Quart.OUT))
                .repeatYoyo(Tween.INFINITY,0f)
                .start(tweenManager);

        grid.start();
        Tween.to(grid.getWater(), SpriteAccessor.SCALE, 0.3f).target(1.4f).ease(Circ.OUT)
                .repeatYoyo(Tween.INFINITY, 0).start(tweenManager);
    }

    public void changeBlock(GridBlock blockToBeChanged){
        //check if water is present
        if (blockToBeChanged.getFlowCount() != 0){
            return;
        }

        undoBlockPos = new Vector2(blockToBeChanged.getPosX(), blockToBeChanged.getPosY());
        if (blockToBeChanged.getPipe() != null) {
            undoBlockPipeType = blockToBeChanged.getPipe().getType();
        } else {
            undoBlockPipeType = null;
        }

        if (blockToBeChanged.getPipe() == null){
            addPoints(blockToBeChanged, Score.ScoreType.PIPE_PLACE);
        } else {
            blockToBeChanged.setPipeChangeCount(blockToBeChanged.getPipeChangeCount() + 1);
            addPoints(blockToBeChanged, Score.ScoreType.PIPE_CHANGE);
        }

        gui.upperBarUI.setUndoIcon(true);

        //statistics handle
        GameData.getInstance().statistics.incrementValue(StatisticsType.TOTAL_PIPE, 1);
        YayPipe.playService.incrementAchievement(AchievementType.SORE_FINGER, 1);

        //disable grid touchable until the next block is in moved in place
        grid.setTouchable(Touchable.disabled);

        blockToBeChanged.setPipe(gui.nextPipeUI.blockOrderQueue.first().getPipe());
        gui.nextPipeUI.setNextBlock();
    }

    public void clear() {
        grid.setTouchable(Touchable.disabled);

        gui.showGameOverWindow(true);

        //reset score counter to 0
        score.reset();

        //apply bonus multiplier for full cross pipe uses
        score.applyMultiplier();

        YayPipe.consecutiveRestartCount = 0;
        YayPipe.consecutiveFailCount = 0;
        YayPipe.consecutiveClearCount++;
        if (YayPipe.consecutiveClearCount == 3){
            YayPipe.playService.unlockAchievement(AchievementType.ON_FIRE);
        }

        //high score/best time check for statistics
        Statistics statistics = GameData.getInstance().statistics;
        statistics.checkHighScore(difficulty, score.getTotalScore());
        statistics.stopTimer();
        statistics.checkBestTime(difficulty);
        statistics.incrementClear(difficulty);
        statistics.incrementValue(StatisticsType.TOTAL_TRY, 1);
        statistics.countPipeUse(grid.getGridBlockArray());
        statistics.checkCount();

        //very resourceful achievement
        if (grid.getGridBlockArray().get(0).get(0).getFlowCount() > 0 &&
                grid.getGridBlockArray().get(0).peek().getFlowCount() > 0 &&
                grid.getGridBlockArray().peek().get(0).getFlowCount() > 0 &&
                grid.getGridBlockArray().peek().peek().getFlowCount() > 0){
            YayPipe.playService.unlockAchievement(AchievementType.VERY_RESOURCEFUL);
        }

        // careful planner achievement
        if (statistics.getPipeChangeCount() == 0 && difficulty.isHigherLevelComparedTo(Difficulty.EASY)){
            YayPipe.playService.unlockAchievement(AchievementType.CAREFUL_PLANNER);
        }

        // keep calm and plumb on, no pain no flow, try hard achievement
        YayPipe.playService.incrementAchievement(AchievementType.PLAY_TIME, 1);

        setState(GameState.IDLE);
    }

    public void fail(){
        grid.setTouchable(Touchable.disabled);
        grid.getWater().stop();

        YayPipe.consecutiveRestartCount = 0;
        YayPipe.consecutiveClearCount = 0;
        YayPipe.consecutiveFailCount++;
        if (YayPipe.consecutiveFailCount == 3){
            YayPipe.playService.unlockAchievement(AchievementType.THINGS_HAPPEN);
        }

        Statistics statistics = GameData.getInstance().statistics;
        statistics.stopTimer();
        statistics.incrementValue(StatisticsType.TOTAL_FAIL, 1);
        statistics.incrementValue(StatisticsType.TOTAL_TRY, 1);

        gui.showGameOverWindow(false);

        //reset score counter to 0
        score.reset();

        setState(GameState.IDLE);
    }


    public void pause(){
        state = GameState.PAUSED;
        grid.getWater().stop();
        GameData.getInstance().statistics.stopTimer();

        gui.showPausedWindow();
    }

    public void resume(){
        state = GameState.RUNNING;
        grid.getWater().resume();
        GameData.getInstance().statistics.startTimer();
    }

    public void addPoints(GridBlock block, Score.ScoreType scoreType){
        score.addPoints(block, scoreType);
        addScoreEffect(block, score.getScoreChange());
    }

    public void undo() {
        //statistics handle
        GameData.getInstance().statistics.addUndoCount();

        gui.upperBarUI.setUndoIcon(false);

        final GridBlock blockToBeChanged = grid.getGridBlockArray().get((int) undoBlockPos.y).get((int) undoBlockPos.x);

        //pipe popout animation
        Timeline.createSequence()
                .push(Tween.to(blockToBeChanged.getPipe(), SpriteAccessor.SCALE, 0.1f).target(0f).ease(Quart.IN)
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                if (undoBlockPipeType == null){
                                    blockToBeChanged.setPipe(null);
                                } else {
                                    blockToBeChanged.setPipe(assetLoader.getPipeImage(undoBlockPipeType), undoBlockPipeType);
                                }
                            }
                        }))
                .push(Tween.to(blockToBeChanged.getPipe(), SpriteAccessor.SCALE, 0.1f).target(1f).ease(Quart.OUT))
                .start(tweenManager);

        score.undoScore();
        addScoreEffect(blockToBeChanged, score.getScoreChange());
        blockToBeChanged.setPipeChangeCount(blockToBeChanged.getPipeChangeCount() - 1);
        gui.nextPipeUI.undo();
    }

    private void addScoreEffect(Actor actor, int score){
        final Label label = new Label(String.valueOf(score), assetLoader.uiSkin, "scoreEffect");
        if (score > 0){
            label.setColor(Color.FOREST);
            label.setText("+" + String.valueOf(score));
            assetLoader.getSound(SoundType.PLUS_POINT).play(GameData.getInstance().getSoundVolume());
        } else{
            label.setColor(Color.ORANGE);
            assetLoader.getSound(SoundType.MINUS_POINT).play(GameData.getInstance().getSoundVolume());
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

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Vector2 getUndoBlockPos() {
        return undoBlockPos;
    }

    public Gui getGui() {
        return gui;
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
        return state == GameState.RESTART;
    }

    public boolean isQuit(){
        return state == GameState.QUIT;
    }

    public Score getScore() {
        return score;
    }

    public boolean isPanning() {
        return isPanning;
    }

    public void returnToPresetView(){
        grid.setPosition(YayPipe.SCREEN_WIDTH / 2, YayPipe.SCREEN_HEIGHT / 2, Align.center);
        ((OrthographicCamera)gameWorldStage.getCamera()).zoom = grid.getWidth() / YayPipe.SCREEN_WIDTH * 1.2f;
    }

    public void dispose() {
        Gdx.app.log("GameWorld","disposed");
        GameData.getInstance().setSnailStock(snail.getStock());
        GameData.getInstance().setWandStock(wand.getStock());
        GameData.getInstance().saveData();
        grid.dispose();
        tweenManager.killAll();
    }
}
