package com.example.administrator.sjassistant.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;

/**
 * Created by Administrator on 2016/4/6.
 */
public class ChangeNumberDialog extends Dialog implements View.OnClickListener {
    Context context;

    //private BluetoothDialogListener bluetoothDialogListener = null;
    private static int theme = R.style.dialog_style;
    private TextView okTextView;
    private TextView cancelTextView,content,title,confirm;
    private EditText main_text;

    //0 为更换号码对话框  1 为删除联系人  2 为显示一个确定按钮
    int flag = 0;

    OnItemClickListener onItemClickListener;

    public ChangeNumberDialog(Context context) {
        super(context,theme);
        this.context = context;
    }

    public ChangeNumberDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_change_number);
        okTextView = (TextView)findViewById(R.id.dialog_ok);
        cancelTextView = (TextView)findViewById(R.id.dialog_cancel);
        main_text = (EditText)findViewById(R.id.content);
        content = (TextView)findViewById(R.id.main_text);
        title = (TextView)findViewById(R.id.title);
        confirm = (TextView)findViewById(R.id.confirm);

        if (flag == 0) {
            main_text.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
        } else if (flag == 1) {
            title.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);

            main_text.setVisibility(View.GONE);
        } else if (flag == 2) {
            title.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
            okTextView.setVisibility(View.GONE);
            cancelTextView.setVisibility(View.GONE);
            main_text.setVisibility(View.GONE);
            confirm.setVisibility(View.VISIBLE);
        }

        confirm.setOnClickListener(this);
        cancelTextView.setOnClickListener(this);
        okTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_ok:
                if (flag == 0) {
                    String content = main_text.getText().toString();
                    if (onItemClickListener != null && !TextUtils.isEmpty(content)) {
                        onItemClickListener.onItemClick(content);
                    }
                } else if (flag == 1) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.onDelete(1);
                    }
                }
                this.dismiss();
                break;
            case R.id.dialog_cancel:

                this.dismiss();
                break;
            case R.id.confirm:
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDelete(1);
                }
                this.dismiss();
                break;
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(String str);
    }

    public interface OnDeleteClickListener {
        public void onDelete(int i);
    }

    OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public OnDeleteClickListener getOnDeleteClickListener() {
        return onDeleteClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setMainVisibility(int visibility) {
        main_text.setVisibility(visibility);
    }

    public void setEditVisibility(int visibility) {
        content.setVisibility(visibility);
    }

    public void setContentText(CharSequence s) {
        content.setText(s);
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
