package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Person;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始界面搜索跳转过来的
        String content = getIntent().getStringExtra("name");
        if (!TextUtils.isEmpty(content)) {
            ed_name.setText(content);
            setTopText("搜索结果");
            number.setText(personList.size()+"个搜索结果");
        }


        String type = getIntent().getStringExtra("type");
        if (!TextUtils.isEmpty(type)) {
            ed_name.setText("");
            setTopText("筛选结果");

            customerDept = getIntent().getIntExtra("customerDept", 0);
            customerPost = getIntent().getIntExtra("customerPost", 0);
            customerType = getIntent().getIntExtra("customerType", 0);
            Log.d("response",customerDept+" "+customerPost+" "+customerType);
            //number.setText(personList.size()+"个筛选结果");
        }



    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_search_result);


        Person p = new Person();
        p.setGroup("北京分所");
        p.setApartment("信息科");
        p.setCompanyName("小公司");
        p.setCustomer_type("企业");
        p.setPerson_work("经理");
        p.setName("张三");
        p.setPhoneNumber("13006152436");

        personList.add(p);
        number = (TextView)findViewById(R.id.number);
        lv = (ListView)findViewById(R.id.lv_result);

        lv.setAdapter(new CommonAdapter<Person>(this, personList, R.layout.item_search_result) {
            @Override
            public void convert(ViewHolder holder, Person person) {
                final String phonenumber = person.getPhoneNumber();
                holder.setText(R.id.username, person.getName());
                holder.setText(R.id.group, person.getGroup());

                holder.getView(R.id.phone).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                                + phonenumber));
                        //noinspection ResourceType,ResourceType
                        startActivity(intent);

                        Toast.makeText(SearchResultActivity.this, "电话", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Person p = (Person) lv.getItemAtPosition(position);
                Log.d("tag",position+" ");
                Log.d("tag",p.getName()+" ");
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
