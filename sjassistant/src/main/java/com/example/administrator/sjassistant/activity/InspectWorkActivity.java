package com.example.administrator.sjassistant.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.TabAdapter;
import com.example.administrator.sjassistant.fragment.FinishedBillFragment;
import com.example.administrator.sjassistant.fragment.UnfinishedBillFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/6.
 */
public class InspectWorkActivity extends FragmentActivity implements View.OnClickListener {

    private ImageView bt_left,bt_right;
    private TextView tv_center;

    private TabLayout tabLayout;
    private ViewPager vp;

    private FragmentPagerAdapter fAdapter;

    private List<Fragment> list_fragment;
    private List<String> list_title = new ArrayList<String>();
    FinishedBillFragment finishedBillFragment;
    UnfinishedBillFragment unfinishedBillFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_work);
        initWindow();

        initView();
        fragmentChange();
    }

    private void initView() {
        bt_left = (ImageView)findViewById(R.id.bt_left);
        tv_center = (TextView)findViewById(R.id.tv_center);
        bt_right = (ImageView)findViewById(R.id.bt_right);
        bt_right.setImageResource(R.drawable.search_white);

        tabLayout = (TabLayout)findViewById(R.id.my_tab);
        vp = (ViewPager)findViewById(R.id.vp);

        tv_center.setText("搜索审批单据");
        bt_left.setOnClickListener(this);
        bt_right.setOnClickListener(this);
    }

    private void fragmentChange() {
        list_fragment = new ArrayList<Fragment>();

        finishedBillFragment = new FinishedBillFragment();
        unfinishedBillFragment = new UnfinishedBillFragment();
        list_fragment.add(finishedBillFragment);
        list_fragment.add(unfinishedBillFragment);
        list_title.add("已审批单据");
        list_title.add("未审批单据");
        fAdapter = new TabAdapter(getSupportFragmentManager(),list_fragment,list_title);

        vp.setAdapter(fAdapter);

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#38B994"));
        tabLayout.setTabTextColors(Color.parseColor("#414141"), Color.parseColor("#67C5AA"));
        tabLayout.setSelectedTabIndicatorHeight(5);
        tabLayout.setBackgroundColor(Color.WHITE);

        tabLayout.setupWithViewPager(vp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                onBackPressed();
                break;

        }
    }

    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
