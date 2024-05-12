package com.example.a50beees;

import android.content.Context;
import android.graphics.Canvas;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class SandboxView extends View {
    int timerUpdateInterval = 20;
    class Timer extends CountDownTimer {
        boolean running = false;
        int tick = 0;
        public Timer() {
            super(Integer.MAX_VALUE, timerUpdateInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            tick++;
            update();
        }

        @Override
        public void onFinish() {
        }
    }

    static Timer timer;
    double scaleFactor = 1.0;
    double canvasX = 0, canvasY = 0;
    double focalX = 0, focalY = 0;
    double lastTouchX, lastTouchY;


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

        mGestureDetector = new GestureDetector(context, new MyGestureDetector());
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int sH = this.getHeight(), sW = this.getWidth();
        int x1 = -sW, y1 = -sH;
        int newW = 2 * sW, newH = 4 * sH;
        effectors.add(new Rect(x1, y1, x1 + newW, y1 + 20));
        effectors.add(new Rect(x1, y1, x1 + 20, y1 + newH));
        effectors.add(new Rect(x1 + newW, y1, x1 + newW + 20, y1 + newH + 20));
        effectors.add(new Rect(x1, y1 + newH, x1 + newW, y1 + newH + 20));

    }

    private void update() {
        entities.updateAll();
        invalidate();
    }

    public static int get_time() {return timer.tick;}

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);

        if (mScaleDetector.isInProgress()) return false;

        return mGestureDetector.onTouchEvent(event);
    }

    private GestureDetector mGestureDetector;

    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) {
            lastTouchX = event.getX();
            lastTouchY = event.getY();

            // don't return false here or else none of the other
            // gestures will work
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            double canvasEventX = ((event.getX()) / scaleFactor - canvasX);
            double canvasEventY = ((event.getY()) / scaleFactor - canvasY);

            // they are drawn on canvas therefore use canvas coords in the list
            entities.add(new Rabbit(entities, effectors,
                    new Rect((int) canvasEventX, (int) canvasEventY,
                            (int) canvasEventX + 50, (int) canvasEventY + 50)));
            return true;
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {}

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            onSingleTapConfirmed(e);
            return onSingleTapConfirmed(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, @NonNull MotionEvent e2,
                                float distanceX, float distanceY) {
            canvasX += (-lastTouchX + e2.getX()) / scaleFactor * .6;
            canvasY += (-lastTouchY + e2.getY()) / scaleFactor * .6;

            lastTouchX = e2.getX();
            lastTouchY = e2.getY();

            invalidate();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, @NonNull MotionEvent event2,
                               float velocityX, float velocityY) {
            return true;
        }
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
