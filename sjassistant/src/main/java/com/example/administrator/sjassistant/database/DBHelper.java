package com.example.administrator.sjassistant.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2016/6/6.
 */
public class DBHelper extends SQLiteOpenHelper {

    private String CREATE_USER = "create table user(" +
            "id integer primary key autoincrement," +
            "username varchar(20) not null)";

    public DBHelper(Context context) {
        super(context,"user.db",null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("response","oncreate database");
        db.execSQL(CREATE_USER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        switch (oldVersion) {
            case 1:
                db.execSQL(CREATE_USER);

        }
    }
}
