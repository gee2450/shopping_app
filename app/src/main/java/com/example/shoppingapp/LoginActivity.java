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

    boolean isGuest = true;
    String id = "";
    String password = "";
    String userName = "";
    String phoneNum = "";
    String address = "";

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

    private Intent PutExtra(Intent intent) {
        intent.putExtra("isGuest", isGuest);
        intent.putExtra("id", id);
        intent.putExtra("userName", userName);
        intent.putExtra("phoneNum", phoneNum);
        intent.putExtra("address", address);

        return intent;
    }

    private boolean logIn() {
        id = idInput.getText().toString();

        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        String[] projection = {"id", "password", "userName", "phoneNum", "address"};
        Cursor cur = db.query("userTable", projection, "id=?", new String[]{ id }, null, null, null);

        if (cur == null) return false;

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
            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
            return false;
        }

        isGuest = false;

        return true;
    }
}