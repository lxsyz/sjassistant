package com.example.administrator.sjassistant.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;

/**
 * Created by Administrator on 2016/4/14.
 */
public class ConfirmPopWin extends PopupWindow {

    private View main;

    private TextView prompt_content;

    private int popHeight;

    public ConfirmPopWin(Activity context,int height) {
        super(context);

        main = LayoutInflater.from(context).inflate(R.layout.pop_confirm,null);

        prompt_content = (TextView)main.findViewById(R.id.prompt_content);

        this.setContentView(main);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(height);

        //main.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY);
        popHeight = height;
        this.setFocusable(true);

        //this.setAnimationStyle(R.style.AnimBottom);

        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0));
    }

    /*
     * @param text 设置窗体内容
     */
    public void setContentText(CharSequence text) {
        this.prompt_content.setText(text);

    }

    public int getPopHeight() {
        return popHeight;
    }
}
