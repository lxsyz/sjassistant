package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Person;
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
 * Created by Administrator on 2016/3/31.
 */
@SuppressWarnings("ResourceType")
public class SearchResultActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout search_layout;
    private ImageView search,delete;
    private EditText ed_name;
    private TextView number;

    private ListView lv;
    private List<Person> personList = new ArrayList<Person>();
    private int customerType,customerDept,customerPost;
    private String customerTypeName,deptName,postName;
    private CommonAdapter<Person> commonAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始界面搜索跳转过来的
//        String content = getIntent().getStringExtra("name");
//        if (!TextUtils.isEmpty(content)) {
//            ed_name.setText(content);
//            setTopText("搜索结果");
//            number.setText(personList.size()+"个搜索结果");
//        }


        String type = getIntent().getStringExtra("type");
        if (!TextUtils.isEmpty(type)) {
            ed_name.setText("");
            setTopText("筛选结果");

            customerDept = getIntent().getIntExtra("customerDept", 0);
            customerPost = getIntent().getIntExtra("customerPost", 0);
            customerType = getIntent().getIntExtra("customerType", 0);
            customerTypeName = getIntent().getStringExtra("customerTypeName");
            deptName = getIntent().getStringExtra("deptName");
            postName = getIntent().getStringExtra("postName");
            Log.d("response", customerDept + " " + customerPost + " " + customerType);
        }



    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_search_result);

        number = (TextView)findViewById(R.id.number);
        lv = (ListView)findViewById(R.id.lv_result);




        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Person p = (Person) lv.getItemAtPosition(position);
                p.setDeptName(deptName);
                p.setCustomerTypeName(customerTypeName);
                p.setPostName(postName);
                Intent intent = new Intent(SearchResultActivity.this,PersonDetail.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("person",p);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });

        search_layout = (LinearLayout)findViewById(R.id.search_layout);
        search = (ImageView)search_layout.findViewById(R.id.search);
        ed_name = (EditText)search_layout.findViewById(R.id.search_content);
        delete = (ImageView)search_layout.findViewById(R.id.delete_word);

        search.setOnClickListener(this);
        ed_name.setOnClickListener(this);
        delete.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        getFilterResult();
    }

    /*
     * 获取筛选结果
     */
    private void getFilterResult() {
        String url = Constant.SERVER_URL + "phoneBook/getCustomerByReason";

        OkHttpUtils.post()
                .url(url)
                .addParams("customerType", String.valueOf(customerType))
                .addParams("customerDept", String.valueOf(customerDept))
                .addParams("customerPost", String.valueOf(customerPost))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error",e.getMessage());
                        ErrorUtil.NetWorkToast(SearchResultActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response);
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.optInt("statusCode");
                            if (statusCode == 0) {
                                JSONObject data = object.optJSONObject("data");
                                JSONArray listu = data.optJSONArray("listu");
                                Gson gson = new Gson();
                                if (listu != null) {
                                    personList = gson.fromJson(listu.toString(), new TypeToken<List<Person>>() {
                                    }.getType());
                                    if (commonAdapter != null) {
                                        commonAdapter.updateListView(personList);
                                    } else {
                                        commonAdapter = new CommonAdapter<Person>(SearchResultActivity.this, personList, R.layout.item_search_result) {
                                            @Override
                                            public void convert(ViewHolder holder, final Person person) {
                                                holder.setText(R.id.username, person.getLinkName());
                                                holder.setText(R.id.group, person.getCustomerName());
                                                holder.getView(R.id.phone).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(SearchResultActivity.this, MoreContact.class);
                                                        Bundle bundle = new Bundle();
                                                        bundle.putSerializable("person", person);
                                                        intent.putExtras(bundle);
                                                        startActivity(intent);
                                                    }
                                                });
                                            }
                                        };

                                        lv.setAdapter(commonAdapter);
                                        number.setText(personList.size()+"个筛选结果");
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
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.search:
                if (!TextUtils.isEmpty(ed_name.getText().toString())) {

                    intent = new Intent(this, SearchResultActivity.class);
                    intent.putExtra("name",ed_name.getText().toString());
                    startActivity(intent);
                }
                break;
            case R.id.delete_word:
                ed_name.setText("");
                break;
        }
    }
}
