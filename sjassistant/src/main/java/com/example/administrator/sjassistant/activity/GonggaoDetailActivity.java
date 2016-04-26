package com.example.administrator.sjassistant.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.bean.GongGao;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.ChangeNumberDialog;
import com.example.administrator.sjassistant.view.MyPromptDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;


/**
 * Created by Administrator on 2016/4/17.
 */
public class GonggaoDetailActivity extends BaseActivity implements View.OnClickListener {
    private String title;
    private int id;
    private TextView message_title;
    private TextView message_postman;
    private TextView message_posttime;
    private TextView post_type;


    private WebView wv;

    private String detail_text;

    private MyPromptDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pd = new MyPromptDialog(this);

        Bundle bundle = getIntent().getExtras();
        GongGao gongao = (GongGao)bundle.get("detail");

        title = gongao.getNoteTitle();
        id = gongao.getId();


        setTopText(title);

        message_title.setText(title);



        try {
            if (new File(Environment.getExternalStorageDirectory()+"/test.doc").exists()) {
                //Log.d("savePath","存在");
                //Toast.makeText(this,"存在",Toast.LENGTH_LONG).show();
                //FileUtil.convert2Html(GonggaoDetailActivity.this,Environment.getExternalStorageDirectory()+"/test.doc",Environment.getExternalStorageDirectory()+"/result.html");
            }

        } catch (Exception e) {

        }
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
                        Log.d("response",response+" ");
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            JSONObject data = object.getJSONObject("data");
                            JSONObject detail = data.getJSONObject("detail");


                            if (statusCode == 0) {
                                String temp = "";
                                message_title.setText(detail.getString("noteTitle"));

                                temp = "发布人: " + detail.getString("notePublisher");
                                message_postman.setText(temp);
                                temp = "时间: "+detail.getString("notePublishtime");
                                message_posttime.setText(temp);

                                detail_text = detail.getString("noteDetail");
                                String type = detail.getString("noteType");

                                if (TextUtils.isEmpty(type) || type.equals("null")) {
                                    temp = "公告类型: "+"无";
                                } else {
                                    temp = "公告类型:" + type;
                                }
                                post_type.setText(temp);
                                if (detail_text.contains(".htm")) {
                                    String url = Constant.SERVER_URL + detail_text;

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
                ChangeNumberDialog dialog = new ChangeNumberDialog(GonggaoDetailActivity.this);
                dialog.setFlag(1);
                dialog.show();

                dialog.setContentText("确定下载附件到你的手机");
                dialog.setOnDeleteClickListener(new ChangeNumberDialog.OnDeleteClickListener() {
                    @Override
                    public void onDelete(int i) {
                        if (i == 1) {

                        }
                    }
                });
                break;
        }
    }
}
