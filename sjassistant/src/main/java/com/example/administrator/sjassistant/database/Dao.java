package com.example.administrator.sjassistant.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.sjassistant.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class Dao {
    private static Dao dao = null;

    private Context context;

    private Dao(Context context) {
        this.context = context;
    }

    public static Dao getInstance(Context context) {
        if (dao == null) {
            dao = new Dao(context);
        }
        return dao;
    }

    public SQLiteDatabase getConnection() {
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = new DBHelper(context).getWritableDatabase();
        } catch (Exception e) {

        }
        return sqLiteDatabase;
    }

    private SQLiteDatabase getReadConnection() {
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = new DBHelper(context).getReadableDatabase();
        } catch (Exception e) {

        }
        return sqLiteDatabase;
    }

    /**
     * 保存下载的具体信息
     * @param username
     */
    public synchronized void saveInfos(String username) {

        SQLiteDatabase database = getConnection();

        try {
            String sql = "insert into "+Constant.TABLE_NAME+ " (username) values (?)";
            database.beginTransaction();
            database.execSQL(sql, new String[]{username});
            database.setTransactionSuccessful();
            Log.d("response","transaction");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();

            if (database != null) {
                database.close();
            }

        }
    }

    /**
     * 获取数据
     * @return
     */
    public synchronized List<String> getInfos() {
        List<String> list = new ArrayList<>();

        SQLiteDatabase database = getReadConnection();

        Cursor cursor = null;

        try {
            String sql = "select username from "+Constant.TABLE_NAME;
            cursor = database.rawQuery(sql,null);
            Log.d("response",cursor.getCount()+" ");
            while (cursor.moveToNext()) {
                list.add(cursor.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }

            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 删除数据
     * @param username
     */
    public synchronized void delete(String username) {
        SQLiteDatabase database = getConnection();
        try {
            Log.d("response",username+" ");
            database.beginTransaction();
            database.delete(Constant.TABLE_NAME, "username = ?", new String[]{username});
            database.setTransactionSuccessful();
            Log.d("response", "haha");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
            if (database != null) {
                database.close();
            }
        }
    }
}
