package com.example.administrator.sjassistant.activity;

import android.os.Bundle;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.util.OperatorUtil;

/**
 * Created by Administrator on 2016/3/28.
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_username,et_validate,et_password;
    private ImageView eye,success;
    private Button btn_confirm;
    private TextView tv_getcode;
    private int flag = 1;

    /*
     * 从忘记密码进入   from = 1
     * 从更换邮箱进入   from = 2
     */
    private int from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        btn_confirm = (Button)findViewById(R.id.confirm);


        et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if (!OperatorUtil.isEmail(et_username.getText().toString())) {
                        success.setVisibility(View.VISIBLE);
                    } else {
                        success.setVisibility(View.GONE);
                    }
                }
            }
        });

        tv_getcode.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        eye.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:

                break;
            case R.id.getCode:

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
}
