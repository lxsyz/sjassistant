package com.example.administrator.sjassistant.activity;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.view.TimeSetting;

/**
 * Created by Administrator on 2016/3/30.
 */
public class NoDisturbActivity extends BaseActivity implements View.OnClickListener {

    private int flag = 0;       //0代表关
    private ImageView toggle;
    private TimeSetting timeSetting;

    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    private SharedPreferences disturbSp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_nodisturb);
        setTopText("勿扰模式");
        toggle = (ImageView)findViewById(R.id.toggle);
        timeSetting = (TimeSetting)findViewById(R.id.time_setting);



        timeSetting.setOnHandUpListener(new TimeSetting.OnHandUpListener() {
            @Override
            public void onHandUp(int beginTime, int endTime,float startX,float endX) {
                if (beginTime != -1 && endTime != -1) {
                    SharedPreferences.Editor editor = getSharedPreferences("disturb",MODE_PRIVATE).edit();
                    editor.putInt("beginTime",-1);
                    editor.putInt("endTime", -1);
                    editor.putFloat("startX",startX);
                    editor.putFloat("endX",endX);
                    editor.commit();
                }
            }
        });
        toggle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggle:
                ed = getSharedPreferences(Constant.SETTING_SP,MODE_PRIVATE).edit();
                if (flag == 0) {

                    flag = 1;
                    toggle.setImageResource(R.drawable.toggle_btn_checked);
                    timeSetting.setVisibility(View.VISIBLE);


                } else {
                    flag = 0;
                    toggle.setImageResource(R.drawable.toggle_btn_unchecked);
                    timeSetting.setVisibility(View.GONE);

                    clearTime();
                }
                ed.putInt(Constant.SETTING_DISTURB,flag);
                ed.commit();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sp = getSharedPreferences(Constant.SETTING_SP, MODE_PRIVATE);


        flag = sp.getInt(Constant.SETTING_DISTURB,0);
        if (flag == 0) {
            toggle.setImageResource(R.drawable.toggle_btn_unchecked);
            timeSetting.setVisibility(View.GONE);
            clearTime();
        } else {
            toggle.setImageResource(R.drawable.toggle_btn_checked);
            timeSetting.setVisibility(View.VISIBLE);
            disturbSp = getSharedPreferences("disturb",MODE_PRIVATE);
            float startX = disturbSp.getFloat("startX",-20);
            float endX = disturbSp.getFloat("endX",0);

            timeSetting.setValue(startX,endX);
        }
    }


    //清除设置的时间
    private void clearTime() {
        disturbSp = getSharedPreferences("disturb",MODE_PRIVATE);
        SharedPreferences.Editor disturbEditor = disturbSp.edit();

        disturbEditor.putInt("beginTime",-1);
        disturbEditor.putInt("endTime", -1);
        disturbEditor.putFloat("startX",-20);
        disturbEditor.putFloat("endX",0);
        disturbEditor.commit();
    }
}
