package com.example.shoppingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    HomeFragment homeFragment;
    ProfileFragment profileFragment;

    public Intent intent;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = getIntent();

        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment(intent);

        //첫 화면 띄우기
        getSupportFragmentManager().beginTransaction().add(R.id.mainContainer, homeFragment).commit();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.main_menu_item_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.main_menu_item_delete:
                    HomeFragment.instance.setButtonVisibility(View.GONE, View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, homeFragment).commit();
                    HomeFragment.instance.ResetItemArray();
                    break;
                case R.id.main_menu_item_home:
                    ClothAdapter.instance.ResetAllCard();
                    HomeFragment.instance.ResetItemArray();
                    HomeFragment.instance.setButtonVisibility(View.VISIBLE, View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, homeFragment).commit();
                    HomeFragment.instance.ResetItemArray();
                    break;
                case R.id.main_menu_item_profile:
                    boolean isGuest = intent.getBooleanExtra("isGuest", true);
                    if (!isGuest) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, profileFragment).commit();
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Guest Login");
                        builder.setMessage("이 창은 회원가입자에게만 제공되는 창입니다. " +
                                "로그인 하시겠습니까? 아니면 회원가입하시겠습니까? ");
                        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GoHome();
                            }
                        });
                        builder.setNegativeButton("로그인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder.setPositiveButton("회원가입", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                intent = new Intent(getApplicationContext(), SignupActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder.create().show();
                    }
                    break;
                default:
                    return false;
            }

            return true;
        });
    }

    public void GoHome() {
        ClothAdapter.instance.ResetAllCard();
        HomeFragment.instance.ResetItemArray();
        bottomNavigationView.setSelectedItemId(R.id.main_menu_item_home);
    }
}