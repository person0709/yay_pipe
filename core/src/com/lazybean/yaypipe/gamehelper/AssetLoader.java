package com.lazybean.yaypipe.gamehelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.lazybean.yaypipe.gameobjects.CoinBank;


public class AssetLoader {
    public Array<Color> badgeColor = new Array<>(5);

    public AssetManager manager;

    public Skin uiSkin;
    public TextureAtlas imageAtlas, iconAtlas;

    private TextureRegion pipe_lr, pipe_lb, pipe_lt, pipe_rb, pipe_rt, pipe_tb, pipe_lrtb;
    private TextureRegion pipe_le, pipe_te, pipe_re, pipe_be;

    public TextureRegion gridBlock,redBlock;
    public TextureRegion square, circle;
    public TextureRegion finishIndicator;

    public TextureRegion slider, tapToStart, coin, piggy, hand, itemTray;

    public TextureRegion y_1, a, y_2, ex_mark, p_1, i, p_2, e;

    public TextureRegion pause, restart, resume, item, cross, share, sound, soundOff, undo, fastForward, badgeArrow,
            zoomIn, zoomOut, home, snail, wand, settings, lock;

    public TextureRegion shadow;
    public TextureRegion line;

    public NinePatchDrawable window;

    public Array<TextureRegion> waterDrop_inPipe, waterDrop_splash;
    public TextureRegion waterDrop;

    public BitmapFont extraSmallFont_anja, smallFont_anja, smallMediumFont_anja, mediumFont_anja,
            mediumFontShadow_anja, mediumLargeFont_anja, largeFont_anja, extraLargeFont_anja;
    public BitmapFont doubleExtraSmallFont_noto, extraSmallFont_noto, smallFont_noto, smallMediumFont_noto, mediumFont_noto;
    public BitmapFont messageFont;

    public Sound click;

    public static Preferences prefs;
    public static Statistics stats;
    public static CoinBank coinBank;

    public AssetLoader(){
        manager = new AssetManager();
    }

    public void loadManager(){
        manager.load("asset_image.pack", TextureAtlas.class);
        manager.load("asset_icon.pack", TextureAtlas.class);
        manager.load("pipe_drop_anim.pack", TextureAtlas.class);
        manager.load("splash_anim.pack", TextureAtlas.class);
        manager.load("tutorial.png", Texture.class);
    }

    public void assignAssets(){
        badgeColor.addAll(Colour.PINK, Colour.ORANGE, Colour.GREEN, Colour.BLUE, Colour.PURPLE);

        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), 10, Pixmap.Format.RGBA4444);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        line = new TextureRegion(new Texture(pixmap));

        pixmap = new Pixmap((int)(Gdx.graphics.getWidth() * 0.2f), (int)(Gdx.graphics.getWidth() * 0.2f), Pixmap.Format.RGBA8888);

        pixmap.setColor(Color.WHITE);
        pixmap.fillCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, pixmap.getWidth()/2);

        circle = new TextureRegion(new Texture(pixmap));

        //individual grid block pixmap
        pixmap = new Pixmap((int)(Gdx.graphics.getWidth() * 0.1f), (int)(Gdx.graphics.getWidth() * 0.1f), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        int radius = (int) (pixmap.getWidth() * 0.15f);
        pixmap.fillRectangle(radius, 0, pixmap.getWidth() - radius * 2, pixmap.getHeight());
        pixmap.fillRectangle(0, radius, pixmap.getWidth(), pixmap.getHeight() - radius * 2);
        pixmap.fillCircle(radius, radius, radius);
        pixmap.fillCircle(pixmap.getWidth() - radius, pixmap.getHeight() - radius, radius);
        pixmap.fillCircle(radius, pixmap.getHeight() - radius, radius);
        pixmap.fillCircle(pixmap.getWidth() - radius, radius, radius);

        gridBlock = new TextureRegion(new Texture(pixmap));

        pixmap.dispose();

        uiSkin = new Skin();
        imageAtlas = manager.get("asset_image.pack");
        uiSkin.addRegions(imageAtlas);

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

        finishIndicator = new TextureRegion(imageAtlas.findRegion("finish_indicator"));
        redBlock = new TextureRegion(imageAtlas.findRegion("redBlock"));
        square = new TextureRegion(imageAtlas.findRegion("square"));
        slider = new TextureRegion(imageAtlas.findRegion("slider"));
        tapToStart = new TextureRegion(imageAtlas.findRegion("tapToStart"));
        coin = new TextureRegion(imageAtlas.findRegion("coin"));
        piggy = new TextureRegion(imageAtlas.findRegion("piggy"));
        itemTray = new TextureRegion(imageAtlas.findRegion("itemTray"));
        hand = new TextureRegion(imageAtlas.findRegion("hand"));
        y_1 = new TextureRegion(imageAtlas.findRegion("y", 1));
        a = new TextureRegion(imageAtlas.findRegion("a"));
        y_2 = new TextureRegion(imageAtlas.findRegion("y", 2));
        ex_mark = new TextureRegion(imageAtlas.findRegion("ex_mark"));
        p_1 = new TextureRegion(imageAtlas.findRegion("p", 1));
        i = new TextureRegion(imageAtlas.findRegion("i"));
        p_2 = new TextureRegion(imageAtlas.findRegion("p", 2));
        e = new TextureRegion(imageAtlas.findRegion("e"));

        iconAtlas = manager.get("asset_icon.pack");

        pause = new TextureRegion(iconAtlas.findRegion("pause"));
        restart = new TextureRegion(iconAtlas.findRegion("restart"));
        resume = new TextureRegion(iconAtlas.findRegion("resume"));
        item = new TextureRegion(iconAtlas.findRegion("item"));
        cross = new TextureRegion(iconAtlas.findRegion("cross"));
        share = new TextureRegion(iconAtlas.findRegion("share"));
        sound = new TextureRegion(iconAtlas.findRegion("sound"));
        soundOff = new TextureRegion(iconAtlas.findRegion("soundOff"));
        undo = new TextureRegion(iconAtlas.findRegion("undo"));
        fastForward = new TextureRegion(iconAtlas.findRegion("fastForward"));
        badgeArrow = new TextureRegion(iconAtlas.findRegion("badgeArrow"));
        zoomIn = new TextureRegion(iconAtlas.findRegion("zoomIn"));
        zoomOut = new TextureRegion(iconAtlas.findRegion("zoomOut"));
        home = new TextureRegion(iconAtlas.findRegion("home"));
        snail = new TextureRegion(iconAtlas.findRegion("snail"));
        wand = new TextureRegion(iconAtlas.findRegion("wand"));
        settings = new TextureRegion(iconAtlas.findRegion("settings"));
        lock = new TextureRegion(iconAtlas.findRegion("lock"));

        shadow = new TextureRegion(imageAtlas.findRegion("shadow"));

        waterDrop = new TextureRegion(imageAtlas.findRegion("animation_water"));

        click = Gdx.audio.newSound(Gdx.files.internal("click.ogg"));

    }

    private void loadData(){
        //game data handle
        FileHandle file = Gdx.files.local("Stats.json");
        Json json = new Json();
        if (!file.exists()) {
            Statistics stats = new Statistics();
            String output = json.toJson(stats);
            file.writeString(output, false);
        }
        stats = json.fromJson(Statistics.class, file);

        file = Gdx.files.local("CoinBank.json");
        if (!file.exists()) {
            CoinBank coinBank = new CoinBank();
            String output = json.toJson(coinBank);
            file.writeString(output, false);
        }
        coinBank = json.fromJson(CoinBank.class, file);

        prefs = Gdx.app.getPreferences("YayPipe");
        if(!prefs.contains("highScore")){
            prefs.putInteger("highScore",0);
            prefs.putBoolean("easyUnlocked", true);
            prefs.putBoolean("easyTinyUnlocked",true);
            prefs.flush();
        }
        if(!prefs.contains("soundVolume")){
            prefs.putFloat("soundVolume", 1f);
            prefs.flush();
        }

//        prefs.putInteger("highScore",0);
    }

    private void generateFonts(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Anja.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = (int) (Gdx.graphics.getWidth() * 0.03f);
        parameter.color = Color.WHITE;
        extraSmallFont_anja = generator.generateFont(parameter);

        parameter.size = (int) (Gdx.graphics.getWidth() * 0.05f);
        smallFont_anja = generator.generateFont(parameter);

        parameter.size = (int) (Gdx.graphics.getWidth() * 0.06f);
        smallMediumFont_anja = generator.generateFont(parameter);

        parameter.size = (int) (Gdx.graphics.getWidth() * 0.07f);
        mediumFont_anja = generator.generateFont(parameter);

        parameter.color = Colour.RED;
        parameter.borderColor = Color.WHITE;
        parameter.borderWidth = 5;
        parameter.shadowColor = new Color(0, 0, 0, 0.3f);
        parameter.shadowOffsetX = 2;
        parameter.shadowOffsetY = 2;
        messageFont = generator.generateFont(parameter);

        parameter.borderWidth = 0;
        parameter.color = Color.WHITE;
        parameter.shadowOffsetX = 0;
        parameter.shadowOffsetY = 0;

        parameter.size = (int) (Gdx.graphics.getWidth() * 0.08f);
        mediumLargeFont_anja = generator.generateFont(parameter);

        parameter.size = (int) (Gdx.graphics.getWidth() * 0.09f);
        largeFont_anja = generator.generateFont(parameter);

        parameter.size = (int) (Gdx.graphics.getWidth() * 0.121f);
        extraLargeFont_anja = generator.generateFont(parameter);

        parameter.size = (int) (Gdx.graphics.getWidth() * 0.07f);
        parameter.shadowColor = new Color(0,0,0,0.2f);
        parameter.shadowOffsetX = 4;
        parameter.shadowOffsetY = 4;
        mediumFontShadow_anja = generator.generateFont(parameter);


        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/NotoSans-Bold.ttf"));
        parameter.shadowOffsetX = 0;
        parameter.shadowOffsetY = 0;

        parameter.size = (int) (Gdx.graphics.getWidth() * 0.03f);
        doubleExtraSmallFont_noto = generator.generateFont(parameter);

        parameter.size = (int) (Gdx.graphics.getWidth() * 0.04f);
        extraSmallFont_noto = generator.generateFont(parameter);

        parameter.size = (int) (Gdx.graphics.getWidth() * 0.05f);
        smallFont_noto = generator.generateFont(parameter);

        parameter.size = (int) (Gdx.graphics.getWidth() * 0.06f);
        smallMediumFont_noto = generator.generateFont(parameter);

        parameter.size = (int) (Gdx.graphics.getWidth() * 0.07f);
        mediumFont_noto = generator.generateFont(parameter);

        generator.dispose();
    }

    public TextureRegion getPipeImage(PipeType pipeType){
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
        stats.saveData();
        coinBank.saveData();

        manager.clear();
        manager.dispose();
    }
}
