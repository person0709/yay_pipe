package com.lazybean.yaypipe.gameobjects;

import com.lazybean.yaypipe.gamehelper.gamedata.GameData;

public abstract class Item {
    protected int stock;
    private boolean isAvailable = false;

    public Item(int stock){
        setStock(stock);
    }

    public int getStock(){
        return stock;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setStock(int stock){
        this.stock = stock;

        if (this instanceof Wand){
            GameData.getInstance().setWandStock(stock);
        } else if (this instanceof Snail){
            GameData.getInstance().setSnailStock(stock);
        }

        isAvailable = this.stock > 0;
    }
}
