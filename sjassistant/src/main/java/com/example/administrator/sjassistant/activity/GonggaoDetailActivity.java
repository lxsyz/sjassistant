package com.example.administrator.sjassistant.activity;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.bean.Attachment;
import com.example.administrator.sjassistant.bean.GongGao;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.FileUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.ChangeNumberDialog;
import com.example.administrator.sjassistant.view.MyPromptDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by Administrator on 2016/4/17.
 */
public class GonggaoDetailActivity extends BaseActivity implements View.OnClickListener {
    //private String name;
    private boolean isMulti = false;    //判断是否有多个附件
    private String title;
    private int id;
    private TextView message_title;
    private TextView message_postman;
    private TextView message_posttime;
    private TextView post_type;
    private WebView wv;

    private String detail_text;

    private MyPromptDialog pd;

    private NotificationManager manager;
    private File file;


    private List<Attachment> datalist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pd = new MyPromptDialog(this);

        manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        file = new File(Environment.getExternalStorageDirectory()+"/审计助理");
        if (file.exists()) {

        } else {
            file.mkdirs();
        }

        Bundle bundle = getIntent().getExtras();
        GongGao gongao = (GongGao)bundle.get("detail");

        title = gongao.getNoteTitle();
        id = gongao.getId();


        setTopText(title);

        message_title.setText(title);


        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            wv.getSettings().setDisplayZoomControls(false);
        }

//        wv.loadUrl("file://"+Environment.getExternalStorageDirectory()+"/result.html");

        wv.getSettings().setJavaScriptEnabled(true);//设置使用够执行JS脚本
//        wv.getSettings().setBuiltInZoomControls(true);//设置使支持缩放
//
//        wv.loadUrl("file:///android_asset/index.html");
//
//
//
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);// 使用当前WebView处理跳转
                return true;//true表示此事件在此处被处理，不需要再广播
            }

            @Override   //转向错误时的处理
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
            }
        });


    }

    @Override
    protected void initView() {
        super.initView();

        setCenterView(R.layout.activity_message_detail);

        message_title = (TextView)findViewById(R.id.message_title);
        message_postman = (TextView)findViewById(R.id.message_postman);
        message_posttime = (TextView)findViewById(R.id.message_posttime);
        post_type = (TextView)findViewById(R.id.post_type);



        btn_right.setOnClickListener(this);
        setRightButtonRes(R.drawable.extrafile);
        btn_right.setVisibility(View.VISIBLE);
        wv = (WebView)findViewById(R.id.wv);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getGonggaoDetail();
    }

    /*
     * 获取公告详情
     */
    private void getGonggaoDetail() {

        if (pd != null) pd.createDialog().show();

        String url = Constant.SERVER_URL + "notes/showNotesDetail";

        OkHttpUtils.post()
                .url(url)
                .addParams("id",String.valueOf(id))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(GonggaoDetailActivity.this);
                        pd.dismissDialog();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response + " ");
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            Gson gson = new Gson();
                            JSONObject data = object.getJSONObject("data");
                            JSONObject detail = data.getJSONObject("detail");
                            JSONArray name = data.optJSONArray("name");
                            int len = name.length();

                            if (len > 0) {
                                if (len > 1) {
                                    isMulti = true;
                                } else {
                                    isMulti = false;
                                }
                                btn_right.setVisibility(View.VISIBLE);
                            } else {
                                btn_right.setVisibility(View.GONE);
                            }

                            datalist = gson.fromJson(name.toString(), new TypeToken<List<Attachment>>() {
                            }.getType());

                            if (statusCode == 0) {
                                String temp = "";
                                message_title.setText(detail.getString("noteTitle"));

                                temp = "发布人: " + detail.getString("notePublisher");
                                message_postman.setText(temp);
                                temp = "时间: " + detail.getString("notePublishtime");
                                message_posttime.setText(temp);

                                detail_text = detail.getString("noteDetail");
                                String type = detail.getString("noteType");

                                if (TextUtils.isEmpty(type) || type.equals("null")) {
                                    temp = "公告类型: " + "无";
                                } else {
                                    temp = "公告类型:" + type;
                                }
                                post_type.setText(temp);
                                if (detail_text.contains(".htm")) {
                                    SharedPreferences sp2 = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                                    String url = "http://"+sp2.getString("server_address",null)+":"+sp2.getString("server_port",null)+"/";
                                    url = url + detail_text;

                                    wv.loadUrl(url);
                                } else {
                                    String html = "<!DOCTYPE html>"
                                            + "<meta charset=\"UTF-8\">\n"
                                            + "    <title></title>"
                                            + "<html>"
                                            + "<body>"
                                            + detail_text
                                            + "</body>" + "</html>";
                                    wv.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
                                }
                            } else {
                                ToastUtil.show(GonggaoDetailActivity.this, "服务器异常");
                            }
                            pd.dismissDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_right:
                //单个附件下载
                if (!isMulti) {
                    final String url = "http://219.234.5.13:8080/app/message/download/aa?name="
                            + datalist.get(0).getFilePath()
                            + "&id="
                            + id
                            + "&fileType="
                            + "note";

                    final String filename = datalist.get(0).getFilePath();
                    Log.d("response", filename);
                    if (new File(file, filename).exists()) {
                        ChangeNumberDialog dialog2 = new ChangeNumberDialog(GonggaoDetailActivity.this);
                        dialog2.setFlag(1);
                        dialog2.show();
                        dialog2.setContentText("该文件已存在，是否重新下载覆盖文件？");
                        dialog2.setOnDeleteClickListener(new ChangeNumberDialog.OnDeleteClickListener() {
                            @Override
                            public void onDelete(int i) {
                                if (i == 1) {
                                    downloadFile(url, filename);
                                }
                            }
                        });
                    } else {

                        ChangeNumberDialog dialog = new ChangeNumberDialog(GonggaoDetailActivity.this);
                        dialog.setFlag(1);
                        dialog.show();

                        dialog.setContentText("确定下载附件到你的手机");
                        dialog.setOnDeleteClickListener(new ChangeNumberDialog.OnDeleteClickListener() {
                            @Override
                            public void onDelete(int i) {
                                if (i == 1) {

                                    Log.d("response", filename);

                                    downloadFile(url, filename);
                                }
                            }
                        });
                    }
                } else {
                    //多个附件下载
                    Intent intent = new Intent(GonggaoDetailActivity.this,FileDownloadActivity.class);
                    intent.putExtra("datalist",(ArrayList)datalist);
                    intent.putExtra("fileType","note");
                    startActivity(intent);
                }
                break;
        }
    }

//    public void download(String url) {
//        DownloadManager.Request req = new DownloadManager().Request(Uri.parse(url));
//
//        //req.setAllowedOverRoaming()
//        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        req.setDestinationInExternalFilesDir(this, file.getAbsolutePath(), "2.jpg");
//        req.setDescription("下载完成后请点击打开");
//        req.setMimeType("application/vnd.android.package-archive");
//
//        DownloadManager dm = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
//        long downloadId = dm.enqueue(req);
//    }
    /*
     * 下载文件
     */
    public void downloadFile(String url, final String filename) {

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
                        Log.d("response", "progress" + progress);
                        builder.setContentTitle(filename+"  下载中...");

                        builder.setProgress(100, (int) (progress * 100), false);
                        builder.setContentInfo(((int)(progress*100))+"%");
                        builder.setOngoing(true);
                        builder.setShowWhen(false);
                        //Intent intent = new Intent(this,DownloadService.class);
                        //intent.putExtra("command",1);
                        Notification notification = builder.build();
                        manager.notify(0, notification);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(GonggaoDetailActivity.this);
                    }

                    @Override
                    public void onResponse(File response) {
                        Log.d("response", response.getAbsolutePath());
                        manager.cancel(0);
                        ToastUtil.showShort(GonggaoDetailActivity.this, "下载成功,文件保存至/sdcard/审计助理文件夹下");
                        FileUtil.openFile(GonggaoDetailActivity.this,response);
                    }
                });
    }




}
