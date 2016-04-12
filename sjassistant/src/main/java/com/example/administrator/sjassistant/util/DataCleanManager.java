package com.example.administrator.sjassistant.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by Administrator on 2016/4/7.
 */
public class DataCleanManager {
    /*
     * 清除应用内部缓存
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /*
     * 清除所有数据库
     */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/"+context.getPackageName()
                                + "/databases"));
    }

    /*
     * 清除本应用sp
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName()
                                + "/shared_prefs"));
    }

    //清除应用数据库
    public static void cleanDatabaseByName(Context context,String dbName) {
        context.deleteDatabase(dbName);
    }

    //files下的内容
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /*
     * 清除外部cache下的内容
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    //自定义路径
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    public static void cleanAppicationData(Context context) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
    }

    //删除方法
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item:directory.listFiles()) {
                item.delete();
            }
        }
    }
}
