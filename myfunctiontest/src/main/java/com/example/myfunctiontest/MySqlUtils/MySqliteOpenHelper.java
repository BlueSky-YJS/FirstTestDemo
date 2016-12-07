package com.example.myfunctiontest.MySqlUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by YJSONG on 2016/11/25.
 */

public class MySqliteOpenHelper extends SQLiteOpenHelper{

    private static final String CREATE_TABLE = "create table info_4s (_id integer primary key autoincrement,pname text not null,cname text not null,_4sname text not null,_4saddress text not null,longitude text not null,latitude text not null);";
    private Context mContext;

    public MySqliteOpenHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists info_4s");
        onCreate(db);
    }


}
