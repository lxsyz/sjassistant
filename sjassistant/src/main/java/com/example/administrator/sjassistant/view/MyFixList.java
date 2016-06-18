package com.example.administrator.sjassistant.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/6/17.
 */
public class MyFixList extends ListView {


    public MyFixList(Context context) {
        super(context);
    }

    public MyFixList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFixList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (IndexOutOfBoundsException e) {
            // samsung error
        }
    }
}
