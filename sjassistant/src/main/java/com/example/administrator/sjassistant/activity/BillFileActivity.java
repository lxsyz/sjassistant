package com.example.administrator.sjassistant.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Attachment;
import com.example.administrator.sjassistant.bean.DownloadHandler;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.FileUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/5/24.
 */
public class BillFileActivity extends BaseActivity {

    private static final int FINISH = 2;
    private static final int DOWNLOADING = 1;
    private static final int OPEN = 0;

    private File file;
    ListView lv;
    CommonAdapter<Attachment> commonAdapter;

    private String fileType;

    private List<Attachment> datalist = new ArrayList<>();
    //private List<String> datalist = new ArrayList<>();
    //private Map<Integer,DownloadHandler> Constant.mItemLoadHandlers = new HashMap<>();

    private String filename;
    private NotificationManager manager;


    private int billId;
    private String displayLevel;
    private String level;
    private int fatherId;
    private String billType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("response", "oncreate");
        manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        file = new File(Environment.getExternalStorageDirectory()+"/审计助理/单据附件");
        if (file.exists()) {

        } else {
            file.mkdirs();
        }

        billId = getIntent().getIntExtra("billId", -1);
        displayLevel = getIntent().getStringExtra("displayLevel");
        fatherId = getIntent().getIntExtra("fatherId", 0);
        billType = getIntent().getStringExtra("billType");

        if (TextUtils.isEmpty(displayLevel)) {
            displayLevel = "1";
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

        getData();

    }

    private void getData() {

        datalist.clear();
        String url = Constant.SERVER_URL + "bill/showDetail";

        OkHttpUtils.post()
                .url(url)
                .addParams("displayLevel",displayLevel)
                .addParams("billId",String.valueOf(billId))
                .addParams("fatherId",String.valueOf(fatherId))
                .addParams("billType",billType)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(BillFileActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.optInt("statusCode");
                            JSONObject data = object.optJSONObject("data");
                            if (statusCode == 0) {
                                String list = data.optString("list");
                                if (list != null) {
                                    filename = list;

                                    String url = "http://219.234.5.13:8080/app/message/download/aa?name="
                                            + filename
                                            + "&id="
                                            + 0
                                            + "&fileType="
                                            + "bill";

                                    Attachment attachment = new Attachment();
                                    attachment.setFilePath(filename);
                                    attachment.setUrl(url);
                                    datalist.add(attachment);

                                    commonAdapter = new CommonAdapter<Attachment>(BillFileActivity.this, datalist, R.layout.item_download) {
                                        @Override
                                        public void convert(ViewHolder holder, final Attachment attachment) {
//                ProgressBar bar = holder.getView(R.id.progress);
                                            final int position = holder.getPosition();
                                            TextView btn = holder.getView(R.id.open);
                                            final String filepath = attachment.getFilePath();
                                            holder.setText(R.id.filename, filepath);


                                            final File f = new File(file, filepath);
                                            if (f.exists()) {
                                                btn.setText("打 开");
                                                btn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        FileUtil.openFile(BillFileActivity.this, f);
                                                    }
                                                });
                                            } else {
                                                btn.setText("下 载");
                                                btn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        downloadFile(position, attachment.getUrl(), filepath);
                                                    }
                                                });
                                            }

                                            DownloadHandler handler = null;


                                            if (Constant.billArray.get(position, null) == null) {
                                                handler = new DownloadHandler();
                                                Constant.billArray.put(position, handler);
                                                handler.setBtn(btn);
                                            } else {
                                                handler = Constant.billArray.get(position);
                                                handler.setBtn(btn);
                                            }

                                        }
                                    };

                                    lv.setAdapter(commonAdapter);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
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
                        Constant.billArray.get(position).sendMessage(msg);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(BillFileActivity.this);
                    }

                    @Override
                    public void onResponse(File response) {
                        Log.d("response", response.getAbsolutePath());
                        manager.cancel(0);
                        ToastUtil.showShort(BillFileActivity.this, "下载成功,文件保存至/sdcard/审计助理文件夹下");
                        Message msg = new Message();


                        Constant.NOTE_DOWNLOAD = FINISH;
                        msg.what = Constant.NOTE_DOWNLOAD;
                        Constant.billArray.get(position).sendMessage(msg);
                    }
                });
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
