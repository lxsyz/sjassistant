package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.util.AppManager;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.util.WatcherUtil;
import com.example.administrator.sjassistant.view.ChangeNumberDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

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

        oldPassword.addTextChangedListener(new WatcherUtil(oldPassword,"password"));
        newPassword.addTextChangedListener(new WatcherUtil(newPassword,"password"));
        sureNewPassword.addTextChangedListener(new WatcherUtil(sureNewPassword,"password"));

        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //MyDialog dialog = new MyDialog(ChangePwdActivity.this,R.style.dialog_style);
        switch (v.getId()) {
            case R.id.confirm:

                changePwd();


                break;
        }
    }

    /*
     * 修改密码
     */
    private void changePwd() {
        String url = Constant.SERVER_URL + "user/settings/changePassword";

        if (TextUtils.isEmpty(oldPassword.getText().toString())) {
            ToastUtil.showShort(ChangePwdActivity.this,"原密码不能为空");
            return;
        }

        if (TextUtils.isEmpty(newPassword.getText().toString())) {
            ToastUtil.showShort(ChangePwdActivity.this,"新密码不能为空");
            return;
        }

        if (!newPassword.getText().toString().equals(sureNewPassword.getText().toString()))
        {
            ToastUtil.showShort(ChangePwdActivity.this,"两次输入的密码不一致");
            return;
        }

        if (newPassword.getText().toString().length() < 6 || newPassword.getText().toString().length() > 12) {
            ToastUtil.showShort(ChangePwdActivity.this,"请输入6-12位的新密码");
            return;
        }

        OkHttpUtils.post()
                .url(url)
                .addParams("userCode",Constant.username)
                .addParams("oldPassword",oldPassword.getText().toString())
                .addParams("newPassword",newPassword.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error",e.getMessage()+" ");
                        ErrorUtil.NetWorkToast(ChangePwdActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response);

                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            if (statusCode == 0) {
                                ChangeNumberDialog dialog = new ChangeNumberDialog(ChangePwdActivity.this,R.style.dialog_style);
                                //修改成功
                                dialog.setFlag(2);
                                dialog.show();

                                dialog.setContentText("密码修改成功,请重新登录");
                                dialog.setOnDeleteClickListener(new ChangeNumberDialog.OnDeleteClickListener() {
                                    @Override
                                    public void onDelete(int i) {
                                        if (i == 1) {
                                            AppManager.getInstance().AppExit(ChangePwdActivity.this);
                                            SharedPreferences.Editor editor = getSharedPreferences("userinfo",MODE_PRIVATE).edit();
                                            editor.putString("phone",null);
                                            editor.putString("password",null);
                                            editor.putString("username",null);
                                            editor.putString("name",null);
                                            editor.putString("dept_id",null);
                                            editor.putString("dept_name",null);
                                            editor.putString("role_name",null);
                                            //editor.putString("last_username",Constant.username);
                                            editor.apply();
                                            Intent intent = new Intent(ChangePwdActivity.this,LoginActivity.class);
                                            startActivity(intent);
                                            ChangePwdActivity.this.finish();
                                        }
                                    }
                                });
                            } else if (statusCode == 2) {
                                ToastUtil.showShort(ChangePwdActivity.this,"密码错误");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}
