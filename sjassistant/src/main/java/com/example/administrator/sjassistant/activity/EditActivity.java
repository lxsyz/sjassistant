package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/2.
 */
public class EditActivity extends BaseActivity {

    private ImageView iv_checked;

    private ListView list;

    private TextView tip;
    private String title;
    private int request;

    private List<String> datalist = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getIntent().getStringExtra("top");
        setTopText(title);
        tip.setText("选择" + title);
        request = getIntent().getIntExtra("result",-1);

        getData(request);
        list.setAdapter(new CommonAdapter<String>(this, datalist, R.layout.item_edit) {
            @Override
            public void convert(ViewHolder holder, String s) {
                holder.setText(R.id.type_name, s);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.findViewById(R.id.iv_checked).setVisibility(View.VISIBLE);
                String res = (String) list.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra("result",res);
                setResult(request, intent);
                EditActivity.this.finish();
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();

        setCenterView(R.layout.activity_edit);

        list = (ListView)findViewById(R.id.edit_list);
        tip  = (TextView)findViewById(R.id.tip);

    }

    private void getData(int request) {
        switch (request) {
            case 1:
                datalist.add("企业");
                datalist.add("党政");
                datalist.add("事业");
                break;
            case 2:
                datalist.add("信息");
                datalist.add("财务");
                break;
            case 3:
                datalist.add("经理");
                datalist.add("会计");
                break;
        }
    }
}
