package com.lazybean.yaypipe.gamehelper;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class PipeHashMap {

    public static HashMap<Integer, TextureRegion> numToPipe;
    public static HashMap<TextureRegion, Integer> pipeToNum;
    public PipeHashMap(AssetLoader assetLoader) {
        numToPipe = new HashMap<Integer, TextureRegion>(11);
        numToPipe.put(1, assetLoader.pipe_tb);
        numToPipe.put(2, assetLoader.pipe_lr);
        numToPipe.put(3, assetLoader.pipe_lt);
        numToPipe.put(4, assetLoader.pipe_lb);
        numToPipe.put(5, assetLoader.pipe_rt);
        numToPipe.put(6, assetLoader.pipe_rb);
        numToPipe.put(7, assetLoader.pipe_lrtb);
        numToPipe.put(8, assetLoader.pipe_bs);
        numToPipe.put(9, assetLoader.pipe_ls);
        numToPipe.put(10, assetLoader.pipe_ts);
        numToPipe.put(11, assetLoader.pipe_rs);

        pipeToNum = new HashMap<TextureRegion, Integer>(11);
        pipeToNum.put(assetLoader.pipe_tb, 1);
        pipeToNum.put(assetLoader.pipe_lr, 2);
        pipeToNum.put(assetLoader.pipe_lt, 3);
        pipeToNum.put(assetLoader.pipe_lb, 4);
        pipeToNum.put(assetLoader.pipe_rt, 5);
        pipeToNum.put(assetLoader.pipe_rb, 6);
        pipeToNum.put(assetLoader.pipe_lrtb, 7);
        pipeToNum.put(assetLoader.pipe_bs, 8);
        pipeToNum.put(assetLoader.pipe_ls, 9);
        pipeToNum.put(assetLoader.pipe_ts, 10);
        pipeToNum.put(assetLoader.pipe_rs, 11);
    }
}
