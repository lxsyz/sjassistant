package com.example.administrator.sjassistant.util;

/**
 * Created by Administrator on 2016/6/27.
 */
public interface OnRefreshListener {

    /**
     * 下拉刷新
     */
    void onDownPullRefresh();

    /**
     * 上拉加载更多
     */
    void onLoadingMore();
}
