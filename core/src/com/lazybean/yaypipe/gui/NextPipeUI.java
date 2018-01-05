package com.lazybean.yaypipe.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Queue;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
import com.lazybean.yaypipe.gamehelper.CustomColor;
import com.lazybean.yaypipe.gamehelper.FontType;
import com.lazybean.yaypipe.gamehelper.PipeType;
import com.lazybean.yaypipe.gamehelper.SpriteAccessor;
import com.lazybean.yaypipe.gameobjects.GridBlock;
import com.lazybean.yaypipe.gameobjects.NextBlock;
import com.lazybean.yaypipe.gameobjects.Pipe;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Quad;

public class NextPipeUI extends Group {
    public final static float RED_BLOCK_LENGTH = GridBlock.BLOCK_LENGTH * 1.57f;
    public final static float RED_BLOCK_LINE_LENGTH = RED_BLOCK_LENGTH * 0.11f;

    private AssetLoader assetLoader;
    private GameWorld gameWorld;

    public Array<NextBlock> blockOrderQueue;
    private Array<PipeType> nextPipeList;

    private Group nextBlockQueueGroup;
    public RedBlock redBlock;
    private NextBlock undoBlock;

    private TweenManager tweenManager;

    public NextPipeUI(GameWorld gameWorld, AssetLoader assetLoader){
        this.gameWorld = gameWorld;
        this.assetLoader = assetLoader;
        tweenManager = new TweenManager();

        blockOrderQueue = new Array<>();

        nextBlockQueueGroup = new Group();

        setBounds(0, 0, YayPipe.SCREEN_WIDTH, YayPipe.SCREEN_HEIGHT * 0.12f);

        Background background = new Background();
        background.setColor(CustomColor.YELLOW.getColor());
        background.setHeight(getHeight());

        addActor(background);

        Label.LabelStyle labelStyle = new Label.LabelStyle(assetLoader.getFont(FontType.ANJA_SMALL), Color.BLACK);
        Label label = new Label("next", labelStyle);
        redBlock = new RedBlock(assetLoader.redBlock);
        redBlock.setPosition(getWidth() * 0.1f, getHeight() / 2, Align.center);
        label.setPosition(redBlock.getX() + redBlock.getWidth() / 2, redBlock.getTop(), Align.bottom);
        addActor(redBlock);
        addActor(label);

        nextPipeList = new Array<>();
        switch (gameWorld.difficulty) {
            case TUTORIAL_BASIC:
                nextPipeList.addAll(PipeType.LEFT_RIGHT, PipeType.LEFT_BOTTOM, PipeType.LEFT_BOTTOM, PipeType.ALL_DIRECTION,
                        PipeType.RIGHT_BOTTOM, PipeType.RIGHT_TOP, PipeType.LEFT_TOP, PipeType.LEFT_BOTTOM, PipeType.TOP_BOTTOM);
                nextPipeList.reverse();
                break;

            case TUTORIAL_ADVANCED:
                nextPipeList.addAll(PipeType.LEFT_RIGHT, PipeType.LEFT_BOTTOM, PipeType.ALL_DIRECTION, PipeType.LEFT_TOP,
                        PipeType.RIGHT_TOP, PipeType.RIGHT_BOTTOM, PipeType.LEFT_BOTTOM, PipeType.TOP_BOTTOM);
                nextPipeList.reverse();
                break;

            default:
                nextPipeList.addAll(PipeType.LEFT_RIGHT, PipeType.TOP_BOTTOM, PipeType.LEFT_BOTTOM, PipeType.LEFT_TOP,
                        PipeType.RIGHT_BOTTOM, PipeType.RIGHT_TOP, PipeType.ALL_DIRECTION);
                nextPipeList.shuffle();
                break;
        }

        for (int i = 0; i < 7; i++) {
            NextBlock block = assetLoader.blockFactory.obtainNextBlock();
            PipeType nextPipeType = nextPipeList.pop();
            block.setPipe(assetLoader.getPipeImage(nextPipeType), nextPipeType);
            blockOrderQueue.add(block);
            block.setPosition(i * (block.getWidth() + NextBlock.BLOCK_GAP) ,0);

            Tween.set(block, SpriteAccessor.ALPHA).target(0f).start(tweenManager);
            nextBlockQueueGroup.addActor(block);
        }

        nextBlockQueueGroup.setWidth((NextBlock.BLOCK_LENGTH + NextBlock.BLOCK_GAP) * 5);
        nextBlockQueueGroup.setHeight(NextBlock.BLOCK_LENGTH);
        nextBlockQueueGroup.setPosition(getWidth() * 0.8f, getHeight() / 2, Align.right);
        addActor(nextBlockQueueGroup);
    }

    public void start(){
        Timeline.createParallel()
                .push(Tween.to(blockOrderQueue.get(0), SpriteAccessor.ALPHA, 0.3f).target(1f))
                .push(Tween.to(blockOrderQueue.get(1), SpriteAccessor.ALPHA, 0.3f).target(1f))
                .push(Tween.to(blockOrderQueue.get(2), SpriteAccessor.ALPHA, 0.3f).target(1f))
                .push(Tween.to(blockOrderQueue.get(3), SpriteAccessor.ALPHA, 0.3f).target(1f))
                .push(Tween.to(blockOrderQueue.get(4), SpriteAccessor.ALPHA, 0.3f).target(1f))
                .push(Tween.to(blockOrderQueue.get(5), SpriteAccessor.ALPHA, 0.3f).target(1f))
                .push(Tween.from(nextBlockQueueGroup, SpriteAccessor.POSITION, 0.3f).targetRelative(nextBlockQueueGroup.getWidth(), 0)
                        .ease(Quad.OUT))
                .push(Tween.to(gameWorld.getGrid().getStartBlock().getPipe(), SpriteAccessor.SCALE, 0.5f).target(1f).ease(Elastic.OUT))
                .push(Tween.to(gameWorld.getGrid().getFinishBlock().getPipe(), SpriteAccessor.SCALE, 0.5f).target(1f).ease(Elastic.OUT))
                .beginSequence()
                .push(Tween.to(blockOrderQueue.first(), SpriteAccessor.POSITION, 0.3f).targetRelative(redBlock.getX() - nextBlockQueueGroup.getX() + RED_BLOCK_LINE_LENGTH, 0))
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        gameWorld.getGrid().setTouchable(Touchable.enabled);
                    }
                })
                .end()
                .start(tweenManager);
    }

    public void setNextBlock(){
        Vector2 blockQueuePos = blockOrderQueue.get(1).localToStageCoordinates(new Vector2());
        Timeline.createParallel()
                .push(Tween.to(blockOrderQueue.get(0), SpriteAccessor.ALPHA, 0.2f).target(0f))
                .push(Tween.to(blockOrderQueue.get(1), SpriteAccessor.POSITION, 0.2f).targetRelative(-(blockQueuePos.x - redBlock.getX() - RED_BLOCK_LINE_LENGTH),0))
                .push(Tween.to(blockOrderQueue.get(2), SpriteAccessor.POSITION, 0.2f).targetRelative(-NextBlock.BLOCK_LENGTH - NextBlock.BLOCK_GAP, 0))
                .push(Tween.to(blockOrderQueue.get(3), SpriteAccessor.POSITION, 0.2f).targetRelative(-NextBlock.BLOCK_LENGTH - NextBlock.BLOCK_GAP, 0))
                .push(Tween.to(blockOrderQueue.get(4), SpriteAccessor.POSITION, 0.2f).targetRelative(-NextBlock.BLOCK_LENGTH - NextBlock.BLOCK_GAP, 0))
                .push(Tween.to(blockOrderQueue.get(5), SpriteAccessor.POSITION, 0.2f).targetRelative(-NextBlock.BLOCK_LENGTH - NextBlock.BLOCK_GAP, 0))
                .push(Tween.to(blockOrderQueue.get(6), SpriteAccessor.POSITION, 0.2f).targetRelative(-NextBlock.BLOCK_LENGTH - NextBlock.BLOCK_GAP, 0))
                .push(Tween.to(blockOrderQueue.get(6), SpriteAccessor.ALPHA, 0.2f).target(1f))
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        gameWorld.getGrid().setTouchable(Touchable.enabled);

                        undoBlock = blockOrderQueue.removeIndex(0);
                        undoBlock.remove();

                        NextBlock block = assetLoader.blockFactory.obtainNextBlock();
                        if (nextPipeList.size == 0){
                            nextPipeList.addAll(PipeType.LEFT_RIGHT, PipeType.TOP_BOTTOM, PipeType.LEFT_BOTTOM, PipeType.LEFT_TOP,
                                    PipeType.RIGHT_BOTTOM, PipeType.RIGHT_TOP, PipeType.ALL_DIRECTION);
                            nextPipeList.shuffle();

                        }
                        PipeType pipeType = nextPipeList.pop();
                        block.setPipe(assetLoader.getPipeImage(pipeType), pipeType);

                        block.setPosition(blockOrderQueue.peek().getX() + block.getWidth() + GridBlock.BLOCK_GAP, 0);
                        blockOrderQueue.add(block);

                        Color color = blockOrderQueue.peek().getColor();
                        blockOrderQueue.peek().setColor(color.r,color.g,color.b,0f);

                        nextBlockQueueGroup.addActor(block);
                    }
                })
                .start(tweenManager);
    }

    public void undo(){
        blockOrderQueue.insert(0, undoBlock);
        nextBlockQueueGroup.addActor(undoBlock);

        Vector2 blockQueuePos = blockOrderQueue.get(2).localToStageCoordinates(new Vector2());
        Timeline.createParallel()
                .push(Tween.to(blockOrderQueue.get(0), SpriteAccessor.ALPHA, 0.2f).target(1f))
                .push(Tween.to(blockOrderQueue.get(1), SpriteAccessor.POSITION, 0.2f).targetRelative(blockQueuePos.x - redBlock.getX() - RED_BLOCK_LINE_LENGTH, 0))
                .push(Tween.to(blockOrderQueue.get(2), SpriteAccessor.POSITION, 0.2f).targetRelative(NextBlock.BLOCK_LENGTH + NextBlock.BLOCK_GAP, 0))
                .push(Tween.to(blockOrderQueue.get(3), SpriteAccessor.POSITION, 0.2f).targetRelative(NextBlock.BLOCK_LENGTH + NextBlock.BLOCK_GAP, 0))
                .push(Tween.to(blockOrderQueue.get(4), SpriteAccessor.POSITION, 0.2f).targetRelative(NextBlock.BLOCK_LENGTH + NextBlock.BLOCK_GAP, 0))
                .push(Tween.to(blockOrderQueue.get(5), SpriteAccessor.POSITION, 0.2f).targetRelative(NextBlock.BLOCK_LENGTH + NextBlock.BLOCK_GAP, 0))
                .push(Tween.to(blockOrderQueue.get(6), SpriteAccessor.POSITION, 0.2f).targetRelative(NextBlock.BLOCK_LENGTH + NextBlock.BLOCK_GAP, 0))
                .push(Tween.to(blockOrderQueue.get(6), SpriteAccessor.ALPHA, 0.2f).target(0f))
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        nextPipeList.add(blockOrderQueue.peek().getPipe().getType());
                        blockOrderQueue.peek().remove(); // remove from stage
                        assetLoader.blockFactory.free(blockOrderQueue.pop()); // remove from queue

                        gameWorld.getGrid().setTouchable(Touchable.enabled);
                    }
                })
                .start(tweenManager);
    }

    public void set(int index, Pipe pipe){
        blockOrderQueue.get(index).setPipe(pipe);
    }

    @Override
    public void act(float delta) {
        tweenManager.update(delta);
        super.act(delta);
    }

    public class RedBlock extends Actor {
        private TextureRegion redBlock;

        public RedBlock(TextureRegion redBlock){
            this.redBlock = redBlock;
            setWidth(RED_BLOCK_LENGTH);
            setHeight(RED_BLOCK_LENGTH);
            setOrigin(getWidth()/2, getHeight()/2);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(redBlock,getX(),getY(),getWidth(),getHeight());
        }
    }
}
