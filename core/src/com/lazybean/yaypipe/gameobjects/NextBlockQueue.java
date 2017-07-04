package com.lazybean.yaypipe.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.Difficulty;
import com.lazybean.yaypipe.gamehelper.PipeType;

public class NextBlockQueue extends Group {
    public static Queue<NextBlock> blockQueue;
    private Array<Integer> nextPipeList;
    private NextBlock undoBlock;

    private boolean undoAble = false;


    private AssetLoader assetLoader;

    public NextBlockQueue(AssetLoader assetLoader){
        this.assetLoader = assetLoader;
        setWidth((NextBlock.LENGTH + NextBlock.GAP) * 5);
        setHeight(NextBlock.LENGTH);

        blockQueue = new Queue<>();

        nextPipeList = new Array<>();
        switch (AssetLoader.prefs.getInteger("difficulty")) {
            case Difficulty.TUTORIAL_BASIC:
                nextPipeList.addAll(PipeType.LEFT_RIGHT, PipeType.LEFT_BOTTOM, PipeType.LEFT_BOTTOM, PipeType.ALL_DIRECTION,
                        PipeType.RIGHT_BOTTOM, PipeType.RIGHT_TOP, PipeType.LEFT_TOP, PipeType.LEFT_BOTTOM, PipeType.TOP_BOTTOM);
                nextPipeList.reverse();
                break;

            case Difficulty.TUTORIAL_ADVANCED:
                nextPipeList.addAll(PipeType.LEFT_RIGHT, PipeType.LEFT_BOTTOM, PipeType.ALL_DIRECTION, PipeType.LEFT_TOP,
                        PipeType.RIGHT_TOP, PipeType.RIGHT_BOTTOM, PipeType.LEFT_BOTTOM, PipeType.TOP_BOTTOM);
                nextPipeList.reverse();
                break;

            default:
                nextPipeList.addAll(1, 2, 3, 4, 5, 6, 7);
                nextPipeList.shuffle();
                break;
        }


        for (int i = 0; i < 7; i++) {
            NextBlock block = new NextBlock(assetLoader.gridBlock);
            block.setPipe(nextPipeList.pop());
            blockQueue.addLast(block);
            block.setPosition(i * (block.getLength() + block.getGap()),0);

            addActor(block);
        }
        Color color = blockQueue.last().getColor();
        blockQueue.last().setColor(color.r,color.g,color.b,0f);
    }

    public void addNewBlock(){
        undoBlock = blockQueue.removeFirst();
        undoBlock.remove();

        NextBlock block = new NextBlock(assetLoader.gridBlock);
        if (nextPipeList.size == 0){
            nextPipeList.addAll(1, 2, 3, 4, 5, 6, 7);
            nextPipeList.shuffle();

        }
           block.setPipe(nextPipeList.pop());

        block.setPosition(blockQueue.last().getX() + block.getLength() + block.getGap(),0);
        blockQueue.addLast(block);

        Color color = blockQueue.last().getColor();
        blockQueue.last().setColor(color.r,color.g,color.b,0f);

        addActor(block);

        undoAble = true;
    }

    public void undo(){
        blockQueue.addFirst(undoBlock);
        addActor(undoBlock);

        nextPipeList.add(blockQueue.last().getPipe());
        blockQueue.last().nullify();
        blockQueue.last().remove();
        blockQueue.removeLast();

        undoAble = false;
    }

    public boolean isUndoAble(){
        return undoAble;
    }

    public NextBlock get(int index){
        return blockQueue.get(index);
    }

    public void set(int index, int pipeNum){
        blockQueue.get(index).setPipe(pipeNum);
    }
}
