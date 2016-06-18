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
import com.example.administrator.sjassistant.bean.GroupPerson;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
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
 * Created by Administrator on 2016/5/4.
 */
public class GroupActivity extends BaseActivity implements View.OnClickListener {
    private ImageView search,delete;
    private EditText ed_name;

    private ListView customer_list;

    private List<GroupPerson> datalist = new ArrayList<>();

    private TextView prompt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_customer);
        setTopText("小组");

        search = (ImageView)findViewById(R.id.search);
        ed_name = (EditText)findViewById(R.id.search_content);
        prompt = (TextView)findViewById(R.id.customer_prompt);
        ed_name.setHint("搜索小组");
        delete = (ImageView)findViewById(R.id.delete_word);
        search.setOnClickListener(this);
        delete.setOnClickListener(this);

        customer_list = (ListView)findViewById(R.id.customer_list);


        customer_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupPerson gp = (GroupPerson) customer_list.getItemAtPosition(position);
                Intent intent = new Intent(GroupActivity.this, ContactsDetailActivity.class);
                intent.putExtra("from", 2);
                intent.putExtra("id", gp.getId());
                intent.putExtra("group_name", gp.getGroupName());

                startActivity(intent);
            }
        });

    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bt_right:
                break;
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

    @Override
    protected void onResume() {
        super.onResume();

        getGroup();

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
    }

    /*
     * 获取小组列表
     */
    private void getGroup() {
        String url = Constant.SERVER_URL + "phoneBook/showGroup";

        OkHttpUtils.post()
                .url(url)
                .addParams("userCode",Constant.username)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage());
                        ErrorUtil.NetWorkToast(GroupActivity.this);
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
                                        prompt.setText("我可以查看的小组");
                                        datalist = gson.fromJson(list.toString(), new TypeToken<List<GroupPerson>>() {
                                        }.getType());
                                    } else {
                                        prompt.setText("没有小组");
                                    }
                                    if (commonAdapter !=null) {
                                        commonAdapter.updateListView(datalist);
                                    } else {
                                        commonAdapter = new CommonAdapter<GroupPerson>(GroupActivity.this, datalist, R.layout.item_choose_customer) {
                                            @Override
                                            public void convert(ViewHolder holder, GroupPerson cp) {
                                                holder.setText(R.id.customer_name, cp.getGroupName());
                                            }
                                        };
                                        customer_list.setAdapter(commonAdapter);
                                    }
                                }
                            } else {
                                ToastUtil.showShort(GroupActivity.this, "获取用户列表失败");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /*
     * 搜索小组
     */
    private void filterData(String text) {
        List<GroupPerson> filterList = new ArrayList<>();

        if (TextUtils.isEmpty(text)) {
            filterList = datalist;
        } else {
            filterList.clear();
            for (GroupPerson person:datalist) {
                if (person.getGroupName().contains(text)) {
                    filterList.add(person);
                }
            }
        }
        commonAdapter.updateListView(filterList);
    }

    private CommonAdapter<GroupPerson> commonAdapter;
}
