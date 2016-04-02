package com.example.administrator.sjassistant.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.administrator.sjassistant.R;

/**
 * Created by Administrator on 2016/3/31.
 */
public class PromptActivity extends BaseActivity {

    private ImageView toggle_newMessage,toggle_voice,toggle_vibrate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_prompt);
        setTopText("新消息提醒");

        toggle_newMessage = (ImageView)findViewById(R.id.toggle_newMessage);
        toggle_voice = (ImageView)findViewById(R.id.toggle_voice);
        toggle_vibrate = (ImageView)findViewById(R.id.toggle_vibrate);

    }
}
