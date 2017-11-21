package com.lazybean.yaypipe.gamehelper.gamedata;

import com.lazybean.yaypipe.gamehelper.Difficulty;
import com.lazybean.yaypipe.gamehelper.GridSize;

import java.util.HashMap;

public class Unlock extends HashMap<String, Boolean>{
    public Unlock(){
        for (Difficulty difficulty : Difficulty.values()){
            put(difficulty.name(), false);
            for (GridSize gridSize : GridSize.values()){
                put(difficulty.name() + gridSize.name(), false);
            }
        }

        put(Difficulty.EASY.name(), true);
        put(Difficulty.EASY.name() + GridSize.TINY, true);


    }

    public void setUnlock(Difficulty difficulty, GridSize gridSize, boolean unlock){
        put(difficulty.name() + gridSize.name(), unlock);
    }

    public void setUnlock(Difficulty difficulty, boolean unlock){
        put(difficulty.name(), unlock);
    }

    public boolean isUnlocked(Difficulty difficulty, GridSize gridSize){
        return get(difficulty.name() + gridSize.name());
    }

    public boolean isUnlocked(Difficulty difficulty){
        return get(difficulty.name());
    }
}
