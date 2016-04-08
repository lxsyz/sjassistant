package com.example.administrator.sjassistant.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public class SearchBillActivity extends BaseActivity {

    //private TabLayout tabLayout;
    private ViewPager vp;
    private FragmentPagerAdapter fAdapter;

    private List<Fragment> list_fragment;
    private List<String> list_title;


    private void initView(View view) {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();

//        tabLayout = (TabLayout) view.findViewById(R.id.my_tab);
//        vp = (ViewPager)view.findViewById(R.id.vp);
//
//        tabLayout.setTabMode(TabLayout.MODE_FIXED);
//
//        tabLayout.addTab(tabLayout.newTab().setText("哈哈"));
//        tabLayout.addTab(tabLayout.newTab().setText("哈哈"));
//        tabLayout.addTab(tabLayout.newTab().setText("哈哈"));
//
//        fAdapter = new My_TabAdapter(getActivity().getSupportFragmentManager(),list_fragment,list_title);
//
//        vp.setAdapter(fAdapter);
//        //tabLayout.setBackgroundColor();
//        tabLayout.setSelectedTabIndicatorColor(Color.GRAY);
//        tabLayout.setTabTextColors(Color.WHITE,Color.BLACK);
//        tabLayout.setupWithViewPager(vp);
    }
}
