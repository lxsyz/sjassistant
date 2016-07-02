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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Department;
import com.example.administrator.sjassistant.bean.MyContacts;
import com.example.administrator.sjassistant.bean.Person;
import com.example.administrator.sjassistant.util.AddPersonManager;
import com.example.administrator.sjassistant.util.AppManager;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ServerConfigUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.MyDialog;
import com.example.administrator.sjassistant.view.MyPromptDialog;
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
 * Created by Administrator on 2016/4/25.
 */
public class AddPerson extends Activity implements View.OnClickListener {

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
    //private List<MyContacts> result = new ArrayList<MyContacts>();

    private List<MyContacts> contactData = new ArrayList<MyContacts>();

    private List<Department> departmentData = new ArrayList<Department>();
    private CommonAdapter<MyContacts> contactAdapter;
    private CommonAdapter<Department> departmentAdapter;
    private CommonAdapter<MyContacts> searchAdapter;

    /*
     * 已添加的成员数目
     */
    //private int count = 0;

    /*
     * from = 1表示来自于添加发送消息的联系人
     */
    private int from = 0;

    /*
     * id 表示进入的部门id
     */
    private int id;

    private MyPromptDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_person);
        AppManager.getInstance().addActivity(this);
        AddPersonManager.getInstance().addActivity(this);
        initWindow();

        SharedPreferences sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        Constant.username = sp.getString("username", null);
        if (TextUtils.isEmpty(sp.getString("server_address", null))) {
            Constant.SERVER_URL = Constant.TEST_SERVER_URL;
        } else {
            ServerConfigUtil.setServerConfig(this);
        }

        pd = new MyPromptDialog(this);
        initView();

        //获取从上一层得到的数据
        from = getIntent().getIntExtra("from", 0);
        //count = getIntent().getIntExtra("count", 0);
        id = getIntent().getIntExtra("id",0);
//        Constant.contactResult = (ArrayList<MyContacts>) getIntent().getSerializableExtra("result");
//        if (Constant.contactResult == null) {
//            Constant.contactResult = new ArrayList<MyContacts>();
//        }


    }

    private void initView() {
        search = (ImageView) findViewById(R.id.search);
        ed_name = (EditText) findViewById(R.id.search_content);
        delete = (ImageView) findViewById(R.id.delete_word);
        search.setOnClickListener(this);
        ed_name.setOnClickListener(this);
        delete.setOnClickListener(this);

        tv_center = (TextView) findViewById(R.id.tv_center);
        text_add_person = (TextView)findViewById(R.id.text_add_person);

        apartment_list = (ListView)findViewById(R.id.apartment_list);
        contact_list = (ListView)findViewById(R.id.manager_list);
        search_list = (ListView)findViewById(R.id.search_list);

        tv_center.setText("添加联系人");
        bt_left = (TextView) findViewById(R.id.bt_left2);
        bt_right = (TextView) findViewById(R.id.bt_right);
        bt_left.setVisibility(View.VISIBLE);

        bt_left2 = (ImageView) findViewById(R.id.bt_left);
        bt_left2.setVisibility(View.INVISIBLE);

        bt_right.setText("确定");
        bt_left.setOnClickListener(this);
        bt_right.setOnClickListener(this);


        v = findViewById(R.id.div);
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

                if (m.isCheckState()) {
                    m.setCheckState(false);
                    iv.setImageResource(R.drawable.radio_unchecked);
                    Constant.contactCount--;
                    Constant.contactResult.remove(m);
                } else {
                    m.setCheckState(true);
                    iv.setImageResource(R.drawable.radio_checked);
                    Constant.contactCount++;
                    Constant.contactResult.add(m);
                }
                contactAdapter.notifyDataSetChanged();
                if (Constant.contactCount != 0) {
                    bt_right.setText("确定(" + Constant.contactCount + ")");
                } else {
                    bt_right.setText("确定");
                }
            }
        });

        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyContacts m = (MyContacts) search_list.getItemAtPosition(position);
                ImageView iv = (ImageView) view.findViewById(R.id.iv_checked);

                if (m.isCheckState()) {
                    m.setCheckState(false);
                    iv.setImageResource(R.drawable.radio_unchecked);
                    Constant.contactCount--;
                    Constant.contactResult.remove(m);
                } else {
                    m.setCheckState(true);
                    iv.setImageResource(R.drawable.radio_checked);
                    Constant.contactCount++;
                    Constant.contactResult.add(m);
                }
                searchAdapter.notifyDataSetChanged();
                if (Constant.contactCount != 0) {
                    bt_right.setText("确定(" + Constant.contactCount + ")");
                } else {
                    bt_right.setText("确定");
                }
            }
        });

        apartment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Department department = (Department) apartment_list.getItemAtPosition(position);
                int tempId = department.getId();
                Intent intent = new Intent(AddPerson.this, AddPerson.class);
                intent.putExtra("id", tempId);
                intent.putExtra("from", from);
                //intent.putExtra("count", count);
                //intent.putExtra("result", (ArrayList) Constant.contactResult);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constant.contactCount != 0) {
            bt_right.setText("确定(" + Constant.contactCount + ")");
        } else bt_right.setText("确定");


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

        ed_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {

                    //queryUserByType(ed_name.getText().toString());
                    queryPerson(ed_name.getText().toString());
                    return true;
                }
                return false;
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
                .addParams("userCode",Constant.username)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(AddPerson.this);
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
                                        contact.setTrueName(user.optString("trueName"));
                                        contact.setPhone(user.optString("phone"));
                                        contact.setUserCode(user.optString("userCode"));

                                        for (MyContacts m:Constant.contactResult) {
                                            if (m.getId() == contact.getId()) {
                                                contact.setCheckState(true);
                                            }
                                        }
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
                                    isDividerShow = false;
                                    v.setVisibility(View.GONE);
                                    text_add_person.setText("选择联系人");
                                } else {
                                    isDividerShow = false;
                                    v.setVisibility(View.GONE);
                                    text_add_person.setText("没有更多的联系人了");
                                }

                                departmentAdapter = new CommonAdapter<Department>(AddPerson.this, departmentData, R.layout.item_add_person) {
                                    @Override
                                    public void convert(ViewHolder holder, Department department) {
                                        holder.getView(R.id.iv_checked).setVisibility(View.GONE);
                                        holder.getView(R.id.group).setVisibility(View.GONE);

                                        holder.setText(R.id.name, department.getName());
                                    }
                                };

                                apartment_list.setAdapter(departmentAdapter);
                                setListViewHeight(apartment_list);

                                contactAdapter = new CommonAdapter<MyContacts>(AddPerson.this, contactData, R.layout.item_add_person) {
                                    @Override
                                    public void convert(ViewHolder holder, MyContacts contact) {
                                        holder.getView(R.id.right_arrow1).setVisibility(View.INVISIBLE);
                                        holder.setText(R.id.group, contact.getRoleName());
                                        holder.setText(R.id.name, contact.getTrueName());

                                    }
                                };

                                contact_list.setAdapter(contactAdapter);
                                setListViewHeight(contact_list);

                            } else {
                                ToastUtil.showShort(AddPerson.this, "服务器异常");
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
        setListViewHeight(contact_list);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_word:
                ed_name.setText("");
                break;

            case R.id.bt_left2:
                onBackPressed();
                break;

            case R.id.bt_right:
                if (from == 1) {
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sb2 = new StringBuilder();
                    if (Constant.contactResult.size() > 0) {
                        boolean need = false;

                        for (MyContacts i : Constant.contactResult) {
                            if (need) {
                                sb.append(",");
                                sb2.append(",");
                            }
                            sb.append(i.getTrueName());
                            sb2.append(i.getUserCode());
                            need = true;
                        }
                    }
                    Intent intent = new Intent(AddPerson.this,PostMessageActivity.class);
                    Bundle bundle = new Bundle();
                    //bundle.putSerializable("result",(ArrayList)Constant.contactResult);
                    bundle.putString("result", sb.toString());
                    bundle.putString("data",sb2.toString());
                    intent.putExtras(bundle);

                    startActivity(intent);
                    //AddPersonManager.getInstance().finishAllActivity();
                } else {
                    //已废弃
//                    if (count > 8) {
//                        MyDialog dialog = new MyDialog(AddPerson.this, R.style.dialog_style);
//                        dialog.show();
//                        dialog.setMain_text("多方通话最多允许添加8个人");
//                        dialog.setVisibility(View.GONE);
//                        dialog.setCenterVisibility(View.VISIBLE);
//                    } else {
//                        List<Person> personList = new ArrayList<>();
//                        for (MyContacts m : result) {
//                            Person person = new Person();
//                            person.setLinkPhone(m.getPhone());
//                            person.setLinkName(m.getTrueName());
//                            person.setUserCode(m.getUserCode());
//                            personList.add(person);
//                        }
//                        Intent intent = null;
//                        if (from == 2) {
//                            intent = new Intent(AddPerson.this,StartChattingActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("personList",(ArrayList)personList);
//                            bundle.putInt("add",1);
//                            intent.putExtras(bundle);
//                        } else {
//                            intent = new Intent(AddPerson.this,MoreContact.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("personList",(ArrayList)personList);
//                            intent.putExtras(bundle);
//                        }
//
//                        startActivity(intent);
//                    }
                }
                break;
        }
    }

    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置listview高度的方法
     * @param listView
     */
    public void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);   //获得每个子item的视图
            listItem.measure(0, 0);   //先判断写入的widthMeasureSpec和heightMeasureSpec是否和当前的值相等，如果不等，重新调用onMeasure()
            totalHeight += listItem.getMeasuredHeight();   //累加
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));   //加上每个item之间的距离
        listView.setLayoutParams(params);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //AddPersonManager.getInstance().finishActivity();
    }

    /**
     * 搜索人员
     */
    private void queryPerson(String name) {
        String url = Constant.SERVER_URL + "message/queryPerson";

        OkHttpUtils.post()
                .url(url)
                .addParams("id", String.valueOf(id))
                .addParams("userCode",Constant.username)
                .addParams("personName",name)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(AddPerson.this);
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

                                    JSONArray list = data.optJSONArray("listu");
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
                                            searchAdapter = new CommonAdapter<MyContacts>(AddPerson.this, contactData, R.layout.item_add_person) {
                                                @Override
                                                public void convert(final ViewHolder holder, final MyContacts contact) {
                                                    holder.getView(R.id.right_arrow1).setVisibility(View.INVISIBLE);
                                                    holder.setText(R.id.group, contact.getRoleName());
                                                    holder.setText(R.id.name, contact.getTrueName());
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
                                ToastUtil.show(AddPerson.this, "服务器异常");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private boolean isDividerShow = true;
}
