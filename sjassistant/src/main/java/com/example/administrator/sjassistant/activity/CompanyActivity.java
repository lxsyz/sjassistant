package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/4.
 */
public class CompanyActivity extends BaseActivity implements View.OnClickListener {

    private ImageView search,delete;
    private EditText ed_name;

    private ListView apartment_list;
    private List<String> da = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        super.initView();

        setTopText("公司");
        setCenterView(R.layout.activity_company);

        search = (ImageView)findViewById(R.id.search);
        ed_name = (EditText)findViewById(R.id.search_content);
        delete = (ImageView)findViewById(R.id.delete_word);


        apartment_list = (ListView)findViewById(R.id.apartment_list);

        da.add("全部分所");
        da.add("1");
        da.add("2");
        da.add("3");da.add("4");
        da.add("5");da.add("6");
        da.add("7");da.add("8");
        da.add("9");da.add("10");
        da.add("11");
        da.add("12");
        da.add("13");
        da.add("14");
        da.add("15");
        da.add("16");da.add("17");
        da.add("18");
        da.add("19");da.add("20");
        da.add("21");
        da.add("22");
        apartment_list.setAdapter(new CommonAdapter<String>(this, da, R.layout.item_choose_company) {
            @Override
            public void convert(ViewHolder holder, String s) {
                if (s.equals("全部分所")) {
                    holder.setText(R.id.apartment_name, "全部分所");
                } else {
                    holder.setText(R.id.apartment_name,"北京分所");
                }
            }
        });

        apartment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String)apartment_list.getItemAtPosition(position);

                Log.d("tag",name+" ");
                Intent intent = new Intent(CompanyActivity.this,ContactsDetailActivity.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });


        search.setOnClickListener(this);
        ed_name.setOnClickListener(this);
        delete.setOnClickListener(this);
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
