package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Department;
import com.example.administrator.sjassistant.bean.MyContacts;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.OperatorUtil;
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
 * Created by Administrator on 2016/4/4.
 */
public class CompanyActivity extends BaseActivity implements View.OnClickListener {

    private ImageView search, delete;
    private EditText ed_name;

    private TextView bt_left, bt_right, tv_center;
    private ImageView bt_left2;

    private String name;
    private ListView apartment_list;
    private ListView contact_list;
    private TextView text_add_person;

    private View v;
    /*
     * 保存添加的结果
     */
    private List<MyContacts> result = new ArrayList<MyContacts>();

    private List<MyContacts> contactData = new ArrayList<MyContacts>();

    private List<Department> departmentData = new ArrayList<Department>();
    private CommonAdapter<MyContacts> contactAdapter;
    private CommonAdapter<Department> departmentAdapter;

    /*
     * id 表示进入的部门id
     */
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
            id = savedInstanceState.getInt("id");
        else
            id = getIntent().getIntExtra("id",0);
    }

    @Override
    protected void initView() {
        super.initView();

        setTopText("公司");
        setCenterView(R.layout.activity_company);

        search = (ImageView)findViewById(R.id.search);
        ed_name = (EditText)findViewById(R.id.search_content);
        delete = (ImageView)findViewById(R.id.delete_word);
        text_add_person = (TextView)findViewById(R.id.text_add_person);

        apartment_list = (ListView)findViewById(R.id.apartment_list);
        contact_list = (ListView)findViewById(R.id.manager_list);

        v = findViewById(R.id.div);
//       
//        apartment_list.setAdapter(new CommonAdapter<String>(this, da, R.layout.item_choose_company) {
//            @Override
//            public void convert(ViewHolder holder, String s) {
//                if (s.equals("全部分所")) {
//                    holder.setText(R.id.apartment_name, "全部分所");
//                } else {
//                    holder.setText(R.id.apartment_name,"北京分所");
//                }
//            }
//        });

//        apartment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String name = (String)apartment_list.getItemAtPosition(position);
//
//                Log.d("tag",name+" ");
//                Intent intent = new Intent(CompanyActivity.this,ContactsDetailActivity.class);
//                intent.putExtra("name",name);
//                startActivity(intent);
//            }
//        });


        search.setOnClickListener(this);
        ed_name.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

    /*
     * listview的点击操作
     */
    private void initItemClick() {

        contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyContacts m = (MyContacts) contact_list.getItemAtPosition(position);
                ImageView iv = (ImageView) view.findViewById(R.id.iv_checked);
            }
        });

        apartment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Department department = (Department) apartment_list.getItemAtPosition(position);
                int tempId = department.getId();
                Intent intent = new Intent(CompanyActivity.this, CompanyActivity.class);
                intent.putExtra("id", tempId);
                startActivity(intent);
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();

        if (contactData.size() == 0 && departmentData.size() == 0)
            getData(id);

        ed_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        initItemClick();
    }

    /*
     * 获取部门  人员选择
     */
    private void getData(int id) {
        //departmentData.clear();
        //contactData.clear();
        pd.createDialog().show();
        String url = Constant.SERVER_URL + "message/queryPerson";

        OkHttpUtils.post()
                .url(url)
                .addParams("id",String.valueOf(id))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(CompanyActivity.this);
                        pd.dismissDialog();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.optInt("statusCode", 0);
                            JSONObject data = object.optJSONObject("data");
                            JSONArray listu = data.optJSONArray("listu");
                            JSONArray listd = data.optJSONArray("listd");
                            int lenU = listu.length();
                            int lenD = listd.length();
                            if (statusCode == 0) {
                                if (lenU != 0) {
                                    for (int i = 0; i < lenU; i++) {
                                        JSONObject user = listu.optJSONObject(i);
                                        MyContacts contact = new MyContacts();
                                        contact.setId(user.optInt("id"));
                                        contact.setUsername(user.optString("username"));
                                        contactData.add(contact);
                                    }
                                }

                                if (lenD != 0) {
                                    for (int i = 0; i < lenD; i++) {
                                        JSONObject depart = listd.optJSONObject(i);
                                        Department department = new Department();
                                        department.setId(depart.optInt("id"));
                                        department.setName(depart.optString("name"));
                                        department.setCode(depart.optString("code"));
                                        departmentData.add(department);
                                    }
                                    text_add_person.setText("选择部门");
                                } else if (lenU != 0) {
                                    v.setVisibility(View.GONE);
                                    text_add_person.setText("选择联系人");
                                } else {
                                    v.setVisibility(View.GONE);
                                    text_add_person.setText("没有更多的联系人了");
                                }

                                departmentAdapter = new CommonAdapter<Department>(CompanyActivity.this, departmentData, R.layout.item_company) {
                                    @Override
                                    public void convert(ViewHolder holder, Department department) {
                                        holder.getView(R.id.phone).setVisibility(View.GONE);
                                        holder.getView(R.id.group).setVisibility(View.GONE);
                                        holder.getView(R.id.right_arrow1).setVisibility(View.VISIBLE);
                                        holder.setText(R.id.name, department.getName());
                                    }
                                };

                                apartment_list.setAdapter(departmentAdapter);
                                OperatorUtil.setListViewHeight(apartment_list);

                                contactAdapter = new CommonAdapter<MyContacts>(CompanyActivity.this, contactData, R.layout.item_company) {
                                    @Override
                                    public void convert(ViewHolder holder, MyContacts contact) {
                                        holder.getView(R.id.phone).setVisibility(View.VISIBLE);
                                        holder.getView(R.id.right_arrow1).setVisibility(View.GONE);
                                        holder.setText(R.id.name, contact.getTrueName());
                                        holder.setText(R.id.group,contact.getDeptName());
                                    }
                                };

                                contact_list.setAdapter(contactAdapter);
                                OperatorUtil.setListViewHeight(contact_list);

                            } else {
                                ToastUtil.showShort(CompanyActivity.this, "服务器异常");
                            }
                            pd.dismissDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }



    /*
     * 搜索联系人
     */
    private void filterData(String text) {
        List<MyContacts> filterList = new ArrayList<MyContacts>();

        if (TextUtils.isEmpty(text)) {
            filterList = contactData;
        } else {
            for (MyContacts contacts:contactData) {
                if (contacts.getUsername().contains(text)) {
                    filterList.add(contacts);
                }
            }
        }

        contactAdapter.updateListView(filterList);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("id",id);
    }
}
