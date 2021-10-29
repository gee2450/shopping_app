package com.example.shoppingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    ClothAdapter adapter;

    MySQLite mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelper = new MySQLite(this);
        adapter = new ClothAdapter();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.itemRecyclerList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

//        saveImageToTable();

        InitializeClothData();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.main_menu_item_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.main_menu_item_delete:
                    break;
                case R.id.main_menu_item_home:
                    break;
                case R.id.main_menu_item_profile:
                    Toast.makeText(getApplicationContext(), "회원가입", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    return false;
            }

            return true;
        });
    }


    private void saveImageToTable() {
        Bitmap itemImage;
        ByteArrayOutputStream baos;
        byte[] bytes;

        ContentValues values = new ContentValues();
        itemImage = BitmapFactory.decodeResource(getResources(), R.drawable.shirt);
        baos= new ByteArrayOutputStream();
        itemImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        bytes = baos.toByteArray();
        values.put("itemImage", bytes);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        db.update("items", values, "itemName=?", new String[]{"와이셔츠"});

        values = new ContentValues();
        itemImage = BitmapFactory.decodeResource(getResources(), R.drawable.jeans);
        baos= new ByteArrayOutputStream();
        itemImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        bytes = baos.toByteArray();
        values.put("itemImage", bytes);
        db.update("items", values, "itemName=?", new String[]{"청바지"});

        values = new ContentValues();
        itemImage = BitmapFactory.decodeResource(getResources(), R.drawable.skirt);
        baos= new ByteArrayOutputStream();
        itemImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        bytes = baos.toByteArray();
        values.put("itemImage", bytes);
        db.update("items", values, "itemName=?", new String[]{"치마"});

        values = new ContentValues();
        itemImage = BitmapFactory.decodeResource(getResources(), R.drawable.sweatshirt);
        baos= new ByteArrayOutputStream();
        itemImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        bytes = baos.toByteArray();
        values.put("itemImage", bytes);
        db.update("items", values, "itemName=?", new String[]{"맨투맨"});

        values = new ContentValues();
        itemImage = BitmapFactory.decodeResource(getResources(), R.drawable.shoes);
        baos= new ByteArrayOutputStream();
        itemImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        bytes = baos.toByteArray();
        values.put("itemImage", bytes);
        db.update("items", values, "itemName=?", new String[]{"신발"});

        db.close();
    }


    private void InitializeClothData() {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        String[] projection = {"itemId", "itemName", "itemPrice", "itemImage"};
        Cursor cur = db.query("items", projection, null, null, null, null, null);

        int itemName_col = cur.getColumnIndex("itemName");
        int itemPrice_col = cur.getColumnIndex("itemPrice");
        int itemImage_col = cur.getColumnIndex("itemImage");

        while (cur.moveToNext()) {
            String itemName = cur.getString(itemName_col);
            int itemPrice = cur.getInt(itemPrice_col);
            byte[] itemImage = cur.getBlob(itemImage_col);
            Bitmap bm = BitmapFactory.decodeByteArray(itemImage, 0, itemImage.length);

            adapter.addItem(new SampleData(bm, itemName, ""+itemPrice));

            Log.d("recycle",itemName + itemPrice + itemImage);
        }
        adapter.notifyDataSetChanged();

        cur.close();
        db.close();
    }

}