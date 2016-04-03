package com.example.administrator.sjassistant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/3.
 */
public class ChooseApartmentActivity extends Activity implements View.OnClickListener {

    private ImageView bt_left;

    private TextView tv_center;
    private TextView btn_right;
    private TextView btn_left;
    private LinearLayout search_layout;
    private ImageView search,delete;
    private EditText ed_name;

    private ListView apartment_list;
    private List<String> da = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_apartment);

        initWindow();
        initView();
        initListeners();
    }

    private void initView() {


        tv_center = (TextView)findViewById(R.id.tv_center);
        bt_left = (ImageView)findViewById(R.id.bt_left);
        btn_left = (TextView)findViewById(R.id.bt_left2);
        btn_right = (TextView)findViewById(R.id.bt_right);

        tv_center.setText("添加部门");

        bt_left.setVisibility(View.INVISIBLE);
        btn_left.setText("取消");
        btn_left.setVisibility(View.VISIBLE);
        btn_right.setText("确定");


        search_layout = (LinearLayout)findViewById(R.id.search_layout);
        search = (ImageView)search_layout.findViewById(R.id.search);
        ed_name = (EditText)search_layout.findViewById(R.id.search_content);
        delete = (ImageView)search_layout.findViewById(R.id.delete_word);




        apartment_list = (ListView)findViewById(R.id.apartment_list);

        da.add("asdsd");
        da.add("faff");
        apartment_list.setAdapter(new CommonAdapter<String>(this,da,R.layout.item_choose_apartment) {
            @Override
            public void convert(ViewHolder holder, String s) {

            }
        });
    }

    private void initListeners() {
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        search.setOnClickListener(this);
        ed_name.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left2:
                onBackPressed();
                break;
            case R.id.bt_right:
                break;
            case R.id.search:
                if (!TextUtils.isEmpty(ed_name.getText().toString())) {

//                    intent = new Intent(this, SearchResultActivity.class);
//                    intent.putExtra("name",ed_name.getText().toString());
//                    startActivity(intent);
                }
                break;
            case R.id.delete_word:
                ed_name.setText("");
                break;
        }
    }

    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
