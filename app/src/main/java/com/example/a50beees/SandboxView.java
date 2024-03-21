package com.example.a50beees;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;

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

    ArrayList<Entity> entities = new ArrayList<>();

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

    private void update() {
        for (Entity e : entities) {
            e.update();
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.scale(mScaleFactor, mScaleFactor);

        for (Entity e : entities) {
                e.draw(canvas);
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        performClick();

        mScaleDetector.onTouchEvent(event);

        entities.add(new Bee(getContext(),
                new Rect((int) event.getX(), (int) event.getY(),
                        (int) event.getX() + 50, (int) event.getY() + 50)));
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            invalidate();
            return true;
        }
    }
}
