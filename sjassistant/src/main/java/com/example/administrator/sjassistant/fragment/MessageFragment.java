package com.example.administrator.sjassistant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.administrator.sjassistant.R;

/**
 * Created by Administrator on 2016/3/28.
 */
public class MessageFragment extends Fragment implements View.OnClickListener {

    private LinearLayout message_inform_layout,gonggao_layout,unfinishedwork_layout,assistant_layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message,container,false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        message_inform_layout = (LinearLayout)rootView.findViewById(R.id.message_inform_layout);
        gonggao_layout = (LinearLayout)rootView.findViewById(R.id.gonggao_layout);
        unfinishedwork_layout = (LinearLayout)rootView.findViewById(R.id.unfinishedwork_layout);
        assistant_layout = (LinearLayout)rootView.findViewById(R.id.assistant_layout);

        message_inform_layout.setOnClickListener(this);
        gonggao_layout.setOnClickListener(this);
        unfinishedwork_layout.setOnClickListener(this);
        assistant_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_inform_layout:
                break;
            case R.id.unfinishedwork_layout:
                break;
            case R.id.assistant_layout:
                break;
            case R.id.gonggao_layout:
                break;
        }
    }
}
