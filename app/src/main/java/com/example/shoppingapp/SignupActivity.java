package com.example.shoppingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.sql.SQLInput;

public class SignupActivity extends AppCompatActivity {
    private MySQLite mDBHelpher;

    private EditText idInput;
    private EditText pwInput;
    private EditText repwInput;
    private EditText nameInput;
    private EditText phoneNumInput;
    private EditText addressInput;

    private Button btnBasicInfo;
    private Button btnNext;
    private Button btnCompleteSignUp;

    private LinearLayout basicInfoContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // 뒤로가기 버튼 생성
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDBHelpher = new MySQLite(this);

        // EditText 연동
        idInput = (EditText) findViewById(R.id.editTextId);
        pwInput = (EditText) findViewById(R.id.editTextPw);
        repwInput = (EditText) findViewById(R.id.editTextRepw);
        nameInput = (EditText) findViewById(R.id.editTextName);
        phoneNumInput = (EditText) findViewById(R.id.editTextPhone);
        addressInput = (EditText) findViewById(R.id.editTextAddress);

        // Button 연동
        btnBasicInfo = (Button) findViewById(R.id.btnBasicInfo);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnCompleteSignUp = (Button) findViewById(R.id.btnCompleteSignUp);

        // Layout 연동
        basicInfoContents = (LinearLayout) findViewById(R.id.basicInfoContents);

        // button 클릭 함수 구현
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
                boolean result = checkPassword();

                if (result) {
                    basicInfoContents.setVisibility(View.GONE);
                    btnBasicInfo.setClickable(true);
                }
            }
        });
        btnCompleteSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = SignUp();

                Toast.makeText(getApplicationContext(), "로그인", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

    private boolean checkPassword() {
        String pw = pwInput.getText().toString();
        String repw = repwInput.getText().toString();

        // password 특수 규칙 적용

        if (!pw.equals(repw)) return false;
        if (pw == null) return false;

        return true;
    }

    private boolean SignUp() {
        // 만약 약관 동의 안하면 return false

        // userName, phoneNum, address 비어있으면 return false
        if (nameInput.getText() == null | phoneNumInput.getText() == null | addressInput.getText() == null) {
            return false;
        }

        SQLiteDatabase db = mDBHelpher.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", idInput.getText().toString());
        values.put("password", pwInput.getText().toString());
        values.put("userName", nameInput.getText().toString());
        values.put("phoneNum", phoneNumInput.getText().toString());
        values.put("address", addressInput.getText().toString());
        db.insert("userTable", null, values);

        mDBHelpher.close();
        return true;
    }

    // 뒤로가기 버튼 코드 구현
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // android.R.id.home : toolbar 의back
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}