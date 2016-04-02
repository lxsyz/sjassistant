package com.example.administrator.sjassistant.activity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.view.TimeSetting;

/**
 * Created by Administrator on 2016/3/30.
 */
public class NoDisturbActivity extends BaseActivity implements View.OnClickListener {

    private int flag = 0;       //0代表关
    private ImageView toggle;
    private TimeSetting timeSetting;
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

        toggle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggle:
                if (flag == 0) {
                    flag = 1;
                    toggle.setImageResource(R.drawable.toggle_btn_checked);

                } else {
                    flag = 0;
                    toggle.setImageResource(R.drawable.toggle_btn_unchecked);
                }
                break;
        }
    }
}
