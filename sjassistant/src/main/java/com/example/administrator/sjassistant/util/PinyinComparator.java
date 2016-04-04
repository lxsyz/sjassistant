package com.example.administrator.sjassistant.util;

import com.example.administrator.sjassistant.bean.SortModel;

import java.util.Comparator;

/**
 * Created by Administrator on 2016/4/4.
 */
public class PinyinComparator implements Comparator<SortModel> {

    public int compare(SortModel o1, SortModel o2) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
        if (o2.getSortLetter().equals("#")) {
            return -1;
        } else if (o1.getSortLetter().equals("#")) {
            return 1;
        } else {
            return o1.getSortLetter().compareTo(o2.getSortLetter());
        }
    }
}
