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
import com.example.administrator.sjassistant.bean.Bill;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/5.
 */
public class UnfinishedWorkActivity extends BaseActivity implements View.OnClickListener {

    private ImageView search,delete;
    private EditText ed_name;

    private ListView lv;

    private List<Bill> datalist = new ArrayList<Bill>();

    private TextView read_flag;

    private CommonAdapter commonAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();

        setCenterView(R.layout.activity_unfinished_work);
        setTopText("待办工作");

        search = (ImageView)findViewById(R.id.search);
        ed_name = (EditText)findViewById(R.id.search_content);
        delete = (ImageView)findViewById(R.id.delete_word);
        search.setOnClickListener(this);
        delete.setOnClickListener(this);

        lv = (ListView)findViewById(R.id.unfinished_list);
        read_flag = (TextView)findViewById(R.id.read_flag);

        Bill bill1 = new Bill();
        bill1.setName("Jimmy");
        bill1.setPostman("Jimmy");
        bill1.setPosttime("2013");
        bill1.setType("经费");
        Bill bill2 = new Bill();
        bill2.setName("Jimmy");
        bill2.setPostman("Jimmy");
        bill2.setPosttime("2013");
        bill2.setType("经费");
        datalist.add(bill1);datalist.add(bill2);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UnfinishedWorkActivity.this,BillInspectActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

        getUnfinishedWork();

        commonAdapter = new CommonAdapter<Bill>(UnfinishedWorkActivity.this, datalist, R.layout.item_a) {
            @Override
            public void convert(ViewHolder holder, Bill bill) {
                holder.setText(R.id.id_type_value, bill.getType());
            }
        };

        lv.setAdapter(commonAdapter);

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
     * 获取待办
     */
    private void getUnfinishedWork() {

        String url = Constant.SERVER_URL + "bill/show";

        OkHttpUtils.post()
                .url(url)
                .addParams("userCode", Constant.username)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(UnfinishedWorkActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response+" ");



                        commonAdapter = new CommonAdapter<Bill>(UnfinishedWorkActivity.this, datalist, R.layout.item_a) {
                            @Override
                            public void convert(ViewHolder holder, Bill bill) {
                                holder.setText(R.id.id_type_value, bill.getType());
                            }
                        };

                        lv.setAdapter(commonAdapter);
                    }
                });



    }

    /*
         * 搜索待办
         */
    private void filterData(String text) {
        List<Bill> filterList = new ArrayList<Bill>();

        if (TextUtils.isEmpty(text)) {
            filterList = datalist;
        } else {
            for (Bill b : datalist) {


                filterList.add(b);
            }
        }


    }

}
