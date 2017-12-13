package com.lazybean.yaypipe.gamehelper;

public class NumberAnimator {
    private int currentNum;
    private int targetNum;

    private int changeConstant;
    private int changeSum;
    private float delayCount;
    //delay between increment/decrement
    private float delay;
    private int accelerationConstant;

    private boolean isStart;

    public NumberAnimator(){
        currentNum = 0;
        targetNum = 0;
        changeConstant = 5;
        changeSum = changeConstant;
        delayCount = 0;
        delay = 0;
        accelerationConstant = 0;
        isStart = false;
    }

    public NumberAnimator(int startNum, int targetNum, int changeConstant, float delay, int accelerationConstant){
        currentNum = startNum;
        this.targetNum = targetNum;
        this.changeConstant = changeConstant;
        this.delay = delay;
        this.accelerationConstant = accelerationConstant;

        changeSum = changeConstant;
    }

    public void start(){
        isStart = true;
    }

    public void stop(){
        isStart = false;
    }

    public void setRange(int startNum, int targetNum){
        currentNum = startNum;
        this.targetNum = targetNum;
    }

    public void setStartNum(int startNum){
        currentNum = startNum;
    }

    public void setTargetNum(int targetNum){
        this.targetNum = targetNum;
    }

    public void setChangeConstant(int changeConstant) {
        this.changeConstant = changeConstant;
    }

    public void setAccelerationConstant(int accelerationConstant){
        this.accelerationConstant = accelerationConstant;
    }

    public void setDelay(float delay){
        this.delay = delay;
    }

    public int getCurrentNum(){
        return currentNum;
    }

    public void skip(){
        currentNum = targetNum;
    }

    public void update(float delta){
        if (isStart) {
            delayCount += delta;
        }

        if (delayCount > delay){
            delayCount = 0;

            changeSum += accelerationConstant;

            if (currentNum < targetNum){
                if (currentNum + changeSum < targetNum){
                    currentNum += changeSum;
                }
                else{
                    changeSum = changeConstant;
                    currentNum = targetNum;
                }
            }
            if (currentNum > targetNum){
                if (currentNum - changeSum > targetNum) {
                    currentNum -= changeSum;
                }
                else {
                    changeSum = changeConstant;
                    currentNum = targetNum;
                }
            }
        }
    }
}
