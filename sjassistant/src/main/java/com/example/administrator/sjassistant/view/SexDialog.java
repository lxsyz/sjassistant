package com.example.administrator.sjassistant.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.util.Constant;

/**
 * Created by Administrator on 2016/3/31.
 */
public class SexDialog extends Dialog implements View.OnClickListener {
    Context context;

    private ImageView iv_boy,iv_girl;
    private RelativeLayout bottom_layout;

    //1 表示男  2表示女
    //private int gender;


    private static int theme = R.style.dialog_style;
    public SexDialog(Context context) {
        super(context,theme);
        this.context = context;
    }

    public SexDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_sex);

        this.setCanceledOnTouchOutside(false);

        iv_boy = (ImageView)findViewById(R.id.iv_boy);
        iv_girl = (ImageView)findViewById(R.id.iv_girl);
        bottom_layout = (RelativeLayout)findViewById(R.id.bottom_layout);

        iv_boy.setOnClickListener(this);
        iv_girl.setOnClickListener(this);
        bottom_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Message msg = new Message();
        msg.what = 0x11;
        switch (v.getId()) {
            case R.id.iv_boy:
                iv_boy.setImageResource(R.drawable.radio_checked);
                iv_girl.setImageResource(R.drawable.radio_unchecked);
                Constant.gender = 1;


                break;
            case R.id.iv_girl:
                iv_boy.setImageResource(R.drawable.radio_unchecked);
                iv_girl.setImageResource(R.drawable.radio_checked);
                Constant.gender = 2;

                break;
            case R.id.bottom_layout:
                handler.sendMessage(msg);
                this.dismiss();
                break;
        }

    }


    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private Handler handler;
}
