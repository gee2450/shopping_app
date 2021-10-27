package com.example.shoppingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<SampleData> clothList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeClothData();

        Test();

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

        clothList.add(new SampleData(R.drawable.shirt, "와이셔츠", "27500"));
        clothList.add(new SampleData(R.drawable.jeans, "청바지", "55000"));
        clothList.add(new SampleData(R.drawable.skirt, "치마", "39900"));
        clothList.add(new SampleData(R.drawable.sweatshirt, "맨투맨", "49900"));
        clothList.add(new SampleData(R.drawable.shoes, "신발", "35000"));
    }

    private void Test() {   // 선택한 이미지 내부 저장소에 저장
        FileOutputStream fos;
        try {
            fos = openFileOutput("test.txt", Context.MODE_PRIVATE);
            fos.write("test".getBytes());
            fos.close();
            Toast.makeText(getApplicationContext(), "파일 추가 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }

    }

    public void bt2(View view, String imgName) {    // 이미지 삭제
        try {
            File file = getCacheDir();  // 내부저장소 캐시 경로를 받아오기
            File[] flist = file.listFiles();
            for (int i = 0; i < flist.length; i++) {    // 배열의 크기만큼 반복
                if (flist[i].getName().equals(imgName)) {   // 삭제하고자 하는 이름과 같은 파일명이 있으면 실행
                    flist[i].delete();  // 파일 삭제
                    Toast.makeText(getApplicationContext(), "파일 삭제 성공", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 삭제 실패", Toast.LENGTH_SHORT).show();
        }
    }
}