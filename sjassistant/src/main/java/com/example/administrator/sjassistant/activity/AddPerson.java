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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.example.administrator.sjassistant.util.AddPersonManager;
import com.example.administrator.sjassistant.util.AppManager;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ServerConfigUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.MyDialog;
import com.example.administrator.sjassistant.view.MyPromptDialog;
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
     * 已添加的成员数目
     */
    private int count = 0;

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
        count = getIntent().getIntExtra("count", 0);
        id = getIntent().getIntExtra("id",0);
        result = (ArrayList<MyContacts>) getIntent().getSerializableExtra("result");
        if (result == null) {
            result = new ArrayList<MyContacts>();
        }
        if (count != 0) {
            bt_right.setText("确定(" + count + ")");
        } else bt_right.setText("确定");

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
                    count--;
                    result.remove(m);
                } else {
                    m.setCheckState(true);
                    iv.setImageResource(R.drawable.radio_checked);
                    count++;
                    result.add(m);
                }
                contactAdapter.notifyDataSetChanged();
                if (count != 0) {
                    bt_right.setText("确定(" + count + ")");
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
                intent.putExtra("from", 1);
                intent.putExtra("count", count);
                intent.putExtra("result", (ArrayList) result);
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

                    if (result.size() > 0) {
                        boolean need = false;

                        for (MyContacts i : result) {
                            if (need) {
                                sb.append(",");
                            }
                            sb.append(i.getUsername());
                            need = true;
                        }
                    }
                    Log.d("response","result.size "+result.size()+"sb  "+sb.toString());
                    Intent intent = new Intent(AddPerson.this,PostMessageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("result",sb.toString());
//                    intent.putExtra("result",sb.toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    //AddPersonManager.getInstance().finishAllActivity();
                }
                else {


                    if (count > 8) {
                        MyDialog dialog = new MyDialog(AddPerson.this, R.style.dialog_style);
                        dialog.show();
                        dialog.setMain_text("多方通话最多允许添加8个人");
                        dialog.setVisibility(View.GONE);
                        dialog.setCenterVisibility(View.VISIBLE);
                    } else {
                        this.finish();
                    }
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
        AddPersonManager.getInstance().finishActivity();
    }
}
