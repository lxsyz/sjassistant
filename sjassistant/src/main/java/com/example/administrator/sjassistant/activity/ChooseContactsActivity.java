package com.example.administrator.sjassistant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;

/**
 * Created by Administrator on 2016/4/2.
 */
public class ChooseContactsActivity extends Activity implements View.OnClickListener {

    private ImageView bt_left;
    private TextView bt_right;
    private TextView title;
    private RelativeLayout layout_top;

    private LinearLayout person_work_layout,person_apartment_layout,customer_type_layout;
    private TextView customer_type,person_work,person_apartment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosecontacts);
        initWindow();
        initView();
    }

    private void initView() {
        layout_top = (RelativeLayout)findViewById(R.id.layout_top);
        bt_right = (TextView)layout_top.findViewById(R.id.bt_right);
        bt_left = (ImageView)layout_top.findViewById(R.id.bt_left);
        title = (TextView)findViewById(R.id.tv_center);

        customer_type_layout = (LinearLayout)findViewById(R.id.customer_type_layout);
        person_work_layout = (LinearLayout)findViewById(R.id.person_work_layout);
        person_apartment_layout = (LinearLayout)findViewById(R.id.person_apartment_layout);

        person_work = (TextView)findViewById(R.id.person_work);
        customer_type = (TextView)findViewById(R.id.customer_type);
        person_apartment = (TextView)findViewById(R.id.person_apartment);

        customer_type_layout.setOnClickListener(this);
        person_apartment_layout.setOnClickListener(this);
        person_work_layout.setOnClickListener(this);

        bt_left.setOnClickListener(this);
        title.setText("筛选联系人");
        bt_right.setText("确定");
        bt_left.setOnClickListener(this);
        bt_right.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bt_left:
                onBackPressed();
                break;
            case R.id.bt_right:
                intent = new Intent(ChooseContactsActivity.this,SearchResultActivity.class);
                intent.putExtra("type","筛选结果");
                startActivity(intent);
                break;
            case R.id.customer_type_layout:
                intent = new Intent(ChooseContactsActivity.this,EditActivity.class);
                intent.putExtra("top","客户类型").putExtra("result",1);
                startActivityForResult(intent,1);
                break;
            case R.id.person_apartment_layout:
                intent = new Intent(ChooseContactsActivity.this,EditActivity.class);
                intent.putExtra("top","人员部门").putExtra("result",2);
                startActivityForResult(intent,2);
                break;
            case R.id.person_work_layout:
                intent = new Intent(ChooseContactsActivity.this,EditActivity.class);
                intent.putExtra("top","人员职务").putExtra("result",3);
                startActivityForResult(intent,3);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("tag",requestCode+"  "+resultCode+"  ");
        String result = "";
        switch (resultCode) {
            case 1:
                result = data.getStringExtra("result");
                customer_type.setText(result);
                break;
            case 2:
                result = data.getStringExtra("result");
                person_apartment.setText(result);
                break;
            case 3:
                result = data.getStringExtra("result");
                person_work.setText(result);
                break;
        }


        super.onActivityResult(requestCode, resultCode, data);

    }

    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
