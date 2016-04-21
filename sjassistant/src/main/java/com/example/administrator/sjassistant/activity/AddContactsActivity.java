package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;

/**
 * Created by Administrator on 2016/4/4.
 */
public class AddContactsActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout customer_type_layout,apartment_layout,person_work_layout;

    private EditText company_name,name,phone;

    private TextView customer_type_value,apartment_value,person_work_value;

    private Button confirm;
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = "";
        switch (resultCode) {
            case 1:
                result = data.getStringExtra("result");
                customer_type_value.setText(result);
                break;
            case 2:
                result = data.getStringExtra("result");
                apartment_value.setText(result);
                break;
            case 3:
                result = data.getStringExtra("result");
                person_work_value.setText(result);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
