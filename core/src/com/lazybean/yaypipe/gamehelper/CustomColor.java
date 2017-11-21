package com.lazybean.yaypipe.gamehelper;

import com.badlogic.gdx.graphics.Color;

public enum CustomColor {
    YELLOW (255/255f, 228/255f, 0, 1f),
    TURQUOISE (6/255f, 169/255f, 169/255f, 1f),
    INDIGO (0/255f, 63/255f, 107/255f, 1f),
    BLUEGREY (153/255f, 204/255f, 204/255f, 1f),
    RED (236/255f, 0f, 72/255f, 1f),
    PINK (255/255f, 51/255f, 102/255f, 1f),
    BLUE (0f, 154/255f, 202/255f, 1f),
    GREEN (51/255f, 153/255f, 51/255f, 1f),
    ORANGE (255/255f, 102/255f, 0f, 1f),
    PURPLE (102/255f, 51/255f, 206/255f, 1f);

    public float r,g,b,a;

    CustomColor(float r, float g, float b, float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color getColor(){
        return new Color(r, g, b, a);
    }
}
