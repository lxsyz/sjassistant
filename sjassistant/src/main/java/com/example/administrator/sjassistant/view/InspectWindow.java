package com.example.administrator.sjassistant.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;

/**
 * Created by Administrator on 2016/6/17.
 */
public class InspectWindow extends PopupWindow {

    private View main;
    private LinearLayout choose_layout,add_layout;
    private TextView item1;
    private TextView item2;
    public InspectWindow(Activity activity, View.OnClickListener paramOnClickListener, int paramInt1, int paramInt2) {
        super(activity);

        main = LayoutInflater.from(activity).inflate(R.layout.inspect_window,null);

//        choose_layout = (LinearLayout)main.findViewById(R.id.choose);
//        add_layout = (LinearLayout)main.findViewById(R.id.add);
//        item1 =(TextView)main.findViewById(R.id.item1);
//        item2 = (TextView)main.findViewById(R.id.item2);
//
//        if (paramOnClickListener != null) {
//            choose_layout.setOnClickListener(paramOnClickListener);
//            add_layout.setOnClickListener(paramOnClickListener);
//        }

        setContentView(main);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(paramInt2);

        setAnimationStyle(R.style.AnimContacts);
        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0));
    }

//    public void setItem1Text(CharSequence str) {
//        item1.setText(str);
//    }
//
//    public void setItem2Text(CharSequence str) {
//        item2.setText(str);
//
//    }
}
