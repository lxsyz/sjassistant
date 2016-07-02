package com.example.administrator.sjassistant.util;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/4/10.
 */
public class Notifier {
    private Context context;

    private SharedPreferences sp;

    //private NotificationManager notificationManager;
    private BasicPushNotificationBuilder builder;
    public Notifier(Context context) {
        this.context = context;
        this.sp = context.getSharedPreferences(Constant.SETTING_SP,Context.MODE_PRIVATE);
        //this.notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new BasicPushNotificationBuilder(context);
    }

    public void noti() {
        Log.d("tag","notify()");

        if (isNotificationEnabled()) {

//            Notification.Builder builder = new Notification.Builder(context);
//            builder.setContentText(message);
//            builder.setContentTitle("Test");
//            builder.setSmallIcon(R.mipmap.ic_launcher);
//            builder.setTicker("Test2");
//            builder.setSubText("haha");
//            builder.setWhen(System.currentTimeMillis());
            //builder.setDefaults(Notification.DEFAULT_LIGHTS);
            builder.notificationDefaults = Notification.DEFAULT_LIGHTS;
            //Notification notification = builder.build();
            //notification.defaults = Notification.DEFAULT_LIGHTS;
            if (isNotificationSoundEnabled()) {
                //notification.defaults |= Notification.DEFAULT_SOUND;
                builder.notificationDefaults |= Notification.DEFAULT_SOUND;
            }

            if (isNotificationVibrateEnabled()) {
                builder.notificationDefaults |= Notification.DEFAULT_VIBRATE;
            }
            //JPushInterface.setPushNotificationBuilder(1,builder);
            JPushInterface.setDefaultPushNotificationBuilder(builder);
            //notificationManager.notify(0,notification);
        }
    }


    private boolean isNotificationEnabled() {
        return sp.getBoolean(Constant.SETTING_NEW_MESSAGE,true);
    }

    private boolean isNotificationSoundEnabled() {
        return sp.getBoolean(Constant.SETTING_VOICE,true);
    }

    private boolean isNotificationVibrateEnabled() {
        return sp.getBoolean(Constant.SETTING_VIBRATE,true);
    }
}
