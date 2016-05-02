package com.example.administrator.sjassistant.util;

import android.content.Context;

/**
 * Created by Administrator on 2016/4/11.
 */
public class ErrorUtil {

    /*
     * 没网的提示
     */
    public static void NetWorkToast(Context context) {
        if (!OperatorUtil.isNetWorkAvailable(context)) {
            ToastUtil.showShort(context, "网络不给力啊,请检查网络连接");
        } else {
            ToastUtil.showShort(context, "服务器异常,请稍后再试");
        }
    }
}
