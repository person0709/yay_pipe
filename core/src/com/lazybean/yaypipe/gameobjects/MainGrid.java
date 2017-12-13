package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.gamehelper.Direction;
import com.lazybean.yaypipe.gamehelper.GameState;
import com.lazybean.yaypipe.gamehelper.GridSize;
import com.lazybean.yaypipe.gamehelper.PipeType;
import com.lazybean.yaypipe.gamehelper.Score;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.gamehelper.Difficulty;
import com.lazybean.yaypipe.gui.Badge;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Back;

public class MainGrid extends Group implements Disposable{
    private final float BLOCK_LENGTH = GridBlock.BLOCK_LENGTH;
    private final float BLOCK_GAP = GridBlock.BLOCK_GAP;

    private GameWorld gameWorld;
    private AssetLoader assetLoader;
    private Difficulty difficulty;
    private GridSize gridSize;

    private Array<Array<GridBlock>> mainGridBlockArray;

    /**
     * used to prevent start pipe spawning 1 tile adjacent to stops or end pipe
     */
    private Array<Vector2> gridCheck;

    private Water water;

    private GridBlock startBlock;
    private GridBlock finishBlock;
    private GridBlock currentWaterBlock;

    private Array<Badge> badgeArray;
    private int stopCount = 0;

    private TweenManager tweenManager;

//    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public MainGrid(final GameWorld gameWorld, AssetLoader assetLoader) {
        //setDebug(true);

        this.gameWorld = gameWorld;
        this.tweenManager = gameWorld.getTweenManager();
        this.assetLoader = assetLoader;
        this.difficulty = gameWorld.difficulty;
        this.gridSize = gameWorld.gridSize;

        badgeArray = new Array<>(gameWorld.difficulty.stopNum);

        gridCheck = new Array<>(gridSize.x * gridSize.y);
        mainGridBlockArray = new Array<>(gridSize.y);
        for (int i = 0; i < gridSize.y; i++){
            mainGridBlockArray.add(new Array<GridBlock>(gridSize.x));
            for (int j = 0; j < gridSize.x; j++){
                GridBlock block = assetLoader.blockFactory.obtainGridBlock();
                block.setGridPos(j, i);
                block.setPosition(j * BLOCK_LENGTH + j * BLOCK_GAP, i * BLOCK_LENGTH + i * BLOCK_GAP);
                addActor(block);

                mainGridBlockArray.get(i).add(block);
                gridCheck.add(new Vector2(j,i));
            }
        }

        setWidth(BLOCK_LENGTH * gridSize.x + BLOCK_GAP * (gridSize.x - 1));
        setHeight(BLOCK_LENGTH * gridSize.y + BLOCK_GAP * (gridSize.y - 1));
        setPosition(YayPipe.SCREEN_WIDTH / 2, YayPipe.SCREEN_HEIGHT / 2, Align.center);

        initGrid(gridSize, difficulty);

        addListener(new ActorGestureListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0 && !gameWorld.isPanning()) {
                    try {
                        GridBlock hitActor = (GridBlock) getParent().hit(getX() + x, getY() + y, true);
                        gameWorld.changeBlock(hitActor);
                    } catch (Exception ignored) {
                    }
                }
            }
        });
    }

    private void initGrid(GridSize gridSize, Difficulty difficulty){
        if (difficulty == Difficulty.TUTORIAL_ADVANCED){
            mainGridBlockArray.get(4).get(2).addStopBadge(new Badge(assetLoader, "1"), 1);
            mainGridBlockArray.get(2).get(2).addStopBadge(new Badge(assetLoader, "2"), 2);
            mainGridBlockArray.get(3).get(3).addStopBadge(new Badge(assetLoader, "3"), 3);
        }
        else {
            //assign stops to random blocks
            for (int i = 1; i <= difficulty.stopNum; i++) {
                Vector2 randomPos = new Vector2(pickRandomPoint());
                GridBlock block = mainGridBlockArray.get((int) randomPos.y).get((int) randomPos.x);
                block.addStopBadge(new Badge( assetLoader, String.valueOf(i)), i);
                badgeArray.add(block.getBadge());
            }
        }


        if (difficulty == Difficulty.TUTORIAL_BASIC || difficulty == Difficulty.TUTORIAL_ADVANCED){
            finishBlock = mainGridBlockArray.get(1).get(3);
            finishBlock.setPipe(assetLoader.getPipeImage(PipeType.BOTTOM_END), PipeType.BOTTOM_END);
            finishBlock.addFlowCount();

            startBlock = mainGridBlockArray.get(4).get(0);
            startBlock.setPipe(assetLoader.getPipeImage(PipeType.LEFT_END), PipeType.LEFT_END);
            startBlock.addFlowCount();
            water = new Water(assetLoader, startBlock, difficulty);
            currentWaterBlock = startBlock;
        }
        else {
            PipeType finishPipeType;
            Vector2 randomPos = new Vector2(pickRandomPoint());

            if (randomPos.x == 0) {
                finishPipeType = PipeType.LEFT_END;
            } else if (randomPos.x == gridSize.x - 1) {
                finishPipeType = PipeType.RIGHT_END;
            } else if (randomPos.y == 0) {
                finishPipeType = PipeType.BOTTOM_END;
            } else if (randomPos.y == gridSize.y - 1) {
                finishPipeType = PipeType.TOP_END;
            } else {
                finishPipeType = PipeType.values()[MathUtils.random(7, 10)];
            }

            finishBlock = mainGridBlockArray.get((int) randomPos.y).get((int) randomPos.x);
            finishBlock.setPipe(assetLoader.getPipeImage(finishPipeType), finishPipeType);
            finishBlock.addFlowCount();

            PipeType startPipeType = PipeType.values()[MathUtils.random(7, 10)];

            gridCheck.shuffle();
            while(true){
                Vector2 temp = gridCheck.pop();
                if (temp.x > 0 && temp.x < gridSize.x-1 && temp.y > 0 && temp.y < gridSize.y-1){
                    startBlock = mainGridBlockArray.get((int) temp.y).get((int) temp.x);
                    startBlock.setPipe(assetLoader.getPipeImage(startPipeType), startPipeType);
                    startBlock.addFlowCount();
                    water = new Water(assetLoader, startBlock, difficulty);
                    currentWaterBlock = startBlock;
                    break;
                }
            }
        }

        startBlock.getPipe().setScale(0f);
        finishBlock.getPipe().setScale(0f);
    }

    /**
     * picks a random coordinate for end pipe and stops
     */
    private Vector2 pickRandomPoint() {
        Vector2 randomPos = new Vector2(gridCheck.random());

        Vector2 tempPos = new Vector2(randomPos.x-2,randomPos.y-2);
        for (int i = 0; i < 5; i ++){
            for (int j = 0; j < 5; j++){
                gridCheck.removeValue(tempPos, false);
                tempPos.x++;
            }
            tempPos.x -= 5;
            tempPos.y++;
        }

        return randomPos;
    }

    public void checkNextBlock(){
        final GridBlock nextBlock;
        //check if the next block is out of bounds
        try {
            nextBlock = mainGridBlockArray.get(currentWaterBlock.getPosY() + water.getDirection().y)
                    .get(currentWaterBlock.getPosX() + water.getDirection().x);
        } catch (IndexOutOfBoundsException e) {
            Gdx.app.log("GameOver", "Water reached the end");
            gameWorld.setState(GameState.FAIL);
            return;
        }

        //check if the next block has a pipe
        if (nextBlock.getPipe() == null){
            Gdx.app.log("GameOver", "No pipe placed");
            gameWorld.setState(GameState.FAIL);
            return;
        }

        //handles flow stop check
        if (nextBlock.getStopNum() != 0) {
            if (nextBlock.getStopNum() == stopCount + 1) {
                Tween.to(badgeArray.get(stopCount), SpriteAccessor.SCALE, 0.5f).target(0f).ease(Back.IN)
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                gameWorld.addPoints(nextBlock, Score.ScoreType.STOP_PASS);
                                gameWorld.getGui().badgeIndicator.toNextBadge();
                                nextBlock.removeBadge();
                            }
                        }).start(tweenManager);
                stopCount++;
            } else {
                Gdx.app.log("GameOver", "Wrong stop");
                gameWorld.setState(GameState.FAIL);
                return;
            }
        }

        Array<Direction> nextPipeEnds = new Array<>(nextBlock.getPipe().getType().getOpenEnds());
        // if the pipe is all-direction-pipe, water flows straight
        if (nextPipeEnds.size == 4){
            water.addStraightWaterPath(nextBlock, water.getDirection());
            gameWorld.addPoints(nextBlock, Score.ScoreType.PIPE_CONNECT);
        }
        else{
            // if the water can flow through the pipe
            if (nextPipeEnds.contains(water.getDirection().getOpposite(), true)){
                // if the pipe is the end pipe,
                if (nextPipeEnds.size == 1){
                    // but the stops were not passed
                    if (stopCount != difficulty.stopNum){
                        Gdx.app.log("GameOver", "Stops not passed");
                        gameWorld.setState(GameState.FAIL);
                        return;
                    }
                    water.addFinalWaterPath(nextBlock, water.getDirection());
                    gameWorld.addPoints(nextBlock, Score.ScoreType.FINISH);
                }
                // if the next pipe is a straight pipe
                else if (nextPipeEnds.contains(water.getDirection(), true)){
                    water.addStraightWaterPath(nextBlock, water.getDirection());
                    gameWorld.addPoints(nextBlock, Score.ScoreType.PIPE_CONNECT);
                }
                // if the pipe is a curve pipe
                else{
                    nextPipeEnds.removeValue(water.getDirection().getOpposite(), true);
                    water.addCurvedWaterPath(nextBlock, water.getDirection(), nextPipeEnds.pop());
                    gameWorld.addPoints(nextBlock, Score.ScoreType.PIPE_CONNECT);
                }
            } else{
                Gdx.app.log("GameOver", "Wrong pipe");
                gameWorld.setState(GameState.FAIL);
                return;
            }
        }
        currentWaterBlock = nextBlock;

        if (gameWorld.getUndoBlockPos().x == nextBlock.getPosX() && gameWorld.getUndoBlockPos().y == nextBlock.getPosY()) {
            gameWorld.getGui().upperBarUI.setUndoIcon(false);
        }
    }

    public void start() {
        addActor(water);
    }

    public Water getWater(){
        return water;
    }

    public Block getStartBlock(){
        return startBlock;
    }

    public Block getFinishBlock(){
        return finishBlock;
    }

    public Array<Badge> getBadgeArray(){
        return badgeArray;
    }

    public Array<Array<GridBlock>> getGridBlockArray(){
        return mainGridBlockArray;
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    @Override
    public void dispose() {
        clearChildren();
        for (int i = 0; i < mainGridBlockArray.size; i++){
            for (int j = 0; j < mainGridBlockArray.get(i).size; j++){
                Block block = mainGridBlockArray.get(i).get(j);
                block.clearChildren();
                assetLoader.blockFactory.free(block);
            }
        }
    }
}