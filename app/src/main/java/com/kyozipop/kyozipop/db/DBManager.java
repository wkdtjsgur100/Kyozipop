package com.kyozipop.kyozipop.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jang on 2016-03-30.
 */
public class DBManager extends SQLiteOpenHelper{

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table room_info(idx INTEGER PRIMARY KEY AUTOINCREMENT, host_id TEXT, host_idx INTEGER, user_idxs TEXT);");
        db.execSQL("create table friend_info(id TEXT, nickname TEXT, phone_number TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void execQuery(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }
}
