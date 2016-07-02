package com.example.administrator.sjassistant.util;

import android.util.SparseArray;

import com.example.administrator.sjassistant.bean.Department;
import com.example.administrator.sjassistant.bean.DownloadHandler;
import com.example.administrator.sjassistant.bean.MyContacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/28.
 */
public class Constant {
    public static final String APPID = "1f03e6208426181ecd28b08ebb2c2a73";


    public static int gender = 1;
    public static String work;      //职务
    public static String apartment; //部门
    public static String nickname;  //昵称
    public static String address;   //地址

    public static String imgPath = "";   //头像路径

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

    //存储添加部门时的部门
    public static List<Department> result = new ArrayList<>();
    public static int count = 0;

    public static List<MyContacts> contactResult = new ArrayList<>();
    public static int contactCount = 0;
    public static final String TABLE_NAME = "user";
}
