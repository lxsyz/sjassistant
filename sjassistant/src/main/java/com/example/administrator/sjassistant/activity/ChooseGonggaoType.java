package com.example.administrator.sjassistant.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/3.
 */
public class ChooseGonggaoType extends BaseActivity {

    private ListView type_list;

    private List<String> datalist = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_choose_type);
        setTopText("公告类型");

        type_list = (ListView)findViewById(R.id.type_list);
        datalist.add("hha");
        type_list.setAdapter(new CommonAdapter<String>(this,datalist,R.layout.item_type) {
            @Override
            public void convert(ViewHolder holder, String s) {

            }
        });

    }
}
