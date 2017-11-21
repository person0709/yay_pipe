package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.lazybean.yaypipe.YayPipe;

public class PageScrollPane extends ScrollPane {
    private Table content;


    public PageScrollPane() {
        super(null);

        content = new Table();
        super.setActor(content);

        clearListeners();
    }

    public void addPage(Actor page){
        content.add(page).height(YayPipe.SCREEN_HEIGHT * 0.3f).width(YayPipe.SCREEN_WIDTH);
    }

    public void firstPage() {
        setSmoothScrolling(false);
        scrollX(0);
    }

    public void nextPage(){
        setSmoothScrolling(true);
        setScrollX(getScrollX() + YayPipe.SCREEN_WIDTH);
    }

    public void backPage(){
        setSmoothScrolling(true);
        setScrollX(getScrollX() - YayPipe.SCREEN_WIDTH);
    }
}
