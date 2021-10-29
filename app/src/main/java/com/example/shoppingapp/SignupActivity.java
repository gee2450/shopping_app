package com.example.shoppingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {
    private final int idMinLength = 4;
    private final int pwMinLength = 10;

    private MySQLite mDBHelper;

    private EditText idInput;
    private EditText pwInput;
    private EditText repwInput;
    private EditText nameInput;
    private EditText phoneNumInput;
    private EditText addressInput;

    private LinearLayout basicInfoContents;

    private String dialogMessage;

    private String passId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // 뒤로가기 버튼 생성
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDBHelper = new MySQLite(this);

        // EditText 연동
        idInput = (EditText) findViewById(R.id.editTextId);
        pwInput = (EditText) findViewById(R.id.editTextPw);
        repwInput = (EditText) findViewById(R.id.editTextRepw);
        nameInput = (EditText) findViewById(R.id.editTextName);
        phoneNumInput = (EditText) findViewById(R.id.editTextPhone);
        addressInput = (EditText) findViewById(R.id.editTextAddress);

        TextView idHelper = (TextView) findViewById(R.id.idHelper);
        TextView pwHelper = (TextView) findViewById(R.id.pwHelper);
        idHelper.setText("아이디는 최소 "+idMinLength+"자 이상으로 작성해주세요.");
        pwHelper.setText("비밀번호는 최소 "+pwMinLength+"자 이상, \n" +
                "그리고 숫자, 특수기호가 포함되게 작성해주세요.");

        ArrayList<String> idArray = GetIdArray();

        // Button 연동
        Button btnBasicInfo = (Button) findViewById(R.id.btnBasicInfo);
        Button btnCheckId = (Button) findViewById(R.id.btnCheckId);
        Button btnNext = (Button) findViewById(R.id.btnNext);
        Button btnCompleteSignUp = (Button) findViewById(R.id.btnCompleteSignUp);

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
        btnCheckId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = CheckId(idArray);
                if (!result) {
                    MakeDialog("Error");
                }
                else {
                    MakeDialog("ID");
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passId == null || !passId.equals(idInput.getText().toString())) {
                    dialogMessage = "아이디 중복확인을 해주세요. ";
                    MakeDialog("Error");
                    return;
                }

                Boolean result = CheckPassword();

                if (result) {
                    basicInfoContents.setVisibility(View.GONE);
                    btnBasicInfo.setClickable(true);
                }
                else {
                    MakeDialog("Error");
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

    private ArrayList<String> GetIdArray() {
        ArrayList<String> result = new ArrayList<String>();

        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        String[] projection = {"id"};
        Cursor cur = db.query("userTable", projection, null, null, null, null, null);

        while (cur.moveToNext()) {
            result.add(cur.getString(0));
        }

        cur.close();
        db.close();

        return result;
    }

    private void MakeDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setTitle(title);
        builder.setMessage(dialogMessage);
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

    private boolean CheckId(ArrayList<String> idArrays) {
        String id = idInput.getText().toString();
        dialogMessage = "사용할 수 있는 아이디입니다. ";

        if (id.length() < idMinLength) {
            dialogMessage = "아이디는 "+idMinLength+"자 이상이어야합니다. ";
            return false;
        }

        if (idArrays.contains(id)) {
            dialogMessage = "이미 사용중인 아이디입니다. ";
            return false;
        }

        passId = id;

        return true;
    }

    private boolean CheckPassword() {
        String pw = pwInput.getText().toString();
        String repw = repwInput.getText().toString();

        if (pw == null) {
            dialogMessage = "비밀번호가 비어있습니다. ";
            return false;
        }

        if (pw.length() < pwMinLength) {
            dialogMessage = "비밀번호는 "+pwMinLength+"자 이상이어야합니다. ";
            return false;
        }

        if (!pw.matches(".*[a-zA-Z].*")) {
            dialogMessage = "비밀번호에 알파벳이 포함되어있지 않습니다. ";
            return false;
        }

        if (!pw.matches(".*[0-9].*")) {
            dialogMessage = "비밀번호에 숫자가 포함되어있지 않습니다. ";
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
            dialogMessage = "비밀번호에 특수기호가 포함되어있지 않습니다. ";
            return false;
        }

        if (!pw.equals(repw)) {
            dialogMessage = "비밀번호와 확인 비밀번호가 다릅니다. ";
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

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", idInput.getText().toString());
        values.put("password", pwInput.getText().toString());
        values.put("userName", nameInput.getText().toString());
        values.put("phoneNum", phoneNumInput.getText().toString());
        values.put("address", addressInput.getText().toString());
        db.insert("userTable", null, values);

        mDBHelper.close();
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