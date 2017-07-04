package com.lazybean.yaypipe.gamehelper;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.lazybean.yaypipe.gameobjects.Block;

public class PathLoader {
    //path coordinates for fromLeftToRight
    private static Array<Vector2> straightPathPos = new Array<Vector2>();

    //average value of derivative lengths
    public static float straightPathDerAvg;

    //path coordinates for fromLeftToTop
    private static Array<Vector2> curvePathPos = new Array<Vector2>();

    //average value of derivative lengths
    public static float curvePathDerAvg;

    public static Array<Vector2> startFromTop, startFromBottom, startFromLeft, startFromRight;
    public static Array<Vector2> fromLeftToRight, fromRightToLeft, fromBottomToTop, fromTopToBottom;
    public static Array<Vector2> fromLeftToTop, fromTopToLeft, fromRightToTop, fromTopToRight;
    public static Array<Vector2> fromLeftToBottom, fromBottomToLeft, fromRightToBottom, fromBottomToRight;

    public static void load() {
        float blockLength = Block.LENGTH;
        float blockGap = Block.GAP;
        float offset = blockLength * (25f / 115f);

        Vector2 pos1 = new Vector2(0, blockLength / 2);
        Vector2 pos2 = new Vector2(blockLength + blockGap + 2, pos1.y);

        Bezier<Vector2> straightPath = new Bezier<Vector2>(pos1, pos2);

        float sum = 0;
        int sampleNumStraight = (int) straightPath.approxLength(50);
        sampleNumStraight *= 2;
        for (int i = 0; i < sampleNumStraight; i++) {
            Vector2 tmp1 = new Vector2();
            Vector2 tmp2 = new Vector2();
            straightPath.derivativeAt(tmp1, (float) i / (float) (sampleNumStraight - 1));
            sum += tmp1.len();

            straightPath.valueAt(tmp2, (float) i / ((float) sampleNumStraight - 1));
            straightPathPos.add(tmp2);
        }
        straightPathDerAvg = sum / (float) sampleNumStraight;

        pos1 = new Vector2(0, blockLength / 2);
        pos2 = new Vector2(offset, pos1.y);
        Array<Bezier<Vector2>> curvePath = new Array<Bezier<Vector2>>(3);
        curvePath.add(new Bezier<Vector2>(pos1, pos2));

        Vector2 pos3 = new Vector2(pos2);
        Vector2 pos4 = new Vector2(pos3.x + blockLength / 2 - offset, pos3.y);
        Vector2 pos5 = new Vector2(pos3.x + blockLength / 2 - offset, pos3.y + blockLength / 2 - offset);
        curvePath.add(new Bezier<Vector2>(pos3, pos4, pos5));

        Vector2 pos6 = new Vector2(pos5);
        Vector2 pos7 = new Vector2(pos6.x, blockLength + blockGap + 2);
        curvePath.add(new Bezier<Vector2>(pos6, pos7));

        sum = 0;
        for (int i = 0; i < 3; i++) {
            int sampleNumCurve = (int) curvePath.get(i).approxLength(50);
            sampleNumCurve *= 2;
            for (int j = 0; j < sampleNumCurve; j++) {
                Vector2 tmp1 = new Vector2();
                Vector2 tmp2 = new Vector2();
                curvePath.get(i).derivativeAt(tmp1, (float) j / (float) (sampleNumCurve - 1));
                sum += tmp1.len();

                curvePath.get(i).valueAt(tmp2, (float) j / (float) (sampleNumCurve - 1));
                curvePathPos.add(tmp2);
            }
            curvePathDerAvg += sum / (float) sampleNumCurve;
        }
        curvePathDerAvg = curvePathDerAvg / 3f;


        //load all the path using the templates
        fromLeftToRight = new Array<Vector2>(straightPathPos);

        startFromLeft = new Array<Vector2>();
        for (int i = (int) (straightPathPos.size * 0.35); i < straightPathPos.size; i++) {
            startFromLeft.add(fromLeftToRight.get(i).cpy());
        }

        fromRightToLeft = new Array<Vector2>();
        for (int i = 0; i < fromLeftToRight.size; i++) {
            fromRightToLeft.add(fromLeftToRight.get(i).cpy());
            fromRightToLeft.get(i).sub(blockLength / 2, blockLength / 2);
            fromRightToLeft.get(i).rotate(180);
            fromRightToLeft.get(i).add(blockLength / 2, blockLength / 2);
        }

        startFromRight = new Array<Vector2>();
        for (int i = (int) (straightPathPos.size * 0.35); i < straightPathPos.size; i++) {
            startFromRight.add(fromRightToLeft.get(i).cpy());
        }

        fromBottomToTop = new Array<Vector2>();
        for (int i = 0; i < fromLeftToRight.size; i++) {
            fromBottomToTop.add(fromLeftToRight.get(i).cpy());
            fromBottomToTop.get(i).sub(blockLength / 2, blockLength / 2);
            fromBottomToTop.get(i).rotate90(1);
            fromBottomToTop.get(i).add(blockLength / 2, blockLength / 2);
        }

        startFromBottom = new Array<Vector2>();
        for (int i = (int) (straightPathPos.size * 0.35); i < straightPathPos.size; i++) {
            startFromBottom.add(fromBottomToTop.get(i).cpy());
        }

        fromTopToBottom = new Array<Vector2>();
        for (int i = 0; i < fromBottomToTop.size; i++) {
            fromTopToBottom.add(fromBottomToTop.get(i).cpy());
            fromTopToBottom.get(i).sub(blockLength / 2, blockLength / 2);
            fromTopToBottom.get(i).rotate(180);
            fromTopToBottom.get(i).add(blockLength / 2, blockLength / 2);
        }

        startFromTop = new Array<Vector2>();
        for (int i = (int) (straightPathPos.size * 0.35); i < straightPathPos.size; i++) {
            startFromTop.add(fromTopToBottom.get(i).cpy());
        }


        fromLeftToTop = new Array<Vector2>(curvePathPos);

        fromRightToBottom = new Array<Vector2>();
        for (int i = 0; i < curvePathPos.size; i++) {
            fromRightToBottom.add(fromLeftToTop.get(i).cpy());
            fromRightToBottom.get(i).sub(blockLength / 2, blockLength / 2);
            fromRightToBottom.get(i).rotate(180);
            fromRightToBottom.get(i).add(blockLength / 2, blockLength / 2);
        }

        fromTopToRight = new Array<Vector2>();
        for (int i = 0; i < curvePathPos.size; i++) {
            fromTopToRight.add(fromRightToBottom.get(i).cpy());
            fromTopToRight.get(i).sub(blockLength / 2, blockLength / 2);
            fromTopToRight.get(i).rotate90(1);
            fromTopToRight.get(i).add(blockLength / 2, blockLength / 2);
        }

        fromBottomToLeft = new Array<Vector2>();
        for (int i = 0; i < curvePathPos.size; i++) {
            fromBottomToLeft.add(fromTopToRight.get(i).cpy());
            fromBottomToLeft.get(i).sub(blockLength / 2, blockLength / 2);
            fromBottomToLeft.get(i).rotate(180);
            fromBottomToLeft.get(i).add(blockLength / 2, blockLength / 2);
        }

        Matrix3 matrix = new Matrix3(new float[]{0, 1, 0, 1, 0, 0, 0, 0, 1});
        fromLeftToBottom = new Array<Vector2>();
        for (int i = 0; i < curvePathPos.size; i++) {
            fromLeftToBottom.add(fromBottomToLeft.get(i).cpy());
            fromLeftToBottom.get(i).sub(blockLength / 2, blockLength / 2);
            fromLeftToBottom.get(i).mul(matrix);
            fromLeftToBottom.get(i).add(blockLength / 2, blockLength / 2);
        }

        fromRightToTop = new Array<Vector2>();
        for (int i = 0; i < curvePathPos.size; i++) {
            fromRightToTop.add(fromLeftToBottom.get(i).cpy());
            fromRightToTop.get(i).sub(blockLength / 2, blockLength / 2);
            fromRightToTop.get(i).rotate(180);
            fromRightToTop.get(i).add(blockLength / 2, blockLength / 2);
        }

        fromTopToLeft = new Array<Vector2>();
        for (int i = 0; i < curvePathPos.size; i++) {
            fromTopToLeft.add(fromRightToTop.get(i).cpy());
            fromTopToLeft.get(i).sub(blockLength / 2, blockLength / 2);
            fromTopToLeft.get(i).rotate90(1);
            fromTopToLeft.get(i).add(blockLength / 2, blockLength / 2);
        }

        fromBottomToRight = new Array<Vector2>();
        for (int i = 0; i < curvePathPos.size; i++) {
            fromBottomToRight.add(fromTopToLeft.get(i).cpy());
            fromBottomToRight.get(i).sub(blockLength / 2, blockLength / 2);
            fromBottomToRight.get(i).rotate(180);
            fromBottomToRight.get(i).add(blockLength / 2, blockLength / 2);
        }
    }

    public static void dispose(){
        straightPathPos.clear();
        curvePathPos.clear();
        startFromTop.clear();
        startFromBottom.clear();
        startFromLeft.clear();
        startFromRight.clear();
        fromLeftToRight.clear();
        fromRightToLeft.clear();
        fromBottomToTop.clear();
        fromTopToBottom.clear();
        fromLeftToTop.clear();
        fromTopToLeft.clear();
        fromRightToTop.clear();
        fromTopToRight.clear();
        fromLeftToBottom.clear();
        fromBottomToLeft.clear();
        fromRightToBottom.clear();
        fromBottomToRight.clear();
        straightPathDerAvg = 0;
        curvePathDerAvg = 0;
    }
}
