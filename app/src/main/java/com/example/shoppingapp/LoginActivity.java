package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private MySQLite mDBHelper;

    private EditText idInput;
    private EditText pwInput;

    Button btnSignUp;
    Button btnLogIn;
    Button btnMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDBHelper = new MySQLite(this);

        idInput = (EditText) findViewById(R.id.id_input);
        pwInput = (EditText) findViewById(R.id.password_input);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        btnMain = (Button) findViewById(R.id.btnMain);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "회원가입", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = logIn();

                if (result) {
                    Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "로그인 없이 앱 이용하기", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean logIn() {
        String id = idInput.getText().toString();

        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        String[] projection = {"id", "password"};
        Cursor cur = db.query("userTable", projection, "id=?", new String[]{ id }, null, null, null);

        if (cur == null) return false;

        int password_col = cur.getColumnIndex("password");
        String password = "";

        while (cur.moveToNext()) {
            password = cur.getString(password_col);
        }

        cur.close();

        if (!password.equals(pwInput.getText().toString())) {
            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}