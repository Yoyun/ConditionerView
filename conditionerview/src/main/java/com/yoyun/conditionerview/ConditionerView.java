package com.yoyun.conditionerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Yoyun on 2017/7/21.
 */

public class ConditionerView extends View {
    // 半径
    private float radius;
    //中心X坐标
    private float centerX;
    //中心Y坐标
    private float centerY;

    private RectF conditionerDrawArea;

    private int bgColor = Color.parseColor("#2e2e2e");
    private int layer1Color = Color.parseColor("#181818");
    private int layer2Color = Color.parseColor("#232222");
    private int layer3Color = Color.parseColor("#3d8eb4");
    private int numberColor = Color.parseColor("#aaaaaa");

    private Paint progressPaint;
    private Paint progress1Paint;
    private Paint progressBgPaint;
    private Paint pointerPaint;
    private Paint scalePaint;
    private Paint scaleNumberPaint;

    private float coditionerAngle = 160.0f;
    private float progress = 0;
    private float max = 10;
    private float pointerAngle = 0;
    private OnProgressChangedListener onProgressChangedListener;

    public ConditionerView(Context context) {
        this(context, null);
    }

    public ConditionerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConditionerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ConditionerView, defStyle, 0);

        bgColor = a.getColor(R.styleable.ConditionerView_bgColor, bgColor);
        layer1Color = a.getColor(R.styleable.ConditionerView_layer1Color, layer1Color);
        layer2Color = a.getColor(R.styleable.ConditionerView_layer2Color, layer2Color);
        layer3Color = a.getColor(R.styleable.ConditionerView_layer3Color, layer3Color);
        numberColor = a.getColor(R.styleable.ConditionerView_numberColor, numberColor);
        max = a.getFloat(R.styleable.ConditionerView_max, max);
        progress = a.getFloat(R.styleable.ConditionerView_progress, progress);

        a.recycle();

        updateProgress();

        progress1Paint = new Paint();
        progress1Paint.setAntiAlias(true);
        progress1Paint.setStrokeWidth(6f);
        progress1Paint.setColor(layer1Color);
        progress1Paint.setStrokeCap(Paint.Cap.ROUND);
        progress1Paint.setStyle(Paint.Style.STROKE);

        progressBgPaint = new Paint();
        progressBgPaint.setAntiAlias(true);
        progressBgPaint.setStrokeWidth(16f);
        progressBgPaint.setColor(layer2Color);
        progressBgPaint.setStrokeCap(Paint.Cap.ROUND);
        progressBgPaint.setStyle(Paint.Style.STROKE);

        scalePaint = new Paint();
        scalePaint.setAntiAlias(true);
        scalePaint.setColor(layer2Color);
        scalePaint.setStyle(Paint.Style.FILL);

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeWidth(4f);
        progressPaint.setColor(layer3Color);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStyle(Paint.Style.STROKE);

        pointerPaint = new Paint();
        pointerPaint.setAntiAlias(true);
        pointerPaint.setColor(layer3Color);
        pointerPaint.setStyle(Paint.Style.FILL);

        scaleNumberPaint = new Paint();
        scaleNumberPaint.setAntiAlias(true);
        scaleNumberPaint.setTextSize(20);
        scaleNumberPaint.setTextAlign(Paint.Align.CENTER);
        scaleNumberPaint.setColor(numberColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        float size;
        if (getHeight() > getWidth()) {
            size = Math.min(getWidth(), getHeight());
        } else {
            size = Math.max(getWidth(), getHeight());
        }
        int padding = 32;
        float left = (getWidth() - size) / 2;
        float top = (getHeight() - size) / 2;
        conditionerDrawArea = new RectF(
                left,
                top,
                getWidth() - left,
                getHeight() - top
        );

        conditionerDrawArea.top += conditionerDrawArea.width() / 4;
        conditionerDrawArea.bottom += conditionerDrawArea.width() / 4;

        conditionerDrawArea.set(
                conditionerDrawArea.left + padding,
                conditionerDrawArea.top + padding,
                conditionerDrawArea.right - padding,
                conditionerDrawArea.bottom - padding
        );

        //中心坐标
        centerX = conditionerDrawArea.left + conditionerDrawArea.width() / 2;
        centerY = conditionerDrawArea.top + conditionerDrawArea.height() / 2;
        radius = conditionerDrawArea.width() / 2;
        postInvalidate();
        super.onSizeChanged(w, h, oldW, oldH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(bgColor);
        drawProgressBg(canvas);
        drawProgress(canvas);
        drawPointer(canvas);
        drawScale(canvas);
        drawScaleNumber(canvas);
    }

    private void drawProgressBg(Canvas canvas) {
        canvas.drawArc(conditionerDrawArea, 190, coditionerAngle, false, progressBgPaint);
    }

    private void drawProgress(Canvas canvas) {
        canvas.drawArc(conditionerDrawArea, 190, coditionerAngle, false, progress1Paint);
        float angle = coditionerAngle * (progress / max);

        if (angle > 0) {
            canvas.drawArc(conditionerDrawArea, 190, angle, false, progressPaint);
        }
    }

    private void drawPointer(Canvas canvas) {
        canvas.rotate(pointerAngle, centerX, centerY);
        RectF rectF = new RectF(centerX - 4.0f, centerY - (radius - 32.0f), centerX + 4.0f, centerY + 32.0f);
        canvas.drawRoundRect(rectF, rectF.width() / 2, rectF.width() / 2, pointerPaint);
        canvas.drawCircle(centerX, centerY, 16, pointerPaint);
        canvas.rotate(-pointerAngle, centerX, centerY);
    }

    private void drawScale(Canvas canvas) {
        RectF rectF;
        for (int i = 0; i <= 30; i++) {
            float ao = 190.0f + coditionerAngle / 30.0f * (float) i;
            float[] loc = angle2Loc(centerX, centerY, radius + 10, ao);
            if (i % 3 == 0) {
                rectF = new RectF(loc[0] - 2, loc[1] - 16, loc[0] + 2, loc[1] + 2);
            } else {
                rectF = new RectF(loc[0] - 2, loc[1] - 8, loc[0] + 2, loc[1] + 2);
            }
            canvas.rotate(ao + 90, loc[0], loc[1]);
            canvas.drawRoundRect(rectF, rectF.width() / 2, rectF.width() / 2, scalePaint);
            canvas.rotate(-(ao + 90), loc[0], loc[1]);
        }
    }

    private void drawScaleNumber(Canvas canvas) {
        for (int i = 0; i <= 10; i++) {
            float ao = 190.0f + coditionerAngle / 10.0f * (float) i;
            float[] loc = angle2Loc(centerX, centerY, radius - 26, ao);
            canvas.rotate(ao + 90, loc[0], loc[1]);
            canvas.drawText("" + (int) (max / 10) * i, loc[0], loc[1], scaleNumberPaint);
            canvas.rotate(-(ao + 90), loc[0], loc[1]);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (x < 0 || y < 0 || x > getWidth() || y > getHeight()) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float v = (float) Math.atan2(centerX - event.getX(), centerY - event.getY());
                v = (float) (-v * (180.0f / Math.PI));
                pointerAngle = v >= 0 ? Math.min(coditionerAngle / 2.0f, v) : Math.max(-coditionerAngle / 2.0f, v);
                float tmp = progress;
                progress = (pointerAngle + coditionerAngle / 2.0f) / coditionerAngle * max;
                if (tmp != progress) {
                    postInvalidate();
                    if (onProgressChangedListener != null) {
                        onProgressChangedListener.onProgressChanged(this, progress);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

    private float[] angle2Loc(float ox, float oy, float radius, float angle) {
//        x1   =   x0   +   r   *   cos(ao   *   3.14   /180   )
//        y1   =   y0   +   r   *   sin(ao   *   3.14   /180   )

        return new float[]{
                (float) (ox + radius * Math.cos(angle * Math.PI / 180.0f)),
                (float) (oy + radius * Math.sin(angle * Math.PI / 180.0f))
        };
    }

    public float getProgress() {
        return progress;
    }

    public float getMax() {
        return max;
    }

    public ConditionerView setMax(float max) {
        this.max = max;
        this.progress = Math.min(this.progress, this.max);
        updateProgress();
        postInvalidate();
        return this;
    }

    public ConditionerView setProgress(float progress) {
        if (progress < 0 || progress > max) {
            return this;
        }

        this.progress = progress;
        updateProgress();
        postInvalidate();
        return this;
    }

    public ConditionerView setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.onProgressChangedListener = onProgressChangedListener;
        return this;
    }

    private void updateProgress() {
        pointerAngle = coditionerAngle * (progress * 1.0f / max) - coditionerAngle / 2.0f;
    }

    public interface OnProgressChangedListener {
        void onProgressChanged(ConditionerView view, float progress);
    }
}
