package com.example.administrator.sjassistant.view;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.administrator.sjassistant.R;

/**
 * Created by Administrator on 2016/4/5.
 */
public class ChooseShareWindow implements View.OnClickListener {

    private final int QQ_ITEM = 1;
    private final int WEIXIN_ITEM = 2;

    private Context context;
    private View view;
    private PopupWindow chooseShareWindow;


    private RelativeLayout qq_layout,weinxin_layout;
    private LinearLayout other_layout;
    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.choose_share_layout,null);
        qq_layout = (RelativeLayout) view.findViewById(R.id.qq_layout);
        weinxin_layout = (RelativeLayout)view.findViewById(R.id.weixin_layout);
        other_layout = (LinearLayout)view.findViewById(R.id.other_layout);
        chooseShareWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        other_layout.setOnClickListener(this);
        qq_layout.setOnClickListener(this);
        weinxin_layout.setOnClickListener(this);
        chooseShareWindow.setAnimationStyle(R.style.AnimBottom);
    }

    public ChooseShareWindow(Context context) {
        this.context = context;
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qq_layout:
                if (onItemClickListener != null) {
                    Log.d("test2", "test2");
                    onItemClickListener.onItemClick(QQ_ITEM);
                }
                closeWindow();
                break;
            case R.id.weixin_layout:
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(WEIXIN_ITEM);
                }
                closeWindow();
                break;
            case R.id.other_layout:
                closeWindow();
                break;
        }
    }

    public void showChooseShareWindow(View view) {
        if (chooseShareWindow != null) {
            chooseShareWindow.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
        }
    }

    public void closeWindow() {
        if (chooseShareWindow.isShowing()) {
            chooseShareWindow.dismiss();
        }
    }

    public PopupWindow getChooseShareWindow() {
        return chooseShareWindow;
    }

    private OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener {
        public void onItemClick(int flag);
    }
}
