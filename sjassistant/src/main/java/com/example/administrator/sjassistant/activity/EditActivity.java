package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.FilterCondition;
import com.example.administrator.sjassistant.bean.Role;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
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
 * Created by Administrator on 2016/4/2.
 */
public class EditActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_checked;

    private ListView lv;

    private TextView tip;
    private String title;
    private int request;

    private String billType;
    private int billId;

    //判断是否从筛选联系人进入
    private String from;

    private List<FilterCondition> datalist = new ArrayList<>();
    private List<FilterCondition> reslist = new ArrayList<>();

    private RelativeLayout prompt_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getIntent().getStringExtra("top");
        billId = getIntent().getIntExtra("billId", -1);
        billType = getIntent().getStringExtra("billType");
        setTopText(title);
        tip.setText("选择" + title);
        request = getIntent().getIntExtra("result",-1);

        from = getIntent().getStringExtra("from");
        if (from != null && from.equals("choose")) {
            bt_right_text.setVisibility(View.VISIBLE);
            bt_right_text.setText("确定");
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FilterCondition res = (FilterCondition) lv.getItemAtPosition(position);
                ImageView iv_checked = (ImageView) view.findViewById(R.id.iv_checked);
                if (from != null && from.equals("choose")) {
                    if (res.isChecked()) {
                        res.setChecked(false);
                        iv_checked.setVisibility(View.GONE);
                        reslist.remove(res);
                    } else {
                        res.setChecked(true);
                        iv_checked.setVisibility(View.VISIBLE);
                        reslist.add(res);
                    }
                } else {
                    iv_checked.setVisibility(View.VISIBLE);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("result", res);
                    intent.putExtras(bundle);
                    setResult(request, intent);
                    EditActivity.this.finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(request);
    }

    @Override
    protected void initView() {
        super.initView();

        setCenterView(R.layout.activity_edit);

        lv = (ListView)findViewById(R.id.edit_list);
        tip  = (TextView)findViewById(R.id.tip);
        prompt_layout = (RelativeLayout)findViewById(R.id.prompt_layout);

        bt_right_text.setOnClickListener(this);
    }

    private void getData(int request) {
        datalist.clear();
        switch (request) {
            case 1:
                getCustomerType("customerType");
                break;
            case 2:
                getCustomerType("customerDept");
                break;
            case 3:
                getCustomerType("customerPost");
                break;
            case 4:
                prompt_layout.setVisibility(View.GONE);
                getNextName();
                break;
            case 5:
                prompt_layout.setVisibility(View.GONE);
                showRoles();
                break;
        }
    }

    /*
     *
     */
    private void getCustomerType(String type) {
        String url = Constant.SERVER_URL + "phoneBook/show";

        OkHttpUtils.post()
                .url(url)
                .addParams("type",type)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(EditActivity.this);
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
                                JSONArray list = data.optJSONArray("list");
                                if (list != null && list.length() != 0) {
                                    int len = list.length();
                                    for (int i = 0; i < len; i++) {
                                        FilterCondition fc = new FilterCondition();
                                        fc.setId(list.optJSONObject(i).optInt("id"));
                                        fc.setName(list.optJSONObject(i).optString("name"));
                                        datalist.add(fc);
                                    }
                                }
                                lv.setAdapter(new CommonAdapter<FilterCondition>(EditActivity.this, datalist, R.layout.item_edit) {
                                    @Override
                                    public void convert(ViewHolder holder, FilterCondition s) {
                                        holder.setText(R.id.type_name, s.getName());
                                        if (s.isChecked()) {
                                            holder.getView(R.id.iv_checked).setVisibility(View.VISIBLE);
                                        } else {
                                            holder.getView(R.id.iv_checked).setVisibility(View.GONE);
                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    /*
     * 获取角色列表
     *
     */
    private void showRoles() {
        String url = Constant.SERVER_URL + "bill/showRoles";

        OkHttpUtils.post()
                .url(url)
                .addParams("billId", String.valueOf(billId))
                .addParams("billType",billType)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error",e.getMessage()+" ");
                        ErrorUtil.NetWorkToast(EditActivity.this);
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
                                JSONArray list = data.optJSONArray("list");
                                if (list != null && list.length() != 0) {
                                   // List<Role> roleList = new ArrayList<Role>();
                                    Gson gson = new Gson();
                                    datalist = gson.fromJson(list.toString(),new TypeToken<List<FilterCondition>>(){}.getType());
                                    lv.setAdapter(new CommonAdapter<FilterCondition>(EditActivity.this, datalist, R.layout.item_edit) {
                                        @Override
                                        public void convert(ViewHolder holder, FilterCondition s) {
                                            holder.setText(R.id.type_name, s.getName());
                                        }
                                    });
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /*
     * 获取下一环节名称
     */
    private void getNextName() {
        String url = Constant.SERVER_URL + "bill/showNextStep";

        OkHttpUtils.post()
                .addParams("billId",String.valueOf(billId))
                .addParams("billType",billType)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error",e.getMessage()+" ");
                        ErrorUtil.NetWorkToast(EditActivity.this);
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
                                JSONArray list = data.optJSONArray("list");
                                if (list != null && list.length() != 0) {
                                    // List<Role> roleList = new ArrayList<Role>();
                                    Gson gson = new Gson();
                                    datalist = gson.fromJson(list.toString(),new TypeToken<List<FilterCondition>>(){}.getType());
                                    lv.setAdapter(new CommonAdapter<FilterCondition>(EditActivity.this, datalist, R.layout.item_edit) {
                                        @Override
                                        public void convert(ViewHolder holder, FilterCondition s) {
                                            holder.setText(R.id.type_name, s.getName());
                                        }
                                    });
                                }

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
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_right_text) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("result", (ArrayList)reslist);
            intent.putExtras(bundle);
            setResult(request, intent);
            EditActivity.this.finish();
        }
    }
}
