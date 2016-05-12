package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.util.WatcherUtil;
import com.example.administrator.sjassistant.view.MyPromptDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/3/28.
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_username,et_validate,et_password;
    private ImageView eye,success;
    private Button btn_confirm;
    private TextView tv_getcode,prompt_tv;
    private int flag = 1;
    private MyPromptDialog pd;
    //控制验证码60秒才能再获取
    private Timer timer = new Timer();
    private TimerTask mTimerTask;
    private int time = 60;
    /*
     * 从忘记密码进入   from = 1
     * 从更换邮箱进入   from = 2
     */
    private int from;

    //存储验证码信息
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new MyPromptDialog(this);
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activty_forgetpassword);


        from = getIntent().getIntExtra("from",-1);

        if (from == 1) {
            setTopText(R.string.findPassword);
        } else if(from == 2) {
            setTopText("更换绑定邮箱");
        }
        et_validate = (EditText)findViewById(R.id.validateCode);
        et_password = (EditText)findViewById(R.id.password);
        et_username = (EditText)findViewById(R.id.email);
        eye = (ImageView)findViewById(R.id.eye);
        success = (ImageView)findViewById(R.id.prompt);
        tv_getcode = (TextView)findViewById(R.id.getCode);
        prompt_tv = (TextView)findViewById(R.id.prompt_text);
        btn_confirm = (Button)findViewById(R.id.confirm);


//        et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    if (!OperatorUtil.isEmail(et_username.getText().toString())) {
//                        success.setVisibility(View.GONE);
//                        prompt_tv.setVisibility(View.VISIBLE);
//                    } else {
//                        success.setVisibility(View.VISIBLE);
//                        prompt_tv.setVisibility(View.GONE);
//                    }
//                }
//            }
//        });

        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (OperatorUtil.isEmail(s.toString())) {
                    prompt_tv.setVisibility(View.GONE);
                } else  {
                    prompt_tv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_password.addTextChangedListener(new WatcherUtil(et_password,"password"));

        tv_getcode.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        eye.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                if (from == 1) {
                    change();
                } else if (from == 2){
                    changeEmail();
                }

                break;
            case R.id.getCode:

                if (TextUtils.isEmpty(et_username.getText().toString())) {
                    prompt_tv.setVisibility(View.VISIBLE);
                    return;
                }

                if (mTimerTask != null) {
                    mTimerTask.cancel();
                    mTimerTask = null;
                }
                time = 60;
                mTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        time--;
                        Message m = new Message();
                        m.what = 1;
                        handler.sendMessage(m);
                    }
                };
                timer = new Timer();
                timer.schedule(mTimerTask,1000,1000);
                tv_getcode.setEnabled(false);
                getValidateCode();

                break;
            case R.id.eye:
                //设置密码可见或隐藏
                if (flag == 1) {
                    eye.setImageResource(R.drawable.noshowpassword);
                    flag = 0;
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    //et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    eye.setImageResource(R.drawable.showpassword);
                    flag = 1;
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

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
     * 获取验证码
     */
    private void getValidateCode() {
        String url = Constant.SERVER_URL+"user/getValidateCode";



        OkHttpUtils.post()
                .url(url)
                .addParams("email",et_username.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error",e.getMessage()+" ");
                        ErrorUtil.NetWorkToast(ForgetPasswordActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response+" ");
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            message = object.getString("message");

                            if (statusCode == 0) {
                                Toast.makeText(ForgetPasswordActivity.this,"获取验证码成功",Toast.LENGTH_LONG).show();
                            } else if (statusCode == 4) {
                                ToastUtil.showShort(ForgetPasswordActivity.this,"邮箱错误");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    /*
     * 提交修改密码请求
     */
    private void change() {

        if (TextUtils.isEmpty(et_username.getText().toString())) {
            prompt_tv.setVisibility(View.VISIBLE);
            return;
        }
        if (TextUtils.isEmpty(et_password.getText().toString())) {
            ToastUtil.show(ForgetPasswordActivity.this, "密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(et_validate.getText().toString())) {
            ToastUtil.show(ForgetPasswordActivity.this,"验证码不能为空");
            return;
        }

        if (pd != null) {
            pd.createDialog().show();
        }
        if (message.equals(et_validate.getText().toString())) {
            String url = Constant.SERVER_URL + "user/find";
            OkHttpUtils.post()
                    .url(url)
                    .addParams("email",et_username.getText().toString())
                    .addParams("newPassword",et_password.getText().toString())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            Log.d("error",e.getMessage()+" ");
                            pd.dismissDialog();
                            ErrorUtil.NetWorkToast(ForgetPasswordActivity.this);
                        }

                        @Override
                        public void onResponse(String response) {
                            Log.d("response",response+" ");
                            pd.dismissDialog();
                            try {
                                JSONObject object = new JSONObject(response);
                                int statusCode = object.getInt("statusCode");

                                Log.d("statusCode",statusCode+" ");
                                if (statusCode == 0) {
                                    ToastUtil.showShort(ForgetPasswordActivity.this, "密码修改成功");
                                    SharedPreferences.Editor editor = getSharedPreferences("userinfo",MODE_PRIVATE).edit();
                                    editor.putString("password",et_password.getText().toString());
                                    editor.apply();
                                    ForgetPasswordActivity.this.finish();
                                } else if (statusCode == 4) {
                                    ToastUtil.showShort(ForgetPasswordActivity.this,"邮箱错误");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtil.show(ForgetPasswordActivity.this,"验证码不正确");
        }
    }

    /*
     * 更换邮箱
     */
    private void changeEmail() {
        if (TextUtils.isEmpty(et_username.getText().toString())) {
            prompt_tv.setVisibility(View.VISIBLE);
            return;
        }
        if (TextUtils.isEmpty(et_password.getText().toString())) {
            ToastUtil.show(ForgetPasswordActivity.this, "密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(et_validate.getText().toString()) || !et_validate.getText().toString().equals(message)) {
            ToastUtil.show(ForgetPasswordActivity.this,"验证码不正确");
            return;
        }

        if (pd != null) {
            pd.createDialog().show();
        }
        if (message.equals(et_validate.getText().toString())) {
            String url = Constant.SERVER_URL + "user/settings/changeEmail";
            OkHttpUtils.post()
                    .url(url)
                    .addParams("userCode",Constant.username)
                    .addParams("newEmail", et_username.getText().toString())
                    .addParams("password",et_password.getText().toString())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            Log.d("error",e.getMessage()+" ");
                            pd.dismissDialog();
                            ErrorUtil.NetWorkToast(ForgetPasswordActivity.this);
                        }

                        @Override
                        public void onResponse(String response) {
                            Log.d("response",response+" ");
                            pd.dismissDialog();
                            try {
                                JSONObject object = new JSONObject(response);
                                int statusCode = object.getInt("statusCode");

                                Log.d("statusCode",statusCode+" ");
                                if (statusCode == 0) {
                                    ToastUtil.showShort(ForgetPasswordActivity.this, "邮箱修改成功");
                                    Intent intent = new Intent();
                                    intent.putExtra("email",et_username.getText().toString());
                                    setResult(1,intent);
                                    ForgetPasswordActivity.this.finish();
                                } else if (statusCode == 4) {
                                    ToastUtil.showShort(ForgetPasswordActivity.this,"密码错误");
                                } else if (statusCode == 16) {
                                    ToastUtil.showShort(ForgetPasswordActivity.this,"邮箱已存在");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtil.show(ForgetPasswordActivity.this,"验证码不正确");
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                tv_getcode.setText(time + "秒后再获取");
                if (time < 1) {
                    timer.cancel();
                    tv_getcode.setEnabled(true);
                    tv_getcode.setText("免费获取验证码");
                }
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("message",message+" ");
    }
}
