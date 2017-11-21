package com.lazybean.yaypipe.gamehelper;

import com.badlogic.gdx.utils.Array;

import static com.lazybean.yaypipe.gamehelper.Direction.TO_EAST;
import static com.lazybean.yaypipe.gamehelper.Direction.TO_NORTH;
import static com.lazybean.yaypipe.gamehelper.Direction.TO_SOUTH;
import static com.lazybean.yaypipe.gamehelper.Direction.TO_WEST;

public enum PipeType {
    TOP_BOTTOM(TO_NORTH, TO_SOUTH),
    LEFT_RIGHT(TO_EAST, TO_WEST),
    LEFT_TOP(TO_WEST, TO_NORTH),
    LEFT_BOTTOM(TO_WEST, TO_SOUTH),
    RIGHT_TOP(TO_EAST, TO_NORTH),
    RIGHT_BOTTOM(TO_EAST, TO_SOUTH),
    ALL_DIRECTION(TO_SOUTH, TO_EAST, TO_NORTH, TO_WEST),
    BOTTOM_END(TO_NORTH), //pipe that ends on the bottom end (U shaped)
    LEFT_END(TO_EAST), // C shaped
    TOP_END(TO_SOUTH),
    RIGHT_END(TO_WEST);

    /**
     * the direction the pipe's open ends are facing towards
     */
    private Array<Direction> openEnds;

    PipeType(Direction... openEnds){
        this.openEnds = new Array<>(openEnds);
    }

    public Array<Direction> getOpenEnds() {
        return openEnds;
    }
}
