package com.example.administrator.sjassistant.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.administrator.sjassistant.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/5/18.
 */
public class AppUtil {
    private AppUtil()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    private static File file;

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下载安装包
     */
    public static void startDownload(final Context context,String url, final String filename) {
        file = new File(Environment.getExternalStorageDirectory()+"/审计助理");
        if (file.exists()) {

        } else {
            file.mkdirs();
        }

        final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.push));
        //禁止用户点击删除按钮删除
        //builder.setAutoCancel(false);
        //禁止滑动删除
        builder.setOngoing(true);
        //取消右上角的时间显示
        builder.setShowWhen(false);

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(file.getAbsolutePath(), filename) {
                    @Override
                    public void inProgress(float progress, long total) {
                        builder.setContentTitle(filename + "  下载中...");

                        builder.setProgress(100, (int) (progress * 100), false);
                        builder.setContentInfo(((int) (progress * 100)) + "%");
                        builder.setOngoing(true);
                        builder.setShowWhen(false);

                        Notification notification = builder.build();
                        manager.notify(0, notification);

                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(context);
                    }

                    @Override
                    public void onResponse(File response) {
                        Log.d("response", response.getAbsolutePath());
                        manager.cancel(0);

                        FileUtil.openApk(context,response);
                    }
                });

    }
}
