package com.example.administrator.sjassistant.util;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by syz on 2016/4/27.
 * 管理添加联系人时的层级产生的多个相同的activity
 */
public class AddPersonManager {
    private static Stack<Activity> activityStack;
    private static AddPersonManager instance;

    public static AddPersonManager getInstance() {
        if (instance == null) {
            instance = new AddPersonManager();
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

    private void finishActivity(Activity activity) {
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

    public void finishActivity() {
        Activity activity = currentActivity();
        if (activity != null) {
            activityStack.remove(activity);
            if (!activity.isFinishing())
                activity.finish();
            activity = null;
        }

    }
}
