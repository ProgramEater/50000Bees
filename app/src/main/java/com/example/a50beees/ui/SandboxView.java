package com.example.a50beees.ui;

import android.content.Context;
import android.graphics.Canvas;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.AttributeSet;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.a50beees.ui.Activities.SandboxActivity;
import com.example.a50beees.ui.Entities.Bee;
import com.example.a50beees.ui.Entities.Entity;
import com.example.a50beees.ui.Entities.Rabbit;
import com.example.a50beees.ui.Entities.SpriteGroup;

import java.util.ArrayList;

public class SandboxView extends View {
    static private final int timerUpdateInterval = 20;
    class Timer extends CountDownTimer {
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
    static private double time_coefficient = 1;
    private double scaleFactor = 1.0;
    private double canvasX = 0, canvasY = 0;
    private double focalX = 0, focalY = 0;
    private double lastTouchX, lastTouchY;

    private int borderW, borderH;
    private SandboxBackground backgroundController;

    private final Paint paint = new Paint();

    private final static SpriteGroup<Entity> entities = new SpriteGroup<>();
    // list of platforms/blocks that interact with entities
    private final static ArrayList<Rect> effectors = new ArrayList<>();

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

        borderW = 1500 * 4;
        borderH = 1500 * 4;

        backgroundController = new SandboxBackground(borderW, borderH);

        int pW = 1000;
        effectors.add(new Rect(-pW, -pW, borderW + pW, 0));
        effectors.add(new Rect(-pW, 0, 0, borderH + pW));
        effectors.add(new Rect(borderW, 0, borderW + pW, borderH + pW));
        effectors.add(new Rect(0, borderH, borderW + pW, borderH + pW));

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

        backgroundController.draw(canvas, paint);

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
        if (event.getPointerCount() >= 2) return false;

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
            String spawn_entity_type = ((SandboxActivity) getContext()).getSpawn_entity_type();

            int number_of_entities = 5;

            switch (spawn_entity_type) {
                case "bee": for (int i = 0; i <= number_of_entities; i++) entities.add(new Bee(new Rect((int) canvasEventX, (int) canvasEventY, 0, 0))); break;
                case "rabbit": for (int i = 0; i <= number_of_entities; i++) entities.add(new Rabbit(new Rect((int) canvasEventX, (int) canvasEventY, 0, 0))); break;
                default:  Log.i("SandboxView onSingleTapConfirmed", String.format("Spawn entity not found: %s", spawn_entity_type));
            }

            return true;
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {}

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent event) {
            onSingleTapConfirmed(event);
            onSingleTapConfirmed(event);
            return true;

        }

        @Override
        public boolean onScroll(MotionEvent e1, @NonNull MotionEvent e2,
                                float distanceX, float distanceY) {
            if (mScaleDetector.isInProgress()) {
                lastTouchX = e2.getX();
                lastTouchY = e2.getY();
                return false;
            }

            canvasX += (-lastTouchX + e2.getX()) / scaleFactor * .6;
            canvasY += (-lastTouchY + e2.getY()) / scaleFactor * .6;

            lastTouchX = e2.getX();
            lastTouchY = e2.getY();

            canvasX = Math.min(Math.max(-borderW, canvasX), getWidth() / scaleFactor);
            canvasY = Math.min(Math.max(-borderH, canvasY), getHeight() / scaleFactor);

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
            scaleFactor = Math.max(0.1f, Math.min(30.f, scaleFactor));

            invalidate();
            return true;
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void setTime_coefficient(float v) {
        time_coefficient = v;
    }

    public static double getTime_coefficient() {
        return time_coefficient;
    }

    public static int getTimerUpdateInterval() {
        return timerUpdateInterval;
    }

    public static ArrayList<Rect> getEffectors() {
        return effectors;
    }

    public static SpriteGroup<Entity> getEntities() {
        return entities;
    }
}
