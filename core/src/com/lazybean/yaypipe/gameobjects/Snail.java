package com.lazybean.yaypipe.gameobjects;

public class Snail extends Item{
    public static final float WATER_SPEED = 5f;
    public static final int MAX_STOCK = 1;
    public static final int PRICE = 500;
    public static final float ACTIVE_TIME = 5f;

    private boolean isActive = false;

    public Snail(int stock){
        super(stock);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
