package com.example.shoppingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class HomeFragment extends Fragment {
    ClothAdapter adapter;
    MySQLite mDBHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);

        adapter = new ClothAdapter();

        mDBHelper = new MySQLite((MainActivity)getActivity());

        // 사진 테이블 저장용 코드
//        saveImageToTable();

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.itemRecyclerList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        InitializeClothData();

        recyclerView.setAdapter(adapter);

        Button btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), AddItemActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void saveImageToTable() {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        String[] clothes = new String[] {"와이셔츠", "청바지", "치마", "맨투맨", "신발"};
        int[] clothesImage = new int[] {R.drawable.shirt, R.drawable.jeans, R.drawable.skirt, R.drawable.sweatshirt, R.drawable.shoes};

        for (int i = 0; i<clothes.length; i++) {
            Bitmap itemImage = BitmapFactory.decodeResource(getResources(), clothesImage[i]);
            ByteArrayOutputStream baos= new ByteArrayOutputStream();
            itemImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] bytes = baos.toByteArray();

            ContentValues values = new ContentValues();
            values.put("itemImage", bytes);

            db.update("items", values, "itemName=?", new String[]{clothes[i]});
        }

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