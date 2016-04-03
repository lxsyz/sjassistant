package com.example.administrator.sjassistant.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.bean.MessageInform;

/**
 * Created by Administrator on 2016/4/3.
 */
public class MessageDetailActivity extends BaseActivity {

    private String title;
    private String username;
    private String time;

    private TextView message_title;
    private TextView message_postman;
    private TextView message_posttime;


    private WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        MessageInform messageInform = (MessageInform)bundle.get("detail");

        title = messageInform.getTitle();
        username = messageInform.getUsername();
        time = messageInform.getTime();

        setTopText(title);

        message_title.setText(title);
        message_postman.setText("发布人:  " + username);
        message_posttime.setText("发布时间: "+time);
    }


    @Override
    protected void initView() {
        super.initView();

        setCenterView(R.layout.activity_message_detail);

        message_title = (TextView)findViewById(R.id.message_title);
        message_postman = (TextView)findViewById(R.id.message_postman);
        message_posttime = (TextView)findViewById(R.id.message_posttime);
        wv = (WebView)findViewById(R.id.wv);


        wv.getSettings().setJavaScriptEnabled(true);//设置使用够执行JS脚本
        wv.getSettings().setBuiltInZoomControls(true);//设置使支持缩放

        wv.loadUrl("file:///android_asset/index.html");
        wv.setWebViewClient(new WebViewClient() {
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
    }
}
