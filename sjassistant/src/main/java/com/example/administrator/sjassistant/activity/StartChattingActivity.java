package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.RecyclerAdapter;
import com.example.administrator.sjassistant.bean.Person;
import com.example.administrator.sjassistant.bean.SortModel;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.ChangeNumberDialog;
import com.example.administrator.sjassistant.view.MyDialog;
import com.example.administrator.sjassistant.view.MyPromptDialog;
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
public class StartChattingActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<Person> datalist = new ArrayList<Person>();

    private MyPromptDialog pd;

    //是否开启免提和静音
    private int open_flag = 0,voice_flag = 0;
    private ImageView iv_open,iv_novoice;
    
    private Person master;


    private String sessionId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("response", "startchatting create");

        master = (Person) getIntent().getSerializableExtra("master");
        datalist = (ArrayList<Person>)getIntent().getSerializableExtra("person");
        //datalist.add(0,master);

        if (master != null) {
            datalist.add(0,master);
        }
        if (datalist == null) {
            datalist = new ArrayList<>();
        }

        pd = new MyPromptDialog(this);

        Log.d("response",datalist.size()+" ");
        Person add = new Person();
        datalist.add(add);
        OperatorUtil.closeSpeaker(this);
//        if (savedInstanceState == null) {
//            sessionId = getIntent().getStringExtra("sessionId");
//        } else {
//            sessionId = savedInstanceState.getString("sessionId");
//        }
        if (sessionId == null)
            create();


        adapter = new RecyclerAdapter(datalist);

        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new RecyclerAdapter.OnItemClickListener() {

            @Override
            public void onClick(View v, final Object object) {
                if (object.equals("add")) {
                    Intent intent = new Intent(StartChattingActivity.this, AddChatContact.class);
                    intent.putExtra("count", adapter.getItemCount() - 1);
                    //Bundle bundle = new Bundle();
                    //bundle.putSerializable("data",(ArrayList)datalist);
                    //intent.putExtras(bundle);
                    startActivityForResult(intent, 2);
                } else {
                    ChangeNumberDialog d = new ChangeNumberDialog(StartChattingActivity.this);
                    d.setFlag(1);
                    d.show();
                    d.setContentText("删除 " + ((Person) object).getLinkName() + " 此次的电话会议");
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
        RelativeLayout open_layout = (RelativeLayout) findViewById(R.id.open_layout);
        RelativeLayout novoice_layout = (RelativeLayout) findViewById(R.id.novoice_layout);
        RelativeLayout end = (RelativeLayout) findViewById(R.id.end);

        iv_novoice = (ImageView)findViewById(R.id.iv_novoice);
        iv_open = (ImageView)findViewById(R.id.iv_open);

        open_layout.setOnClickListener(this);
        novoice_layout.setOnClickListener(this);
        end.setOnClickListener(this);
        GridLayoutManager manager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(manager);



    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("response", "resume->"+datalist.size() + " ");

    }

    /*
         * 添加多方通话人员
         */
    private void add(String phones) {
        String url = Constant.SERVER_URL + "phone/add";

        OkHttpUtils.post()
                .addParams("sessionId", sessionId)
                .addParams("phones",phones)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
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
                .addParams("phones", person.getLinkPhone())
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
                            } else {

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
                if (open_flag == 0) {
                    open_flag = 1;
                    iv_open.setImageResource(R.drawable.open_checked);
                } else {
                    open_flag = 0;
                    iv_open.setImageResource(R.drawable.open_unchecked);
                }
                OperatorUtil.toggleSpeaker(this);
                break;
            case R.id.novoice_layout:
                if (voice_flag == 0) {
                    voice_flag = 1;
                    iv_novoice.setImageResource(R.drawable.novoice_checked);
                } else {
                    voice_flag = 0;
                    iv_novoice.setImageResource(R.drawable.novoice_unchecked);
                }
                OperatorUtil.toggleVoice(this);
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


        if (sessionId == null) {
            StartChattingActivity.this.finish();
            return;
        }
        if (pd != null) pd.createDialog().show();
        Constant.isMaster = false;
        String url = Constant.SERVER_URL + "phone/end";

        OkHttpUtils.post()
                .url(url)
                .addParams("sessionId",sessionId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage()+" ");
                        ErrorUtil.NetWorkToast(StartChattingActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            if (statusCode == 0) {
                                if (pd != null)
                                    pd.dismissDialog();
                                ToastUtil.showShort(StartChattingActivity.this, "通话结束");
                                StartChattingActivity.this.finish();
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

        String url = Constant.SERVER_URL + "phone/create";

        OkHttpUtils.post()
                .url(url)
                .addParams("caller", master.getLinkPhone())
                .addParams("phones", getInvolvers())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            if (statusCode == 0) {
                                sessionId= object.optString("data");
                                Constant.isMaster = true;
                                //JSONObject data = object.optJSONObject("data");
//                                if (data != null) {
//                                    sessionId = data.optString("data");
//                                    String describe = data.optString("describe");

//                                    if (!describe.equals("success")) {
//                                        ToastUtil.showShort(StartChattingActivity.this, "发起失败");
//                                        return;
//                                    }

                                    //sessionId = sessionId;
                                    //Intent intent = new Intent(StartChattingActivity.this, StartChattingActivity.class);
                                    //intent.putExtra("sessionId", sessionId);
                                    //startActivity(intent);
                            } else if (statusCode == 3){
                                ToastUtil.showShort(StartChattingActivity.this,"创建失败");
                            }

                            //}
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
        boolean need = false;

        for (Person i : datalist) {
            if (i.equals(datalist.get(0))) {
                continue;
            }
            if (need) {
                sb.append(",");
            }
            sb.append(i.getLinkPhone());
            need = true;
        }
        return sb.toString();
    }

    @Override
    public void onBackPressed() {
        toastForExit();
        //super.onBackPressed();
    }

    private long exitTime = 0;

    private void toastForExit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showShort(StartChattingActivity.this,"再按一次结束通话");
            exitTime = System.currentTimeMillis();
        } else {
            end();
        }
    }

    /*
         * 添加多个联系人
         */
    private String addInvolvers(List<SortModel> list) {
        StringBuilder sb = new StringBuilder();
        boolean need = false;

        for (SortModel i : list) {
            if (need) {
                sb.append(",");
            }
            sb.append(i.getPhoneNumber());
            need = true;
        }
        return sb.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            List<SortModel> modelList = new ArrayList<>();
            modelList = (ArrayList<SortModel>)data.getSerializableExtra("result");

            Log.d("response",modelList.size()+" ");

            if (modelList.size() != 0) {
                String result = addInvolvers(modelList);
                add(result);
                for (SortModel sm:modelList) {
                    Log.d("response",sm.getName()+" ");
                    Person person = new Person();
                    person.setLinkName(sm.getName());
                    person.setUserCode(sm.getUserCode());
                    person.setLinkPhone(sm.getPhoneNumber());

                    datalist.add(datalist.size()-1,person);

                }
                adapter.update(datalist);
                //Log.d()
                //adapter = new RecyclerAdapter(datalist);
                //recyclerView.setAdapter(adapter);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected void onDestroy() {
        //end();
        super.onDestroy();
    }
}
