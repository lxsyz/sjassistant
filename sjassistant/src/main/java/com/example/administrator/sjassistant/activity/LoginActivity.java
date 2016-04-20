package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.MD5;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.util.WatcherUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_username,et_password;
    private TextView tv_prompt,tv_serverConfig,tv_forgetPassword;
    private ImageView iv_eye;
    private Button btn_login;
    private String username,password;

    WatcherUtil watcherUtil;

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

//        et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//
//                    if (!OperatorUtil.isEmail(et_username.getText().toString())) {
//                        tv_prompt.setVisibility(View.VISIBLE);
//                        btn_login.setClickable(false);
//                    } else {
//                        tv_prompt.setVisibility(View.GONE);
//                        btn_login.setClickable(true);
//                    }
//                }
//            }
//        });

        watcherUtil = new WatcherUtil(et_password,"password");
        //过滤中文空格
        et_password.addTextChangedListener(watcherUtil);

        btn_login.setOnClickListener(this);
        tv_serverConfig.setOnClickListener(this);
        tv_forgetPassword.setOnClickListener(this);
        iv_eye.setOnClickListener(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
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
                intent.putExtra("from",1);
                startActivity(intent);
                break;
            case R.id.serverConfig:
                intent = new Intent(LoginActivity.this,ServerConfigActivity.class);
                startActivity(intent);
                break;
            case R.id.login:

                login();


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
        String url = Constant.SERVER_URL+"user/login";
        if (TextUtils.isEmpty(et_username.getText().toString())) {
            tv_prompt.setVisibility(View.VISIBLE);
            return;
        }
        if (TextUtils.isEmpty(et_password.getText().toString())) {
            ToastUtil.showShort(LoginActivity.this,"密码不能为空");
            return;
        }
        if (pd != null) pd.createDialog().show();

        OkHttpUtils.post()
                .url(url)
                .addParams("userCode",et_username.getText().toString())
                .addParams("password",et_password.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("response error",e.getMessage()+" ");
                        pd.dismissDialog();
                        ErrorUtil.NetWorkToast(LoginActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response + " ");
                        pd.dismissDialog();

                        try {
                            JSONObject object = new JSONObject(response);
                            String statusCode = object.getString("statusCode");
                            String message = object.getString("message");
                            if (statusCode.equals("0")) {
                                SharedPreferences sp = getSharedPreferences("userinfo",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                Constant.username = et_username.getText().toString();
                                editor.putString("username",et_username.getText().toString());
                                editor.putString("password", et_password.getText().toString());
                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                ToastUtil.showShort(LoginActivity.this,"登录成功");
                                LoginActivity.this.finish();
                            } else if (statusCode.equals("1")) {
                                ToastUtil.show(LoginActivity.this,"用户名不存在");
                            } else if (statusCode.equals("2")) {
                                ToastUtil.show(LoginActivity.this,"密码错误");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });

    }
}
