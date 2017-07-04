package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class CoinBank {
    private int balance;
    private int currentBalance;

    public CoinBank(){
        balance = 0;
    }

    public void addBalance(int coin){
        currentBalance = balance;
        this.balance += coin;
    }

    public int getBalance(){
        return balance;
    }

    public void update(int step){
        if (currentBalance < balance){
            if (currentBalance + step < balance){
                currentBalance += step;
            }
            else{
                currentBalance = balance;
            }
        }
        if (currentBalance > balance){
            if (currentBalance - step > balance){
                currentBalance -= step;
            }
            else{
                currentBalance = balance;
            }
        }
    }

    public int getCurrentBalance(){
        return currentBalance;
    }

    public void saveData(){
        FileHandle file = Gdx.files.local("CoinBank.json");
        Json json = new Json();
        String output = json.toJson(this);
        file.writeString(output, false);
    }

}
