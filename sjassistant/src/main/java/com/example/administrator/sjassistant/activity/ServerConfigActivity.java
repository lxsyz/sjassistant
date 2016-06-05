package com.example.administrator.sjassistant.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.util.WatcherUtil;

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


        SharedPreferences sp = getSharedPreferences("userinfo",MODE_PRIVATE);
        String server_address = sp.getString("server_address", null);
        String server_port = sp.getString("server_port",null);

        Log.d("response",server_address+" ");

        if (server_address != null) {
            et_server_address.setText(server_address);
            et_server_address.setSelection(et_server_address.getText().length());
        }

        if (server_port != null) {
            et_server_port.setText(server_port);
            et_server_port.setSelection(et_server_port.getText().length());
        }

        et_server_address.addTextChangedListener(new WatcherUtil(et_server_address,"text"));

        et_server_port.addTextChangedListener(new WatcherUtil(et_server_port,"text"));

        btn_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.confirm:
                if (TextUtils.isEmpty(et_server_address.getText().toString())) {
                    ToastUtil.showShort(ServerConfigActivity.this,"服务器地址不能为空");
                    return;
                }
                if (TextUtils.isEmpty(et_server_port.getText().toString())) {
                    ToastUtil.showShort(ServerConfigActivity.this,"端口号不能为空");
                    return;
                }
                SharedPreferences.Editor sp = getSharedPreferences("userinfo",MODE_PRIVATE).edit();

                sp.putString("server_address",et_server_address.getText().toString());
                sp.putString("server_port", et_server_port.getText().toString());

                sp.apply();

                //SharedPreferences sp2 = getSharedPreferences("userinfo",MODE_PRIVATE);

                Constant.SERVER_URL = "http://"+et_server_address.getText().toString()+":"+et_server_port.getText().toString()+"/app/";
                ToastUtil.showShort(ServerConfigActivity.this,"配置成功");

                ServerConfigActivity.this.finish();
                break;
        }
    }
}
