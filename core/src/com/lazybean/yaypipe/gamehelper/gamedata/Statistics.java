package com.lazybean.yaypipe.gamehelper.gamedata;

import com.badlogic.gdx.utils.Array;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.*;
import com.lazybean.yaypipe.gameobjects.GridBlock;

import java.util.HashMap;

import static com.lazybean.yaypipe.gamehelper.StatisticsType.*;

public class Statistics {
    private HashMap<String, Integer> hashMap;

    private long totalStartTime;
    private long stageTimeSum = 0;
    private boolean isTimerOn = false;

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

    public String convertSecondToHour(int time){
        String second = String.valueOf(time % 60);
        if (second.length() == 1){
            second = "0" + second;
        }
        String minute = String.valueOf((time / 60) % 60);
        if (minute.length() == 1){
            minute = "0" + minute;
        }
        String hour = String.valueOf(time / 3600);
        if (hour.length() == 1){
            hour = "0" + hour;
        }

        String string = String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(second);

        return string;
    }

    public String convertSecondToMinute(int time){
        String second = String.valueOf(time % 60);
        if (second.length() == 1){
            second = "0" + second;
        }
        String minute = String.valueOf((time / 60) % 60);

        String string = String.valueOf(minute) + ":" + String.valueOf(second);

        return string;
    }

    public void startTimer(){
        totalStartTime = System.currentTimeMillis();
        isTimerOn = true;
    }

    public void stopTimer(){
        long timeSum = get(TOTAL_PLAYTIME);
        timeSum += (System.currentTimeMillis() - totalStartTime)/1000;
        put(TOTAL_PLAYTIME, (int) timeSum);

        stageTimeSum += System.currentTimeMillis() - totalStartTime;
        isTimerOn = false;
    }

    public void reset(){
        stageTimeSum = 0;
        undoCount = 0;
        pipeChangeCount = 0;
        fullAllDirectionCount = 0;
    }

    public int getStageTimeSum(){
        return (int) stageTimeSum/1000;
    }

    public void checkBestTime(Difficulty difficulty) {
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

            case EXTREME:
                key = BEST_TIME_EXTREME;
                break;

            case MASTER:
                key = BEST_TIME_MASTER;
                break;

            default:
                return;
        }

        if (get(key) > getStageTimeSum() || get(key) == 0) {
            put(key, getStageTimeSum());
        }
    }

    public void checkHighScore(Difficulty difficulty, int highScore) {
        //score achievement
        if (highScore >= 3000){
            GameData.getInstance().unlock.setUnlock(Difficulty.NORMAL, true);
            YayPipe.playService.unlockAchievement(AchievementType.NOVICE_PLUMBER);
        }
        if (highScore >= 5000){
            GameData.getInstance().unlock.setUnlock(Difficulty.HARD, true);
            YayPipe.playService.unlockAchievement(AchievementType.MEDIOCRE_PLUMBER);
        }
        if (highScore >= 10000){
            GameData.getInstance().unlock.setUnlock(Difficulty.EXTREME, true);
            YayPipe.playService.unlockAchievement(AchievementType.PROFESSIONAL_PLUMBER);
        }
        if (highScore >= 25000){
            GameData.getInstance().unlock.setUnlock(Difficulty.MASTER, true);
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

            case EXTREME:
                key = HIGHSCORE_EXTREME;
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

    public boolean isTimerOn(){
        return isTimerOn;
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

            case EXTREME:
                YayPipe.playService.unlockAchievement(AchievementType.PIPING_HOT);
                incrementValue(CLEAR_EXTREME, 1);
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

        YayPipe.playService.incrementAchievement(AchievementType.BE_WATER_MY_FRIEND, fullAllDirectionCount);


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
