package com.rage.tangentcircle;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by nzq on 17/3/2.
 */

public class TangentCircleView extends View {
    int mWidth;
    int mHeight;
    Paint mPaint;
    int BigRound = 300;
    int smallRound = 274;
    int mNum = 6;
    int mDuration = 2600;
    float devider = 0.35f;
    ArrayList<float[]> smallCircleList = new ArrayList<>();

    float xChange;
    float yChange;

    Rect mTextBound;
    int baseline;

    AnimatorSet animatorSet;
    Paint.FontMetricsInt fontMetrics;

    Boolean isStarted = false;
    String mText = "99%";
    private List<Animator> mAnimList = new ArrayList<>();

    public TangentCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setTextSize(70);
        mTextBound = new Rect();
        fontMetrics = mPaint.getFontMetricsInt();
        mPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
        baseline = (mTextBound.bottom - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;

        startAnimator();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, mHeight / 2);

        mPaint.setAlpha(175);
        canvas.drawCircle(0, 0, BigRound, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(mText,
                0, baseline, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < mNum; i++) {
            mPaint.setAlpha(255 * (mNum ) / mNum);
            canvas.drawCircle(smallCircleList.get(i)[0], smallCircleList.get(i)[1], smallRound, mPaint);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }


    private void startAnimator() {
        isStarted = true;
        animatorSet = new AnimatorSet();
        for (int i = 0; i < mNum; i++) {
            final int positon = i;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat((float) Math.PI * 2 + devider * i, 0f + devider * i);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    xChange = (float) ((BigRound - smallRound) * Math.sin((float) animation.getAnimatedValue()));
                    yChange = (float) ((BigRound - smallRound) * Math.cos((float) animation.getAnimatedValue()));
                    float[] tempList = {xChange, yChange};
                    smallCircleList.add(positon, tempList);
                    if (positon == mNum - 1) {
                        invalidate();
                    }
                }
            });
            valueAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            valueAnimator.setRepeatMode(ObjectAnimator.RESTART);
            mAnimList.add(valueAnimator);

        }

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.e("tcv", "start" + isStarted);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isStarted = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isStarted = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.e("tcv", "onAnimationRepeat" + isStarted);
            }
        });
        animatorSet.playTogether(mAnimList);
        animatorSet.setDuration(mDuration);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();
    }

    public void setText(String text) {
        if (TextUtils.isEmpty(text)) {
            mText = text;
        }
    }

    public void startTangent() {
        if (!isStarted) {
            startAnimator();
        }
    }

    public void stopTangent() {
        isStarted = false;
        animatorSet.end();
        smallCircleList.clear();
        for (int i = 0; i < mNum; i++) {
            float[] initData = {0, 0};
            smallCircleList.add(i, initData);
        }
        invalidate();
    }

}
