package com.example.administrator.sjassistant.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.bean.MessageInform;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.MyPromptDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/3.
 */
public class MessageDetailActivity extends BaseActivity {

    private String title;

    private TextView message_title;
    private TextView message_postman;
    private TextView message_posttime;
    private int id;
    private LinearLayout post_type;
    private WebView wv;

    private String detail_text;

    private MyPromptDialog pd;

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
        post_type = (LinearLayout)findViewById(R.id.post_type_layout);

        wv = (WebView)findViewById(R.id.wv);
        pd = new MyPromptDialog(this);
        post_type.setVisibility(View.GONE);


        wv.getSettings().setJavaScriptEnabled(true);//设置使用够执行JS脚本
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            wv.getSettings().setDisplayZoomControls(false);
        }

        //wv.loadUrl("file:///android_asset/index.html");
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
        if (pd != null) pd.createDialog().show();
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
                                String temp= "";
                                message_title.setText(detail.getString("messageTitle"));

                                temp = "发布人: " + detail.getString("messagePublisher");
                                message_postman.setText(temp);
                                temp = "时间: "+detail.getString("messagePublishtime");
                                message_posttime.setText(temp);
                                detail_text = detail.getString("messageDetail");

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
                                ToastUtil.show(MessageDetailActivity.this, "服务器异常");
                            }
                            pd.dismissDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
