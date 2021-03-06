package com.example.administrator.sjassistant.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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

    //状态标识
    private int count = 1;


    private int length;

    private float percent;

    private Context context;

    //View的宽高
    private int height;
    private int width;

    //线的宽
    private float lineWidth;

    private float mScreenWidth = 0;
    private float mScreenHeight = 0;

    //字体大小
    private float textSize = 26.0f;
    private float textHeight;

    private float padding;
    private float margin;

    //开始和结束坐标 以及运动过程中坐标
    private float startX = -10;
    private float startY;
    private float endX;
    private float endY;
    private float offsetX;
    private float offsetY;

    //勿扰模式开始结束时间
    private int beginTime = -1,endTime = -1;

    //圆半径
    private float radius = 20.0f;


    Paint paint;
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
       paint = new Paint();
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
            height = (int)(2 * radius) + 150;
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
        Log.d("tag", "count:  " + count);
        drawLine(canvas);
        if(count != 1) {
            drawCircle(canvas, startX, startY);
            drawRedLine(canvas,offsetX,startX);
            drawEndCircle(canvas,endX,endY);
        }
        //抬起
        if (count == 3) {
            Log.d("tag","draw");
            if (endX > startX) {
                if (endX - startX > 100) {
                    //小球在最左边防止文字出界
                    if (startX < 40) {
                        drawText(canvas, startX + 40, startY, 3);
                        //drawEndText(canvas, endX + 40, startY, 3);
                    } else {
                        drawText(canvas, startX, startY, 3);

                    }
                    drawEndText(canvas, endX, startY, 3);

                } else {
                    if (startX < 40) {
                        drawText(canvas, startX + 40, startY, 3);
                    } else
                        drawText(canvas, startX, startY, 3);
                    drawEndText(canvas, endX, startY, 4);
                }
            }
        }
    }

    /*
     * 画时间线
     */
    private void drawLine(Canvas canvas) {

        paint.setStrokeWidth(15);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#e3e3e3"));
        canvas.drawLine(padding, radius, (float) width - padding, radius, paint);

        lineWidth = width - 2 * padding;
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
                count = 3;
                invalidate();

                if (onHandUpListener != null) {
                    onHandUpListener.onHandUp(beginTime,endTime,startX,endX,offsetX);
                }
                break;
        }
        return true;
    }

    /*
     * 画起始的圆
     */
    private void drawCircle(Canvas canvas,float startX,float startY) {

        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#2EB690"));
        if (startY < height / 2 + 15 || startY > height / 2 - 15) {
            canvas.drawCircle(startX,radius , radius, paint);
        }
    }

    /*
     * 画松手的圆
     */
    private void drawEndCircle(Canvas canvas,float endX,float endY) {

        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#2EB690"));
        if (endX > startX && endX < width - 2 * padding) {

            canvas.drawCircle(endX, radius, radius, paint);
        }
    }

    /*
     * 画手指移动时的线
     */
    private void drawRedLine(Canvas canvas,float offsetX,float startX) {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(16);
        paint.setColor(Color.parseColor("#2EB690"));
        if (offsetX > startX) {
            canvas.drawLine(startX, radius, offsetX, radius,  paint);
        }
    }

    /*
     * 画开始的文本
     */
    private void drawText(Canvas canvas,float startX,float startY,int n) {
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#6D6D6D"));
        paint.setTextSize(textSize);


        Paint.FontMetrics fm = paint.getFontMetrics();
        textHeight = fm.descent-fm.ascent;

        percent = (startX-padding) / lineWidth;

        int value = (int)(percent * 24);
        RectF rect = new RectF();
        rect.set(startX - 20, startY, startX + 20, startY + n * textHeight);

        beginTime = value;
        canvas.drawText(String.valueOf(value)+"点开始",startX - 40,rect.bottom - fm.descent,paint);

    }


    /*
     * 画结尾的文本
     */
    private void drawEndText(Canvas canvas,float endX,float startY,int n) {
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#6D6D6D"));
        paint.setTextSize(textSize);

        Paint.FontMetrics fm = paint.getFontMetrics();
        textHeight = fm.descent-fm.ascent;

        percent = (endX-padding) / lineWidth;

        int value = (int)(percent * 24);
        RectF rect = new RectF();
        rect.set(endX - 20, startY, endX + 20, startY + n * textHeight);

        endTime = value;

        canvas.drawText(String.valueOf(value) + "点结束", endX - 40, rect.bottom - fm.descent, paint);
    }

    public interface OnHandUpListener {
        public void onHandUp(int beginTime,int endTime,float startX,float endX,float offsetX);
    }

    private OnHandUpListener onHandUpListener;

    public void setOnHandUpListener(OnHandUpListener onHandUpListener) {
        this.onHandUpListener = onHandUpListener;
    }

    /*
     * Resume后的设置值  并重绘
     */
    public void setValue (float startX,float endX,float offsetX) {
        this.startX = startX;
        this.endX = endX;
        this.offsetX = offsetX;
        count = 3;
        invalidate();
    }
}
