package com.example.administrator.sjassistant.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.sjassistant.R;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/6/1.
 */
public class WelcomeActivity extends Activity {
    SharedPreferences sp;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        handler.postDelayed(runnable, 3000);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(runnable);
            sp = getSharedPreferences("userinfo",MODE_PRIVATE);
            if (sp.getString("username",null) != null) {
                intent = new Intent(WelcomeActivity.this,MainActivity.class);
            } else {
                intent = new Intent(WelcomeActivity.this,LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (runnable != null) {
            handler.postDelayed(runnable,3000);
        }
        JPushInterface.onResume(this);
    }
}
