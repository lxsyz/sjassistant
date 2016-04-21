package com.example.administrator.sjassistant.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.administrator.sjassistant.bean.MessageInform;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by syz on 2016/3/28.
 * 功能封装库 包括功能：
 * 1. 判断是否是email格式
 * 2. 判断是否是手机号码
 * 3. dp2px   px2dp
 * 4. 获取汉字的首字母
 * 5. 计算两段时间相差多少秒 例如 下午3点  下午5点  "下午HH点"
 * 6. 判断是否联网
 * 7. 判断是否是wifi  3g等等
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

    /*
     * dp -> px
     */
    public static int dp2px (Context context,float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /*
     * px -> dp
     */
    public static int px2dp (Context context,float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /*
     * @param String
     * @return 汉字首字母
     * 获取汉字首字母
     */
    public static String getFirstChar(String value) {
        // 首字符
        char firstChar = value.charAt(0);
        // 首字母分类
        String first = null;
        // 是否是非汉字
        String[] print = PinyinHelper.toHanyuPinyinStringArray(firstChar);

        if (print == null) {

            // 将小写字母改成大写
            if ((firstChar >= 97 && firstChar <= 122)) {
                firstChar -= 32;
            }
            if (firstChar >= 65 && firstChar <= 90) {
                first = String.valueOf((char) firstChar);
            } else {
                // 认为首字符为数字或者特殊字符
                first = "#";
            }
        } else {
            // 如果是中文 分类大写字母
            first = String.valueOf((char)(print[0].charAt(0) -32));
        }
        if (first == null) {
            first = "?";
        }
        return first;
    }

    /*
     * 验证是否是手机号
     * @param String
     */
    public static boolean isPhoneNumber(String number) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(number);
        return m.matches();
    }

    /*
     * 计算两段时间相差多少秒
     *
     * @String startTime 开始时间
     * @String endTime 结束时间
     * @String format 日期格式
     * @return 返回秒数
     */
    public static Long dateDiff(String startTime, String endTime,  String format) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        // 获得两个时间的毫秒时间差异
        try {
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime(); //毫秒
            //System.out.println(diff);
            day = diff / nd;// 计算差多少天
            hour = diff % nd / nh + day * 24;// 计算差多少小时
            min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
            sec = diff / 1000;// 计算差多少秒
            // 输出结果
            //System.out.println("时间相差：" + day + "天" + (hour - day * 24) + "小时"
              //      + (min - day * 24 * 60) + "分钟" + sec + "秒。");
            //System.out.println("hour=" + hour + ",min=" + min);


        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return  sec;
    }

    /*
     * 判断是否联网
     * 权限： access_network_state
     * @param Context
     * @return true 联网
     * @return false 未联网
     */
    public static boolean isNetWorkAvailable (Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.d("cm", cm.getActiveNetworkInfo() + " ");
        if (cm == null) {
            return false;
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return true;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /*
     * 判断是否是3G网络
     * @param Context
     * @return
     */
    public static boolean is3G(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * 判断是否是wifi
     * @param Context
     * @return
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return ((cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED)
                || mgrTel.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /*
     * 判断是wifi还是3g网络
     * @param Context
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else {
            return false;
        }
    }

    
}
