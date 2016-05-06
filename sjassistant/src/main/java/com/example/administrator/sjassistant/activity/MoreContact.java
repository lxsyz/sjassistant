package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ContactAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.LinkPerson;
import com.example.administrator.sjassistant.bean.Person;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/6.
 */
public class MoreContact extends BaseActivity implements View.OnClickListener {

    private ListView listView;

    private List<Person> datalist = new ArrayList<Person>();

    private RelativeLayout change_layout;

    private ImageView iv_start,add;
    private EditText number,content;


    private TextView dialog_ok,dialog_cancel,master;

    ContactAdapter adapter;
    int result = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();

        setTopText("多方通话");
        setCenterView(R.layout.activity_more_contact);

        listView = (ListView)findViewById(R.id.person);

        iv_start = (ImageView)findViewById(R.id.iv_start);
        number = (EditText)findViewById(R.id.number);
        add = (ImageView)findViewById(R.id.add);
        change_layout = (RelativeLayout)findViewById(R.id.change_layout);
        master = (TextView)findViewById(R.id.master);

        Person p = new Person();
        p.setName("张  三");
        p.setPhoneNumber("13003152436");
        datalist.add(p);
        p.setName("李 四");
        p.setPhoneNumber("13003152436");
        datalist.add(p);

        adapter = new ContactAdapter(this,datalist);

        listView.setAdapter(adapter);
        OperatorUtil.setListViewHeight(listView);
        change_layout.setOnClickListener(this);
        iv_start.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        ChangeNumberDialog dialog2 = new ChangeNumberDialog(this);
        switch (v.getId()) {
            case R.id.iv_start:
                //发起通话
                create();
                break;
            case R.id.add:
                //最多添加八人
                if (adapter.getCount() >= 8) {
                    dialog2.show();
                    dialog2.setFlag(2);
                }

                if (!TextUtils.isEmpty(number.getText().toString())) {
                    if (OperatorUtil.isPhoneNumber(number.getText().toString())) {
                        getUser();
                    }
                } else {
                    intent = new Intent(MoreContact.this,AddPerson.class);
                    intent.putExtra("count",adapter.getCount());
                    startActivity(intent);
                }



                break;
            case R.id.change_layout:
                dialog2.show();
                dialog2.setFlag(0);
                dialog2.setOnItemClickListener(new ChangeNumberDialog.OnItemClickListener() {
                    @Override
                    public void onItemClick(String str) {
                        if (OperatorUtil.isPhoneNumber(str))
                            master.setText(str);
                        else {
                            ToastUtil.showShort(MoreContact.this,"手机号码格式不正确");
                            master.setText("");
                        }
                    }
                });

                break;

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String result = "";
        result = intent.getStringExtra("result");
        super.onNewIntent(intent);

    }

    /*
     * 查询用户
     */
    private void getUser() {
        String url = Constant.SERVER_URL + "phoneBook/getUserByPhone";
        OkHttpUtils.post()
                .url(url)
                .addParams("linkPhone",number.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error",e.getMessage());
                        ErrorUtil.NetWorkToast(MoreContact.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            if (statusCode == 0) {
                                JSONObject data = object.optJSONObject("data");
                                if (data != null) {
                                    Gson gson = new Gson();
                                    JSONObject user = data.optJSONObject("user");
                                    LinkPerson linkPerson = gson.fromJson(user.toString(),LinkPerson.class);


                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /*
         * 发起多方通话
         */
    private void create() {

        if (!OperatorUtil.isPhoneNumber(master.getText().toString())) {
            ToastUtil.showShort(MoreContact.this,"请输入正确的手机号");
        }

        String url = Constant.SERVER_URL + "phone/create";

        OkHttpUtils.post()
                .url(url)
                .addParams("caller",master.getText().toString())
                .addParams("phones",getInvolvers())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error",e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            if (statusCode == 0) {
                                JSONObject data = object.optJSONObject("data");
                                if (data != null) {
                                    String sessionId = data.optString("sessionId");
                                    String describe = data.optString("describe");

                                    if (!describe.equals("success")) {
                                        ToastUtil.showShort(MoreContact.this,"发起失败");
                                        return;
                                    }

                                    Intent intent = new Intent(MoreContact.this,StartChattingActivity.class);
                                    intent.putExtra("sessionId",sessionId);
                                    startActivity(intent);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    /*
     * 逗号连接
     */
    private String getInvolvers() {
        StringBuilder sb = new StringBuilder();

        return sb.toString();
    }
}
