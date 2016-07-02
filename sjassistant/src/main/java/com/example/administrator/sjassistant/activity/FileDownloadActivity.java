package com.example.administrator.sjassistant.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Attachment;
import com.example.administrator.sjassistant.bean.BarInfo;
import com.example.administrator.sjassistant.bean.DownloadHandler;
import com.example.administrator.sjassistant.service.DownLoadService;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.FileUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/5/21.
 */
public class FileDownloadActivity extends BaseActivity{

    private static final int FINISH = 2;
    private static final int DOWNLOADING = 1;
    private static final int OPEN = 0;

    private File file;
    ListView lv;
    CommonAdapter<Attachment> commonAdapter;

    private String fileType;

    private List<Attachment> datalist = new ArrayList<>();

    //private Map<Integer,DownloadHandler> Constant.mItemLoadHandlers = new HashMap<>();

    private NotificationManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("response","oncreate");
        Log.d("response", FileDownloadActivity.this+" ");
        manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        file = new File(Environment.getExternalStorageDirectory()+"/审计助理");
        if (file.exists()) {

        } else {
            file.mkdirs();
        }

        fileType = getIntent().getStringExtra("fileType");
        datalist = (ArrayList<Attachment>)getIntent().getSerializableExtra("datalist");
        for (int i = 0;i < datalist.size();i++) {
            String filepath = datalist.get(i).getFilePath();
            String url = Constant.SERVER_URL+"message/download/aa?name="
                    + filepath
                    + "&id="
                    + datalist.get(i).getId()
                    + "&fileType="
                    + fileType;

            datalist.get(i).setUrl(url);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_download);
        setTopText("附件列表");
        lv = (ListView)findViewById(R.id.list);



    }

    @Override
    protected void onResume() {
        super.onResume();

        commonAdapter = new CommonAdapter<Attachment>(FileDownloadActivity.this,datalist,R.layout.item_download) {
            @Override
            public void convert(ViewHolder holder, final Attachment attachment) {
//                ProgressBar bar = holder.getView(R.id.progress);
                final int position = holder.getPosition();
                TextView btn = holder.getView(R.id.open);
                final String filepath = attachment.getFilePath();
                holder.setText(R.id.filename, filepath);



                final File f = new File(file,filepath);
                if (f.exists()) {
                    btn.setText("打 开");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FileUtil.openFile(FileDownloadActivity.this, f);
                        }
                    });
                } else {
                    btn.setText("下 载");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            downloadFile(position,attachment.getUrl(),filepath);
                        }
                    });
                }

                DownloadHandler handler = null;
                if (fileType.equals("note")) {
                    if (!Constant.mItemLoadHandlers.containsKey(position)) {
                        handler = new DownloadHandler();
                        Constant.mItemLoadHandlers.put(position, handler);
                        handler.setBtn(btn);
                    } else {
                        handler = Constant.mItemLoadHandlers.get(position);
                        handler.setBtn(btn);
                    }
                } else if (fileType.equals("message")) {

                    if (Constant.messageArray.get(position,null) == null) {
                        handler = new DownloadHandler();
                        Constant.messageArray.put(position, handler);
                        handler.setBtn(btn);
                    } else {
                        handler = Constant.messageArray.get(position);
                        handler.setBtn(btn);
                    }
                }

//                if (!map.containsKey(position))
//                {// 设置 BarInfo 如果当前行已经存在就不用设置了
//                    BarInfo value = new BarInfo();
//                    value.setCurrentProgress(0);
//                    map.put(position, value);
//                }
//                BarInfo bi = map.get(position);
//                Log.d("response", bi.getCurrentProgress() + " ");
//                bar.setProgress(bi.getCurrentProgress());
            }
        };

        lv.setAdapter(commonAdapter);
    }


    /*
     * 下载文件
     */
    public void downloadFile(final int position, final String url, final String filename) {

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
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

                        Message msg = new Message();

                        msg.arg1 = position;
                        Constant.NOTE_DOWNLOAD = DOWNLOADING;
                        msg.what = Constant.NOTE_DOWNLOAD;

                        if (fileType.equals("note")) {
                            Constant.mItemLoadHandlers.get(position).sendMessage(msg);
                        } else if (fileType.equals("message")) {
                            Constant.messageArray.get(position).sendMessage(msg);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.FileToast(FileDownloadActivity.this,filename);
                    }

                    @Override
                    public void onResponse(File response) {
                        Log.d("response", response.getAbsolutePath());
                        manager.cancel(0);
                        ToastUtil.showShort(FileDownloadActivity.this, "下载成功,文件保存至/sdcard/审计助理文件夹下");
                        Message msg = new Message();


                        Constant.NOTE_DOWNLOAD = FINISH;
                        msg.what = Constant.NOTE_DOWNLOAD;
                        if (fileType.equals("note")) {
                            Constant.mItemLoadHandlers.get(position).sendMessage(msg);
                        } else if (fileType.equals("message")) {
                            Constant.messageArray.get(position).sendMessage(msg);
                        }
                        //Constant.mItemLoadHandlers.get(position).sendMessage(msg);
                        //FileUtil.openFile(FileDownloadActivity.this, response);
                    }
                });
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
