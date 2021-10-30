package com.example.shoppingapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MySQLite extends SQLiteOpenHelper {
    public static MySQLite instance;

    static final String ROOT_DIR = "/data/data/com.example.shoppingapp/databases/";
    static final String DB_NAME = "shoppingApp.db";

    public MySQLite(Context context) {
        super(context, DB_NAME, null, 1);
        GetDB(context);
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

    void GetDB(Context context) {
        File folder = new File(ROOT_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        AssetManager assetManager = context.getResources().getAssets();

        File outfile = new File(ROOT_DIR + DB_NAME);
        InputStream is = null;
        FileOutputStream fo = null;
        long fileSize = 0;
        try {
            is = assetManager.open(DB_NAME, AssetManager.ACCESS_BUFFER);
            fileSize = is.available();
            if (outfile.length() <= 0) {
                byte[] data = new byte[(int) fileSize];
                is.read(data);
                is.close();
                outfile.createNewFile();
                fo = new FileOutputStream(outfile);
                fo.write(data);
                fo.close();
            }
        } catch (IOException e) {}
    }
}
