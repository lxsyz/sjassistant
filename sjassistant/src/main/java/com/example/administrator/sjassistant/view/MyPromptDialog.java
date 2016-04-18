package com.example.administrator.sjassistant.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;

/**
 * Created by Administrator on 2016/4/17.
 */
public class MyPromptDialog {

    private Context mContext;

    private Dialog mDialog;

    public MyPromptDialog(Context context) {
        this.mContext = context;
    }

    public Dialog createDialog(CharSequence str) {
        mDialog = new Dialog(mContext, R.style.dialog);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_prompt,null);
        TextView textView = (TextView)v.findViewById(R.id.textView);
        textView.setText(str);
        v.setBackgroundColor(Color.parseColor("#40000000"));
        mDialog.setContentView(v);
        mDialog.setCanceledOnTouchOutside(false);
        return mDialog;
    }

}
