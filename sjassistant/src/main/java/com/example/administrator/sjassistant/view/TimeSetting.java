package com.example.administrator.sjassistant.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/3/30.
 */
public class TimeSetting extends View {

    //第一次进来
    private int count = 1;


    private int length;

    private float percent;

    private Context context;

    //View的宽高
    private int height;
    private int width;

    private float mScreenWidth = 0;
    private float mScreenHeight = 0;

    private float textSize = 15.0f;

    private float padding;
    private float margin;

    //开始和结束坐标 以及运动过程中坐标
    private float startX = -10;
    private float startY;
    private float endX;
    private float endY;
    private float offsetX;
    private float offsetY;

    //圆半径
    private float radius = 20.0f;

    public TimeSetting(Context context) {
        this(context, null);
    }

    public TimeSetting(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeSetting(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = wm.getDefaultDisplay().getWidth();
        mScreenHeight = wm.getDefaultDisplay().getHeight();

        margin =  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        padding = getPaddingLeft();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (int)(2 * radius);
        }

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width =(int)(mScreenWidth - 2 * getPaddingLeft());
        }

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Log.d("tag","tafdsfsdf");
        drawLine(canvas);
        if(count != 1) {
            drawCircle(canvas, startX, startY);
            drawRedLine(canvas,offsetX,startX);
            drawEndCircle(canvas,endX,endY);
        }
    }

    private void drawLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(15);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#e3e3e3"));
        canvas.drawLine(padding, (float) height / 2, (float) width - padding, (float) height / 2, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                offsetX = -10;
                offsetY = -10;
                endY = -10;
                endX = -10;

                startX = event.getX();
                startY = event.getY();
                invalidate();
                count = 2;
                break;
            case MotionEvent.ACTION_MOVE:
                offsetX = event.getX();
                offsetY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                endX = event.getX();
                endY = event.getY();
                invalidate();
                break;
        }
        return true;
    }

    private void drawCircle(Canvas canvas,float startX,float startY) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#2EB690"));
        if (startY < height / 2 + 15 || startY > height / 2 - 15) {
            canvas.drawCircle(startX, height/2, radius, mPaint);
        }
    }

    private void drawEndCircle(Canvas canvas,float endX,float endY) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#2EB690"));
        if (endX > startX) {

            canvas.drawCircle(endX, height/2, radius, mPaint);
        }
    }

    private void drawRedLine(Canvas canvas,float offsetX,float startX) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(16);
        mPaint.setColor(Color.parseColor("#2EB690"));
        if (offsetX > startX) {
            canvas.drawLine(startX, (float) height / 2, offsetX, (float) height / 2, mPaint);
        }
    }

    private void drawText(Canvas canvas,float startX,float startY) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#6D6D6D"));
        mPaint.setTextSize(textSize);

    }

    private void drawEndText(Canvas canvas,float endX,float endY) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#6D6D6D"));
        mPaint.setTextSize(textSize);
    }
}
