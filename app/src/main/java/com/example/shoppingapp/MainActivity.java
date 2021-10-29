package com.example.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    HomeFragment homeFragment;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();

        //첫 화면 띄우기
        getSupportFragmentManager().beginTransaction().add(R.id.mainContainer, homeFragment).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.main_menu_item_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.main_menu_item_delete:
                case R.id.main_menu_item_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, homeFragment).commit();
                    break;
                case R.id.main_menu_item_profile:
                    if (true) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, profileFragment).commit();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "회원가입", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                default:
                    return false;
            }

            return true;
        });
    }







}