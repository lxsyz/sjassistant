package com.example.administrator.sjassistant.activity;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.pgyersdk.crash.PgyCrashManager;
import com.example.administrator.sjassistant.util.Constant;


import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/4/13.
 */
public class MainApplication extends Application {

    private static MainApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        Log.d("activity", "main application create");
        PgyCrashManager.register(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(getApplicationContext());
        SharedPreferences editor = getSharedPreferences("disturb",MODE_PRIVATE);
        int beginTime = editor.getInt("beginTime", -1);
        int endTime = editor.getInt("endTime", -1);
        if (beginTime != -1 && endTime != -1) {
            JPushInterface.setSilenceTime(getApplicationContext(),beginTime,0,endTime,59 );
        }
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(),0);
            Constant.version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static MainApplication getInstance() {
        return instance;
    }
}
