package com.lazybean.yaypipe.gamehelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gui.Background;


public final class AssetLoader {
    private final String SOUND_DIRECTORY = "sounds/";
    private final String FONT_DIRECTORY = "fonts/";

    public AssetManager manager;
    public BlockFactory blockFactory;

    public Skin uiSkin;
    public TextureAtlas imageAtlas, iconAtlas;

    private TextureRegion pipe_lr, pipe_lb, pipe_lt, pipe_rb, pipe_rt, pipe_tb, pipe_lrtb;
    private TextureRegion pipe_le, pipe_te, pipe_re, pipe_be;

    public TextureRegion gridBlock,redBlock;
    public TextureRegion square, circle;
    public TextureRegion finishIndicator;

    public TextureRegion slider, tapToStart, coin, piggy, hand;

    public TextureRegion y_1, a, y_2, ex_mark, p_1, i, p_2, e;

    public TextureRegion shadow;
    public TextureRegion line;

    public NinePatchDrawable button, window, itemTray;

    public TextureRegion waterDrop;

    public AssetLoader(){
        manager = new AssetManager();
    }

    public void loadManager(){
        manager.load("asset_image.atlas", TextureAtlas.class);
        manager.load("asset_icon.atlas", TextureAtlas.class);
        manager.load("pipe_drop_anim.atlas", TextureAtlas.class);
        manager.load("splash_anim.atlas", TextureAtlas.class);
        manager.load("tutorial.png", Texture.class);

        for (SoundType type : SoundType.values()){
            manager.load(SOUND_DIRECTORY + type.fileName, Sound.class);
        }
    }

    public void assignAssets(){
        Pixmap pixmap = new Pixmap(YayPipe.SCREEN_WIDTH, 10, Pixmap.Format.RGBA4444);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        line = new TextureRegion(new Texture(pixmap));

        pixmap = new Pixmap((int)(YayPipe.SCREEN_WIDTH * 0.2f), (int)(YayPipe.SCREEN_WIDTH * 0.2f), Pixmap.Format.RGBA8888);

        pixmap.setColor(Color.WHITE);
        pixmap.fillCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, pixmap.getWidth()/2);

        circle = new TextureRegion(new Texture(pixmap));

        //individual grid block pixmap
        pixmap = new Pixmap((int)(YayPipe.SCREEN_WIDTH * 0.1f), (int)(YayPipe.SCREEN_WIDTH * 0.1f), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        int radius = (int) (pixmap.getWidth() * 0.15f);
        pixmap.fillRectangle(radius, 0, pixmap.getWidth() - radius * 2, pixmap.getHeight());
        pixmap.fillRectangle(0, radius, pixmap.getWidth(), pixmap.getHeight() - radius * 2);
        pixmap.fillCircle(radius, radius, radius);
        pixmap.fillCircle(pixmap.getWidth() - radius, pixmap.getHeight() - radius, radius);
        pixmap.fillCircle(radius, pixmap.getHeight() - radius, radius);
        pixmap.fillCircle(pixmap.getWidth() - radius, radius, radius);

        gridBlock = new TextureRegion(new Texture(pixmap));
        blockFactory = new BlockFactory(gridBlock);

        pixmap.dispose();

        imageAtlas = manager.get("asset_image.atlas");

        pipe_re = new TextureRegion(imageAtlas.findRegion("pipe_re"));

        pipe_le = new TextureRegion(pipe_re);
        pipe_le.flip(true,false);

        pipe_be = new TextureRegion(imageAtlas.findRegion("pipe_be"));

        pipe_te = new TextureRegion(pipe_be);
        pipe_te.flip(false, true);

        pipe_lr = new TextureRegion(imageAtlas.findRegion("pipe_lr"));
        pipe_tb = new TextureRegion(imageAtlas.findRegion("pipe_tb"));
        pipe_rb = new TextureRegion(imageAtlas.findRegion("pipe_rb"));

        pipe_rt = new TextureRegion(pipe_rb);
        pipe_rt.flip(false,true);

        pipe_lb = new TextureRegion(pipe_rb);
        pipe_lb.flip(true,false);

        pipe_lt = new TextureRegion(pipe_rb);
        pipe_lt.flip(true,true);

        pipe_lrtb = new TextureRegion(imageAtlas.findRegion("pipe_lrtb"));

        square = new TextureRegion(imageAtlas.findRegion("white"));
        finishIndicator = new TextureRegion(imageAtlas.findRegion("finishIndicator"));
        redBlock = new TextureRegion(imageAtlas.findRegion("redBlock"));
        slider = new TextureRegion(imageAtlas.findRegion("slider"));
        tapToStart = new TextureRegion(imageAtlas.findRegion("tapToStart"));
        coin = new TextureRegion(imageAtlas.findRegion("coin"));
        piggy = new TextureRegion(imageAtlas.findRegion("piggy"));
        hand = new TextureRegion(imageAtlas.findRegion("hand"));
        y_1 = new TextureRegion(imageAtlas.findRegion("y", 1));
        a = new TextureRegion(imageAtlas.findRegion("a"));
        y_2 = new TextureRegion(imageAtlas.findRegion("y", 2));
        ex_mark = new TextureRegion(imageAtlas.findRegion("ex_mark"));
        p_1 = new TextureRegion(imageAtlas.findRegion("p", 1));
        i = new TextureRegion(imageAtlas.findRegion("i"));
        p_2 = new TextureRegion(imageAtlas.findRegion("p", 2));
        e = new TextureRegion(imageAtlas.findRegion("e"));
        itemTray = new NinePatchDrawable(imageAtlas.createPatch("itemTray"));
        window = new NinePatchDrawable(imageAtlas.createPatch("window"));
        button = new NinePatchDrawable(imageAtlas.createPatch("button"));

        iconAtlas = manager.get("asset_icon.atlas");

        shadow = new TextureRegion(imageAtlas.findRegion("shadow"));

        waterDrop = new TextureRegion(imageAtlas.findRegion("animation_water"));

        skinSetUp();
    }

    private void skinSetUp(){
        uiSkin = new Skin();

        TextButton.TextButtonStyle menuButtonStyle = new TextButton.TextButtonStyle();
        menuButtonStyle.up = button;
        menuButtonStyle.font = getFont(FontType.ANJA_MEDIUM);
        menuButtonStyle.fontColor = Color.BLACK;
        menuButtonStyle.pressedOffsetY = -10;
        uiSkin.add("mainMenu", menuButtonStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle(getFont(FontType.ANJA_SMALL), Color.BLACK);
        uiSkin.add("mainMenuHighScore", labelStyle);

        labelStyle = new Label.LabelStyle(getFont(FontType.ANJA_MESSAGE), Color.WHITE);
        uiSkin.add("message", labelStyle);

        labelStyle = new Label.LabelStyle(getFont(FontType.ANJA_LARGE), Color.BLACK);
        uiSkin.add("setupTitle", labelStyle);

        labelStyle = new Label.LabelStyle(getFont(FontType.ANJA_MEDIUM), Color.BLACK);
        uiSkin.add("setupSubtitle", labelStyle);

        labelStyle = new Label.LabelStyle(getFont(FontType.ANJA_MEDIUM_SMALL), Color.BLACK);
        uiSkin.add("setupValue", labelStyle);

        labelStyle = new Label.LabelStyle(getFont(FontType.NOTO_SMALL), Color.WHITE);
        uiSkin.add("setupUpperUIChar", labelStyle);

        labelStyle = new Label.LabelStyle(getFont(FontType.NOTO_MEDIUM), Color.WHITE);
        uiSkin.add("setupUpperUIValue", labelStyle);

        labelStyle = new Label.LabelStyle(getFont(FontType.ANJA_EXTRA_SMALL), Color.WHITE);
        uiSkin.add("badge", labelStyle);

        labelStyle = new Label.LabelStyle(getFont(FontType.NOTO_MEDIUM_LARGE), Color.BLACK);
        uiSkin.add("gameOverTime", labelStyle);

        labelStyle = new Label.LabelStyle(getFont(FontType.NOTO_EXTRA_SMALL), Color.BLACK);
        uiSkin.add("gameOverText", labelStyle);

        labelStyle = new Label.LabelStyle(getFont(FontType.NOTO_MEDIUM), Color.WHITE);
        uiSkin.add("upperBarHighScore", labelStyle);

        labelStyle = new Label.LabelStyle(getFont(FontType.ANJA_SHADOW), Color.WHITE);
        uiSkin.add("gameOverFinalScore", labelStyle);

        labelStyle = new Label.LabelStyle(getFont(FontType.ANJA_SMALL), Color.WHITE);
        uiSkin.add("scoreEffect", labelStyle);

        Window.WindowStyle windowStyle = new Window.WindowStyle(getFont(FontType.ANJA_MEDIUM_LARGE), Color.BLACK, window);
        Pixmap background = new Pixmap(1,1, Pixmap.Format.RGBA4444);
        background.setColor(0,0,0,0.7f);
        background.fill();
        windowStyle.stageBackground = new TextureRegionDrawable(new TextureRegion(new Texture(background)));
        background.dispose();
        uiSkin.add("default", windowStyle);
    }

    public BitmapFont getFont(FontType fontType){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_DIRECTORY + fontType.fontName));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (YayPipe.SCREEN_WIDTH * fontType.sizeByScreenWidth);

        switch (fontType){
            case ANJA_MESSAGE:
                parameter.color = CustomColor.RED.getColor();
                parameter.borderColor = Color.WHITE;
                parameter.borderWidth = 5;
                parameter.shadowColor = new Color(0, 0, 0, 0.3f);
                parameter.shadowOffsetX = 2;
                parameter.shadowOffsetY = 2;
                break;

            case ANJA_SHADOW:
                parameter.shadowColor = new Color(0,0,0,0.2f);
                parameter.shadowOffsetX = 4;
                parameter.shadowOffsetY = 4;
                break;
        }

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    public Sound getSound(SoundType soundType){
        return manager.get("sounds/" + soundType.fileName);
    }

    public TextureRegion getIconTexture(IconType iconType){
        return iconAtlas.findRegion(iconType.fileName);
    }

    public TextureRegion getPipeImage(PipeType pipeType){
        if (pipeType == null){
            return null;
        }

        switch (pipeType){
            case TOP_BOTTOM:
                return pipe_tb;
            case LEFT_RIGHT:
                return pipe_lr;
            case LEFT_TOP:
                return pipe_lt;
            case LEFT_BOTTOM:
                return pipe_lb;
            case RIGHT_TOP:
                return pipe_rt;
            case RIGHT_BOTTOM:
                return pipe_rb;
            case ALL_DIRECTION:
                return pipe_lrtb;
            case BOTTOM_END:
                return pipe_be;
            case LEFT_END:
                return pipe_le;
            case RIGHT_END:
                return pipe_re;
            case TOP_END:
                return pipe_te;
            default:
                throw new RuntimeException("Invalid pipe type");
        }
    }

    public void dispose(){
        manager.clear();
        manager.dispose();
    }
}
