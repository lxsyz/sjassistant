package com.example.administrator.sjassistant.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.DetailExpandableAdapter;
import com.example.administrator.sjassistant.bean.Bill;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/6.
 */
public class BillDetailActivity extends BaseActivity {

    private int flag=0;

    private ExpandableListView expandableListView;

    private GridView gv;

    private DetailExpandableAdapter adapter;

    private ArrayList<Object> billList = new ArrayList<Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();

        setTopText("单据详细信息");

        flag = getIntent().getIntExtra("flag",-1);

        if (flag == 0) {
            setCenterView(R.layout.activity_bill_detail);
        } else if (flag == 1) {
            setCenterView(R.layout.activity_expand_detail);
            expandableListView = (ExpandableListView)findViewById(R.id.expanded_list);
            billList.add("单据");
            billList.add("单据");billList.add("单据");
            adapter = new DetailExpandableAdapter(this,billList);
            expandableListView.setAdapter(adapter);
        } else if (flag == 2) {
            setCenterView(R.layout.activity_grid_detail);

            gv = (GridView)findViewById(R.id.gv);
            for (int i = 0;i < 15;i++) {
                billList.add("成交信息");
            }
            gv.setAdapter(new ArrayAdapter(this,R.layout.grid_item,R.id.textView,
                                            billList));

        }

    }
}
