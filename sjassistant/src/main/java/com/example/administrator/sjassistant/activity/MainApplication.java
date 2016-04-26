package com.example.administrator.sjassistant.activity;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.administrator.sjassistant.util.Constant;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/4/13.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("activity","main application create");

        JPushInterface.setDebugMode(true);
        JPushInterface.init(getApplicationContext());

        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(),0);
            Constant.version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
}
