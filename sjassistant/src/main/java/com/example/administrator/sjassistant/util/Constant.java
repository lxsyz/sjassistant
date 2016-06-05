package com.example.administrator.sjassistant.util;

import android.util.SparseArray;

import com.example.administrator.sjassistant.bean.DownloadHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/28.
 */
public class Constant {
    public static int gender = 1;
    public static String work;      //职务
    public static String apartment; //部门
    public static String nickname;  //昵称
    public static String address;   //地址

    public static String imgPath;   //头像路径

    public static String username = "";

    public static String version;   //产品版本号

    public static String SERVER_URL="";

    public static final String SETTING_SP = "setting";

    public static final String SETTING_NEW_MESSAGE = "isChecked_message";
    public static final String SETTING_VIBRATE = "isChecked_vibrate";
    public static final String SETTING_VOICE = "isChecked_voice";
    public static final String SETTING_DISTURB = "isDisturb";

    public static final String TEST_SERVER_URL = "http://219.234.5.13:8080/app/";

    //public static String communicateTime;

    public static int NOTE_DOWNLOAD = 0;
    public static Map<Integer,DownloadHandler> mItemLoadHandlers = new HashMap<>();
    public static SparseArray<DownloadHandler> messageArray = new SparseArray<>();
    public static SparseArray<DownloadHandler> billArray = new SparseArray<>();

    //判断是否是主持人
    public static boolean isMaster = false;
}
