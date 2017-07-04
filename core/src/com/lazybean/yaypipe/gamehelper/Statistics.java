package com.lazybean.yaypipe.gamehelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.lazybean.yaypipe.gameobjects.Block;

import java.util.HashMap;

public class Statistics extends HashMap<String, Integer>{
    private long totalStartTime;
    private long stageTimeSum = 0;
    private boolean isTimerOn = false;

    private int undoCount = 0;
    private int pipeChangeCount = 0;
    private int crossPipeUse = 0;

    public Statistics(){
        put("totalPlayTime", 0);
        put("totalUndoPerformed",0);
        put("totalRestartPerformed",0);
        put("totalTryNumber", 0);
        put("totalFailNumber", 0);
        put("totalPipePlaced",0);

        put("easyBestTime",0);
        put("normalBestTime",0);
        put("hardBestTime",0);
        put("professionalBestTime",0);
        put("masterBestTime",0);

        put("easyCleared",0);
        put("normalCleared",0);
        put("hardCleared",0);
        put("professionalCleared",0);
        put("masterCleared",0);

        put("easyHighScore",0);
        put("normalHighScore",0);
        put("hardHighScore",0);
        put("professionalHighScore",0);
        put("masterHighScore",0);

        put("mostPipeConnected",0);
        put("mostPipeChanged",0);
        put("mostUndoPerformed", 0);

        put("pipe1Used",0);
        put("pipe2Used",0);
        put("pipe3Used",0);
        put("pipe4Used",0);
        put("pipe5Used",0);
        put("pipe6Used",0);
        put("pipe7Used",0);
        put("pipe7UsedFull", 0);
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
        long timeSum = get("totalPlayTime");
        timeSum += (System.currentTimeMillis() - totalStartTime)/1000;
        put("totalPlayTime", (int) timeSum);

        stageTimeSum += System.currentTimeMillis() - totalStartTime;
        isTimerOn = false;
    }

    public void reset(){
        stageTimeSum = 0;
        undoCount = 0;
        pipeChangeCount = 0;
        crossPipeUse = 0;
    }

    public int getStageTimeSum(){
        return (int) stageTimeSum/1000;
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

    public int getCrossPipeUse() {
        return crossPipeUse;
    }

    public void addCrossPipeUse() {
        crossPipeUse++;
    }

    public void checkBestTime() {
        String key = "";
        switch (AssetLoader.prefs.getInteger("difficulty")) {
            case Difficulty.EASY:
                key = "easyBestTime";
                break;

            case Difficulty.NORMAL:
                key = "normalBestTime";
                break;

            case Difficulty.HARD:
                key = "hardBestTime";
                break;

            case Difficulty.EXTREME:
                key = "professionalBestTime";
                break;

            case Difficulty.MASTER:
                key = "masterBestTime";
                break;
        }

        if (containsKey(key)) {
            if (get(key) > getStageTimeSum() || get(key) == 0) {
                put(key, getStageTimeSum());
            }
        }
    }

    public void checkHighScore(int highScore) {
        String key = "";
        switch (AssetLoader.prefs.getInteger("difficulty")) {
            case Difficulty.EASY:
                key = "easyHighScore";
                break;

            case Difficulty.NORMAL:
                key = "normalHighScore";
                break;

            case Difficulty.HARD:
                key = "hardHighScore";
                break;

            case Difficulty.EXTREME:
                key = "professionalHighScore";
                break;

            case Difficulty.MASTER:
                key = "masterHighScore";
                break;
        }

        if (containsKey(key)) {
            if (get(key) < highScore) {
                put(key, highScore);
            }
        }
    }

    public boolean isTimerOn(){
        return isTimerOn;
    }

    public void incrementValue(String key) {
        if (containsKey(key)) {
            put(key, get(key) + 1);
        }
    }

    public void incrementClear(){
        String key = "";
        switch (AssetLoader.prefs.getInteger("difficulty")){
            case Difficulty.EASY:
                key = "easyCleared";
                break;

            case Difficulty.NORMAL:
                key = "normalCleared";
                break;

            case Difficulty.HARD:
                key = "hardCleared";
                break;

            case Difficulty.EXTREME:
                key = "professionalCleared";
                break;

            case Difficulty.MASTER:
                key = "masterCleared";
                break;
        }
        incrementValue(key);
    }

    public void countPipeUse(Array<Array<Block>> array){
        int pipeConnectCount = 0;

        for (int i = 0; i < array.size; i++){
            for (int j = 0; j < array.get(i).size; j++){
                //most pipe connected count
                if (array.get(i).get(j).getFlowCount() > 0){
                    pipeConnectCount++;

                    int num = array.get(i).get(j).getPipeImage();
                    if (1 <= num && num <= 7) {
                        String key = "pipe" + String.valueOf(num) + "Used";
                        incrementValue(key);
                    }
                }
                if (array.get(i).get(j).getFlowCount() == 2){
                    incrementValue("pipe7UsedFull");
                }
            }
        }

        if (get("mostPipeConnected") < pipeConnectCount) {
            put("mostPipeConnected", pipeConnectCount);
        }
    }

    public void checkCount() {
        if (undoCount > get("mostUndoPerformed")){
            put("mostUndoPerformed", undoCount);
        }

        if (pipeChangeCount > get("mostPipeChanged")){
            put("mostPipeChanged", pipeChangeCount);
        }
    }

    public void saveData(){
        FileHandle file = Gdx.files.local("Stats.json");
        Json json = new Json();
        String output = json.toJson(this);
        file.writeString(output, false);
    }
}
