package com.example.a50beees.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class MainActivityBackgroundView extends View {
    final Paint paint = new Paint();
    int face_x, face_y, face_w, face_h;
    BeeWatcher beeWatcher;
    public MainActivityBackgroundView(Context context) {
        super(context);
        beeWatcher = new BeeWatcher(getWidth() / 7, getWidth(), getHeight());
    }

    public MainActivityBackgroundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        beeWatcher = new BeeWatcher(getWidth() / 7, getWidth(), getHeight());
    }

    public MainActivityBackgroundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        beeWatcher = new BeeWatcher(getWidth() / 7, getWidth(), getHeight());
    }

    class Circle {
        int x, y, radius;

        Circle(int x, int y, int radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

        public int left() {
            return x - radius;
        }
        public int top() {
            return y - radius;
        }

    }

    class BeeWatcher {
        Circle[] circles;
        int max_w, max_h;

        BeeWatcher(int r, int max_w, int max_h) {
            circles = new Circle[] {new Circle(max_w / 2, max_h / 2, r),
                                    new Circle(max_w / 2, max_h / 2, (int) (r * 1.6)),
                                    new Circle(max_w / 2, max_h / 2, (int) (r * 1.6 * 1.5)),
                                    new Circle(max_w / 2, max_h / 2, (int) (r * 1.6 * 1.5 * 1.3)),
                                    new Circle(max_w / 2, max_h / 2, (int) (r * 1.6 * 1.5 * 1.3 * 1.1)),
                                    new Circle(max_w / 2, max_h / 2, (int) (r * 7))};
            this.max_h = max_h;
            this.max_w = max_w;
        }

        public void set_to(double propX, double propY) {
            for (int i = circles.length - 2; i >= 0; i--) {
                Circle c = circles[i];

                c.x = (int) (circles[i + 1].x + (double) (circles[i + 1].radius - c.radius) * propX * (i <= 1 ? 2 - i : 1));
                c.y = (int) (circles[i + 1].y + (double) (circles[i + 1].radius - c.radius) * propY * (i <= 1 ? 2 - i : 1));
            }
        }

        public void draw(Canvas canvas) {
            for (int i = circles.length - 1; i >= 0; i--) {
                if (i / 2 * 2 == i) paint.setColor(Color.parseColor("#FFC300"));
                else paint.setColor(Color.BLACK);

                Circle c = circles[i];
                canvas.drawCircle(c.x, c.y, c.radius, paint);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        beeWatcher = new BeeWatcher(getWidth() / 5, getWidth(), getHeight());

        face_w = getWidth() / 20;
        face_h = getHeight() / 20;

        face_x = getWidth() / 2 - face_w;
        face_y = getHeight() / 2 - face_h;

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        double propX = Math.pow(Math.abs((double) event.getX() / getWidth() - 0.5),  1) * (event.getX() >= getWidth() / 2 ? 1 : -1) / 3;
        double propY = Math.pow(Math.abs((double) event.getY() / getHeight() - 0.5), 1) * (event.getY() >= getHeight() / 2 ? 1 : -1) / 5;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                beeWatcher.set_to(propX, propY);

                Circle fcircle = beeWatcher.circles[0];

                int gap_x = face_w + (int) (fcircle.radius * 0.3), gap_y = face_h + (int) (fcircle.radius * 0.3);
                if (event.getX() < getWidth() / 2.f) face_x = (int) Math.max(fcircle.left() + face_w, event.getX());
                else face_x = (int) Math.min(fcircle.left() + fcircle.radius * 2 - gap_x, event.getX());

                if (event.getY() < getHeight() / 2.f) face_y = (int) Math.max(fcircle.top() + face_h, event.getY());
                else face_y = (int) Math.min(fcircle.top() + fcircle.radius * 2 - gap_y, event.getY());

                face_y = Math.min(Math.max(0, face_y), getHeight() - face_h);

                invalidate();
                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                beeWatcher.set_to(propX, propY);

                Circle fcircle = beeWatcher.circles[0];

                int gap_x = face_w + (int) (fcircle.radius * 0.3), gap_y = face_h + (int) (fcircle.radius * 0.3);
                if (event.getX() < getWidth() / 2.f) face_x = (int) Math.max(fcircle.left() + face_w, event.getX());
                else face_x = (int) Math.min(fcircle.left() + fcircle.radius * 2 - gap_x, event.getX());

                if (event.getY() < getHeight() / 2.f) face_y = (int) Math.max(fcircle.top() + face_h, event.getY());
                else face_y = (int) Math.min(fcircle.top() + fcircle.radius * 2 - gap_y, event.getY());

                face_y = Math.min(Math.max(0, face_y), getHeight() - face_h);

                invalidate();
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.FILL);
        beeWatcher.draw(canvas);

        draw_face(canvas);
    }

    private void draw_face(Canvas canvas) {
        paint.setColor(Color.BLACK);

        canvas.drawCircle((float) (face_x), (float) (face_y ), face_w / 5, paint);
        canvas.drawCircle((float) (face_x + face_w), (float) (face_y), face_w / 5, paint);
        canvas.drawRect(face_x + face_w / 5, face_y + face_h, face_x + face_w * 4 / 5, face_y + face_h + face_w / 5, paint);
    }


}
