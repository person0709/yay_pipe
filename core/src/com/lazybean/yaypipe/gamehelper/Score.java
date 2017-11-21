package com.lazybean.yaypipe.gamehelper;

import com.badlogic.gdx.math.MathUtils;
import com.lazybean.yaypipe.gamehelper.gamedata.GameData;
import com.lazybean.yaypipe.gameobjects.GridBlock;

public class Score
{
    public enum ScoreType {PIPE_PLACE, PIPE_CONNECT, STOP_PASS, PIPE_CHANGE, FINISH}

    private static final int PIPE_PLACE_POINT = 5;
    private static final int PIPE_CHANGE_PENALTY = -30;
    private static final float PIPE_CHANGE_PENALTY_MULTIPLIER = 1.2f;
    private static final int PIPE_CONNECT_POINT = 100;
    private static final int STOP_PASS_INITIAL_POINT = 1000;
    private static final int STOP_PASS_ADDITION = 1000;
    private static final int FINISH_BONUS = 500;

    private int pipeConnectScore = 0;
    private int stopPassScore = 0;
    private int stopPassCount = 0;
    private int pipeChangeScore = 0;

    private int totalScore;
    private int currentScore;
    private int undoScore;
    private ScoreType undoType;

    private int scoreChange = 0;

    private int stepChange=1;

    public Score(){
        totalScore = 0;
        currentScore = 0;
    }

    public synchronized void addPoints(GridBlock block, ScoreType scoreType){
        currentScore = totalScore;

        int points = 0;

        switch (scoreType){
            case PIPE_PLACE:
                points = PIPE_PLACE_POINT;
                undoType = scoreType;
                break;

            case PIPE_CONNECT:
                points = PIPE_CONNECT_POINT;
                pipeConnectScore += points;
                break;

            case STOP_PASS:
                points = STOP_PASS_INITIAL_POINT + STOP_PASS_ADDITION * stopPassCount;
                stopPassScore += points;
                stopPassCount++;
                break;

            case PIPE_CHANGE:
                points = PIPE_CHANGE_PENALTY;
                for (int i = 0; i < block.getPipeChangeCount(); i++){
                    points *= PIPE_CHANGE_PENALTY_MULTIPLIER;
                }
                pipeChangeScore += points;
                undoType = scoreType;
                break;

            case FINISH:
                points = FINISH_BONUS;
                pipeConnectScore += points;
                break;
        }

        undoScore = -points;
        totalScore += points;
        scoreChange = points;
    }

    public synchronized void undoScore() {
        currentScore = totalScore;
        totalScore += undoScore;

        switch (undoType){
            case PIPE_CONNECT:
                pipeConnectScore += undoScore;
                break;

            case PIPE_CHANGE:
                pipeChangeScore += undoScore;
                break;
        }

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

    public void applyMultiplier(){
        totalScore = MathUtils.round(totalScore * (float) (1.0 + 0.1 * GameData.getInstance().statistics.getFullPipeCount()));
    }

    public synchronized int getCurrentScore(){
        return currentScore;
    }

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

    public int getScoreChange(){
        return scoreChange;
    }
}
