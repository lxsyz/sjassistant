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
import com.example.administrator.sjassistant.activity.InspectActivity;
import com.example.administrator.sjassistant.activity.MoreContact;

/**
 * Created by Administrator on 2016/3/28.
 */
public class MyApplicationFragment extends Fragment implements View.OnClickListener {

    private ImageView btn_left,btn_right;
    private TextView title;

    private LinearLayout deal_layout,chat_layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myapplication,container,false);

        initView(rootView);
        return rootView;
    }

    private void initView(View root) {
        deal_layout = (LinearLayout)root.findViewById(R.id.deal_layout);
        chat_layout = (LinearLayout)root.findViewById(R.id.chat_layout);
        btn_left = (ImageView)root.findViewById(R.id.bt_left);
        btn_left.setVisibility(View.INVISIBLE);
        title = (TextView)root.findViewById(R.id.tv_center);
        title.setText("应用");

        deal_layout.setOnClickListener(this);
        chat_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.chat_layout:
                intent = new Intent(getActivity(), MoreContact.class);
                startActivity(intent);
                break;
            case R.id.deal_layout:
                intent = new Intent(getActivity(),InspectActivity.class);
                startActivity(intent);
                break;
        }
    }
}
