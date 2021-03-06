package com.example.shoppingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    public static HomeFragment instance;

    ClothAdapter adapter;
    MySQLite mDBHelper;

    Button btnAdd;
    Button btnDelete;

    int btnAddVis = View.VISIBLE;
    int btnDeleteVis = View.GONE;

    private ArrayList<Integer> deleteItemsIdArray = new ArrayList<Integer>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        instance = this;

        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);

        adapter = new ClothAdapter(rootView);
        mDBHelper = MySQLite.instance;

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.itemRecyclerList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        InitializeClothData();

        recyclerView.setAdapter(adapter);

        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), AddItemActivity.class);
                startActivity(intent);
            }
        });

        btnDelete = (Button) rootView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteItemsIdArray.size() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                    builder.setTitle("Delete Error");
                    builder.setMessage("????????? ????????? ????????????. ");
                    builder.setNeutralButton("OK", null);
                    builder.create().show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                    builder.setTitle("????????? ?????????????????????????");
                    builder.setMessage("??? "+deleteItemsIdArray.size()+"?????? ????????? ???????????????. ");

                    // yes no ????????? ???????????? ???????????? ????????? ??????????????????.
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int itemId : deleteItemsIdArray) {
                                DeleteClothData(itemId);
                                ((MainActivity) getActivity()).GoHome();
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.setPositiveButton("NO", null);
                    builder.create().show();
                }
            }
        });

        btnAdd.setVisibility(btnAddVis);
        btnDelete.setVisibility(btnDeleteVis);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        adapter.notifyDataSetChanged();
    }

    public void setButtonVisibility(int add, int delete) {
        btnAdd.setVisibility(add);
        btnAddVis = add;
        btnDelete.setVisibility(delete);
        btnDeleteVis = delete;
    }

    public void AddItemArray( int itemId ) {
        deleteItemsIdArray.add(itemId);
    }

    public void RemoveItemArray( int itemId) {
        deleteItemsIdArray.remove(deleteItemsIdArray.indexOf(itemId));
    }

    public void ResetItemArray() {
        deleteItemsIdArray = new ArrayList<Integer>();
    }

    private void DeleteClothData(int id) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        db.delete("items", "itemId = ?", new String[]{ String.valueOf(id) });
        adapter.deleteItem(id);

        db.close();
    }

    private void InitializeClothData() {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        String[] projection = {"itemId", "itemName", "itemPrice", "itemImage"};
        Cursor cur = db.query("items", projection, null, null, null, null, null);

        int itemName_id = cur.getColumnIndex("itemId");
        int itemName_col = cur.getColumnIndex("itemName");
        int itemPrice_col = cur.getColumnIndex("itemPrice");
        int itemImage_col = cur.getColumnIndex("itemImage");

        while (cur.moveToNext()) {
            int itemId = cur.getInt(itemName_id);
            String itemName = cur.getString(itemName_col);
            int itemPrice = cur.getInt(itemPrice_col);
            byte[] itemImage = cur.getBlob(itemImage_col);
            Bitmap bm = BitmapFactory.decodeByteArray(itemImage, 0, itemImage.length);

            adapter.addItem(new SampleData(itemId, bm, itemName, ""+itemPrice));

            Log.d("recycle",itemName + itemPrice + itemImage);
        }
        adapter.notifyDataSetChanged();

        cur.close();
        db.close();
    }
}