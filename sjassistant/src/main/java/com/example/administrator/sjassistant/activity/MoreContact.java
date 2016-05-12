package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.administrator.sjassistant.view.MyListView;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/6.
 */
public class MoreContact extends BaseActivity implements View.OnClickListener {

    private MyListView listView;

    private List<Person> datalist = new ArrayList<Person>();

    private RelativeLayout change_layout;

    private ImageView iv_start,add;
    private EditText number,content;



    private TextView master;

    private TextView rest_time;

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

        btn_right = (ImageView)findViewById(R.id.bt_right);
        btn_right.setImageResource(R.drawable.add_link_person);
        btn_right.setVisibility(View.VISIBLE);

        adapter = new ContactAdapter(this,datalist,listView);

        listView.setAdapter(adapter);
        OperatorUtil.setListViewHeight(listView);

        change_layout.setOnClickListener(this);
        iv_start.setOnClickListener(this);
        add.setOnClickListener(this);
        btn_right.setOnClickListener(this);
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
                    dialog2.show();
                    dialog2.setFlag(2);
                    dialog2.setContentText("最多添加八人");
                    return;
                }

                if (!TextUtils.isEmpty(number.getText().toString())) {
                    if (OperatorUtil.isPhoneNumber(number.getText().toString())) {
                        getUser(number.getText().toString(),0);
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
                    dialog2.show();
                    dialog2.setFlag(2);
                    dialog2.setContentText("最多添加八人");
                    return;
                }
                intent = new Intent(MoreContact.this,AddChatContact.class);
                intent.putExtra("count",adapter.getCount());
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d("response", "onnewintent");



    }

    /*
     * 查询用户
     *
     * @param num
     * @param flag 标记主持人  参与人
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
            List<SortModel> modelList = new ArrayList<>();
            modelList = (ArrayList<SortModel>)data.getSerializableExtra("result");
            if (modelList != null) {
                for (SortModel sm:modelList) {
                    Person person = new Person();
                    person.setLinkName(sm.getName());
                    person.setUserCode(sm.getUserCode());
                    person.setLinkPhone(sm.getPhoneNumber());

                    datalist.add(person);

                }
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
                        Log.d("error",e.getMessage()+" ");
                        ErrorUtil.NetWorkToast(MoreContact.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            int statusCode = object.optInt("statusCode");
                            if (statusCode == 0) {
                                JSONObject data = object.optJSONObject("data");
                                int resource_num = data.optInt("resource_name");
                                String text = "剩余分钟数：" + resource_num;
                                rest_time.setText(text);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }

}
