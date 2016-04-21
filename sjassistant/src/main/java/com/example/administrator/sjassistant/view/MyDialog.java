package com.example.administrator.sjassistant.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.util.Constant;

/**
 * Created by Administrator on 2016/3/30.
 */
public class MyDialog extends Dialog implements View.OnClickListener {
    Context context;

    //private BluetoothDialogListener bluetoothDialogListener = null;
    private static int theme = R.style.dialog_style;
    private TextView okTextView;
    private TextView cancelTextView;
    private TextView main_text;
    private TextView confirm;
    private RelativeLayout bottom_layout;
    private EditText content;

    //private RadioButton radioButton;
    private boolean isOpen = false;

    public MyDialog(Context context) {
        super(context, theme);
        this.context = context;
    }

    public MyDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog);
        //this.setCanceledOnTouchOutside(false);
        okTextView = (TextView) findViewById(R.id.dialog_ok);
        cancelTextView = (TextView) findViewById(R.id.dialog_cancel);
        main_text = (TextView)findViewById(R.id.main_text);
        confirm = (TextView)findViewById(R.id.confirm);
        bottom_layout = (RelativeLayout)findViewById(R.id.bottom_layout);
        content = (EditText)findViewById(R.id.content);

        confirm.setOnClickListener(this);
        okTextView.setOnClickListener(this);
        cancelTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_ok:
                Log.v("TAG", "ok");
                okTextView.setBackgroundColor(context.getResources().getColor(
                        R.color.blueviolet));
                okTextView.setTextColor(context.getResources().getColor(
                        R.color.white));

                this.dismiss();
                break;
            case R.id.dialog_cancel:
                Log.v("TAG", "cancel");
                cancelTextView.setBackgroundColor(context.getResources().getColor(
                        R.color.blueviolet));
                cancelTextView.setTextColor(context.getResources().getColor(
                        R.color.white));
                this.dismiss();
                break;

            case R.id.confirm:
                if (content.getVisibility() == View.VISIBLE) {
                    switch (flag) {
                        case 1:
                            Constant.nickname = content.getText().toString();
                            break;
                        case 2:
                            Constant.apartment = content.getText().toString();
                            break;
                        case 3:
                            Constant.work = content.getText().toString();
                            break;
                        case 4:
                            Constant.address = content.getText().toString();
                            break;
                    }
                    Message msg = new Message();
                    msg.what = flag;

                    handler.sendMessage(msg);
                }
                this.dismiss();
                break;
            default:
                break;
        }

    }

    public void setMain_text(CharSequence text) {

        main_text.setText(text);
    }

    public void setMainTextVisibility(int visibility) {
        main_text.setVisibility(visibility);
    }

    public void setContentVisibility(int visibility) {
        content.setVisibility(visibility);
    }

    public void setVisibility(int visibility) {
        okTextView.setVisibility(visibility);
        cancelTextView.setVisibility(visibility);

    }

    //设置中间按钮可见性
    public void setCenterVisibility(int visibility) {
        confirm.setVisibility(visibility);
    }

    private Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    //标志是昵称  地址等等
    private int flag = 0;

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }



}
