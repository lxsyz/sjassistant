package com.example.administrator.sjassistant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Department;
import com.example.administrator.sjassistant.bean.GonggaoType;
import com.example.administrator.sjassistant.util.AppManager;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

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
    private List<Department> datalist = new ArrayList<Department>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_apartment);
        AppManager.getInstance().addActivity(this);
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


        apartment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Log.d("tag","header");

                }
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

    @Override
    protected void onResume() {
        super.onResume();

        getApartment();

    }

    /*
     * 获取部门列表
     */
    private void getApartment() {
        String url = Constant.SERVER_URL + "notes/showDepartment";

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(ChooseApartmentActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response + " ");
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            Log.d("statusCode",statusCode+" ");
                            if (statusCode == 0) {
                                JSONObject data = object.getJSONObject("data");
                                JSONArray list = data.getJSONArray("list");

                                int len = list.length();

                                for (int i = 0;i < len;i++) {
                                    JSONObject o = list.getJSONObject(i);
                                    Department type = new Department();
                                    type.setId(o.getInt("id"));
                                    type.setCode(o.getString("code"));
                                    type.setName(o.getString("name"));
                                    datalist.add(type);
                                }

                                apartment_list.addHeaderView(getLayoutInflater().inflate(R.layout.item_choose_apartment,null));
                                apartment_list.setAdapter(new CommonAdapter<Department>(ChooseApartmentActivity.this, datalist, R.layout.item_choose_apartment) {
                                    @Override
                                    public void convert(ViewHolder holder, Department apartment) {
                                        holder.setText(R.id.apartment_name, apartment.getName());
                                    }
                                });


                            } else {
                                ToastUtil.show(ChooseApartmentActivity.this,"服务器异常");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
