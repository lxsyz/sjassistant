package com.example.administrator.sjassistant.activity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.bean.MessageInform;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

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

    private int id;

    private WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        MessageInform messageInform = (MessageInform)bundle.get("detail");

        id = messageInform.getId();
        title = messageInform.getMessageTitle();

        setTopText(title);


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

    @Override
    protected void onResume() {
        super.onResume();

        showDetail(id);
    }

    /*
     * 显示消息详情
     */
    private void showDetail(int id) {
        String url = Constant.SERVER_URL + "message/showMessageDetail";

        OkHttpUtils.post()
                .url(url)
                .addParams("id",String.valueOf(id))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(MessageDetailActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response+" ");
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            JSONObject detail = object.getJSONObject("detail");
                            if (statusCode == 0) {
                                message_title.setText(detail.getString("messageTitle"));
                                message_postman.setText(detail.getString("messagePublisher"));
                                message_posttime.setText(detail.getString("messagePublishtime"));
                            } else {
                                ToastUtil.show(MessageDetailActivity.this, "服务器异常");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
