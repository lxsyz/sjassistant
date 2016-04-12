package com.example.administrator.sjassistant.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.administrator.sjassistant.R;

/**
 * Created by Administrator on 2016/4/10.
 */
public class Notifier {
    private Context context;

    private SharedPreferences sp;

    private NotificationManager notificationManager;

    public Notifier(Context context) {
        this.context = context;
        this.sp = context.getSharedPreferences(Constant.SETTING_SP,Context.MODE_PRIVATE);
        this.notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void notify(String notificationId,String title,String message,String uri) {
        Log.d("tag","notify()");

        if (isNotificationEnabled()) {

            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentText(message);
            builder.setContentTitle("Test");
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setTicker("Test2");
            builder.setSubText("haha");
            builder.setWhen(System.currentTimeMillis());
            //builder.setDefaults(Notification.DEFAULT_LIGHTS);
            Notification notification = builder.build();
            notification.defaults = Notification.DEFAULT_LIGHTS;
            if (isNotificationSoundEnabled()) {
                notification.defaults |= Notification.DEFAULT_SOUND;

            }

            if (isNotificationVibrateEnabled()) {
                Log.d("tag","vibrate true");
                notification.defaults |= Notification.DEFAULT_VIBRATE;
            }

            notificationManager.notify(0,notification);
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
