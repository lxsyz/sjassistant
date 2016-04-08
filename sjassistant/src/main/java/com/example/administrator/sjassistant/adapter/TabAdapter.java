package com.example.administrator.sjassistant.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/4/6.
 */
public class TabAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> titleList;
    public TabAdapter(FragmentManager fm, List<Fragment> list, List<String> titleList) {
        super(fm);
        this.fragmentList = list;
        this.titleList = titleList;
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public CharSequence getPageTitle(int position) {
        return titleList.get(position % titleList.size());
    }
}
