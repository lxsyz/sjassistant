package com.example.administrator.sjassistant.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.sjassistant.R;

/**
 * Created by Administrator on 2016/3/28.
 */
public class ServerConfigActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_server_address;
    private EditText et_server_port;
    private Button btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activty_serverconfig);
        setTopText(getString(R.string.serverConfig));
        et_server_address = (EditText)findViewById(R.id.server_address);
        et_server_port = (EditText)findViewById(R.id.server_port);
        btn_confirm = (Button)findViewById(R.id.confirm);

        btn_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                break;
        }
    }
}
