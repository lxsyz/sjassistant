package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.sjassistant.util.AppManager;
import com.example.administrator.sjassistant.util.DataCleanManager;
import com.example.administrator.sjassistant.view.ChangeNumberDialog;
import com.example.administrator.sjassistant.view.MyDialog;
import com.example.administrator.sjassistant.R;

/**
 * Created by Administrator on 2016/3/29.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout changeEmail,changePwd,changeServer,clearCache,update,noDisturb,prompt;
    private Button quit;

    private TextView cache_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_setting);
        setTopText("设置");

        changeEmail = (LinearLayout)findViewById(R.id.changeEmail);
        changeServer = (LinearLayout)findViewById(R.id.changeServer);
        changePwd = (LinearLayout)findViewById(R.id.changePwd);
        clearCache = (LinearLayout)findViewById(R.id.clearCache);
        update = (LinearLayout)findViewById(R.id.update);
        noDisturb = (LinearLayout)findViewById(R.id.noDisturb);
        prompt = (LinearLayout)findViewById(R.id.prompt);
        quit = (Button)findViewById(R.id.quit);

        cache_size = (TextView)findViewById(R.id.cache_size);



        changeEmail.setOnClickListener(this);
        changeServer.setOnClickListener(this);
        changePwd.setOnClickListener(this);
        clearCache.setOnClickListener(this);
        update.setOnClickListener(this);
        noDisturb.setOnClickListener(this);
        prompt.setOnClickListener(this);
        quit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        MyDialog dialog = new MyDialog(SettingActivity.this,R.style.dialog_style);
        ChangeNumberDialog dialog2 = new ChangeNumberDialog(SettingActivity.this,R.style.dialog_style);
        Intent intent = null;
        switch (v.getId()) {
            case R.id.changeEmail:
                intent = new Intent(SettingActivity.this,ForgetPasswordActivity.class);
                intent.putExtra("from",2);
                startActivity(intent);
                break;
            case R.id.changeServer:
                intent = new Intent(SettingActivity.this,ServerConfigActivity.class);
                startActivity(intent);
                break;
            case R.id.changePwd:
                intent = new Intent(SettingActivity.this,ChangePwdActivity.class);
                startActivity(intent);
                break;
            case R.id.clearCache:
                dialog2.setFlag(3);
                dialog2.show();

                dialog2.setContentText(getString(R.string.clear));
                dialog2.setOnDeleteClickListener(new ChangeNumberDialog.OnDeleteClickListener() {
                    @Override
                    public void onDelete(int i) {
                        if (i == 1) {
                            //DataCleanManager.cleanAppicationData(SettingActivity.this);
                            DataCleanManager.clearAllCache(SettingActivity.this);
                            cache_size.setText("0.0M");
                            Toast.makeText(SettingActivity.this,"清除成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//                dialog = new MyDialog(SettingActivity.this,R.style.dialog_style);
//                dialog.show();
//                dialog.setCenterVisibility(View.GONE);
//
//                dialog.setVisibility(View.VISIBLE);
//                dialog.setMain_text(getString(R.string.clear));
//                dialog.show();
                break;
            case R.id.update:
//                dialog =
                dialog.show();
                dialog.setMain_text("当前版本已是最新版本");
                dialog.setVisibility(View.GONE);
                dialog.setCenterVisibility(View.VISIBLE);
                dialog.show();
                break;
            case R.id.noDisturb:
                intent = new Intent(SettingActivity.this,NoDisturbActivity.class);
                startActivity(intent);
                break;
            case R.id.prompt:
                intent = new Intent(SettingActivity.this,PromptActivity.class);
                startActivity(intent);
                break;
            case R.id.quit:
                dialog2.setFlag(3);
                dialog2.show();

                dialog2.setContentText("是否确定退出APP");
                dialog2.setOnDeleteClickListener(new ChangeNumberDialog.OnDeleteClickListener() {
                    @Override
                    public void onDelete(int i) {
                        if (i == 1) {
                            //确定退出清掉所有信息
                            SharedPreferences.Editor editor = getSharedPreferences("userinfo",MODE_PRIVATE).edit();
                            editor.putString("phonenumber",null);
                            editor.putString("password",null);
                            editor.putString("imgPath",null);
                            editor.commit();

                            AppManager.getInstance().AppExit(SettingActivity.this);
                        }
                    }
                });
//                new MyDialog(this,R.style.dialog_style);
//                dialog.show();
//                dialog.setMain_text("是否确定退出APP");
//                dialog.setCenterVisibility(View.GONE);
//                dialog.show();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            cache_size.setText(DataCleanManager.getTotalCacheSize(SettingActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
