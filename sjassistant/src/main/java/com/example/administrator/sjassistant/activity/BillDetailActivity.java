package com.example.administrator.sjassistant.activity;

import android.os.Bundle;

import com.example.administrator.sjassistant.R;

/**
 * Created by Administrator on 2016/4/6.
 */
public class BillDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();

        setTopText("单据详细信息");
        setCenterView(R.layout.activity_bill_detail);
    }
}
