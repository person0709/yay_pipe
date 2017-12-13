package com.lazybean.yaypipe.gamehelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.text.DecimalFormat;

public class Stopwatch extends Actor {
    private static final Stopwatch ourInstance = new Stopwatch();

    public static Stopwatch getInstance() {
        return ourInstance;
    }

    private float totalTime = 0;
    private boolean isTimerOn = false;

    private Stopwatch() {

    }

    public void start(){
        totalTime = 0;
        isTimerOn = true;
    }

    public void resume(){
        isTimerOn = true;
    }

    public void stop(){
        isTimerOn = false;
    }

    public void reset(){
        totalTime = 0;
        isTimerOn = false;
    }

    public float getFloatTime(){
        return totalTime;
    }

    public int getIntTime(){
//        Gdx.app.log("tag", String.valueOf(Math.round(totalTime)));
        return Math.round(totalTime);
    }

    @Override
    public void act(float delta) {
        if (isTimerOn) {
            totalTime += delta;
        }
    }

    @Override
    public boolean remove() {
        reset();
        return super.remove();
    }

    public static String convertSecondToHour(int time){
        String second = String.valueOf(time % 60);
        if (second.length() == 1){
            second = "0" + second;
        }
        String minute = String.valueOf((time / 60) % 60);
        if (minute.length() == 1){
            minute = "0" + minute;
        }
        String hour = String.valueOf(time / 3600);
        if (hour.length() == 1){
            hour = "0" + hour;
        }

        String string = String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(second);

        return string;
    }

    public static String convertSecondToMinute(int time){
        String second = String.valueOf(time % 60);
        if (second.length() == 1){
            second = "0" + second;
        }
        String minute = String.valueOf((time / 60) % 60);

        String string = String.valueOf(minute) + ":" + String.valueOf(second);

        return string;
    }

    public static String toTwoDecimalPoints(long time){
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        return decimalFormat.format(time);
    }
}
