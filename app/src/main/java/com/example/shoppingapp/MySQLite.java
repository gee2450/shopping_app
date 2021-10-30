package com.example.shoppingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLite extends SQLiteOpenHelper {
    public static MySQLite instance;

    public MySQLite(Context context) {
        super(context, "shoppingApp.db", null, 1);
        instance = this;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS userTable ("
                + "id TEXT, password TEXT, "
                + "userName TEXT, phoneNum TEXT, "
                + "address TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS userTable;");
        onCreate(db);
    }
}
