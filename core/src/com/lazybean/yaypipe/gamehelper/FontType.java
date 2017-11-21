package com.lazybean.yaypipe.gamehelper;

public enum FontType {
    ANJA_EXTRA_SMALL    ("Anja.ttf", 0.03f), // badge label
    ANJA_SMALL          ("Anja.ttf", 0.05f), // score effect, main menu/highscore, setup/item price
    ANJA_MEDIUM_SMALL   ("Anja.ttf", 0.06f), // setup/difficulty,grid size value label,
    ANJA_MEDIUM         ("Anja.ttf", 0.07f), // setup/difficulty,grid size label
    ANJA_MEDIUM_LARGE   ("Anja.ttf", 0.08f), // pause,quit window/title label
    ANJA_LARGE          ("Anja.ttf", 0.09f), //
    ANJA_EXTRA_LARGE    ("Anja.ttf", 0.121f),// game over window/title label

    ANJA_SHADOW         ("Anja.ttf", 0.07f), // game over window/final score
    ANJA_MESSAGE        ("Anja.ttf", 0.07f), // pop up message

    NOTO_EXTRA_SMALL    ("NotoSans.ttf", 0.03f), // game over window/text labels
    NOTO_SMALL          ("NotoSans.ttf", 0.04f), // pause window/icon labels, setup/"high score" label, statistics/text label
    NOTO_MEDIUM_SMALL   ("NotoSans.ttf", 0.05f), // gameplay/"high" label, setup/highscore value
    NOTO_MEDIUM         ("NotoSans.ttf", 0.06f), // gameplay/"score" label, score value label
    NOTO_MEDIUM_LARGE   ("NotoSans.ttf", 0.07f), // game over window/play time label, setup/purchase window text
    NOTO_LARGE          ("NotoSans.ttf", 0.08f),
    NOTO_EXTRA_LARGE    ("NotoSans.ttf", 0.09f);

    public String fontName;
    public float sizeByScreenWidth;

    FontType(String fontName, float sizeByScreenWidth){
        this.fontName = fontName;
        this.sizeByScreenWidth = sizeByScreenWidth;
    }
}
