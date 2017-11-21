package com.lazybean.yaypipe.gamehelper;

import com.badlogic.gdx.math.Vector2;

/**
 * y-up coordinate system
 */
public enum Direction {

    TO_EAST(1,0),
    TO_WEST(-1,0),
    TO_SOUTH(0,-1),
    TO_NORTH(0,1);

    static{
        TO_EAST.opposite = TO_WEST;
        TO_WEST.opposite = TO_EAST;
        TO_SOUTH.opposite = TO_NORTH;
        TO_NORTH.opposite = TO_SOUTH;
    }

    public int x, y;
    private Vector2 vector;
    private Direction opposite;

    Direction(int x, int y){
        this.x = x;
        this.y = y;
        vector = new Vector2(x, y);
    }

    public Vector2 getVector() {
        return vector;
    }

    public Direction getOpposite(){
        return opposite;
    }
}
