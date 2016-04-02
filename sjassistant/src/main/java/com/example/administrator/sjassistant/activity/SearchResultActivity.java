package com.example.administrator.sjassistant.activity;

import android.os.Bundle;

import com.example.administrator.sjassistant.R;

/**
 * Created by Administrator on 2016/3/31.
 */
public class SearchResultActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_search_result);
        setTopText("搜索结果");
    }
}
