package com.example.administrator.sjassistant.activity;

import android.app.Activity;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2016/4/3.
 */
public class PostMessageActivity extends Activity implements View.OnClickListener {

    private EditText search_content,message_title,message_content;
    private ImageView add_contacts,bt_left;

    private TextView tv_center;
    private TextView btn_right;
    private TextView btn_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_message);

        initWindow();
        initView();
        initListeners();
    }

    private void initView() {
        search_content = (EditText)findViewById(R.id.search_content);
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

    }

    private void initListeners() {
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        add_contacts.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left2:
                onBackPressed();
                break;
            case R.id.bt_right:
                break;
            case R.id.add_contacts:
                break;
        }
    }

    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
