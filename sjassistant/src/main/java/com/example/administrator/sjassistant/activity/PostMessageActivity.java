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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.bean.MyContacts;
import com.example.administrator.sjassistant.util.AddPersonManager;
import com.example.administrator.sjassistant.util.AppManager;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ServerConfigUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.util.WatcherUtil;
import com.example.administrator.sjassistant.view.MyPromptDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/3.
 */
public class PostMessageActivity extends Activity implements View.OnClickListener {

    private EditText message_reader,message_title,message_content;
    private ImageView add_contacts,bt_left;

    private TextView tv_center;
    private TextView btn_right;
    private TextView btn_left;

    private MyPromptDialog pd;

    private List<MyContacts> readerList = new ArrayList<>();

    private String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_message);
        AppManager.getInstance().addActivity(this);
        //AddPersonManager.getInstance().addActivity(this);
        initWindow();

        SharedPreferences sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        Constant.username = sp.getString("username", null);
        if (TextUtils.isEmpty(sp.getString("server_address",null))) {
            Constant.SERVER_URL = Constant.TEST_SERVER_URL;
        } else {
            ServerConfigUtil.setServerConfig(this);
        }

        Log.d("response","oncreate");
        initView();
        initListeners();

    }

    private void initView() {
        message_reader = (EditText)findViewById(R.id.message_reader);
        message_content = (EditText)findViewById(R.id.message_content);
        message_title = (EditText)findViewById(R.id.message_title);

        add_contacts = (ImageView)findViewById(R.id.add_contacts);

        tv_center = (TextView)findViewById(R.id.tv_center);
        bt_left = (ImageView)findViewById(R.id.bt_left);
        btn_left = (TextView)findViewById(R.id.bt_left2);
        btn_right = (TextView)findViewById(R.id.bt_right);

        tv_center.setText("发布消息、通知");

        bt_left.setVisibility(View.INVISIBLE);
        btn_left.setText("取消");
        btn_left.setVisibility(View.VISIBLE);
        btn_right.setText("发送");

        pd = new MyPromptDialog(this);

    }

    private void initListeners() {
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        add_contacts.setOnClickListener(this);

        message_reader.addTextChangedListener(new WatcherUtil(message_reader,"text"));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        readerList.clear();
        //readerList = (ArrayList<MyContacts>)intent.getSerializableExtra("result");
        String str = intent.getStringExtra("result");
        data = intent.getStringExtra("data");
        Log.d("response",data+" ");
//        StringBuilder sb = new StringBuilder();
//
//        if (readerList.size() > 0) {
//            boolean need = false;
//
//            for (MyContacts i : readerList) {
//                if (need) {
//                    sb.append(",");
//                }
//                sb.append(i.getTrueName());
//                need = true;
//            }
//        }
        if (str != null) {
            message_reader.setText(str);
        }
        message_reader.setSelection(message_reader.getText().length());

        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constant.contactResult.clear();
        Constant.contactCount = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left2:
                onBackPressed();
                break;
            case R.id.bt_right:

                if (TextUtils.isEmpty(message_reader.getText().toString())) {
                    ToastUtil.showShort(PostMessageActivity.this,"接收人不能为空");
                    return;
                }
                if (TextUtils.isEmpty(message_content.getText().toString())) {
                    ToastUtil.showShort(PostMessageActivity.this,"消息内容不能为空");
                    return;
                }
                if (TextUtils.isEmpty(message_title.getText().toString())) {
                    ToastUtil.showShort(PostMessageActivity.this,"消息标题不能为空");
                    return;
                }

                send();


                break;
            case R.id.add_contacts:

                Intent intent = new Intent(PostMessageActivity.this,AddPerson.class);
                intent.putExtra("from",1);
                intent.putExtra("count", 0);
                //startActivityForResult(intent, 1);
                startActivity(intent);
                break;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        String result = "";
//        switch (resultCode) {
//            case 1:
//                result = data.getStringExtra("result");
//                message_reader.setText(result);
//                message_reader.setSelection(message_reader.getText().length());
//                break;
//
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//    }

    /*
     * 发布消息
     */
    private void send() {
        String url = Constant.SERVER_URL + "message/addMessage";



        OkHttpUtils.post()
                .url(url)
                .addParams("messagePublisher", Constant.username)
                .addParams("messageTitle", message_title.getText().toString())
                .addParams("messageDetail",message_content.getText().toString())
                .addParams("messageReader",data)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Log.d("error", e.getMessage() + " ");
                ErrorUtil.NetWorkToast(PostMessageActivity.this);
            }

            @Override
            public void onResponse(String response) {
                Log.d("response", response + " ");
                try {
                    JSONObject object = new JSONObject(response);
                    int statusCode = object.getInt("statusCode");
                    Log.d("statusCode", statusCode + " ");
                    if (statusCode == 0) {
                        ToastUtil.showShort(PostMessageActivity.this, "发布成功");
                        PostMessageActivity.this.finish();
                    } else {
                        ToastUtil.showShort(PostMessageActivity.this, "服务器异常");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
     * 按照用户名发送消息
     */
    private String getReader() {

        StringBuilder sb = new StringBuilder();

        if (readerList.size() > 0) {
            boolean need = false;

            for (MyContacts i : readerList) {
                if (need) {
                    sb.append(",");
                }
                sb.append(i.getUserCode());
                need = true;
            }
        }

        return sb.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //AddPersonManager.getInstance().finishActivity();
    }

    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
