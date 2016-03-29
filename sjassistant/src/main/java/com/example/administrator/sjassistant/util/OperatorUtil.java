package com.example.administrator.sjassistant.util;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/3/28.
 * 简单计算方法
 */
public class OperatorUtil {

    /*
    * 判断用户名是否格式正确
    */
    public static boolean isEmail(String content) {
        String str = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(content);
        boolean b = m.matches();
        Log.d("tag", b + " ");
        return b;
    }
}
