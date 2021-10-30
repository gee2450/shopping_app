package com.example.shoppingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    private MySQLite mDBHelper;

    private EditText idInput;
    private EditText pwInput;

    Button btnSignUp;
    Button btnLogIn;
    Button btnMain;

    boolean isGuest = true;
    String id = "";
    String password = "";
    String userName = "";
    String phoneNum = "";
    String address = "";

    String dialogMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDBHelper = new MySQLite(this);

        idInput = (EditText) findViewById(R.id.id_input);
        pwInput = (EditText) findViewById(R.id.password_input);

        // id, pw 불러오기
        SetIdPw(savedInstanceState);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        btnMain = (Button) findViewById(R.id.btnMain);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = logIn();

                if (result) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent = PutExtra(intent);
                    startActivity(intent);
                    finish();
                }
                else {
                    MakeDialog("Login Error");
                }
            }
        });
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void SetIdPw(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            SharedPreferences pf = getSharedPreferences("user", MODE_PRIVATE);

            idInput.setText(pf.getString("id", ""));
            pwInput.setText(pf.getString("pw", ""));
        }
    }

    private Intent PutExtra(Intent intent) {
        intent.putExtra("isGuest", isGuest);
        intent.putExtra("id", id);
        intent.putExtra("userName", userName);
        intent.putExtra("phoneNum", phoneNum);
        intent.putExtra("address", address);

        return intent;
    }

    private void MakeDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(title);
        builder.setMessage(dialogMessage);
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }


    private boolean logIn() {
        id = idInput.getText().toString();

        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        String[] projection = {"id", "password", "userName", "phoneNum", "address"};
        Cursor cur = db.query("userTable", projection, "id=?", new String[]{ id }, null, null, null);

        if (cur == null) {
            return false;
        }
        else if (cur.getCount() < 1) {
            dialogMessage = "해당 아이디를 찾을 수 없습니다. ";
            return false;
        }

        int password_col = cur.getColumnIndex("password");
        int userName_col = cur.getColumnIndex("userName");
        int phoneNum_col = cur.getColumnIndex("phoneNum");
        int address_col = cur.getColumnIndex("address");

        while (cur.moveToNext()) {
            password = cur.getString(password_col);
            userName = cur.getString(userName_col);
            phoneNum = cur.getString(phoneNum_col);
            address = cur.getString(address_col);
        }

        cur.close();

        if (!password.equals(pwInput.getText().toString())) {
            dialogMessage = "올바르지 않은 비밀번호입니다. ";
            return false;
        }

        isGuest = false;

        SharedPreferences pf = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = pf.edit();
        editor.putString("id", id);
        editor.putString("pw", password);
        editor.apply();

        return true;
    }
}