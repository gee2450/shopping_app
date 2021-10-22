package com.example.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<SampleData> clothList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeClothData();

        ListView listView = (ListView)findViewById(R.id.itemList);
        final ClothAdapter adapter = new ClothAdapter(this, clothList);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.main_menu_item_home);

        listView.setAdapter(adapter);

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

    private void InitializeClothData() {
        clothList = new ArrayList<SampleData>();

        clothList.add(new SampleData(R.drawable.logo, "와이셔츠"));
        clothList.add(new SampleData(R.drawable.logo, "청바지"));
        clothList.add(new SampleData(R.drawable.logo, "치마"));
        clothList.add(new SampleData(R.drawable.logo, "치마"));
        clothList.add(new SampleData(R.drawable.logo, "치마"));
    }
}