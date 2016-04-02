package com.example.administrator.sjassistant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;

/**
 * Created by Administrator on 2016/3/31.
 */
public class ReportActivity extends Activity implements View.OnClickListener {

    private ImageView bt_left;
    private TextView bt_right;
    private TextView title;
    private RelativeLayout layout_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        initView();
    }

    private void initView() {

        layout_top = (RelativeLayout)findViewById(R.id.title_layout);
        bt_right = (TextView)layout_top.findViewById(R.id.bt_right);
        bt_left = (ImageView)layout_top.findViewById(R.id.bt_left);
        title = (TextView)findViewById(R.id.tv_center);

        bt_right.setText("发送");
        title.setText("意见反馈");
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
                break;
        }
    }

}
