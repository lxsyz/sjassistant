package com.example.administrator.sjassistant.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.Stack;

/**
 * Created by Administrator on 2016/4/7.
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }


    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            if (!activity.isFinishing())
                activity.finish();
            activity = null;
        }
    }

    public void finishAllActivity() {
        while (true) {
            if (activityStack.size() == 0) {
                break;
            }
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            finishActivity(activity);
        }
    }


    public void AppExit(Context context) {
        try {
            finishAllActivity();

            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            int currentVersion = android.os.Build.VERSION.SDK_INT;
            if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startMain);
                activityMgr.killBackgroundProcesses(context.getPackageName());
            } else {// android2.1
                activityMgr.restartPackage(context.getPackageName());
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            activityMgr.killBackgroundProcesses(context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
