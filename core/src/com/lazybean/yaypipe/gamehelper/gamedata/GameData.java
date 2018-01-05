package com.lazybean.yaypipe.gamehelper.gamedata;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.Difficulty;
import com.lazybean.yaypipe.gamehelper.GridSize;

import java.util.HashMap;
import java.util.Map;

public class GameData {
    private Map<String, Object> saveData;
    public Statistics statistics;

    private static final GameData instance = new GameData();

    public static GameData getInstance() {
        return instance;
    }

    private Preferences preferences;

    //Blank game data
    private GameData() {
        preferences = Gdx.app.getPreferences("YayPipe");
    }

    /**
     * set everything to initial values for a new game data
     */
    public void init(){
        preferences.putBoolean("isFirstRun", false);

        for (Difficulty difficulty : Difficulty.values()){
            preferences.putBoolean(difficulty.name(), false);
            for (GridSize gridSize : GridSize.values()){
                preferences.putBoolean(difficulty.name() + gridSize.name(), false);
            }
        }

        preferences.putBoolean(Difficulty.EASY.name(), true);
        preferences.putBoolean(Difficulty.EASY.name() + GridSize.TINY, true);

        preferences.putInteger("coin", 0);
        preferences.putInteger("wandStock", 0);
        preferences.putInteger("snailStock", 0);
        preferences.putBoolean("sound", true);
        preferences.putFloat("soundVolume", 1f);
        preferences.flush();

        statistics = new Statistics();
    }

    public boolean isSoundOn(){
        return preferences.getBoolean("sound");
    }

    public void setSoundOn(boolean sound){
        preferences.putBoolean("sound", sound);
        preferences.flush();
    }

    public void setSoundVolume(float volume){
        preferences.putFloat("soundVolume", volume);
        preferences.flush();
    }

    public float getSoundVolume(){
        if (isSoundOn()){
            return preferences.getFloat("soundVolume");
        } else {
            return 0;
        }
    }

    public int getWandStock(){
        return preferences.getInteger("wandStock");
    }

    public void setWandStock(int wandStock) {
        preferences.putInteger("wandStock", wandStock);
        preferences.flush();
    }

    public int getSnailStock(){
        return preferences.getInteger("snailStock");
    }

    public void setSnailStock(int snailStock) {
        preferences.putInteger("snailStock", snailStock);
        preferences.flush();
    }

    public int getCoin(){
        return preferences.getInteger("coin");
    }

    public void addCoin(int coin){
        int balance = getCoin() + coin;
        preferences.putInteger("coin", balance);
        preferences.flush();
    }

    public void setUnlock(Difficulty difficulty, GridSize gridSize){
        preferences.putBoolean(difficulty.name() + gridSize.name(), true);
        preferences.flush();
    }

    public void setUnlock(Difficulty difficulty){
        preferences.putBoolean(difficulty.name(), true);
        preferences.flush();
    }

    public boolean isUnlocked(Difficulty difficulty, GridSize gridSize){
        return preferences.getBoolean(difficulty.name() + gridSize.name());
    }

    public boolean isUnlocked(Difficulty difficulty){
        return preferences.getBoolean(difficulty.name());
    }

    public void saveLocal(){
        if (statistics == null){
            return;
        }
        Json json = new Json();
        preferences.putString("statistics", json.toJson(statistics, Statistics.class));
        preferences.flush();
    }

    public void saveLocalToCloud(){
        YayPipe.androidHelper.showProgressBar();

        if (YayPipe.playService.isConnectedToInternet()) {
            Json json = new Json();
            saveData.put("coin", preferences.getInteger("coin"));
            saveData.put("wandStock", preferences.getInteger("wandStock"));
            saveData.put("snailStock", preferences.getInteger("snailStock"));
            saveData.put("statistics", preferences.getString("statistics"));

            for (Difficulty difficulty : Difficulty.values()) {
                String key = difficulty.name();
                saveData.put(key, preferences.getBoolean(key));
                for (GridSize gridSize : GridSize.values()) {
                    String key2 = key + gridSize.name();
                    saveData.put(key2, preferences.getBoolean(key2));
                }
            }

            YayPipe.playService.saveToSnapshot(json.toJson(saveData));
        }
    }

    public int loadFromCloud(){
        // if connected to internet, try loading from cloud
        if (YayPipe.playService.isConnectedToInternet()) {
            Json json = new Json();
            byte[] data = YayPipe.playService.loadFromSnapshot();
            //when there is no cloud data, abort
            if (data == null || data.length == 0) {
                return -1;
            }
            // when there is cloud data,
            else {
                saveData = json.fromJson(HashMap.class, new String(data));

                copyCloudToLocal();

                statistics = json.fromJson(Statistics.class, preferences.getString("statistics"));
                return 0;
            }
        }
        //when not connected to internet
        else {
            return -2;
        }
    }

    public void loadFromLocal(){
        Json json = new Json();
        saveData = new HashMap<>();

        // when it's first time running the app
        if (preferences.getBoolean("isFirstRun", true)) {
            init();
        } else {
            // load statistics from preference

            String stat = preferences.getString("statistics");
            if (stat.length() == 0) {
                statistics = new Statistics();
            } else {
                statistics = json.fromJson(Statistics.class, preferences.getString("statistics"));
            }
        }
    }

    private void copyCloudToLocal(){
        preferences.putInteger("coin", (Integer) saveData.get("coin"));
        preferences.putInteger("wandStock", (Integer) saveData.get("wandStock"));
        preferences.putInteger("snailStock", (Integer) saveData.get("snailStock"));
        preferences.putString("statistics", (String) saveData.get("statistics"));

        for (Difficulty difficulty : Difficulty.values()){
            String key = difficulty.name();
            preferences.putBoolean(key, (Boolean) saveData.get(key));
            for (GridSize gridSize : GridSize.values()){
                String key2 = key + gridSize.name();
                preferences.putBoolean(key2, (Boolean) saveData.get(key2));
            }
        }

        preferences.flush();
    }
}
