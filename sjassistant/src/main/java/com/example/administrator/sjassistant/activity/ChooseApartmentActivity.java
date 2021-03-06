package com.example.administrator.sjassistant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
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
import com.example.administrator.sjassistant.bean.MyContacts;
import com.example.administrator.sjassistant.util.AddPersonManager;
import com.example.administrator.sjassistant.util.AppManager;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ServerConfigUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
    private TextView prompt;
    private LinearLayout search_layout;
    private ImageView search,delete;
    private EditText ed_name;

    private ListView apartment_list;
    private ListView search_list;

    private List<Department> datalist = new ArrayList<>();


    //private List<Department> result = new ArrayList<>();

    private CommonAdapter<Department> commonAdapter;
    private CommonAdapter<Department> searchAdapter;
    private boolean isHeaderChecked = false;

    /*
    * 已添加的成员数目
    */
    //private int count = 0;


    /*
     * id 表示进入的部门id
     */
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_apartment);
        AppManager.getInstance().addActivity(this);
        //AddPersonManager.getInstance().addActivity(this);
        SharedPreferences sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        Constant.username = sp.getString("username", null);

        if (TextUtils.isEmpty(sp.getString("server_address", null))) {
            Constant.SERVER_URL = Constant.TEST_SERVER_URL;
        } else {
            ServerConfigUtil.setServerConfig(this);
        }

//        count = getIntent().getIntExtra("count", 0);
        id = getIntent().getIntExtra("id",0);

//        result = (ArrayList<Department>) getIntent().getSerializableExtra("result");
//        if (result == null) {
//            result = new ArrayList<Department>();
//        }

        initWindow();
        initView();


        initListeners();
    }

    private void initView() {


        tv_center = (TextView)findViewById(R.id.tv_center);
        bt_left = (ImageView)findViewById(R.id.bt_left);
        btn_left = (TextView)findViewById(R.id.bt_left2);
        btn_right = (TextView)findViewById(R.id.bt_right);
        prompt = (TextView)findViewById(R.id.prompt);

        tv_center.setText("添加部门");

        bt_left.setVisibility(View.INVISIBLE);
        btn_left.setText("取消");
        btn_left.setVisibility(View.VISIBLE);
        btn_right.setText("确定");


        search_layout = (LinearLayout)findViewById(R.id.search_layout);
        search = (ImageView)search_layout.findViewById(R.id.search);
        ed_name = (EditText)search_layout.findViewById(R.id.search_content);
        delete = (ImageView)search_layout.findViewById(R.id.delete_word);

        ed_name.setHint("搜索部门");


        apartment_list = (ListView)findViewById(R.id.apartment_list);
        search_list = (ListView)findViewById(R.id.search_list);

        apartment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ImageView iv = (ImageView) view.findViewById(R.id.iv_checked);
                Department d = (Department) apartment_list.getItemAtPosition(position);
                Intent intent = new Intent(ChooseApartmentActivity.this, ChooseApartmentActivity.class);
                intent.putExtra("id", d.getId());
//                intent.putExtra("count", count);
                //intent.putExtra("result", (ArrayList) result);
                startActivity(intent);

            }
            // }
        });

        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Department d = (Department) apartment_list.getItemAtPosition(position);
                Intent intent = new Intent(ChooseApartmentActivity.this, ChooseApartmentActivity.class);
                intent.putExtra("id", d.getId());
//                intent.putExtra("count", count);
                //intent.putExtra("result", (ArrayList) result);
                startActivity(intent);
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
                StringBuilder sb = new StringBuilder();
                StringBuilder sb2 = new StringBuilder();
                if (Constant.result.size() > 0) {
                    boolean need = false;

                    for (Department i : Constant.result) {
                        if (need) {
                            sb.append(",");
                            sb2.append(",");
                        }
                        sb.append(i.getName());
                        sb2.append(i.getId());
                        need = true;
                    }
                }
                Intent intent = new Intent(ChooseApartmentActivity.this,PostInformActivity.class);
                intent.putExtra("result",sb.toString());
                intent.putExtra("departmentId", sb2.toString());
                Constant.result.clear();
                Constant.count = 0;
                startActivity(intent);
                this.finish();
                break;
            case R.id.search:
                if (!TextUtils.isEmpty(ed_name.getText().toString())) {

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
        if (Constant.count != 0) {
            btn_right.setText("确定(" + Constant.count + ")");
        } else btn_right.setText("确定");


        getApartment();

        ed_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDepartment(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ed_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {

                    queryUser(ed_name.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    /*
     * 获取部门列表
     */
    private void getApartment() {
        datalist.clear();
        String url = Constant.SERVER_URL + "message/queryPerson";

        OkHttpUtils.post()
                .url(url)
                .addParams("id", String.valueOf(id))
                .addParams("userCode",Constant.username)
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
                            Log.d("statusCode", statusCode + " ");
                            if (statusCode == 0) {
                                JSONObject data = object.getJSONObject("data");
                                JSONArray list = data.getJSONArray("listd");

                                int len = list.length();

                                if (len != 0) {
                                    for (int i = 0; i < len; i++) {
                                        JSONObject depart = list.optJSONObject(i);
                                        Department department = new Department();
                                        department.setId(depart.optInt("id"));
                                        department.setName(depart.optString("name"));
                                        department.setCode(depart.optString("code"));
                                        department.setCanChoose(depart.optString("canChoose"));
                                        //根据id判断哪些是已选择的部分
                                        //解决回退时之前选择消失的bug
                                        for (Department d:Constant.result) {
                                            if (d.getId() == department.getId()) {
                                                department.setCheckState(true);
                                            }
                                        }

                                        datalist.add(department);
                                    }
                                    prompt.setText("选择部门");

                                } else {
                                    prompt.setText("没有部门提供选择");
                                }


                                if (commonAdapter != null) {
                                    commonAdapter.updateListView(datalist);
                                } else {
                                    commonAdapter = new CommonAdapter<Department>(ChooseApartmentActivity.this, datalist, R.layout.item_choose_apartment) {
                                        @Override
                                        public void convert(ViewHolder holder, final Department department) {
                                            holder.setText(R.id.apartment_name, department.getName());


                                            final ImageView iv = holder.getView(R.id.iv_checked);

                                            if (department.getCanChoose().equals("1")) {
                                                iv.setVisibility(View.VISIBLE);
                                            } else {
                                                iv.setVisibility(View.GONE);
                                            }

                                            if (department.isCheckState()) {
                                                holder.setImageResource(R.id.iv_checked, R.drawable.radio_checked);
                                            } else {
                                                holder.setImageResource(R.id.iv_checked, R.drawable.radio_unchecked);
                                            }
                                            iv.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (department.isCheckState()) {
                                                        iv.setImageResource(R.drawable.radio_unchecked);
                                                        //result.remove(department);
                                                        Constant.result.remove(department);
                                                        department.setCheckState(false);
                                                        Constant.count--;
                                                    } else {
                                                        //result.add(department);
                                                        Constant.result.add(department);
                                                        department.setCheckState(true);
                                                        iv.setImageResource(R.drawable.radio_checked);
                                                        Constant.count++;
                                                    }

                                                    if (Constant.count != 0) {
                                                        btn_right.setText("确定(" + Constant.count + ")");
                                                    } else {
                                                        btn_right.setText("确定");
                                                    }
                                                }
                                            });
                                        }
                                    };
                                    apartment_list.setAdapter(commonAdapter);
                                }
                            } else {
                                ToastUtil.show(ChooseApartmentActivity.this, "服务器异常");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 搜索部门
     * @param name
     */
    private void queryUser(String name) {
        String url = Constant.SERVER_URL + "message/queryPerson";

        OkHttpUtils.post()
                .url(url)
                .addParams("id", String.valueOf(id))
                .addParams("userCode",Constant.username)
                .addParams("name",name)
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
                            Log.d("statusCode", statusCode + " ");
                            if (statusCode == 0) {
                                JSONObject data = object.getJSONObject("data");

                                Gson gson = new Gson();
                                if (data != null && data.length() != 0) {

                                    JSONArray list = data.optJSONArray("listd");
                                    if (list.length() != 0) {
                                        search_list.setVisibility(View.VISIBLE);
                                        apartment_list.setVisibility(View.GONE);
                                        prompt.setText("搜索结果");
                                        List<Department> contactData = gson.fromJson(list.toString(), new TypeToken<List<Department>>() {
                                        }.getType());

                                        if (searchAdapter != null) {
                                            searchAdapter.updateListView(contactData);
                                        } else {
                                            searchAdapter = new CommonAdapter<Department>(ChooseApartmentActivity.this, contactData, R.layout.item_choose_apartment) {
                                                @Override
                                                public void convert(ViewHolder holder, final Department department) {
                                                    holder.setText(R.id.apartment_name, department.getName());


                                                    final ImageView iv = holder.getView(R.id.iv_checked);

                                                    if (department.getCanChoose().equals("1")) {
                                                        iv.setVisibility(View.VISIBLE);
                                                    } else {
                                                        iv.setVisibility(View.GONE);
                                                    }

                                                    if (department.isCheckState()) {
                                                        holder.setImageResource(R.id.iv_checked, R.drawable.radio_checked);
                                                    } else {
                                                        holder.setImageResource(R.id.iv_checked, R.drawable.radio_unchecked);
                                                    }
                                                    iv.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if (department.isCheckState()) {
                                                                iv.setImageResource(R.drawable.radio_unchecked);
                                                                Constant.result.remove(department);
                                                                department.setCheckState(false);
                                                                Constant.count--;
                                                            } else {
                                                                Constant.result.add(department);
                                                                department.setCheckState(true);
                                                                iv.setImageResource(R.drawable.radio_checked);
                                                                Constant.count++;
                                                            }

                                                            if (Constant.count != 0) {
                                                                btn_right.setText("确定(" + Constant.count + ")");
                                                            } else {
                                                                btn_right.setText("确定");
                                                            }
                                                        }
                                                    });
                                                }
                                            };
                                            search_list.setAdapter(searchAdapter);
                                        }
                                    } else {
                                        search_list.setVisibility(View.GONE);
                                        prompt.setText("无搜索结果");
                                    }
                                }
                            } else {
                                ToastUtil.show(ChooseApartmentActivity.this, "服务器异常");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /*
     * 搜索部门
     */
    private void filterDepartment(String text) {
        List<Department> filterlist = new ArrayList<Department>();

        if (TextUtils.isEmpty(text)) {
            filterlist = datalist;

            search_list.setVisibility(View.GONE);
            apartment_list.setVisibility(View.VISIBLE);
        } else {
            prompt.setText("选择部门");
            apartment_list.setVisibility(View.GONE);
            filterlist.clear();
            for (Department d:datalist) {
                String name = d.getName();
                if (name.contains(text)) {
                    filterlist.add(d);
                }
            }
        }
        commonAdapter.updateListView(filterlist);
    }



    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void onBackPressed() {
        //AddPersonManager.getInstance().finishActivity();
        super.onBackPressed();
    }
}
