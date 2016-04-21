package com.example.administrator.sjassistant.activity;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.bean.GonggaoType;
import com.example.administrator.sjassistant.util.AppManager;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/3.
 */
public class PostInformActivity extends Activity implements View.OnClickListener {

    private EditText search_content,message_title,message_content;
    private ImageView add_contacts,bt_left;

    private LinearLayout gonggaotype_layout;

    private TextView tv_center;
    private TextView btn_right;
    private TextView btn_left,gonggao_type;


    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_inform);
        AppManager.getInstance().addActivity(this);
        initWindow();
        initView();
        initListeners();
    }

    private void initView() {
        search_content = (EditText)findViewById(R.id.search_content);
        message_content = (EditText)findViewById(R.id.gonggao_content);
        message_title = (EditText)findViewById(R.id.gonggao_title);
        gonggao_type = (TextView)findViewById(R.id.gonggao_type);

        add_contacts = (ImageView)findViewById(R.id.add_contacts);
        gonggaotype_layout = (LinearLayout)findViewById(R.id.gonggaotype_layout);


        tv_center = (TextView)findViewById(R.id.tv_center);
        bt_left = (ImageView)findViewById(R.id.bt_left);
        btn_left = (TextView)findViewById(R.id.bt_left2);
        btn_right = (TextView)findViewById(R.id.bt_right);

        tv_center.setText("发布公告");

        bt_left.setVisibility(View.INVISIBLE);
        btn_left.setText("取消");
        btn_left.setVisibility(View.VISIBLE);
        btn_right.setText("发送");

    }

    private void initListeners() {
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        add_contacts.setOnClickListener(this);
        gonggaotype_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.bt_left2:
                onBackPressed();
                break;
            case R.id.bt_right:

                send();

                break;
            case R.id.add_contacts:
                intent = new Intent(PostInformActivity.this,ChooseApartmentActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.gonggaotype_layout:
                intent = new Intent(PostInformActivity.this,ChooseGonggaoType.class);
                startActivityForResult(intent, 2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = "";
        switch (resultCode) {
            case 1:
                result = data.getStringExtra("result");
                search_content.setText(result);
                search_content.setSelection(search_content.getText().length());
                break;
            case 2:
                GonggaoType type = (GonggaoType) data.getSerializableExtra("result");
                id = type.getId();
                gonggao_type.setText(type.getName());
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /*
     * 发送公告
     */
    private void send() {
        String url = Constant.SERVER_URL +"notes/addNotes";

        if (TextUtils.isEmpty(message_title.getText().toString())) {
            ToastUtil.showShort(PostInformActivity.this,"公告标题不能为空");
        }
        if (TextUtils.isEmpty(message_content.getText().toString())) {
            ToastUtil.showShort(PostInformActivity.this,"公告内容不能为空");
        }

        if(TextUtils.isEmpty(search_content.getText().toString())) {
            ToastUtil.showShort(PostInformActivity.this,"接收部门不能为空");
        }

        if (TextUtils.isEmpty(gonggao_type.getText().toString())) {
            ToastUtil.showShort(PostInformActivity.this,"公告类型不能为空");
        }

        SharedPreferences sp = getSharedPreferences("userinfo",MODE_PRIVATE);
        String username = sp.getString("username",null);

        OkHttpUtils.post()
                .url(url)
                .addParams("notePublisher",username)
                .addParams("noteTitle", message_title.getText().toString())
                .addParams("noteDetail",message_content.getText().toString())
                .addParams("noteDepartment",search_content.getText().toString())
                .addParams("notetypeId",String.valueOf(id))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(PostInformActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response + " ");
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            Log.d("statusCode", statusCode + " ");
                            if (statusCode == 0) {
                                ToastUtil.showShort(PostInformActivity.this,"发布成功");
                                PostInformActivity.this.finish();
                            } else {
                                ToastUtil.show(PostInformActivity.this, "服务器异常");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("activity",MainActivity.instance+" "+android.os.Process.myPid());
    }
}
