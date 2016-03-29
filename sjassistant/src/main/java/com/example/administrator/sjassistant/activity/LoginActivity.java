package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.util.OperatorUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_username,et_password;
    private TextView tv_prompt,tv_serverConfig,tv_forgetPassword;
    private ImageView iv_eye;
    private Button btn_login;
    private String username,password;

    private int flag = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_login);
        setTopText("登录");
        et_username = (EditText)findViewById(R.id.username);
        et_password = (EditText)findViewById(R.id.password);
        tv_prompt = (TextView)findViewById(R.id.prompt);
        iv_eye = (ImageView)findViewById(R.id.eye);

        btn_login = (Button)findViewById(R.id.login);
        tv_serverConfig = (TextView)findViewById(R.id.serverConfig);
        tv_forgetPassword = (TextView)findViewById(R.id.forgetPassword);

        et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if (!OperatorUtil.isEmail(et_username.getText().toString())) {
                        tv_prompt.setVisibility(View.VISIBLE);
                    } else {
                        tv_prompt.setVisibility(View.GONE);
                    }
                }
            }
        });

        btn_login.setOnClickListener(this);
        tv_serverConfig.setOnClickListener(this);
        tv_forgetPassword.setOnClickListener(this);
        iv_eye.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("login destroy", "login destroy");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgetPassword:
                Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.serverConfig:
                intent = new Intent(LoginActivity.this,ServerConfigActivity.class);
                startActivity(intent);
                break;
            case R.id.login:
                login();
                intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.eye:
                if (flag == 1) {
                    iv_eye.setImageResource(R.drawable.noshowpassword);
                    flag = 0;
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    //et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    iv_eye.setImageResource(R.drawable.showpassword);
                    flag = 1;
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    //et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                //设置光标在最后一行
                CharSequence charSequence = et_password.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
                break;
        }
    }



    /*
     * 访问服务器登录
     */
    private void login() {

    }
}
