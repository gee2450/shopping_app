package com.example.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
                    Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                    HomeFragment.instance.setButtonVisibility(View.GONE, View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, homeFragment).commit();
                    HomeFragment.instance.ResetItemArray();
                    break;
                case R.id.main_menu_item_home:
                    Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_SHORT).show();
                    HomeFragment.instance.setButtonVisibility(View.VISIBLE, View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, homeFragment).commit();
                    HomeFragment.instance.ResetItemArray();
                    break;
                case R.id.main_menu_item_profile:
                    if (true) {
                        Toast.makeText(getApplicationContext(), "profile", Toast.LENGTH_SHORT).show();
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