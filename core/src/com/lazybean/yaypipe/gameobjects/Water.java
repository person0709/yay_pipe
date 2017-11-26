package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gamehelper.Difficulty;
import com.lazybean.yaypipe.gamehelper.Direction;
import com.lazybean.yaypipe.gamehelper.GridSize;
import com.lazybean.yaypipe.gamehelper.PathLoader;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

public class Water extends Actor {
    private final Color WATER_COLOR = CustomColor.TURQUOISE.getColor();
    private final float WATER_WIDTH = GridBlock.BLOCK_LENGTH / 5f;
    private final float SAMPLE_DISTANCE = 2f;
    private final float INITIAL_SPEED = 5f;
    private final float SPEED_INCREASE_INTERVAL = 5f;

    private Difficulty difficulty;

    private Vector2 velocity;

    // the positions of water blobs to be drawn up until the current block from the start
    private Array<Vector2> drawingPath = new Array<>();
    private int drawingPathNum = 0;

    // the positions of water blobs to be drawn in the current block
    private Array<Vector2> animatingPath = new Array<>();

    // the index of animatingPath array that needs to be drawn up to. i.e. all the positions from index 0 to current need to be drawn
    private float current = 0;

    private boolean isStop = false;
    private boolean isFinalPipe = false;
    private boolean isClear = false;
    private boolean isSnailActive = false;

    private Vector2 stageCoordinates = new Vector2();

    private TextureRegion waterSquare;
    private TextureRegion waterCircle;


    Water(AssetLoader assetLoader, Block startBlock, Difficulty difficulty) {
        this.difficulty = difficulty;
        Direction startingDir = startBlock.getPipe().getType().getOpenEnds().first();
        velocity = new Vector2(startingDir.x * INITIAL_SPEED, startingDir.y * INITIAL_SPEED);

        this.waterSquare = assetLoader.square;
        this.waterCircle = assetLoader.circle;

        startBlock.localToParentCoordinates(stageCoordinates);

        Array<Vector2> startingPath = PathLoader.getPath(startingDir, startingDir, true);
        Vector2 tmp;
        drawingPath.add(startingPath.first().cpy().add(stageCoordinates));
        for (int i = 0; i < startingPath.size; i++) {
            tmp = new Vector2(startingPath.get(i).cpy().add(stageCoordinates));
            animatingPath.add(tmp.cpy());

            if (drawingPath.peek().dst(tmp) > WATER_WIDTH/2) {
                drawingPath.add(tmp.cpy());
                drawingPathNum++;
            }
        }
        drawingPath.add(animatingPath.peek().cpy());
        drawingPathNum++;

        setBounds(startingPath.first().x + stageCoordinates.x - WATER_WIDTH/2,
                startingPath.first().y + stageCoordinates.y - WATER_WIDTH/2, WATER_WIDTH, WATER_WIDTH);

        setOrigin(WATER_WIDTH/2, WATER_WIDTH/2);
    }

    public void addStraightWaterPath(GridBlock block, Direction direction) {
        block.addFlowCount();

        animatingPath.clear();
        Vector2 tmp;
        stageCoordinates.setZero();
        block.localToParentCoordinates(stageCoordinates);

        Array<Vector2> waterPath = PathLoader.getPath(direction, direction, false);
        drawingPath.add(waterPath.first().cpy().add(stageCoordinates));
        for (int i = 0; i < waterPath.size; i++) {
            tmp = new Vector2(waterPath.get(i).cpy().add(stageCoordinates));
            animatingPath.add(tmp.cpy());

            if (drawingPath.peek().dst(tmp) > WATER_WIDTH/2) {
                drawingPath.add(tmp.cpy());
                drawingPathNum++;
            }
        }
        drawingPath.add(animatingPath.peek().cpy());
        drawingPathNum++;
    }

    public void addCurvedWaterPath(GridBlock block, Direction enteringDir, Direction exitingDir) {
        block.addFlowCount();

        animatingPath.clear();
        Vector2 tmp;
        stageCoordinates.setZero();
        block.localToParentCoordinates(stageCoordinates);

        Array<Vector2> waterPath = PathLoader.getPath(enteringDir, exitingDir, false);
        drawingPath.add(waterPath.first().cpy().add(stageCoordinates));
        for (int i = 0; i < waterPath.size; i++) {
            tmp = new Vector2(waterPath.get(i).cpy().add(stageCoordinates));
            animatingPath.add(tmp.cpy());

            if (drawingPath.peek().dst(tmp) > SAMPLE_DISTANCE) {
                drawingPath.add(tmp.cpy());
                drawingPathNum++;
            }
        }
        drawingPath.add(animatingPath.peek().cpy());
        drawingPathNum++;

        float len = velocity.len();
        velocity.x = exitingDir.x * len;
        velocity.y = exitingDir.y * len;
    }

    public void addFinalWaterPath(GridBlock block, Direction direction){
        animatingPath.clear();
        Vector2 tmp;
        stageCoordinates.setZero();
        block.localToParentCoordinates(stageCoordinates);

        Array<Vector2> waterPath = PathLoader.getPath(direction, direction, false);
        drawingPath.add(waterPath.first().cpy().add(stageCoordinates));
        for (int i = 0; i < waterPath.size * 0.65; i++) {
            tmp = new Vector2(waterPath.get(i).cpy().add(stageCoordinates));
            animatingPath.add(tmp.cpy());

            if (drawingPath.peek().dst(tmp) > WATER_WIDTH/2) {
                drawingPath.add(tmp.cpy());
                drawingPathNum++;
            }
        }
        drawingPath.add(animatingPath.peek().cpy());
        drawingPathNum++;

        isFinalPipe = true;
    }

    public void setSnail(boolean snail) {
        this.isSnailActive = snail;

        if (isSnailActive){
            setVelocity(Snail.WATER_SPEED);
        }
    }

    /**
     * stop water flow
     */
    public void stop(){
        isStop = true;
    }

    /**
     * resume water flow
     */
    public void resume(){
        isStop = false;
    }

    /**
     * check if water reached the final pipe, hence clearing the game
     */
    public boolean isClear() {
        return isClear;
    }

    public void skip(){
        velocity.setLength(200f);
    }

    public Direction getDirection(){
        if (velocity.x > 0){
            return Direction.TO_EAST;
        }
        else if (velocity.x < 0){
            return Direction.TO_WEST;
        }
        else if (velocity.y > 0){
            return Direction.TO_NORTH;
        }
        else{
            return Direction.TO_SOUTH;
        }
    }

    private void setVelocity(float speed){
        Vector2 newVelocity = new Vector2(velocity);
        newVelocity.nor();
        newVelocity.scl(speed);
        velocity = newVelocity;
    }

    private float timeCount = 0;
    @Override
    public void act(float delta) {
        if (!isStop) {
            current += (delta * velocity.len() / PathLoader.straightPathDerAvg * animatingPath.size);
            timeCount += delta;
        }

        // speed increase every interval
        if (timeCount > SPEED_INCREASE_INTERVAL && velocity.len() < difficulty.waterSpeedLimit && !isSnailActive){
            timeCount = 0;
            setVelocity(velocity.len() + difficulty.waterSpeedIncrease);
        }

        Gdx.app.log("currentSpeed", String.valueOf(velocity.len()));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = batch.getColor();
        batch.setColor(this.WATER_COLOR);

        batch.draw(waterCircle, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

        // draws water flow in the current block
        for (int i = 0; i < (int) current; i++) {
            try {
                batch.draw(waterCircle, animatingPath.get(i).x - WATER_WIDTH / 2,
                        animatingPath.get(i).y - WATER_WIDTH / 2, WATER_WIDTH, WATER_WIDTH);
            }
            catch (Exception ignored){
            }
        }

        if (current > animatingPath.size) {
            if (isFinalPipe) {
                isStop = true;
                isClear = true;
            }
            else{
//                batch.end();
//
//                buffer.begin();
//
//                Matrix4 matrix = new Matrix4();
//                matrix.setToOrtho2D(0,0,getStage().getWidth(), getStage().getHeight());
//                batch.setProjectionMatrix(matrix);
//
//                batch.begin();
//                for (int i = 0; i < (int) current; i++) {
//                    batch.draw(waterCircle, animatingPath.get(i).x - WATER_WIDTH / 2,
//                            animatingPath.get(i).y - WATER_WIDTH / 2, WATER_WIDTH, WATER_WIDTH);
//                }
//                batch.end();
//
//                buffer.end();
//
//                texture = new TextureRegion(buffer.getColorBufferTexture());
//                texture.flip(false,true);
//
//                batch.begin();

                drawingPathNum = 0;
                current = 5;
                ((MainGrid)getParent()).checkNextBlock();
            }
        }

        //draws water path so far
        for (int i = 0; i < drawingPath.size - drawingPathNum; i++) {
            try {
                if (drawingPath.get(i).dst(drawingPath.get(i + 1)) <= WATER_WIDTH / 2 || i == 0 || i == drawingPath.size - (drawingPathNum +1)) {
                    batch.draw(waterCircle, drawingPath.get(i).x - WATER_WIDTH / 2,
                            drawingPath.get(i).y - WATER_WIDTH / 2, WATER_WIDTH, WATER_WIDTH);
                } else {
                    batch.draw(waterSquare, drawingPath.get(i).x - WATER_WIDTH / 2,
                            drawingPath.get(i).y - WATER_WIDTH / 2, WATER_WIDTH, WATER_WIDTH);
                }
            }
            catch (Exception e){
                batch.draw(waterCircle, drawingPath.get(i).x - WATER_WIDTH / 2,
                        drawingPath.get(i).y - WATER_WIDTH / 2, WATER_WIDTH, WATER_WIDTH);
            }
        }

//        if (texture != null) {
//            batch.draw(texture, getX(), getY(), YayPipe.SCREEN_WIDTH, YayPipe.SCREEN_HEIGHT);
//        }

        batch.setColor(color);
    }
}