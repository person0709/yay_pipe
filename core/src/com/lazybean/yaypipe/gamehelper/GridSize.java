package com.lazybean.yaypipe.gamehelper;

public enum GridSize {
    TUTORIAL(5, 6),
    TINY(6, 8),
    SMALL(7, 10),
    REGULAR(8, 12),
    LARGE(9, 14),
    EXTRA_LARGE(10, 16);

    public int x, y;

    GridSize(int x, int y){
        this.x = x;
        this.y = y;
    }
}
