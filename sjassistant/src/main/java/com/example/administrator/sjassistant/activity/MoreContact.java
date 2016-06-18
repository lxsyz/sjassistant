package com.example.administrator.sjassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.ContactAdapter;
import com.example.administrator.sjassistant.bean.Person;
import com.example.administrator.sjassistant.bean.SortModel;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.ChangeNumberDialog;
import com.example.administrator.sjassistant.view.MyDialog;
import com.example.administrator.sjassistant.view.MyListView;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/6.
 */
public class MoreContact extends BaseActivity implements View.OnClickListener {

    private MyListView listView;

    private List<Person> datalist = new ArrayList<Person>();

    private List<Person> personList = new ArrayList<>();

    private RelativeLayout change_layout;

    private ImageView iv_start,add;
    private EditText number,content;



    private TextView master;

    private TextView rest_time,user_time,company_time;

    ContactAdapter adapter;
    int result = 0;

    Person resultPerson;
    SortModel sortPerson;

    Person masterPerson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resultPerson = (Person) getIntent().getSerializableExtra("person");
        sortPerson = (SortModel)getIntent().getSerializableExtra("sortPerson");
        personList = (ArrayList<Person>)getIntent().getSerializableExtra("personlist");
        if (resultPerson != null) {
            datalist.add(resultPerson);
            adapter.addView(datalist);
            OperatorUtil.setListViewHeight(listView);
        }

        if (sortPerson != null) {
            Person person = new Person();
            person.setLinkPhone(sortPerson.getPhoneNumber());
            person.setLinkName(sortPerson.getName());
            datalist.add(person);
            adapter.addView(datalist);
            OperatorUtil.setListViewHeight(listView);
        }

        if (personList != null) {
            datalist.addAll(personList);
            adapter.addView(datalist);
            OperatorUtil.setListViewHeight(listView);
        }

        initData();
    }

    @Override
    protected void initView() {
        super.initView();

        setTopText("多方通话");
        setCenterView(R.layout.activity_more_contact);

        listView = (MyListView)findViewById(R.id.person);

        iv_start = (ImageView)findViewById(R.id.iv_start);
        number = (EditText)findViewById(R.id.number);
        add = (ImageView)findViewById(R.id.add);
        change_layout = (RelativeLayout)findViewById(R.id.change_layout);
        master = (TextView)findViewById(R.id.master);

        rest_time = (TextView)findViewById(R.id.rest_time);
        user_time = (TextView)findViewById(R.id.currentmonth_time);
        company_time = (TextView)findViewById(R.id.currentCompany);

        btn_right = (ImageView)findViewById(R.id.bt_right);
        btn_right.setImageResource(R.drawable.add_link_person);
        btn_right.setVisibility(View.VISIBLE);

        adapter = new ContactAdapter(this,datalist,listView);

        listView.setAdapter(adapter);
        OperatorUtil.setListViewHeight(listView);

        number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("response", "hasFocus  " + hasFocus);
                if (hasFocus) {
                    //最多添加八人
                    if (adapter.getCount() >= 8) {
                        MyDialog dialog = new MyDialog(MoreContact.this, R.style.dialog_style);
                        dialog.show();
                        dialog.setMain_text("多方通话最多允许添加8个人");
                        dialog.setVisibility(View.GONE);
                        dialog.setCenterVisibility(View.VISIBLE);
                        return;
                    }
                }
            }
        });

        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //最多添加八人
                if (adapter.getCount() >= 8) {
                    MyDialog dialog = new MyDialog(MoreContact.this, R.style.dialog_style);
                    dialog.show();
                    dialog.setMain_text("多方通话最多允许添加8个人");
                    dialog.setVisibility(View.GONE);
                    dialog.setCenterVisibility(View.VISIBLE);
                    return;
                }
                number.clearFocus();
                number.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        change_layout.setOnClickListener(this);
        iv_start.setOnClickListener(this);
        add.setOnClickListener(this);
        btn_right.setOnClickListener(this);
    }

    /*
     *
     */
    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone",null);
        if (!TextUtils.isEmpty(phone)) {

            getUser(phone,1);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        ChangeNumberDialog dialog2 = new ChangeNumberDialog(this);
        switch (v.getId()) {
            case R.id.iv_start:
                if (!OperatorUtil.isPhoneNumber(master.getText().toString())) {
                    ToastUtil.showShort(MoreContact.this,"请输入正确的手机号");
                    return;
                }

                if (datalist == null || datalist.size() == 0) {
                    ToastUtil.showShort(MoreContact.this,"请添加参与人");
                    return;
                }

                intent = new Intent(MoreContact.this,StartChattingActivity.class);
                Bundle bundle = new Bundle();
                //bundle.putString("master",master.getText().toString());
                bundle.putSerializable("master",masterPerson);
                bundle.putSerializable("person",(ArrayList)datalist);
                intent.putExtras(bundle);
                startActivity(intent);
                //发起通话
                //create();
                break;
            case R.id.add:

                //最多添加八人
                if (adapter.getCount() >= 8) {
                    MyDialog dialog = new MyDialog(MoreContact.this, R.style.dialog_style);
                    dialog.show();
                    dialog.setMain_text("多方通话最多允许添加8个人");
                    dialog.setVisibility(View.GONE);
                    dialog.setCenterVisibility(View.VISIBLE);
                    return;
                }
                String num = number.getText().toString();
                Log.d("response","numbertext+"+num);
                if (!TextUtils.isEmpty(num.trim())) {
                    if (OperatorUtil.isPhoneNumber(num)) {
                        if (!isExist(num,datalist)) {
                            getUser(num, 0);
                        } else {
                            ToastUtil.showShort(MoreContact.this,"该用户已经添加过了");
                        }
                    } else {
                        ToastUtil.showShort(MoreContact.this,"手机号码格式不正确");
                    }
                }

                break;
            case R.id.change_layout:
                dialog2.show();
                dialog2.setFlag(0);
                dialog2.setOnItemClickListener(new ChangeNumberDialog.OnItemClickListener() {
                    @Override
                    public void onItemClick(String str) {
                        if (OperatorUtil.isPhoneNumber(str)) {
                            master.setText(str);
                            getUser(str,1);
                        }
                        else {
                            ToastUtil.showShort(MoreContact.this,"手机号码格式不正确");
                            master.setText("");
                        }
                    }
                });

                break;

            case R.id.bt_right:
                //最多添加八人
                if (adapter.getCount() >= 8) {
                    MyDialog dialog = new MyDialog(MoreContact.this, R.style.dialog_style);
                    dialog.show();
                    dialog.setMain_text("多方通话最多允许添加8个人");
                    dialog.setVisibility(View.GONE);
                    dialog.setCenterVisibility(View.VISIBLE);
                    return;
                }
                intent = new Intent(MoreContact.this,AddChatContact.class);
                intent.putExtra("count",adapter.getCount());
                intent.putExtra("data",(ArrayList)datalist);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d("response", "onnewintent");

    }

    @Override
    protected void onResume() {
        super.onResume();

        queryResources();
        queryCurrentMonth();
    }

    /**
     * 查询用户
     *
     * @param num  手机号
     * @param flag 1 标记主持人  0标记参与人
     */
    private void getUser(final String num, final int flag) {
        String url = Constant.SERVER_URL + "phoneBook/getUserByPhone";
        OkHttpUtils.post()
                .url(url)
                .addParams("linkPhone",num)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage());
                        ErrorUtil.NetWorkToast(MoreContact.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            if (statusCode == 0) {
                                JSONObject data = object.optJSONObject("data");
                                if (data != null) {
                                    Gson gson = new Gson();
                                    JSONObject user = data.optJSONObject("user");
                                    if (flag == 0) {
                                        if (user != null) {
                                            Person linkPerson = gson.fromJson(user.toString(), Person.class);
                                            if (linkPerson != null) {
                                                datalist.add(linkPerson);
                                                adapter.addView(datalist);
                                                OperatorUtil.setListViewHeight(listView);
                                                number.setText("");
                                            }
                                        } else {
                                            Person person = new Person();
                                            person.setLinkName("");
                                            person.setLinkPhone(number.getText().toString());
                                            datalist.add(person);
                                            adapter.addView(datalist);
                                            OperatorUtil.setListViewHeight(listView);
                                            number.setText("");
                                        }
                                    } else {
                                        if (user != null) {
                                            masterPerson = gson.fromJson(user.toString(), Person.class);
                                            if (masterPerson.getLinkPhone()!= null)
                                                master.setText(masterPerson.getLinkPhone());
                                        } else {
                                            //Person person = new Person();
                                            masterPerson = new Person();
                                            masterPerson.setUserCode(" ");
                                            if (num != null) {
                                                masterPerson.setLinkPhone(num);
                                            }
                                            masterPerson.setLinkName(" ");
                                            master.setText(masterPerson.getLinkPhone());
                                        }
                                    }

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            List<SortModel> modelList;
            modelList = (ArrayList<SortModel>)data.getSerializableExtra("result");

            if (modelList != null) {
                //datalist.clear();
                List<Person> tempList = new ArrayList<>();
                //处理返回结果 去除重复号码
                for (SortModel sm:modelList) {
                    if (!isExist(sm.getPhoneNumber(),datalist)) {
                        Person person = new Person();
                        person.setLinkName(sm.getName());
                        person.setUserCode(sm.getUserCode());
                        person.setLinkPhone(sm.getPhoneNumber());

                        tempList.add(person);
                    }
//                    for (Person p:datalist) {
//                        if (!sm.getPhoneNumber().equals(p.getLinkPhone())) {
//                            Person person = new Person();
//                            person.setLinkName(sm.getName());
//                            person.setUserCode(sm.getUserCode());
//                            person.setLinkPhone(sm.getPhoneNumber());
//
//                            tempList.add(person);
//                        }
//                    }
                }
                datalist.addAll(tempList);

                adapter.addView(datalist);
                OperatorUtil.setListViewHeight(listView);
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    /*
     * 查询剩余分钟数
     *
     */
    private void queryResources() {
        String url = Constant.SERVER_URL + "phone/queryResources";

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(MoreContact.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            int statusCode = object.optInt("statusCode");
                            if (statusCode == 0) {
                                JSONObject data = object.optJSONObject("data");
                                int resource_num = data.optInt("resource_num");
                                String text = "剩余分钟数：" + resource_num;
                                rest_time.setText(text);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }

    /*
     * 查询当月所用时长
     */
    public void queryCurrentMonth() {
        String url = Constant.SERVER_URL + "phone/queryCurrentMonth";

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error",e.getMessage()+ " ");
                        ErrorUtil.NetWorkToast(MoreContact.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            int statusCode = object.optInt("statusCode");
                            if (statusCode == 0) {
                                JSONObject data = object.optJSONObject("data");
                                int user_call_time = data.optInt("user_call_time");
                                int admin_call_time = data.optInt("admin_call_time");
                                String text = "当月用户时长："+user_call_time;
                                user_time.setText(text);
                                text = "当月企业时长：" + admin_call_time;
                                company_time.setText(text);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }

    /*
	 * 判断自身数据是否重复
	 */
    public ArrayList<Person> removeMethod(ArrayList<Person> array) {
        ArrayList<Person> arr = new ArrayList<Person>();
        Iterator<Person> it = array.iterator();
        while (it.hasNext()) {
            Person obj = it.next();
            if (!arr.contains(obj))
                arr.add(obj);
        }
        return arr;
    }

    /*
     * 判断电话号码是否已经存在
     * @param String
     * @return
     */
    public boolean isExist(String phone,List<Person> list) {
        for (Person p : list) {
            if (p.getLinkPhone().equals(phone)) {
                return true;
            }
        }
        return false;
    }
}
