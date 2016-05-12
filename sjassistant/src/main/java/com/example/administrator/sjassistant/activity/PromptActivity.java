package com.example.administrator.sjassistant.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.util.Constant;

/**
 * Created by Administrator on 2016/3/31.
 */
public class PromptActivity extends BaseActivity implements View.OnClickListener {

    private ImageView toggle_newMessage,toggle_voice,toggle_vibrate;

    private LinearLayout voice_layout,vibrate_layout;

    private View v_line;

    private boolean isChecked_message=true,isChecked_voice = true,isChecked_vibrate=true;
    private SharedPreferences sp;
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
        voice_layout = (LinearLayout)findViewById(R.id.voice_layout);
        vibrate_layout = (LinearLayout)findViewById(R.id.vibrate_layout);

        v_line = findViewById(R.id.v);

        toggle_newMessage.setOnClickListener(this);
        toggle_vibrate.setOnClickListener(this);
        toggle_voice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = getSharedPreferences(Constant.SETTING_SP,MODE_PRIVATE).edit();
        switch (v.getId()) {
            case R.id.toggle_newMessage:
                if (isChecked_message) {
                    isChecked_message = false;
                    toggle_newMessage.setImageResource(R.drawable.toggle_btn_unchecked);
                    voice_layout.setVisibility(View.GONE);
                    vibrate_layout.setVisibility(View.GONE);
                    v_line.setVisibility(View.GONE);
                    editor.putBoolean(Constant.SETTING_NEW_MESSAGE,isChecked_message);
                } else {
                    isChecked_message = true;
                    toggle_newMessage.setImageResource(R.drawable.toggle_btn_checked);
                    vibrate_layout.setVisibility(View.VISIBLE);
                    voice_layout.setVisibility(View.VISIBLE);
                    v_line.setVisibility(View.VISIBLE);
                    editor.putBoolean(Constant.SETTING_NEW_MESSAGE,isChecked_message);
                }

                break;
            case R.id.toggle_vibrate:
                if (isChecked_vibrate) {
                    isChecked_vibrate = false;
                    toggle_vibrate.setImageResource(R.drawable.toggle_btn_unchecked);

                } else {
                    isChecked_vibrate = true;
                    toggle_vibrate.setImageResource(R.drawable.toggle_btn_checked);
                }
                editor.putBoolean(Constant.SETTING_VIBRATE,isChecked_vibrate);
                break;
            case R.id.toggle_voice:
                if (isChecked_voice) {
                    isChecked_voice = false;
                    toggle_voice.setImageResource(R.drawable.toggle_btn_unchecked);
                    editor.putBoolean(Constant.SETTING_VOICE,isChecked_voice);
                } else {
                    isChecked_voice = true;
                    toggle_voice.setImageResource(R.drawable.toggle_btn_checked);
                    editor.putBoolean(Constant.SETTING_VOICE,isChecked_voice);
                }
                break;
        }
        Log.d("tag","commit");
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sp = getSharedPreferences(Constant.SETTING_SP,MODE_PRIVATE);
        isChecked_message = sp.getBoolean(Constant.SETTING_NEW_MESSAGE,true);
        isChecked_vibrate = sp.getBoolean(Constant.SETTING_VIBRATE,true);
        isChecked_voice = sp.getBoolean(Constant.SETTING_VOICE,true);

        if (isChecked_message) {
            toggle_newMessage.setImageResource(R.drawable.toggle_btn_checked);
        } else {
            toggle_newMessage.setImageResource(R.drawable.toggle_btn_unchecked);
        }

        if (isChecked_vibrate) {
            toggle_vibrate.setImageResource(R.drawable.toggle_btn_checked);
        } else {
            toggle_vibrate.setImageResource(R.drawable.toggle_btn_unchecked);
        }

        if (isChecked_voice) {
            toggle_voice.setImageResource(R.drawable.toggle_btn_checked);
        } else {
            toggle_voice.setImageResource(R.drawable.toggle_btn_unchecked);
        }

        if (!isChecked_message) {
            vibrate_layout.setVisibility(View.GONE);
            voice_layout.setVisibility(View.GONE);
        }

    }
}
