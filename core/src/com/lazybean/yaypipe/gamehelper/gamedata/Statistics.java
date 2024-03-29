package com.lazybean.yaypipe.gamehelper.gamedata;

import com.badlogic.gdx.utils.Array;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.*;
import com.lazybean.yaypipe.gameobjects.GridBlock;

import java.util.HashMap;

import static com.lazybean.yaypipe.gamehelper.StatisticsType.*;

public class Statistics {
    private HashMap<String, Integer> hashMap;

    private int undoCount = 0;
    private int pipeChangeCount = 0;
    private int fullAllDirectionCount = 0;

    public Statistics(){
        hashMap = new HashMap<>();
        Array<StatisticsType> keys = new Array<>(StatisticsType.values());

        for (StatisticsType item : keys){
            hashMap.put(item.name(), 0);
        }
    }

    public int get(StatisticsType statisticsType){
        return hashMap.get(statisticsType.name());
    }

    private void put(StatisticsType statisticsType, int value){
        hashMap.put(statisticsType.name(), value);
    }

    public void reset(){
        undoCount = 0;
        pipeChangeCount = 0;
        fullAllDirectionCount = 0;
    }

    public void addPlayTime(float time) {
        long timeSum = get(TOTAL_PLAYTIME);
        put(TOTAL_PLAYTIME, (int)(timeSum + time));
    }

    public void checkBestTime(Difficulty difficulty, float time) {
        StatisticsType key;
        switch (difficulty) {
            case EASY:
                key = BEST_TIME_EASY;
                break;

            case NORMAL:
                key = BEST_TIME_NORMAL;
                break;

            case HARD:
                key = BEST_TIME_HARD;
                break;

            case PROFESSIONAL:
                key = BEST_TIME_PRO;
                break;

            case MASTER:
                key = BEST_TIME_MASTER;
                break;

            default:
                return;
        }

        if (get(key) > time || get(key) == 0) {
            put(key, Math.round(time));
        }
    }

    public void checkHighScore(Difficulty difficulty, int highScore) {
        //score achievement
        if (highScore >= 3000){
            GameData.getInstance().setUnlock(Difficulty.NORMAL);
            YayPipe.playService.unlockAchievement(AchievementType.NOVICE_PLUMBER);
        }
        if (highScore >= 5000){
            GameData.getInstance().setUnlock(Difficulty.HARD);
            YayPipe.playService.unlockAchievement(AchievementType.MEDIOCRE_PLUMBER);
        }
        if (highScore >= 10000){
            GameData.getInstance().setUnlock(Difficulty.PROFESSIONAL);
            YayPipe.playService.unlockAchievement(AchievementType.PROFESSIONAL_PLUMBER);
        }
        if (highScore >= 25000){
            GameData.getInstance().setUnlock(Difficulty.MASTER);
            YayPipe.playService.unlockAchievement(AchievementType.EXPERT_PLUMBER);
        }
        if (highScore >= 50000){
            YayPipe.playService.unlockAchievement(AchievementType.MASTER_PLUMBER);
        }

        StatisticsType key;
        switch (difficulty) {
            case EASY:
                key = HIGHSCORE_EASY;
                break;

            case NORMAL:
                key = HIGHSCORE_NORMAL;
                break;

            case HARD:
                key = HIGHSCORE_HARD;
                break;

            case PROFESSIONAL:
                key = HIGHSCORE_PRO;
                break;

            case MASTER:
                key = HIGHSCORE_MASTER;
                break;

            default:
                return;
        }

        if (get(key) < highScore) {
            put(key, highScore);

            if (get(HIGHSCORE_ALL) < highScore){
                put(HIGHSCORE_ALL, highScore);
            }
        }
    }

    public void incrementValue(StatisticsType key, int count) {
        if (hashMap.containsKey(key.name())) {
            put(key, get(key) + count);
        }
    }

    public void incrementClear(Difficulty difficulty){
        switch (difficulty){
            case EASY:
                YayPipe.playService.unlockAchievement(AchievementType.TOO_EASY);
                incrementValue(CLEAR_EASY, 1);
                break;

            case NORMAL:
                YayPipe.playService.unlockAchievement(AchievementType.START_PLUMBING);
                incrementValue(CLEAR_NORMAL, 1);
                break;

            case HARD:
                YayPipe.playService.unlockAchievement(AchievementType.NOT_HARD_ENOUGH);
                incrementValue(CLEAR_HARD, 1);
                break;

            case PROFESSIONAL:
                YayPipe.playService.unlockAchievement(AchievementType.PIPING_HOT);
                incrementValue(CLEAR_PRO, 1);
                break;

            case MASTER:
                YayPipe.playService.unlockAchievement(AchievementType.MARIO_WILL_BE_PROUD);
                incrementValue(CLEAR_MASTER, 1);
                break;
        }
    }

    public void countPipeUse(Array<Array<GridBlock>> array){
        int totalPipeConnectCount = 0;
        int topBottomCount = 0;
        int leftRightCount = 0;
        int leftTopCount = 0;
        int leftBottomCount = 0;
        int rightTopCount = 0;
        int rightBottomCount = 0;
        int allDirectionCount = 0;

        for (int i = 0; i < array.size; i++){
            for (int j = 0; j < array.get(i).size; j++) {
                //most pipe connected count
                if (array.get(i).get(j).getFlowCount() > 0) {
                    totalPipeConnectCount++;

                    switch (array.get(i).get(j).getPipe().getType()) {
                        case TOP_BOTTOM:
                            topBottomCount++;
                            break;

                        case LEFT_RIGHT:
                            leftRightCount++;
                            break;

                        case LEFT_TOP:
                            leftTopCount++;
                            break;

                        case LEFT_BOTTOM:
                            leftBottomCount++;
                            break;

                        case RIGHT_TOP:
                            rightTopCount++;
                            break;

                        case RIGHT_BOTTOM:
                            rightBottomCount++;
                            break;

                        case ALL_DIRECTION:
                            allDirectionCount++;
                            if (array.get(i).get(j).getFlowCount() == 2) {
                                fullAllDirectionCount++;
                            }
                            break;

                    }
                }
            }
        }

        incrementValue(PIPE_TOP_BOTTOM_USE, topBottomCount);
        incrementValue(PIPE_LEFT_BOTTOM_USE, leftBottomCount);
        incrementValue(PIPE_LEFT_RIGHT_USE, leftRightCount);
        incrementValue(PIPE_LEFT_TOP_USE, leftTopCount);
        incrementValue(PIPE_RIGHT_BOTTOM_USE, rightBottomCount);
        incrementValue(PIPE_RIGHT_TOP_USE, rightTopCount);
        incrementValue(PIPE_ALL_DIRECTION_USE, allDirectionCount);
        incrementValue(PIPE_ALL_DIRECTION_FULL_USE, fullAllDirectionCount);

        if (fullAllDirectionCount > 0) {
            YayPipe.playService.incrementAchievement(AchievementType.BE_WATER_MY_FRIEND, fullAllDirectionCount);
        }

        if (get(MOST_PIPE_CONNECTED) < totalPipeConnectCount) {
            put(MOST_PIPE_CONNECTED, totalPipeConnectCount);
        }
    }

    public void checkCount() {
        if (undoCount > get(MOST_UNDO)){
            put(MOST_UNDO, undoCount);
        }

        if (pipeChangeCount > get(MOST_PIPE_CHANGED)){
            put(MOST_PIPE_CHANGED, pipeChangeCount);
        }
    }

    public int getUndoCount() {
        return undoCount;
    }

    public void addUndoCount() {
        undoCount++;
    }

    public int getPipeChangeCount() {
        return pipeChangeCount;
    }

    public void addPipeChangeCount() {
        pipeChangeCount++;
    }

    public void subPipeChangeCount(){
        pipeChangeCount--;
    }

    public int getFullPipeCount(){
        return fullAllDirectionCount;
    }
}
