package com.example.administrator.sjassistant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.activity.AssistantActivity;
import com.example.administrator.sjassistant.activity.GonggaoActivity;
import com.example.administrator.sjassistant.activity.MessageActivity;
import com.example.administrator.sjassistant.activity.PostInformActivity;
import com.example.administrator.sjassistant.activity.PostMessageActivity;
import com.example.administrator.sjassistant.activity.UnfinishedWorkActivity;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.view.AddContactsWin;

/**
 * Created by Administrator on 2016/3/28.
 */
public class MessageFragment extends Fragment implements View.OnClickListener {

    private ImageView btn_left,btn_right;
    private TextView title;
    private LinearLayout message_inform_layout,gonggao_layout,unfinishedwork_layout,assistant_layout;

    private TextView num_unfinish;

    private AddContactsWin popwindow;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message,container,false);
        initView(rootView);

        if (popwindow == null) {
            popwindow = new AddContactsWin(getActivity(),this, OperatorUtil.dp2px(getActivity(), 250),
                    OperatorUtil.dp2px(getActivity(),160));

            popwindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (!hasFocus) {
                        popwindow.dismiss();
                    }
                }
            });
        }
        popwindow.setFocusable(true);
        popwindow.setItem1Text("发布公告");
        popwindow.setItem2Text("发布消息、通知");
        return rootView;
    }

    private void initView(View rootView) {
        btn_left = (ImageView)rootView.findViewById(R.id.bt_left);
        btn_right = (ImageView)rootView.findViewById(R.id.bt_right);
        title = (TextView)rootView.findViewById(R.id.tv_center);

        btn_left.setVisibility(View.INVISIBLE);
        title.setText("消息");
        btn_right.setImageResource(R.drawable.post_message);
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(this);

        message_inform_layout = (LinearLayout)rootView.findViewById(R.id.message_inform_layout);
        gonggao_layout = (LinearLayout)rootView.findViewById(R.id.gonggao_layout);
        unfinishedwork_layout = (LinearLayout)rootView.findViewById(R.id.unfinishedwork_layout);
        assistant_layout = (LinearLayout)rootView.findViewById(R.id.assistant_layout);
        num_unfinish = (TextView)rootView.findViewById(R.id.num_unfinish);


        message_inform_layout.setOnClickListener(this);
        gonggao_layout.setOnClickListener(this);
        unfinishedwork_layout.setOnClickListener(this);
        assistant_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.message_inform_layout:
                intent = new Intent(getActivity(),MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.unfinishedwork_layout:
                intent = new Intent(getActivity(), UnfinishedWorkActivity.class);
                startActivity(intent);
                break;
            case R.id.assistant_layout:
                intent = new Intent(getActivity(),AssistantActivity.class);
                startActivity(intent);
                break;
            case R.id.gonggao_layout:
                intent = new Intent(getActivity(), GonggaoActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_right:
                if (popwindow.isShowing()) {
                    popwindow.dismiss();
                } else
                    popwindow.showAsDropDown(btn_right);
                break;

            case R.id.choose:
                intent = new Intent(getActivity(),PostInformActivity.class);
                startActivity(intent);
                break;
            case R.id.add:
                intent = new Intent(getActivity(), PostMessageActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (popwindow.isShowing()) {
            popwindow.dismiss();
        }
    }
}
