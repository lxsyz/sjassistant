package com.example.administrator.sjassistant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.util.AppManager;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ServerConfigUtil;
import com.example.administrator.sjassistant.view.MyPromptDialog;

/**
 * Created by Administrator on 2016/3/27.
 */
public class BaseActivity extends Activity {

    private TextView tv_title;
    protected TextView bt_right_text;
    private ImageView btn_left;
    protected ImageView btn_right,bt_right2;
    protected View layout_top;
    private LinearLayout centerLayout;
    private long exitTime = 0;

    protected MyPromptDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_base);
        AppManager.getInstance().addActivity(this);

        SharedPreferences sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        Constant.username = sp.getString("username", null);

        if (TextUtils.isEmpty(sp.getString("server_address", null))) {
            Constant.SERVER_URL = Constant.TEST_SERVER_URL;
            ServerConfigUtil.setServerConfig(this,"219.234.5.13","8080");
        } else {
            ServerConfigUtil.setServerConfig(this);
        }
        Log.d("activity","server"+Constant.SERVER_URL+" ");
        initProcess();
    }

    protected void initProcess() {

        initWindow();
        initView();
        initListener();
    }

    protected void initView() {
        layout_top = findViewById(R.id.title_layout);
        centerLayout = (LinearLayout)findViewById(R.id.centerlayout);
        tv_title = (TextView)layout_top.findViewById(R.id.tv_center);
        btn_left = (ImageView)layout_top.findViewById(R.id.bt_left);
        btn_right = (ImageView)layout_top.findViewById(R.id.bt_right);
        bt_right2 = (ImageView)layout_top.findViewById(R.id.bt_right2);
        bt_right_text = (TextView)layout_top.findViewById(R.id.bt_right_text);
        pd = new MyPromptDialog(this);

        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void initListener() {

    }


    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void setCenterView(int layout) {
        centerLayout.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        View addView = inflater.inflate(layout, null);
        //hideSoftInput(addView);
        centerLayout.addView(addView, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }


    protected void setTopVisable(int visable) {
        if (layout_top != null) {
            layout_top.setVisibility(visable);
        }
    }
    protected void setLeftVisable(int visable){
        btn_left.setVisibility(visable);
    }

    protected void setRightButton(int visibility) {
        btn_right.setVisibility(visibility);
    }

    protected void setRightButtonRes(int resId) {
        btn_right.setImageResource(resId);
    }

    protected void setRightButton2(int visibility) {
        bt_right2.setVisibility(visibility);
    }

    protected void setRightButtonRes2(int resId) {
        bt_right2.setImageResource(resId);
    }
    /**
     *
     * 设置标题内容
     * @param text
     *
     */
    protected void setTopText(CharSequence text) {
        tv_title.setText(text);
    }

    protected void setTopText(int stringID) {
        tv_title.setText(getString(stringID));
    }



}
