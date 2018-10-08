package com.lazybean.yaypipe.guiobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.lazybean.yaypipe.YayPipe;

public class Background extends Actor {
    private Texture background;

    public Background() {
        Pixmap pixmap = new Pixmap(1,1, Pixmap.Format.RGBA4444);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        background = new Texture(pixmap);

        pixmap.dispose();

        setBounds(0,0, YayPipe.SCREEN_WIDTH, YayPipe.SCREEN_HEIGHT);
        setTouchable(Touchable.disabled);
    }

    public void setAlpha(float alpha){
        Color color = new Color(getColor());
        setColor(color.r, color.g, color.b, alpha);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(background, getX(), getY(), getWidth(), getHeight());

        batch.setColor(Color.WHITE);
    }
}
