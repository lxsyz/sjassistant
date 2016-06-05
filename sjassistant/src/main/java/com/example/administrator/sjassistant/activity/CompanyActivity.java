package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.ContactsPerson;
import com.example.administrator.sjassistant.bean.Department;
import com.example.administrator.sjassistant.bean.MyContacts;
import com.example.administrator.sjassistant.bean.Person;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.OperatorUtil;
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
    private ListView search_list;
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
    private CommonAdapter<MyContacts> searchAdapter;
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
            id = getIntent().getIntExtra("id", 0);
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
        search_list = (ListView)findViewById(R.id.search_list);

        v = findViewById(R.id.div);


        ed_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {

                    queryUserByType(ed_name.getText().toString());

                    return true;
                }
                return false;
            }
        });

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
                                    Gson gson = new Gson();
                                    contactData = gson.fromJson(listu.toString(), new TypeToken<List<MyContacts>>() {
                                    }.getType());
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
                                    isDividerShow = false;
                                    v.setVisibility(View.GONE);
                                    text_add_person.setText("选择联系人");
                                } else {
                                    isDividerShow = false;
                                    v.setVisibility(View.GONE);
                                    text_add_person.setText("没有更多的联系人了");
                                }

                                if (departmentAdapter != null) {
                                    departmentAdapter.updateListView(departmentData);
                                } else {
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
                                }
                                OperatorUtil.setListViewHeight(apartment_list);

                                if (contactAdapter != null) {
                                    contactAdapter.updateListView(contactData);
                                } else {
                                    contactAdapter = new CommonAdapter<MyContacts>(CompanyActivity.this, contactData, R.layout.item_company) {
                                        @Override
                                        public void convert(final ViewHolder holder, final MyContacts contact) {
                                            holder.getView(R.id.phone).setVisibility(View.VISIBLE);
                                            holder.getView(R.id.right_arrow1).setVisibility(View.GONE);
                                            holder.setText(R.id.name, contact.getTrueName());
                                            holder.setText(R.id.group, contact.getDeptName());

                                            holder.getView(R.id.phone).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(CompanyActivity.this, MoreContact.class);
                                                    Person person = new Person();
                                                    person.setLinkPhone(contact.getPhone());
                                                    person.setLinkName(contact.getTrueName());
                                                    person.setUserCode(contact.getUserCode());
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable("person", person);
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    };

                                    contact_list.setAdapter(contactAdapter);
                                }
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
     * 当前页面搜索联系人
     */
    private void filterData(String text) {
        List<MyContacts> filterList = new ArrayList<MyContacts>();

        if (TextUtils.isEmpty(text)) {
            filterList = contactData;


            //控制部门与联系人之间分割区域的显示
            if (isDividerShow) {
                text_add_person.setText("选择部门");
                v.setVisibility(View.VISIBLE);
            } else {
                text_add_person.setText("选择联系人");
                v.setVisibility(View.GONE);
            }
            search_list.setVisibility(View.GONE);
            apartment_list.setVisibility(View.VISIBLE);
            contact_list.setVisibility(View.VISIBLE);
        } else {
            text_add_person.setText("选择联系人");
            apartment_list.setVisibility(View.GONE);
            v.setVisibility(View.GONE);
            contact_list.setVisibility(View.VISIBLE);
            filterList.clear();
            for (MyContacts contacts:contactData) {
                if (contacts.getTrueName() != null) {
                    if (contacts.getTrueName().contains(text)) {
                        filterList.add(contacts);
                    }
                }
            }
        }

        contactAdapter.updateListView(filterList);
        OperatorUtil.setListViewHeight(contact_list);
    }

    /*
     * 所有联系人搜索
     */
    private void queryUserByType(String name) {
        String url = Constant.SERVER_URL + "phoneBook/queryUserByType";

        OkHttpUtils.post()
                .url(url)
                .addParams("userCode",Constant.username)
                .addParams("trueName",name)
                .addParams("roleName","dept")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(CompanyActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            if (statusCode == 0) {
                                Gson gson = new Gson();
                                JSONObject data = object.optJSONObject("data");
                                if (data != null && data.length() != 0) {

                                    JSONArray list = data.optJSONArray("list");
                                    if (list.length() != 0) {
                                        v.setVisibility(View.GONE);
                                        search_list.setVisibility(View.VISIBLE);
                                        apartment_list.setVisibility(View.GONE);
                                        contact_list.setVisibility(View.GONE);
                                        text_add_person.setText("搜索结果");
                                        List<MyContacts> contactData = gson.fromJson(list.toString(), new TypeToken<List<MyContacts>>() {
                                        }.getType());

                                        if (searchAdapter != null) {
                                            searchAdapter.updateListView(contactData);
                                        } else {
                                            searchAdapter = new CommonAdapter<MyContacts>(CompanyActivity.this, contactData, R.layout.item_company) {
                                                @Override
                                                public void convert(final ViewHolder holder, final MyContacts contact) {
                                                    holder.getView(R.id.phone).setVisibility(View.VISIBLE);
                                                    holder.getView(R.id.right_arrow1).setVisibility(View.GONE);
                                                    holder.setText(R.id.name, contact.getTrueName());
                                                    //holder.setText(R.id.group, contact.getCustomerDept());

                                                    holder.getView(R.id.phone).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(CompanyActivity.this, MoreContact.class);
                                                            Person person = new Person();
                                                            person.setLinkPhone(contact.getPhone());
                                                            person.setLinkName(contact.getTrueName());
                                                            person.setUserCode(contact.getUserCode());
                                                            Bundle bundle = new Bundle();
                                                            bundle.putSerializable("person", person);
                                                            intent.putExtras(bundle);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }
                                            };
                                            search_list.setAdapter(searchAdapter);
                                        }
                                    } else {
                                        search_list.setVisibility(View.GONE);
                                        text_add_person.setText("无搜索结果");
                                    }
                                }
                            } else {
                                ToastUtil.showShort(CompanyActivity.this, "获取用户列表失败");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("id",id);
    }

    private boolean isDividerShow = true;
}
