package com.example.administrator.sjassistant.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/4/23.
 */
public class ServerConfigUtil {

    public static void setServerConfig(Context context) {
        SharedPreferences sp2 = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        Constant.SERVER_URL = "http://"+sp2.getString("server_address",null)+":"+sp2.getString("server_port",null)+"/app/";
    }
}
