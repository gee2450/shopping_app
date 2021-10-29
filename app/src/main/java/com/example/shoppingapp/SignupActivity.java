package com.example.shoppingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.sql.SQLInput;

public class SignupActivity extends AppCompatActivity {
    private final int pwMinLength = 10;

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

    private String signUpDisagreeReason;

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

        TextView pwHelper = (TextView) findViewById(R.id.pwHelper);
        pwHelper.setText("비밀번호는 최소 "+pwMinLength+"자 이상, \n" +
                "그리고 숫자, 특수기호가 포함되게 작성해주세요.");

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
                boolean result = CheckPassword();

                if (result) {
                    basicInfoContents.setVisibility(View.GONE);
                    btnBasicInfo.setClickable(true);
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage(signUpDisagreeReason);
                    builder.setNeutralButton("OK", null);
                    builder.create().show();
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

    private boolean CheckPassword() {
        String pw = pwInput.getText().toString();
        String repw = repwInput.getText().toString();

        if (pw == null) {
            signUpDisagreeReason = "비밀번호가 비어있습니다. ";
            return false;
        }

        if (pw.length() < pwMinLength) {
            signUpDisagreeReason = "비밀번호는 "+pwMinLength+"자 이상이어야합니다. ";
            return false;
        }

        if (!pw.matches(".*[a-zA-Z].*")) {
            signUpDisagreeReason = "비밀번호에 알파벳이 포함되어있지 않습니다. ";
            return false;
        }

        if (!pw.matches(".*[0-9].*")) {
            signUpDisagreeReason = "비밀번호에 숫자가 포함되어있지 않습니다. ";
            return false;
        }

        boolean sResult = false;
        for (int i = 0; i < pw.length(); i++) {
            if (!Character.isLetterOrDigit(pw.charAt(i))) {
                sResult = true;
                break;
            }
        }
        if (!sResult) {
            signUpDisagreeReason = "비밀번호에 특수기호가 포함되어있지 않습니다. ";
            return false;
        }

        if (!pw.equals(repw)) {
            signUpDisagreeReason = "비밀번호와 확인 비밀번호가 다릅니다. ";
            return false;
        }

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