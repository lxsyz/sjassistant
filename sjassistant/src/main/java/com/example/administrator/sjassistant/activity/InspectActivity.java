package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.fragment.FinishedBillFragment;
import com.example.administrator.sjassistant.fragment.UnfinishedBillFragment;
import com.example.administrator.sjassistant.util.OperatorUtil;


/**
 * Created by Administrator on 2016/4/27.
 */
public class InspectActivity extends FragmentActivity implements View.OnClickListener {

    ViewPager viewPager;
    PagerSlidingTabStrip tabs;
//    TabPageIndicator indicator;

    private ImageView bt_left,bt_right;
    private TextView tv_center;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_inspect);
        initWindow();

        viewPager = (ViewPager)findViewById(R.id.pager);

        bt_left = (ImageView)findViewById(R.id.bt_left);
        tv_center = (TextView)findViewById(R.id.tv_center);
        bt_right = (ImageView)findViewById(R.id.bt_right);
        bt_right.setImageResource(R.drawable.search_white);
        tv_center.setText("搜索审批单据");
        bt_left.setOnClickListener(this);
        bt_right.setOnClickListener(this);


        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabs = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        tabs.setIndicatorColor(Color.parseColor("#38B994"));
        tabs.setBackgroundResource(R.color.white);
        tabs.setIndicatorHeight(OperatorUtil.dp2px(this,2));
        //tabs.setDividerColor(R.color.white);

        //tabs.setTextColorResource(R.drawable.background_tab_text);
        //tabs.setActivateTextColor(R.color.background_text_selected);
        //tabs.setDeactivateTextColor(R.color.background_text);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bt_left:
                onBackPressed();
                break;
            case R.id.bt_right:
                intent = new Intent(InspectActivity.this,SearchBillActivity.class);
                startActivity(intent);
                break;
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        String[] title = {"已审批单据","未审批单据"};

        FinishedBillFragment finishedBillFragment;
        UnfinishedBillFragment unfinishedBillFragment;

        public MyPagerAdapter(FragmentManager adapter) {
            super(adapter);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    finishedBillFragment = new FinishedBillFragment();
                    return finishedBillFragment;
                case 1:
                    unfinishedBillFragment = new UnfinishedBillFragment();
                    return  unfinishedBillFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return title.length;
        }

        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
