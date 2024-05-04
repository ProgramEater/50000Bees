package com.example.a50beees;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;

import java.util.ArrayList;
import java.util.logging.Logger;

public class SandboxView extends View {
    int timerUpdateInterval = 20;
    class Timer extends CountDownTimer {
        boolean running = false;
        public Timer() {
            super(Integer.MAX_VALUE, timerUpdateInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            update();
        }

        @Override
        public void onFinish() {
        }
    }

    Timer timer;
    double scaleFactor = 1.0;
    double canvasX = 0, canvasY = 0;
    double focalX = 0, focalY = 0;

    double lastTouchX = 0, lastTouchY = 0;

    Paint paint = new Paint();

    SpriteGroup<Sprite> entities = new SpriteGroup<>();
    // list of platforms/blocks that interact with entities
    ArrayList<Rect> effectors = new ArrayList<>();

    public SandboxView(Context context) {
        super(context);
        onStart(context);
    }

    public SandboxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onStart(context);
    }

    public SandboxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        onStart(context);
    }

    public void onStart(Context context) {
        timer = new Timer();
        timer.start();

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        effectors.add(new Rect(0, 0, 10, getHeight()));
        effectors.add(new Rect(this.getWidth() - 10, 0, this.getWidth(), this.getHeight()));
        effectors.add(new Rect(0, 0, this.getWidth(), 10));
        effectors.add(new Rect(0, this.getHeight() - 10, this.getWidth(), this.getHeight()));

    }

    private void update() {
        entities.updateAll();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        canvas.scale((float) scaleFactor, (float) scaleFactor);
        canvas.translate((float) (canvasX), (float) (canvasY));

        super.onDraw(canvas);
        entities.drawAll(canvas);

        for (Rect r : effectors) {
            canvas.drawRect(r, paint);
        }

        canvas.restore();
    }

    private int mActivePointerId = MotionEvent.INVALID_POINTER_ID;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);

        if (mScaleDetector.isInProgress()) return false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                canvasX += (-lastTouchX + event.getX()) / scaleFactor * .6;
                canvasY += (-lastTouchY + event.getY()) / scaleFactor * .6;

                lastTouchX = event.getX();
                lastTouchY = event.getY();

                invalidate();
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                performClick();

                double canvasEventX = ((event.getX()) / scaleFactor - canvasX);
                double canvasEventY = ((event.getY()) / scaleFactor - canvasY);
                Log.i("1", String.format("%s %s - %s %s", canvasEventX, canvasEventY, event.getX(), event.getY()));
                Log.i("1", String.format("%s %s canvasTranslate", canvasX, canvasY));

                // get a pointer
                lastTouchX = event.getX();
                lastTouchY = event.getY();

                if (mActivePointerId == MotionEvent.INVALID_POINTER_ID) mActivePointerId = event.getPointerId(0);

                // they are drawn on canvas therefore use canvas coords in the list
                entities.add(new Bee(entities, getContext(), effectors,
                        new Rect((int) canvasEventX, (int) canvasEventY,
                                (int) canvasEventX + 50, (int) canvasEventY + 50)));

                break;
            }

            case MotionEvent.ACTION_UP: {
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = event.getActionIndex();
                final int pointerId = event.getPointerId(pointerIndex);

                if (pointerId == mActivePointerId) {
                    // This is the active pointer going up. Choose a new
                    // active pointer and adjust it accordingly.

                    // no idea what that means Im watching top gear
                    lastTouchX = event.getX();
                    lastTouchY = event.getY();
                    mActivePointerId = MotionEvent.INVALID_POINTER_ID;
                    mActivePointerId = event.getPointerId(0);
                }
                break;
            }
        }

        return true;
    }

    private ScaleGestureDetector mScaleDetector;

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(@NonNull ScaleGestureDetector detector) {
            focalX = detector.getFocusX();
            focalY = detector.getFocusY();

            return super.onScaleBegin(detector);
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            canvasX += focalX * (1 - detector.getScaleFactor());
            canvasY += focalY * (1 - detector.getScaleFactor());

            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.01f, Math.min(50.0f, scaleFactor));

            invalidate();
            return true;
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
