package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Colour;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.gamehelper.PathLoader;
import com.lazybean.yaypipe.gamehelper.Difficulty;

public class Water extends Actor {
    private final int TO_NORTH = 1;
    private final int TO_EAST = 2;
    private final int TO_SOUTH = 3;
    private final int TO_WEST = 4;
    private final Color WATER_COLOR = Colour.TURQUOISE;
    private final float WATER_WIDTH = Block.LENGTH / 5f;
    private final float SAMPLE_DISTANCE = 2f;


    private float current = 0;
    private float speed = 5f;

    private Array<Vector2> drawingPath = new Array<Vector2>();
    private int drawingPathNum = 0;
    private Array<Vector2> animatingPath = new Array<Vector2>();

    private boolean isDone = false;
    private boolean isStop = false;
    private boolean isFinal = false;

    private Vector2 stageCoordinates = new Vector2();

    private TextureRegion waterSquare;
    private TextureRegion waterCircle;

    private int stopPoint = 1000;

    private float speedLimit;
    private float speedIncrease;

    private Sound plusPoint = Gdx.audio.newSound(Gdx.files.internal("plusPoint.ogg"));


    Water(Block startBlock, int dir, TextureRegion waterSquare, TextureRegion waterCircle) {

        this.waterSquare = waterSquare;
        this.waterCircle = waterCircle;

        switch (AssetLoader.prefs.getInteger("difficulty")){
            case Difficulty.EASY:
                speedIncrease = 0.005f;
                speedLimit = 50f;
                break;

            case Difficulty.NORMAL:
                speedIncrease = 0.007f;
                speedLimit = 60f;
                break;

            case Difficulty.HARD:
                speedIncrease = 0.008f;
                speedLimit = 70f;
                break;

            case Difficulty.EXTREME:
                speedIncrease = 0.01f;
                speedLimit = 80f;
                break;

            case Difficulty.MASTER:
                speedIncrease = 0.012f;
                speedLimit = 90f;
                break;

            default:
                speed = 0;
                speedIncrease = 0f;
                speedLimit = 10f;
        }
        Array<Vector2> startFromBottom = new Array<Vector2>(PathLoader.startFromBottom);
        Array<Vector2> startFromTop = new Array<Vector2>(PathLoader.startFromTop);
        Array<Vector2> startFromLeft = new Array<Vector2>(PathLoader.startFromLeft);
        Array<Vector2> startFromRight = new Array<Vector2>(PathLoader.startFromRight);
        startBlock.localToParentCoordinates(stageCoordinates);
        switch (dir) {
            case TO_NORTH:
                addStraightWaterPath(startBlock, startFromBottom);
                setBounds(startFromBottom.first().x + stageCoordinates.x - WATER_WIDTH/2,
                        startFromBottom.first().y + stageCoordinates.y - WATER_WIDTH/2, WATER_WIDTH, WATER_WIDTH);
                break;

            case TO_EAST:
                addStraightWaterPath(startBlock, startFromLeft);
                setBounds(startFromLeft.first().x + stageCoordinates.x - WATER_WIDTH/2,
                        startFromLeft.first().y + stageCoordinates.y - WATER_WIDTH/2, WATER_WIDTH, WATER_WIDTH);
                break;

            case TO_SOUTH:
                addStraightWaterPath(startBlock, startFromTop);
                setBounds(startFromTop.first().x + stageCoordinates.x - WATER_WIDTH/2,
                        startFromTop.first().y + stageCoordinates.y - WATER_WIDTH/2, WATER_WIDTH, WATER_WIDTH);
                break;

            case TO_WEST:
                addStraightWaterPath(startBlock, startFromRight);
                setBounds(startFromRight.first().x + stageCoordinates.x - WATER_WIDTH/2,
                        startFromRight.first().y + stageCoordinates.y - WATER_WIDTH/2, WATER_WIDTH, WATER_WIDTH);
                break;
        }
        setOrigin(WATER_WIDTH/2, WATER_WIDTH/2);
    }

    public void addStraightWaterPath(Actor block, Array<Vector2> waterPath) {
        if (drawingPath.size != 0){
            if (((Block) block).getStopNum() == 0) {
                GameWorld.score.addScore(100, (Block) block, Score.PIPE_CONNECT, false);
            }
            else{
                GameWorld.score.addScore(stopPoint, (Block) block, Score.STOP_PASS, false);
                stopPoint += 1000;
            }
            plusPoint.play(AssetLoader.prefs.getFloat("soundVolume"));
        }
        animatingPath.clear();
        Vector2 tmp;
        stageCoordinates.setZero();
        block.localToParentCoordinates(stageCoordinates);
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

    public void addCurvedWaterPath(Actor block, Array<Vector2> waterPath) {
        animatingPath.clear();
        Vector2 tmp;
        stageCoordinates.setZero();
        block.localToParentCoordinates(stageCoordinates);
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
        if (((Block) block).getStopNum() == 0) {
            GameWorld.score.addScore(100, (Block) block, Score.PIPE_CONNECT, false);
        }
        else{
            GameWorld.score.addScore(stopPoint, (Block) block, Score.STOP_PASS, false);
            stopPoint += 1000;
        }
        plusPoint.play(AssetLoader.prefs.getFloat("soundVolume"));
    }

    public void addFinalWaterPath(Actor block, Array<Vector2> waterPath){
        animatingPath.clear();
        Vector2 tmp;
        stageCoordinates.setZero();
        block.localToParentCoordinates(stageCoordinates);
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
        GameWorld.score.addScore(500, (Block) block, Score.PIPE_CONNECT, false);
        plusPoint.play(AssetLoader.prefs.getFloat("soundVolume"));

        isFinal = true;
    }

    public void resetDone() {
        isDone = false;
    }

    public boolean isDone() {
        return isDone;
    }

    public void stop(){
        isStop = true;
    }

    public boolean isStop(){
        return isStop;
    }

    public void resume(){
        isStop = false;
    }

    public void setClear(boolean bool){
        isFinal = bool;
    }

    public float getSpeedLimit(){
        return speedLimit;
    }

    public float getSpeedIncrease(){
        return speedIncrease;
    }

    public void setSpeed(float newSpeed){
        if (newSpeed < speedLimit){
            speed = newSpeed;
        }
    }

    public void skip(){
        speed = 400f;
    }

    public float getSpeed(){
        return speed;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = batch.getColor();
        batch.setColor(this.WATER_COLOR);

        batch.draw(waterCircle, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

        for (int i = 0; i < (int) current; i++) {
            try {
                batch.draw(waterCircle, animatingPath.get(i).x - WATER_WIDTH / 2,
                        animatingPath.get(i).y - WATER_WIDTH / 2, WATER_WIDTH, WATER_WIDTH);
            }
            catch (Exception ignored){
            }
        }

        if (!isStop) {
            current += (Gdx.graphics.getDeltaTime() * speed / PathLoader.straightPathDerAvg * animatingPath.size);
        }

        if (current > animatingPath.size) {
            if (isFinal) {
                stop();
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
                isDone = true;
            }
        }

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
//            batch.draw(texture, getX(), getY(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        }

        batch.setColor(color);
    }
}