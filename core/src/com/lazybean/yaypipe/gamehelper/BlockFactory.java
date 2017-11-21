package com.lazybean.yaypipe.gamehelper;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.lazybean.yaypipe.gameobjects.Block;
import com.lazybean.yaypipe.gameobjects.GridBlock;
import com.lazybean.yaypipe.gameobjects.WandBlock;
import com.lazybean.yaypipe.gameobjects.NextBlock;

public class BlockFactory {
    private TextureRegion gridTexture;

    public BlockFactory(TextureRegion gridTexture){
        this.gridTexture = gridTexture;
    }

    private final Pool<GridBlock> gridBlockPool = new Pool<GridBlock>() {
        @Override
        protected GridBlock newObject() {
            return new GridBlock(gridTexture);
        }
    };

    private final Pool<NextBlock> nextBlockPool = new Pool<NextBlock>() {
        @Override
        protected NextBlock newObject() {
                return new NextBlock(gridTexture);
        }
    };

    private final Pool<WandBlock> itemBlockPool = new Pool<WandBlock>() {
        @Override
        protected WandBlock newObject() {
            return new WandBlock(gridTexture);
        }
    };

    public GridBlock obtainGridBlock(){
        return gridBlockPool.obtain();
    }

    public NextBlock obtainNextBlock(){
        return nextBlockPool.obtain();
    }

    public WandBlock obtainItemBlock(){
        return itemBlockPool.obtain();
    }

    public void free(Block block){
        if (block instanceof GridBlock){
            gridBlockPool.free((GridBlock) block);
        }
        else if (block instanceof NextBlock){
            nextBlockPool.free((NextBlock) block);
        }
        else if (block instanceof WandBlock){
            itemBlockPool.free((WandBlock) block);
        }
    }
}
