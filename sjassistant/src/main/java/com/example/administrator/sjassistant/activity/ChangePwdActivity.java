package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.util.AppManager;
import com.example.administrator.sjassistant.view.ChangeNumberDialog;
import com.example.administrator.sjassistant.view.MyDialog;

/**
 * Created by Administrator on 2016/3/31.
 */
public class ChangePwdActivity extends BaseActivity implements View.OnClickListener {

    private EditText oldPassword,newPassword,sureNewPassword;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_changepwd);
        setTopText("更换登录密码");

        oldPassword = (EditText)findViewById(R.id.oldPassword);
        newPassword = (EditText)findViewById(R.id.newPassword);
        sureNewPassword = (EditText)findViewById(R.id.sureNewPassword);

        confirm = (Button)findViewById(R.id.confirm);

        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //MyDialog dialog = new MyDialog(ChangePwdActivity.this,R.style.dialog_style);
        ChangeNumberDialog dialog = new ChangeNumberDialog(ChangePwdActivity.this,R.style.dialog_style);
        switch (v.getId()) {
            case R.id.confirm:
                //修改成功
                dialog.setFlag(2);
                dialog.show();

                dialog.setContentText("密码修改完请重新登录");
                dialog.setOnDeleteClickListener(new ChangeNumberDialog.OnDeleteClickListener() {
                    @Override
                    public void onDelete(int i) {
                        if (i == 1) {
                            AppManager.getInstance().finishAllActivity();
                            Intent intent = new Intent(ChangePwdActivity.this,LoginActivity.class);
                            startActivity(intent);
                            ChangePwdActivity.this.finish();
                        }
                    }
                });
                break;
        }
    }
}
