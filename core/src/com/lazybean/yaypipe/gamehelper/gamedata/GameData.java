package com.lazybean.yaypipe.gamehelper.gamedata;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

public class GameData {
    public Unlock unlock;
    public CoinBank coinBank;
    public Statistics statistics;

    private static final GameData instance = new GameData();

    private Preferences preferences;

    public static GameData getInstance() {
        return instance;
    }

    private GameData() {
        preferences = Gdx.app.getPreferences("YayPipe");
        if (preferences.getBoolean("isFirstRun", true)){
            unlock = new Unlock();
            coinBank = new CoinBank();
            statistics = new Statistics();

            preferences.putBoolean("sound", true);
            preferences.putFloat("soundVolume", 1f);
            preferences.putBoolean("itemUnlocked", false);
            preferences.putInteger("wandStock", 0);
            preferences.putInteger("snailStock", 0);
            preferences.putBoolean("isFirstRun", false);
            preferences.flush();
        }
        else{
            Json json = new Json();
            unlock = json.fromJson(Unlock.class, preferences.getString("unlock"));
            coinBank = json.fromJson(CoinBank.class, preferences.getString("coinBank"));
            statistics = json.fromJson(Statistics.class, preferences.getString("statistics"));
        }

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
    }

    public int getSnailStock(){
        return preferences.getInteger("snailStock");
    }

    public void setSnailStock(int snailStock) {
        preferences.putInteger("snailStock", snailStock);
    }

    public void saveData(){
        Json json = new Json();
        preferences.putString("unlock", json.toJson(unlock, Unlock.class));
        preferences.putString("coinBank", json.toJson(coinBank, CoinBank.class));
        preferences.putString("statistics", json.toJson(statistics, Statistics.class));
        preferences.flush();
    }
}
