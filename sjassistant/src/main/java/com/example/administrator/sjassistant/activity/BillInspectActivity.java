package com.example.administrator.sjassistant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.TimeAxisAdapter;
import com.example.administrator.sjassistant.view.ChooseShareWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public class BillInspectActivity extends BaseActivity implements View.OnClickListener {

    private ListView list;
    private RelativeLayout bill_detail_layout,pass_layout,cancel_layout;
    private ChooseShareWindow chooseShareWindow;
    private LinearLayout root;

    private TimeAxisAdapter mTimeAxisAdapter;

    private List<HashMap<String,Object>> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (chooseShareWindow != null) {
            chooseShareWindow.getChooseShareWindow().getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        chooseShareWindow.closeWindow();
                    }
                }
            });
        }
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_bill_inspect);
        setTopText("");
        setRightButtonRes(R.drawable.share);
        setRightButtonRes2(R.drawable.chat_more);
        setRightButton2(View.VISIBLE);

        list = (ListView)findViewById(R.id.inspect_list);
        bill_detail_layout = (RelativeLayout)findViewById(R.id.bill_detail_layout);
        pass_layout = (RelativeLayout)findViewById(R.id.pass_layout);
        cancel_layout = (RelativeLayout)findViewById(R.id.cancel_layout);
        root = (LinearLayout)findViewById(R.id.root);

        chooseShareWindow = new ChooseShareWindow(this);

        btn_right.setOnClickListener(this);
        bt_right2.setOnClickListener(this);
        bill_detail_layout.setOnClickListener(this);
        pass_layout.setOnClickListener(this);
        cancel_layout.setOnClickListener(this);

        list.setDividerHeight(0);

        mTimeAxisAdapter = new TimeAxisAdapter(this,getData());
        list.setAdapter(mTimeAxisAdapter);
    }

    private List<HashMap<String,Object>> getData() {
        List<HashMap<String, Object>> listChild = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("content", "Jimmy");
        listChild.add(map);
//        HashMap<String, Object> map1 = new HashMap<String, Object>();
//        map1.put("content", "john");
//        listChild.add(map1);
//        HashMap<String, Object> map2 = new HashMap<String, Object>();
//        map2.put("content", "hhh");
//        listChild.add(map2);
//        HashMap<String, Object> map3 = new HashMap<String, Object>();
//        map3.put("content", "hhhh");
//        listChild.add(map3);
//        HashMap<String, Object> map4 = new HashMap<String, Object>();
//        map4.put("content", "5h");
//        listChild.add(map4);
//        HashMap<String, Object> map5 = new HashMap<String, Object>();
//        map5.put("content", "h");
//        listChild.add(map5);
        return listChild;
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bt_right:
                chooseShareWindow.showChooseShareWindow(root);
                break;
            case R.id.bt_right2:
                break;
            case R.id.pass_layout:
                break;
            case R.id.cancel_layout:
                break;
            case R.id.bill_detail_layout:
                intent = new Intent(BillInspectActivity.this,BillDetailActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        chooseShareWindow.closeWindow();
    }
}
