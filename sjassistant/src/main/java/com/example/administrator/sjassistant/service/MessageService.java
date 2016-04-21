package com.example.administrator.sjassistant.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.administrator.sjassistant.activity.MainActivity;
import com.example.administrator.sjassistant.util.Notifier;

/**
 * Created by Administrator on 2016/4/7.
 */
public class MessageService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private MessageThread myThread = null;

    private Intent messageIntent = null;
    private PendingIntent pendingIntent = null;
    private boolean flag = true;
    private int messageNotificationId = 1000;
    private Notification notification = null;
    private NotificationManager notificationManager = null;


    Notifier notifier;

    @Override
    public void onCreate() {
        Log.d("tag","oncreate");
        //messageService = MessageService.this;
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("tag","onstartCommand");
        Log.d("tag","service"+ android.os.Process.myPid());
        Log.d("tag","id  "+Thread.currentThread().getId()+"   thread"+ Thread.currentThread().getName());
        if (MainActivity.instance != null) {
            notifier = new Notifier(MainActivity.instance);
        }
        myThread = new MessageThread();
        myThread.start();
        return super.onStartCommand(intent, flags, startId);
    }


    class MessageThread extends Thread {
        //public boolean isRunning = true;

        @Override
        public void run() {
            while (flag) {
                try {
                    Thread.sleep(5000);

                    String message = getMessage();

                    send(message);


                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void send(String message) {

//        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        messageIntent = new Intent(this, MainActivity.class);
//        pendingIntent = PendingIntent.getActivity(this,0,messageIntent,0);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.instance);
//        mBuilder.setContentTitle("主人有货")//设置通知栏标题
//                .setContentText(message)
//                .setContentIntent(pendingIntent) //设置通知栏点击意图
//                        //  .setNumber(number) //设置通知集合的数量
//                .setTicker("主人有货") //通知首次出现在通知栏，带上升动画效果的
//                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
//                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
//                        //  .setAutoCancel(true)		//设置这个标志当用户单击面板就可以让通知将自动取消
//                        //    .setOngoing(false)		//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                .setDefaults(Notification.DEFAULT_VIBRATE);//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
//                        //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
//
//        notification = mBuilder.build();
//
//        notification.flags = Notification.FLAG_AUTO_CANCEL;
//
//        notificationManager.notify(0,notification);
        if(notifier != null)
            notifier.notify("10","111","111","sad");



    }


    public String getMessage() {
        return "test";
    }

    @Override
    public void onDestroy() {
        Log.d("tag","service destroy");
        this.flag = false;
        //Intent i = new Intent(this,MessageService.class);
        //startService(i);
        super.onDestroy();
    }

    //public static MessageService messageService;
}
