package com.lazybean.yaypipe.guiobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.lazybean.yaypipe.GameWorld;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.gamehelper.AssetLoader;
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

        setBounds(0, 0, RED_BLOCK_LENGTH * 1.5f, YayPipe.SCREEN_HEIGHT * 0.6f);

        Label.LabelStyle labelStyle = new Label.LabelStyle(assetLoader.getFont(FontType.ANJA_SMALL), Color.BLACK);
        Label label = new Label("next", labelStyle);
        redBlock = new RedBlock(assetLoader.redBlock);
        redBlock.setPosition(getWidth() / 2, redBlock.getHeight() * 0.1f, Align.bottom);
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
            block.setColor(Color.LIGHT_GRAY);
            PipeType nextPipeType = nextPipeList.pop();
            block.setPipe(assetLoader.getPipeImage(nextPipeType), nextPipeType);
            blockOrderQueue.add(block);
            block.setPosition(0,i * (block.getWidth() + NextBlock.BLOCK_GAP));

            Tween.set(block, SpriteAccessor.ALPHA).target(0f).start(tweenManager);
            nextBlockQueueGroup.addActor(block);
        }

        nextBlockQueueGroup.setHeight((NextBlock.BLOCK_LENGTH + NextBlock.BLOCK_GAP) * 5);
        nextBlockQueueGroup.setWidth(NextBlock.BLOCK_LENGTH);
        nextBlockQueueGroup.setPosition(getWidth() / 2, redBlock.getHeight(), Align.bottom);
        addActor(nextBlockQueueGroup);

        Actor nextPipeExpand = new Actor();
        nextPipeExpand.setBounds(0,0, getWidth(), RED_BLOCK_LENGTH);
        addActor(nextPipeExpand);

        nextPipeExpand.addListener(new ActorGestureListener(20, 0.4f, 0.2f, 0.15f){
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Color color = new Color(Color.GRAY);
                for(int i = blockOrderQueue.size - 1; i > 0; i--){
                    color.add(0.07f,0.07f,0.07f,0);
                    blockOrderQueue.get(i).setColor(color);
                    Tween.set(blockOrderQueue.get(i), SpriteAccessor.ALPHA).target(1).start(tweenManager);
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                for(int i = 1; i < blockOrderQueue.size; i++){
                    Tween.set(blockOrderQueue.get(i), SpriteAccessor.ALPHA).target(0f).start(tweenManager);
                }
            }
        });
    }

    public void start(){
        blockOrderQueue.get(0).setColor(Color.WHITE);
        Timeline.createParallel()
                .push(Tween.to(blockOrderQueue.get(0), SpriteAccessor.ALPHA, 0.3f).target(1f))
//                .push(Tween.to(blockOrderQueue.get(1), SpriteAccessor.ALPHA, 0.3f).target(1f))
//                .push(Tween.to(blockOrderQueue.get(2), SpriteAccessor.ALPHA, 0.3f).target(1f))
//                .push(Tween.to(blockOrderQueue.get(3), SpriteAccessor.ALPHA, 0.3f).target(1f))
//                .push(Tween.to(blockOrderQueue.get(4), SpriteAccessor.ALPHA, 0.3f).target(1f))
//                .push(Tween.to(blockOrderQueue.get(5), SpriteAccessor.ALPHA, 0.3f).target(1f))
//                .push(Tween.from(nextBlockQueueGroup, SpriteAccessor.POSITION, 0.3f).targetRelative(nextBlockQueueGroup.getWidth(), 0)
//                        .ease(Quad.OUT))
                .beginSequence()
                .push(Tween.set(blockOrderQueue.first(), SpriteAccessor.POSITION).targetRelative(0, redBlock.getY() - nextBlockQueueGroup.getY() + RED_BLOCK_LINE_LENGTH))
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
                .push(Tween.set(blockOrderQueue.get(0), SpriteAccessor.ALPHA).target(0f))
                .push(Tween.set(blockOrderQueue.get(1), SpriteAccessor.POSITION).targetRelative(0, -(blockQueuePos.y - redBlock.getY() - RED_BLOCK_LINE_LENGTH)))
                .push(Tween.to(blockOrderQueue.get(1), SpriteAccessor.ALPHA, 0.2f).target(1f).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        blockOrderQueue.get(1).setColor(Color.WHITE);
                    }
                }).setCallbackTriggers(TweenCallback.START))
                .push(Tween.set(blockOrderQueue.get(2), SpriteAccessor.POSITION).targetRelative(0, -NextBlock.BLOCK_LENGTH - NextBlock.BLOCK_GAP))
                .push(Tween.set(blockOrderQueue.get(3), SpriteAccessor.POSITION).targetRelative(0, -NextBlock.BLOCK_LENGTH - NextBlock.BLOCK_GAP))
                .push(Tween.set(blockOrderQueue.get(4), SpriteAccessor.POSITION).targetRelative(0, -NextBlock.BLOCK_LENGTH - NextBlock.BLOCK_GAP))
                .push(Tween.set(blockOrderQueue.get(5), SpriteAccessor.POSITION).targetRelative(0, -NextBlock.BLOCK_LENGTH - NextBlock.BLOCK_GAP))
                .push(Tween.set(blockOrderQueue.get(6), SpriteAccessor.POSITION).targetRelative(0, -NextBlock.BLOCK_LENGTH - NextBlock.BLOCK_GAP))
//                .push(Tween.to(blockOrderQueue.get(6), SpriteAccessor.ALPHA, 0.2f).target(1f))
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        gameWorld.getGrid().setTouchable(Touchable.enabled);

                        undoBlock = blockOrderQueue.removeIndex(0);
                        undoBlock.remove();

                        NextBlock block = assetLoader.blockFactory.obtainNextBlock();
                        block.setColor(Color.LIGHT_GRAY);
                        if (nextPipeList.size == 0){
                            nextPipeList.addAll(PipeType.LEFT_RIGHT, PipeType.TOP_BOTTOM, PipeType.LEFT_BOTTOM, PipeType.LEFT_TOP,
                                    PipeType.RIGHT_BOTTOM, PipeType.RIGHT_TOP, PipeType.ALL_DIRECTION);
                            nextPipeList.shuffle();

                        }
                        PipeType pipeType = nextPipeList.pop();
                        block.setPipe(assetLoader.getPipeImage(pipeType), pipeType);

                        block.setPosition(0, blockOrderQueue.peek().getY() + block.getWidth() + GridBlock.BLOCK_GAP);
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
                .push(Tween.set(blockOrderQueue.get(1), SpriteAccessor.ALPHA).target(0f))
                .push(Tween.set(blockOrderQueue.get(1), SpriteAccessor.POSITION).targetRelative(0, blockQueuePos.y - redBlock.getY() - RED_BLOCK_LINE_LENGTH))
                .push(Tween.set(blockOrderQueue.get(2), SpriteAccessor.POSITION).targetRelative(0, NextBlock.BLOCK_LENGTH + NextBlock.BLOCK_GAP))
                .push(Tween.set(blockOrderQueue.get(3), SpriteAccessor.POSITION).targetRelative(0, NextBlock.BLOCK_LENGTH + NextBlock.BLOCK_GAP))
                .push(Tween.set(blockOrderQueue.get(4), SpriteAccessor.POSITION).targetRelative(0, NextBlock.BLOCK_LENGTH + NextBlock.BLOCK_GAP))
                .push(Tween.set(blockOrderQueue.get(5), SpriteAccessor.POSITION).targetRelative(0, NextBlock.BLOCK_LENGTH + NextBlock.BLOCK_GAP))
                .push(Tween.set(blockOrderQueue.get(6), SpriteAccessor.POSITION).targetRelative(0, NextBlock.BLOCK_LENGTH + NextBlock.BLOCK_GAP))
                .push(Tween.set(blockOrderQueue.get(6), SpriteAccessor.ALPHA).target(0f))
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
