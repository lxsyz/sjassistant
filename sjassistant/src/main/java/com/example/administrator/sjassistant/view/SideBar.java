package com.example.administrator.sjassistant.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/4/4.
 */
public class SideBar extends View {

    private OnTouchingLetterChangeListener mOnTouching;

    public static String[] b = {"A","B","C","D","E","F","G","H","I","J","K",
                                "L","M","N","O","P","Q","R","S","T", "U", "V",
                                "W", "X", "Y", "Z" };

    private int choose = -1;       //是否选中

    private Paint paint = new Paint();

    private TextView mTextDialog;

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideBar(Context context) {
        super(context);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();
        int width = getWidth();

        int singleHeight = height / b.length;       //字母对应高度

        for (int i = 0;i < b.length;i++) {
            paint.setColor(Color.parseColor("#969696"));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(20);

            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            if (i == choose) {
                paint.setColor(Color.parseColor("#3399ff"));
                paint.setFakeBoldText(true);

            }
            canvas.drawText(b[i], xPos, yPos, paint);

            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float y = event.getY();
        int oldChoose = choose;

        OnTouchingLetterChangeListener listener = mOnTouching;

        int c = (int)(y/getHeight() * b.length);

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                choose = -1;
                invalidate();
                break;
            default:
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        if (listener != null) {

                            listener.onTouchingLetterChanged(b[c]);
                        }
                        choose = c;
                        invalidate();
                    }
                }
                break;
        }


        return true;

    }


    public void setmOnTouching(OnTouchingLetterChangeListener mOnTouching) {
        this.mOnTouching = mOnTouching;
    }

    public interface OnTouchingLetterChangeListener {
        public void onTouchingLetterChanged(String s);
    }
}
