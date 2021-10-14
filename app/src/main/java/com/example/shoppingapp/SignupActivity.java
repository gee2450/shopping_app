package com.example.shoppingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    ImageButton btnExit;

    Button btnBasicInfo;
    Button btnNext;
    Button btnCompleteSignUp;

    LinearLayout basicInfoContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnExit = (ImageButton) findViewById(R.id.btnExit);
        btnBasicInfo = (Button) findViewById(R.id.btnBasicInfo);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnCompleteSignUp = (Button) findViewById(R.id.btnCompleteSignUp);

        basicInfoContents = (LinearLayout) findViewById(R.id.basicInfoContents);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnBasicInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basicInfoContents.setVisibility(View.VISIBLE);
                btnBasicInfo.setClickable(false);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basicInfoContents.setVisibility(View.GONE);
                btnBasicInfo.setClickable(true);
            }
        });
        btnCompleteSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}