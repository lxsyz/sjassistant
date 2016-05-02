package com.example.administrator.sjassistant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.util.AppManager;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ServerConfigUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/3/31.
 */
public class HelpActivity extends Activity implements View.OnClickListener {

    private WebView webView;
    private ImageView bt_left;
    private TextView bt_right;
    private TextView title;
    private RelativeLayout layout_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        AppManager.getInstance().addActivity(this);
        initWindow();

        SharedPreferences sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        Constant.username = sp.getString("username", null);
        if (TextUtils.isEmpty(sp.getString("server_address", null))) {
            Constant.SERVER_URL = Constant.TEST_SERVER_URL;
        } else {
            ServerConfigUtil.setServerConfig(this);
        }
        initView();
    }

    private void initView() {

        webView = (WebView)findViewById(R.id.wv);
        webView.getSettings().setJavaScriptEnabled(true);//设置使用够执行JS脚本
        webView.getSettings().setBuiltInZoomControls(true);//设置使支持缩放

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);// 使用当前WebView处理跳转
                return true;//true表示此事件在此处被处理，不需要再广播
            }

            @Override   //转向错误时的处理
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
            }
        });

        layout_top = (RelativeLayout)findViewById(R.id.title_layout);
        bt_right = (TextView)layout_top.findViewById(R.id.bt_right);
        bt_left = (ImageView)layout_top.findViewById(R.id.bt_left);
        title = (TextView)findViewById(R.id.tv_center);

        title.setText("帮助反馈");
        bt_right.setText("反馈");
        bt_left.setOnClickListener(this);
        bt_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                onBackPressed();
                break;
            case R.id.bt_right:
                Intent intent = new Intent(HelpActivity.this,ReportActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getHelp();
    }

    /*
     * 获取帮助页面
     */
    private void getHelp() {
        String url = Constant.SERVER_URL + "help";

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(HelpActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response);
                        webView.loadDataWithBaseURL(null, response, "text/html", "utf-8", null);
                    }
                });
    }

    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
