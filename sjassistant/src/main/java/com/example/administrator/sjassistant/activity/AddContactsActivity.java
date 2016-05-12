package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.bean.FilterCondition;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/4.
 */
public class AddContactsActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout customer_type_layout,apartment_layout,person_work_layout;

    private EditText company_name,name,phone;

    private TextView customer_type_value,apartment_value,person_work_value;

    private Button confirm;

    private int customerId;
    private int deptId;
    private int postId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_add_contacts);
        setTopText("创建新联系人");

        customer_type_layout = (LinearLayout)findViewById(R.id.customer_type_layout);
        apartment_layout = (LinearLayout)findViewById(R.id.apartment_layout);
        person_work_layout = (LinearLayout)findViewById(R.id.person_work_layout);

        company_name = (EditText)findViewById(R.id.company_name);
        name = (EditText)findViewById(R.id.name);
        phone = (EditText)findViewById(R.id.phone);
        confirm = (Button)findViewById(R.id.confirm);

        apartment_value = (TextView)findViewById(R.id.apartment_value);
        customer_type_value = (TextView)findViewById(R.id.customer_type_value);
        person_work_value = (TextView)findViewById(R.id.person_work_value);


        customer_type_layout.setOnClickListener(this);
        apartment_layout.setOnClickListener(this);
        person_work_layout.setOnClickListener(this);
        confirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
                case R.id.customer_type_layout:
                    intent = new Intent(AddContactsActivity.this,EditActivity.class);
                    intent.putExtra("top", "客户类型").putExtra("result", 1);
                    startActivityForResult(intent,1);
                    break;
                case R.id.apartment_layout:
                    intent = new Intent(AddContactsActivity.this,EditActivity.class);
                    intent.putExtra("top","人员部门").putExtra("result",2);
                    startActivityForResult(intent,2);
                    break;
                case R.id.person_work_layout:
                    intent = new Intent(AddContactsActivity.this,EditActivity.class);
                    intent.putExtra("top","人员职务").putExtra("result",3);
                    startActivityForResult(intent,3);
                    break;
                case R.id.confirm:
                    postInfo();
                    break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FilterCondition result = null;
            switch (resultCode) {
                case 1:
                    result = (FilterCondition) data.getSerializableExtra("result");
                    customerId = result.getId();
                    customer_type_value.setText(result.getName());
                    break;
                case 2:
                    result = (FilterCondition) data.getSerializableExtra("result");
                    deptId = result.getId();
                    apartment_value.setText(result.getName());
                    break;
                case 3:
                    result = (FilterCondition) data.getSerializableExtra("result");
                    postId = result.getId();
                    person_work_value.setText(result.getName());
                    break;
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
     * 提交
     */
    private void postInfo() {

        String url = Constant.SERVER_URL + "phoneBook/addCustomer";

        if (TextUtils.isEmpty(company_name.getText().toString())) {
            ToastUtil.showShort(AddContactsActivity.this,"公司不能为空");
            return;
        }

        if (TextUtils.isEmpty(name.getText().toString())) {
            ToastUtil.showShort(AddContactsActivity.this,"联系人姓名不能为空");
            return;
        }

        if (!OperatorUtil.isPhoneNumber(phone.getText().toString())) {
            ToastUtil.showShort(AddContactsActivity.this,"电话格式不正确");
            return;
        }
        OkHttpUtils.post()
                .url(url)
                .addParams("customerName",company_name.getText().toString())
                .addParams("customerType", String.valueOf(customerId))
                .addParams("customerDept", String.valueOf(deptId))
                .addParams("customerPost", String.valueOf(postId))
                .addParams("linkPhone", phone.getText().toString())
                .addParams("linkName",name.getText().toString())
                .addParams("userCode",Constant.username)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error",e.getMessage());
                        ErrorUtil.NetWorkToast(AddContactsActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response);
                        try {
                            int statusCode = new JSONObject(response).getInt("statusCode");
                            if (statusCode == 0) {
                                ToastUtil.showShort(AddContactsActivity.this,"添加成功");
                                AddContactsActivity.this.finish();
                            } else {
                                ToastUtil.showShort(AddContactsActivity.this,"添加失败，服务器异常");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}
