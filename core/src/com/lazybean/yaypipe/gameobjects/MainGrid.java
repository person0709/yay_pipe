package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.gamehelper.GridSize;
import com.lazybean.yaypipe.gamehelper.PathLoader;
import com.lazybean.yaypipe.gamehelper.PipeType;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.gamehelper.Difficulty;
import com.lazybean.yaypipe.gui.Icon;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Quart;

public class MainGrid extends Group {
    private final float BLOCK_LENGTH = YayPipe.BLOCK_LENGTH;
    private final float BLOCK_GAP = YayPipe.BLOCK_GAP;

    private final int TO_NORTH = 1;
    private final int TO_EAST = 2;
    private final int TO_SOUTH = 3;
    private final int TO_WEST = 4;

    private final Array<Vector2> fromLeftToRight = new Array<>(PathLoader.fromLeftToRight);
    private final Array<Vector2> fromRightToLeft = new Array<>(PathLoader.fromRightToLeft);
    private final Array<Vector2> fromTopToBottom = new Array<>(PathLoader.fromTopToBottom);
    private final Array<Vector2> fromBottomToTop = new Array<>(PathLoader.fromBottomToTop);
    private final Array<Vector2> fromLeftToTop = new Array<>(PathLoader.fromLeftToTop);
    private final Array<Vector2> fromTopToLeft = new Array<>(PathLoader.fromTopToLeft);
    private final Array<Vector2> fromRightToTop = new Array<>(PathLoader.fromRightToTop);
    private final Array<Vector2> fromTopToRight = new Array<>(PathLoader.fromTopToRight);
    private final Array<Vector2> fromLeftToBottom = new Array<>(PathLoader.fromLeftToBottom);
    private final Array<Vector2> fromBottomToLeft = new Array<>(PathLoader.fromBottomToLeft);
    private final Array<Vector2> fromRightToBottom = new Array<>(PathLoader.fromRightToBottom);
    private final Array<Vector2> fromBottomToRight = new Array<>(PathLoader.fromBottomToRight);

    private GameWorld world;
    private AssetLoader assetLoader;

    private Array<Array<Block>> mainGridBlockArray;
    private Array<Vector2> gridCheck;
    private Water water;

    private Block startBlock;
    private Block finishBlock;
    private Block currentWaterBlock;
    private Block undoBlock;
    private int undoPipe;

    private boolean isPanning = false;
    private float saveX, saveY;
    private int currentDirection;

    private Array<Icon> badgeArray;
    private int stopCount = 1;

    private ActorGestureListener gestureListener;

    private TweenManager tweenManager;

    private Sound pipeSound = Gdx.audio.newSound(Gdx.files.internal("pipe.ogg"));
    private Sound minusPoint = Gdx.audio.newSound(Gdx.files.internal("minusPoint.ogg"));

    public MainGrid(GameWorld gameWorld, AssetLoader assetLoader, GridSize gridSize, Difficulty difficulty) {
        //setDebug(true);

        this.world = gameWorld;
        this.tweenManager = gameWorld.getTweenManager();
        this.assetLoader = assetLoader;
        badgeArray = new Array<>(difficulty.stopNum);

        for (int i = 0; i < gridSize.y; i++){
            for (int j = 0; j < gridSize.x; j++){
                Block block = new Block(assetLoader, j ,i);
                addActor(block);
                block.setPosition(j * BLOCK_LENGTH + j * BLOCK_GAP, i * BLOCK_LENGTH + i * BLOCK_GAP);
            }
        }

        setWidth(BLOCK_LENGTH * gridSize.x + BLOCK_GAP * (gridSize.x - 1));
        setHeight(BLOCK_LENGTH * gridSize.y + BLOCK_GAP * (gridSize.y - 1));
        setX((Gdx.graphics.getWidth() - getWidth()) / 2);
        setY((Gdx.graphics.getHeight() - getHeight()) / 2);

        setOrigin(getWidth()/2, getHeight()/2);

        addListener(new ActorGestureListener(){
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0 && !isPanning) {
                    try {
                        Block hitActor = (Block) getParent().hit(getX() + x, getY() + y, true);
                        if (hitActor != null && hitActor.getFlowCount() == 0) {
                            if (hitActor.getPipeImage() != 0) {
                                //pipe change count stat
                                AssetLoader.stats.addPipeChangeCount();

                                GameWorld.score.addScore(hitActor.getPipePenalty(), hitActor, Score.PIPE_CHANGE, true);
                                minusPoint.play(AssetLoader.prefs.getFloat("soundVolume"));
                                hitActor.getPipe().setScale(0f);
                            } else {
                                pipeSound.play(AssetLoader.prefs.getFloat("soundVolume"));
                                GameWorld.score.addScore(5, hitActor, Score.PIPE_CONNECT, true);
                            }
                            undoPipe = hitActor.getPipeImage();
                            hitActor.setPipe(NextBlockQueue.blockQueue.first().getPipe());
                            Tween.to(hitActor.getPipe(),SpriteAccessor.SCALE,0.15f).target(1f).ease(Quart.OUT)
                                    .start(tweenManager);
                            undoBlock = hitActor;
                            GameWorld.isBlockChanged = true;
                        }
                    }
                    catch (Exception ignored){
                    }
                }
                else if (pointer >= 1 && isPanning) {
                    isPanning = true;
                    saveX = getX();
                    saveY = getY();
                }
                else {
                    isPanning = false;
                    saveX = getX();
                    saveY = getY();
                }
            }
        });


        saveX = getX();
        saveY = getY();

        gestureListener = new ActorGestureListener() {
            @Override
            public void pinch(InputEvent event, Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
                float x1 = initialPointer2.x;
                float y1 = initialPointer2.y;
                float x2 = pointer2.x;
                float y2 = pointer2.y;
                float deltaX = x2 - x1;
                float deltaY = y2 - y1;

                isPanning = true;
                if (deltaX == 0 || deltaY == 0){
                    return;
                }
                setPosition(saveX + deltaX, saveY + deltaY);
            }
        };
        initGrid();
    }

    public void initGrid(){
        if ( == Difficulty.TUTORIAL_ADVANCED){
            mainGridBlockArray.get(4).get(2).setStopNum(1);
            mainGridBlockArray.get(2).get(2).setStopNum(2);
            mainGridBlockArray.get(3).get(3).setStopNum(3);
        }
        else {
            //assign stops to random blocks
            for (int i = 1; i <= stopSize; i++) {
                Vector2 randomPos = new Vector2(pickRandomPoint());
                mainGridBlockArray.get((int) randomPos.y).get((int) randomPos.x).setStopNum(i);
            }
        }


        if (AssetLoader.prefs.getInteger("difficulty") <= Difficulty.TUTORIAL_BASIC){
            finishBlock = mainGridBlockArray.get(1).get(3);

            //7 is the offset for single opened pipes
            finishBlock.setPipe(TO_NORTH + 7);
            finishBlock.addFlowCount();

            startBlock = mainGridBlockArray.get(4).get(0);
            startBlock.setPipe(TO_EAST + 7);
            startBlock.addFlowCount();
            water = new Water(startBlock, TO_EAST, assetLoader.square, assetLoader.circle);
            currentDirection = TO_EAST;
            currentWaterBlock = startBlock;
        }
        else {
            int finishOri;
            Vector2 randomPos = new Vector2(pickRandomPoint());

            if (randomPos.x == 0) {
                finishOri = TO_EAST;
            } else if (randomPos.x == sizeX - 1) {
                finishOri = TO_WEST;
            } else if (randomPos.y == 0) {
                finishOri = TO_NORTH;
            } else if (randomPos.y == sizeY - 1) {
                finishOri = TO_SOUTH;
            } else {
                finishOri = MathUtils.random(1, 4);
            }

            finishBlock = mainGridBlockArray.get((int) randomPos.y).get((int) randomPos.x);
            finishBlock.setPipe(finishOri + 7);
            finishBlock.addFlowCount();

            int startOri = MathUtils.random(1,4);

            gridCheck.shuffle();
            while(true){
                Vector2 temp = gridCheck.pop();
                if (temp.x > 0 && temp.x < sizeX-1 && temp.y > 0 && temp.y < sizeY-1){
                    startBlock = mainGridBlockArray.get((int) temp.y).get((int) temp.x);
                    startBlock.setPipe(startOri + 7);
                    startBlock.addFlowCount();
                    water = new Water(startBlock, startOri, assetLoader.square, assetLoader.circle);
                    currentDirection = startOri;
                    currentWaterBlock = startBlock;
                    break;
                }
            }
        }

        finishIndicator = new Image(assetLoader.finishIndicator);
        finishIndicator.setScaling(Scaling.fillX);
        finishIndicator.setWidth(Gdx.graphics.getWidth() * 0.025f);
        finishIndicator.setPosition(finishBlock.getX() + finishBlock.getWidth()/2 - finishIndicator.getWidth()/2,
                finishBlock.getY() + finishBlock.getHeight() * 0.8f);
        finishIndicator.setAlign(Align.bottom);
        finishIndicator.setVisible(false);
        addActor(finishIndicator);

    }

    //picks a random coordinate
    public Vector2 pickRandomPoint() {
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

    @Override
    public void act(float delta) {
//        super.act(delta);
        //only updates during Running State
        if (world.state == GameWorld.GameState.Running) {

            //when the water reaches the edge of the block
            if (water.isDone()) {
                final Block nextBlock;
                switch (currentDirection) {
                    case TO_NORTH: {
                        try {
                            nextBlock = mainGridBlockArray.get(currentWaterBlock.getPosY() + 1).get(currentWaterBlock.getPosX());
                            currentWaterBlock = nextBlock;
                            if (nextBlock.getStopNum() != 0) {
                                if (nextBlock.getStopNum() != stopCount) {
                                    Gdx.app.log("GameOver", "wrongStop");
                                    world.state = GameWorld.GameState.GameOver;
                                    return;
                                } else {
                                    Tween.to(badgeArray.get(stopCount - 1), SpriteAccessor.SCALE, 0.5f).target(0f).ease(Back.IN)
                                            .setCallback(new TweenCallback() {
                                                @Override
                                                public void onEvent(int type, BaseTween<?> source) {
                                                    world.getBadgeIndicator().toNextBadge();
                                                    nextBlock.setStopNum(0);
                                                }
                                            }).start(tweenManager);
                                    stopCount++;
                                }
                            }
                            if (undoBlock.equals(nextBlock)) {
                                world.getUpperBarUI().setDisable(Icon.UNDO);
                            }
                        } catch (Exception e) {
                            Gdx.app.log("GameOver", "NOTFOUND");
                            world.state = GameWorld.GameState.GameOver;
                            return;
                        }
                        if ((nextBlock.getPipeImage() == PipeType.LEFT_BOTTOM)) {
                            Gdx.app.log("bottomToLeft", "Accessed");
                            water.addCurvedWaterPath(nextBlock, fromBottomToLeft);
                            nextBlock.addFlowCount();
                            currentDirection = TO_WEST;
                        } else if (nextBlock.getPipeImage() == PipeType.TOP_BOTTOM ||
                                nextBlock.getPipeImage() == PipeType.ALL_DIRECTION) {
                            Gdx.app.log("bottomToTop", "Accessed");
                            water.addStraightWaterPath(nextBlock, fromBottomToTop);
                            nextBlock.addFlowCount();
                            currentDirection = TO_NORTH;
                        } else if (nextBlock.getPipeImage() == PipeType.RIGHT_BOTTOM) {
                            Gdx.app.log("bottomToRight", "Accessed");
                            water.addCurvedWaterPath(nextBlock, fromBottomToRight);
                            nextBlock.addFlowCount();
                            currentDirection = TO_EAST;
                        } else if (nextBlock.getPipeImage() == PipeType.TOP_END) {
                            if (stopCount - 1 != stopSize) {
                                Gdx.app.log("GameOver", "stopNotPassed");
                                world.state = GameWorld.GameState.GameOver;
                                return;
                            }
                            Gdx.app.log("topFinish", "Accessed");
                            water.addFinalWaterPath(nextBlock, fromBottomToTop);
                        } else {
                            Gdx.app.log("GameOver", "NORTH");
                            world.state = GameWorld.GameState.GameOver;
                        }
                        break;
                    }

                    case TO_EAST: {
                        try {
                            nextBlock = mainGridBlockArray.get(currentWaterBlock.getPosY()).get(currentWaterBlock.getPosX() + 1);
                            currentWaterBlock = nextBlock;
                            if (nextBlock.getStopNum() != 0) {
                                if (nextBlock.getStopNum() != stopCount) {
                                    Gdx.app.log("GameOver", "wrongStop");
                                    world.state = GameWorld.GameState.GameOver;
                                    return;
                                } else {
                                    Tween.to(badgeArray.get(stopCount - 1), SpriteAccessor.SCALE, 0.5f).target(0f).ease(Back.IN)
                                            .setCallback(new TweenCallback() {
                                                @Override
                                                public void onEvent(int type, BaseTween<?> source) {
                                                    world.getBadgeIndicator().toNextBadge();
                                                    nextBlock.setStopNum(0);
                                                }
                                            }).start(tweenManager);
                                    stopCount++;
                                }
                            }
                            if (undoBlock.equals(nextBlock)) {
                                world.getUpperBarUI().setDisable(Icon.UNDO);
                            }
                        } catch (Exception e) {
                            Gdx.app.log("GameOver", "NOTFOUND");
                            world.state = GameWorld.GameState.GameOver;
                            return;
                        }
                        if ((nextBlock.getPipeImage() == PipeType.LEFT_TOP)) {
                            Gdx.app.log("leftToTop", "Accessed");
                            water.addCurvedWaterPath(nextBlock, fromLeftToTop);
                            nextBlock.addFlowCount();
                            currentDirection = TO_NORTH;
                        } else if (nextBlock.getPipeImage() == PipeType.LEFT_RIGHT ||
                                nextBlock.getPipeImage() == PipeType.ALL_DIRECTION) {
                            Gdx.app.log("leftToRight", "Accessed");
                            water.addStraightWaterPath(nextBlock, fromLeftToRight);
                            nextBlock.addFlowCount();
                            currentDirection = TO_EAST;
                        } else if (nextBlock.getPipeImage() == PipeType.LEFT_BOTTOM) {
                            Gdx.app.log("leftToBottom", "Accessed");
                            water.addCurvedWaterPath(nextBlock, fromLeftToBottom);
                            nextBlock.addFlowCount();
                            currentDirection = TO_SOUTH;
                        } else if (nextBlock.getPipeImage() == PipeType.RIGHT_END) {
                            if (stopCount - 1 != stopSize) {
                                Gdx.app.log("GameOver", "stopNotPassed");
                                world.state = GameWorld.GameState.GameOver;
                                return;
                            }
                            Gdx.app.log("rightFinish", "Accessed");
                            water.addFinalWaterPath(nextBlock, fromLeftToRight);
                        } else {
                            Gdx.app.log("GameOver", "EAST");
                            world.state = GameWorld.GameState.GameOver;
                        }
                        break;
                    }

                    case TO_SOUTH: {
                        try {
                            nextBlock = mainGridBlockArray.get(currentWaterBlock.getPosY() - 1).get(currentWaterBlock.getPosX());
                            currentWaterBlock = nextBlock;
                            if (nextBlock.getStopNum() != 0) {
                                if (nextBlock.getStopNum() != stopCount) {
                                    Gdx.app.log("GameOver", "wrongStop");
                                    world.state = GameWorld.GameState.GameOver;
                                    return;
                                } else {
                                    Tween.to(badgeArray.get(stopCount - 1), SpriteAccessor.SCALE, 0.5f).target(0f).ease(Back.IN)
                                            .setCallback(new TweenCallback() {
                                                @Override
                                                public void onEvent(int type, BaseTween<?> source) {
                                                    world.getBadgeIndicator().toNextBadge();
                                                    nextBlock.setStopNum(0);
                                                }
                                            }).start(tweenManager);
                                    stopCount++;
                                }
                            }
                            if (undoBlock.equals(nextBlock)) {
                                world.getUpperBarUI().setDisable(Icon.UNDO);
                            }
                        } catch (Exception e) {
                            Gdx.app.log("GameOver", "NOTFOUND");
                            world.state = GameWorld.GameState.GameOver;
                            return;
                        }
                        if ((nextBlock.getPipeImage() == PipeType.LEFT_TOP)) {
                            Gdx.app.log("topToLeft", "Accessed");
                            water.addCurvedWaterPath(nextBlock, fromTopToLeft);
                            nextBlock.addFlowCount();
                            currentDirection = TO_WEST;
                        } else if (nextBlock.getPipeImage() == PipeType.TOP_BOTTOM ||
                                nextBlock.getPipeImage() == PipeType.ALL_DIRECTION) {
                            Gdx.app.log("topToBottom", "Accessed");
                            water.addStraightWaterPath(nextBlock, fromTopToBottom);
                            nextBlock.addFlowCount();
                            currentDirection = TO_SOUTH;
                        } else if (nextBlock.getPipeImage() == PipeType.RIGHT_TOP) {
                            Gdx.app.log("topToRight", "Accessed");
                            water.addCurvedWaterPath(nextBlock, fromTopToRight);
                            nextBlock.addFlowCount();
                            currentDirection = TO_EAST;
                        } else if (nextBlock.getPipeImage() == PipeType.BOTTOM_END) {
                            if (stopCount - 1 != stopSize) {
                                Gdx.app.log("GameOver", "stopNotPassed");
                                world.state = GameWorld.GameState.GameOver;
                                return;
                            }
                            Gdx.app.log("bottomFinish", "Accessed");
                            water.addFinalWaterPath(nextBlock, fromTopToBottom);
                        } else {
                            Gdx.app.log("GameOver", "SOUTH");
                            world.state = GameWorld.GameState.GameOver;
                        }
                        break;
                    }

                    case TO_WEST: {
                        try {
                            nextBlock = mainGridBlockArray.get(currentWaterBlock.getPosY()).get(currentWaterBlock.getPosX() - 1);
                            currentWaterBlock = nextBlock;
                            if (nextBlock.getStopNum() != 0) {
                                if (nextBlock.getStopNum() != stopCount) {
                                    Gdx.app.log("GameOver", "wrongStop");
                                    world.state = GameWorld.GameState.GameOver;
                                    return;
                                } else {
                                    Tween.to(badgeArray.get(stopCount - 1), SpriteAccessor.SCALE, 0.5f).target(0f).ease(Back.IN)
                                            .setCallback(new TweenCallback() {
                                                @Override
                                                public void onEvent(int type, BaseTween<?> source) {
                                                    nextBlock.setStopNum(0);
                                                    world.getBadgeIndicator().toNextBadge();
                                                }
                                            }).start(tweenManager);
                                    stopCount++;
                                }
                            }
                            if (undoBlock.equals(nextBlock)) {
                                world.getUpperBarUI().setDisable(Icon.UNDO);
                            }
                        } catch (Exception e) {
                            Gdx.app.log("GameOver", "NOTFOUND");
                            world.state = GameWorld.GameState.GameOver;
                            return;
                        }
                        if ((nextBlock.getPipeImage() == PipeType.RIGHT_TOP)) {
                            Gdx.app.log("rightToTop", "Accessed");
                            water.addCurvedWaterPath(nextBlock, fromRightToTop);
                            nextBlock.addFlowCount();
                            currentDirection = TO_NORTH;
                        } else if (nextBlock.getPipeImage() == PipeType.LEFT_RIGHT ||
                                nextBlock.getPipeImage() == PipeType.ALL_DIRECTION) {
                            Gdx.app.log("rightToLeft", "Accessed");
                            water.addStraightWaterPath(nextBlock, fromRightToLeft);
                            nextBlock.addFlowCount();
                            currentDirection = TO_WEST;
                        } else if (nextBlock.getPipeImage() == PipeType.RIGHT_BOTTOM) {
                            Gdx.app.log("rightToBottom", "Accessed");
                            water.addCurvedWaterPath(nextBlock, fromRightToBottom);
                            nextBlock.addFlowCount();
                            currentDirection = TO_SOUTH;
                        } else if (nextBlock.getPipeImage() == PipeType.LEFT_END) {
                            if (stopCount - 1 != stopSize) {
                                Gdx.app.log("GameOver", "stopNotPassed");
                                world.state = GameWorld.GameState.GameOver;
                                return;
                            }
                            Gdx.app.log("leftFinish", "Accessed");
                            water.addFinalWaterPath(nextBlock, fromRightToLeft);
                        } else {
                            Gdx.app.log("GameOver", "WEST");
                            world.state = GameWorld.GameState.GameOver;
                        }
                        break;
                    }
                }
                water.resetDone();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public void addGesture(){
        getStage().addListener(gestureListener);
    }

    public void removeGesture(){
        getStage().removeListener(gestureListener);
    }

    public void addWater() {
        addActor(water);
    }

    public Water getWater(){
        return water;
    }

    public float getRefLeft(){
        return refLeft;
    }

    public float getRefRight(){
        return refRight;
    }

    public float getRefWidth(){
        return refWidth;
    }

    public float getRefBottom(){
        return refBottom;
    }

    public Block getUndoBlock() {
        return undoBlock;
    }

    public int getUndoPipe(){
        return undoPipe;
    }

    public Block getStartBlock(){
        return startBlock;
    }

    public Block getFinishBlock(){
        return finishBlock;
    }

    public Array<Icon> getBadgeArray(){
        return badgeArray;
    }

    public Array<Array<Block>> getGridBlockArray(){
        return mainGridBlockArray;
    }

    public Image getFinishIndicator(){
        return finishIndicator;
    }
}