package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.RecyclerAdapter;
import com.example.administrator.sjassistant.bean.Person;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.ChangeNumberDialog;
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
public class StartChattingActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<Person> datalist = new ArrayList<Person>();

    private String sessionId;

    private RelativeLayout novoice_layout,open_layout,end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            sessionId = getIntent().getStringExtra("sessionId");
        } else {
            sessionId = savedInstanceState.getString("sessionId");
        }

        for (int i = 0;i < 7;i++) {
            Person p = new Person();
            p.setName("李 四");
            p.setPhoneNumber("13003152436");
            datalist.add(0, p);
        }

        adapter = new RecyclerAdapter(datalist);

        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new RecyclerAdapter.OnItemClickListener() {

            @Override
            public void onClick(View v, final Object object) {
                if (object.equals("add")) {
                    Intent intent = new Intent(StartChattingActivity.this, AddPerson.class);
                    intent.putExtra("count", adapter.getItemCount());
                    startActivity(intent);
                } else {
                    ChangeNumberDialog d = new ChangeNumberDialog(StartChattingActivity.this);
                    d.setFlag(1);
                    d.show();
                    d.setContentText("删除XXX此次的电话会议");
                    d.setOnDeleteClickListener(new ChangeNumberDialog.OnDeleteClickListener() {
                        @Override
                        public void onDelete(int i) {
                            if (i == 1) {
                                minus(object);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();

        setTopText("通话时长");
        setCenterView(R.layout.activity_start_chatting);

        recyclerView = (RecyclerView)findViewById(R.id.id_recyclerview);
        open_layout = (RelativeLayout)findViewById(R.id.open_layout);
        novoice_layout = (RelativeLayout)findViewById(R.id.novoice_layout);
        end = (RelativeLayout)findViewById(R.id.end);

        open_layout.setOnClickListener(this);


        GridLayoutManager manager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(manager);

        Person add = new Person();
        datalist.add(add);
    }

    /*
     * 添加多方通话人员
     */
    private void add() {
        String url = Constant.SERVER_URL + "phone/add";

        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage());
                        ErrorUtil.NetWorkToast(StartChattingActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            if (statusCode == 0) {
                                ToastUtil.showShort(StartChattingActivity.this, "添加成功");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /*
     * 踢出通话人员
     */
    private void minus(final Object o) {
        final Person person = (Person)o;
        String url = Constant.SERVER_URL + "phone/minus";

        OkHttpUtils.post()
                .url(url)
                .addParams("sessionId", sessionId)
                .addParams("phones", person.getPhoneNumber())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage());
                        ErrorUtil.NetWorkToast(StartChattingActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            if (statusCode == 0) {
                                adapter.removePerson(person);

                                ToastUtil.showShort(StartChattingActivity.this, "移除成功");
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

        outState.putString("sessionId", sessionId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_layout:
                break;
            case R.id.novoice_layout:
                break;
            case R.id.end:
                end();
                break;
        }
    }

    /*
     * 结束通话
     */
    private void end() {
        String url = Constant.SERVER_URL + "phone/end";

        OkHttpUtils.post()
                .url(url)
                .addParams("sessionId",sessionId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage());
                        ErrorUtil.NetWorkToast(StartChattingActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            if (statusCode == 0) {

                                ToastUtil.showShort(StartChattingActivity.this, "结束通话");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
