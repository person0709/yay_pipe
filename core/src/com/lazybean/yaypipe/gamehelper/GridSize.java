package com.lazybean.yaypipe.gamehelper;

public enum GridSize {
    TUTORIAL(-1, 5, 6),
    TINY(1, 6, 8),
    SMALL(2, 7, 10),
    REGULAR(3, 8, 12),
    LARGE(4, 9, 14),
    EXTRA_LARGE(5, 10, 16);

    public int gridSizeLevel;
    public int x, y;

    GridSize(int gridSizeLevel, int x, int y){
        this.gridSizeLevel = gridSizeLevel;
        this.x = x;
        this.y = y;
    }
}
