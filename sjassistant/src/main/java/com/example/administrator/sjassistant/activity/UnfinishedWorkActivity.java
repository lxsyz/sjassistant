package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Bill;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public class UnfinishedWorkActivity extends BaseActivity implements View.OnClickListener {

    private ImageView search,delete;
    private EditText ed_name;

    private ListView lv;

    private List<Bill> datalist = new ArrayList<Bill>();

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
        lv.setAdapter(new CommonAdapter<Bill>(this, datalist, R.layout.item_a) {
            @Override
            public void convert(ViewHolder holder, Bill bill) {
                holder.setText(R.id.id_type_value, bill.getType());
            }
        });

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
}
