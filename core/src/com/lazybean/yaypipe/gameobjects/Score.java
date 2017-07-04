package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.math.MathUtils;
import com.lazybean.yaypipe.gamehelper.AssetLoader;

public class Score{
    static final int PIPE_CONNECT = 0;
    static final int STOP_PASS = 1;
    static final int PIPE_CHANGE = 2;

    private int pipeConnectScore = 0;
    private int stopPassScore = 0;
    private int pipeChangeScore = 0;

    private int totalScore;
    private int currentScore;
    private int highScore;
    private int undoScore;
    private int undoType;

    private boolean isScoreChanged = false;
    private Block effectBlock;
    private int scoreChange = 0;

    private int stepChange=1;

    public Score(){
        highScore = AssetLoader.prefs.getInteger("highScore");
       // highScore = 10000;
        totalScore = 0;
        currentScore = 0;
    }

    public void checkHighScore(){
        if (totalScore > highScore) {
            AssetLoader.prefs.putInteger("highScore", totalScore);
            AssetLoader.prefs.flush();
        }
    }

    public boolean isHighScore(){
        return totalScore > highScore;
    }

    public synchronized void addScore(int points, Block block, int type, boolean undo){
        if (undo) {
            undoScore = -points;
            undoType = type;
        }

        switch (type){
            case PIPE_CONNECT:
                pipeConnectScore += points;
                break;

            case STOP_PASS:
                stopPassScore += points;
                break;

            case PIPE_CHANGE:
                pipeChangeScore += points;
        }

        currentScore = totalScore;
        totalScore += points;
        isScoreChanged = true;
        effectBlock = block;
        scoreChange = points;
    }

    public synchronized void undoScore(Block block) {
        currentScore = totalScore;
        totalScore += undoScore;

        switch (undoType){
            case PIPE_CONNECT:
                pipeConnectScore += undoScore;
                break;

            case PIPE_CHANGE:
                pipeChangeScore += undoScore;
                AssetLoader.stats.subPipeChangeCount();
        }

        isScoreChanged = true;
        effectBlock = block;
        scoreChange = undoScore;
    }

    public synchronized void update(int step, boolean accelerate){
        if (accelerate){
            stepChange++;
            step = step + stepChange;
        }

        if (currentScore < totalScore){
            if (currentScore + step < totalScore){
                currentScore += step;
            }
            else{
                currentScore = totalScore;
            }
        }
        if (currentScore > totalScore){
            if (currentScore - step > totalScore) {
                currentScore -= step;
            }
            else {
                currentScore = totalScore;
            }
        }
    }

    public synchronized int getCurrentScore(){
        return currentScore;
    }

    public void applyMultiplier(float multiplier){
        totalScore = MathUtils.round(totalScore * multiplier);
    }

    public boolean isScoreChanged(){
        return isScoreChanged;
    }

    public int getHighScore(){return highScore;}

    public int getTotalScore(){
        return totalScore;
    }

    public int getPipeConnectScore() {
        return pipeConnectScore;
    }

    public int getStopPassScore() {
        return stopPassScore;
    }

    public int getPipeChangeScore() {
        return pipeChangeScore;
    }

    public void reset(){
        currentScore = 0;
    }

    public void skip(){
        currentScore = totalScore;
    }

    public Block getEffectBlock(){
        return effectBlock;
    }

    public int getScoreChange(){
        return scoreChange;
    }

    public void setScoreChange(boolean bool){
        isScoreChanged = bool;
    }
}
