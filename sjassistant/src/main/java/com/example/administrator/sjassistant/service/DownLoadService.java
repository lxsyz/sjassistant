package com.example.administrator.sjassistant.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.administrator.sjassistant.bean.Attachment;
import com.example.administrator.sjassistant.bean.BarInfo;
import com.example.administrator.sjassistant.bean.DownloadHandler;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/5/19.
 */
public class DownLoadService extends Service {
    private File file;
    private ArrayList<Attachment> datalist = new ArrayList<>();

    //private ArrayList<Integer> progressList = new ArrayList<>();

    //最大任务数
    private int[] percent = new int[1000];
    //进度条编号

    private Map<Integer,BarInfo> map  = new HashMap<Integer,BarInfo>();

    public void startDownLoad() {
        int len = datalist.size();
        for (int i = 0;i <len;i++) {
            download(i,datalist.get(i).getUrl(),
                    file.getAbsolutePath(),datalist.get(i).getFilePath());

        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        file = new File(Environment.getExternalStorageDirectory()+"/审计助理");
        if (file.exists()) {

        } else {
            file.mkdirs();
        }

        Log.d("response","oncreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("response","onstart");

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("response","onbind");
        datalist = (ArrayList<Attachment>)intent.getSerializableExtra("datalist");
        return new MsgBinder();
    }

    public class MsgBinder extends Binder {
        public DownLoadService getService() {
            return DownLoadService.this;
        }
    }

    private void download (final int position,String url,String filePath,String filename) {

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(filePath, filename) {
                    @Override
                    public void inProgress(float progress, long total) {
                        BarInfo barInfo = new BarInfo();
                        barInfo.setCurrentProgress((int) (100 * progress));
                        map.put(position, barInfo);

                        Message msg = new Message();

                        msg.arg1 = position;
                        msg.obj = barInfo;
                        //Log.d("response","position"+position);
                        mItemLoadHandlers.get(position).sendMessage(msg);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(DownLoadService.this);
                    }

                    @Override
                    public void onResponse(File response) {
                        Log.d("response", response.getAbsolutePath());

                        //ToastUtil.showShort(context, "下载成功,文件保存至/sdcard/审计助理文件夹下");
                    }
                });
    }

    public int[] getPercent() {
        return percent;
    }

    public Map<Integer, BarInfo> getMap() {
        return map;
    }

    Map<Integer,DownloadHandler> mItemLoadHandlers = new HashMap<Integer, DownloadHandler>();

    public Map<Integer, DownloadHandler> getmItemLoadHandlers() {
        return mItemLoadHandlers;
    }

    public void setmItemLoadHandlers(Map<Integer, DownloadHandler> mItemLoadHandlers) {
        this.mItemLoadHandlers = mItemLoadHandlers;
    }
}
