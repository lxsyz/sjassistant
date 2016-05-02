package com.example.administrator.sjassistant.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;


/**
 * Created by Administrator on 2016/4/29.
 */
public class HListView extends ListView {

    private GestureDetector mGesture;

    /** 列头 */
    public LinearLayout mListHead;
    /** 偏移坐标 */
    private int mOffset = 0;
    /** 屏幕宽度 */
    private int screenWidth;

    public HListView(Context context,AttributeSet attributes) {
        super(context,attributes);
        mGesture = new GestureDetector(context,mOnGesture);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return mGesture.onTouchEvent(ev);
    }

    private GestureDetector.OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {

        public boolean onDown(MotionEvent e){
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            synchronized (HListView.this) {
                int moveX = (int)velocityX;
                int scrollWidth = getWidth();
                int curX = mListHead.getScrollX();
                int dx = moveX;
                if (curX + moveX < 0)
                    dx = 0;
                if (curX + moveX + getScreenWidth() > scrollWidth)
                    dx = scrollWidth - getScreenWidth() - curX;

                mOffset += dx;
                //根据手势滚动Item视图
                for (int i = 0, j = getChildCount(); i < j; i++) {
                    View child = ((ViewGroup) getChildAt(i)).getChildAt(1);
                    if (child.getScrollX() != mOffset)
                        child.scrollTo(mOffset, 0);
                }
                mListHead.scrollBy(dx, 0);
            }
            requestLayout();
            return true;
        }
    };

    public int getScreenWidth() {
        if (screenWidth == 0 ) {
            screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
            if (getChildAt(0) != null) {
                screenWidth -= ((ViewGroup)getChildAt(0)).getChildAt(0)
                        .getMeasuredWidth();
            } else if (mListHead != null) {
                screenWidth -= mListHead.getChildAt(0).getMeasuredWidth();
            }
        }
        return screenWidth;
    }

    /* 获取头偏移量 */
    public int getHeadScrollX() {
        return mListHead.getScrollX();
    }
}
