package com.example.shoppingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    CheckBox checkAllBox;
    CheckBox checkBox1;
    CheckBox checkBox2;

    private LinearLayout basicInfoContents;

    private String dialogMessage;
    private String passId;

    private String terms1;
    private String terms2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // 뒤로가기 버튼 생성
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDBHelper = MySQLite.instance;

        // EditText 연동
        idInput = (EditText) findViewById(R.id.editTextId);
        pwInput = (EditText) findViewById(R.id.editTextPw);
        repwInput = (EditText) findViewById(R.id.editTextRepw);
        nameInput = (EditText) findViewById(R.id.editTextName);
        phoneNumInput = (EditText) findViewById(R.id.editTextPhone);
        addressInput = (EditText) findViewById(R.id.editTextAddress);

        // checkBox 연동
        checkAllBox = (CheckBox) findViewById(R.id.checkAllBox);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);

        // Button 연동
        Button btnBasicInfo = (Button) findViewById(R.id.btnBasicInfo);
        Button btnCheckId = (Button) findViewById(R.id.btnCheckId);
        Button btnNext = (Button) findViewById(R.id.btnNext);
        Button btnText1 = (Button) findViewById(R.id.btnText1);
        Button btnText2 = (Button) findViewById(R.id.btnText2);
        Button btnCompleteSignUp = (Button) findViewById(R.id.btnCompleteSignUp);

        // TextView 연동
        TextView idHelper = (TextView) findViewById(R.id.idHelper);
        TextView pwHelper = (TextView) findViewById(R.id.pwHelper);
        idHelper.setText("아이디는 최소 "+idMinLength+"자 이상으로 작성해주세요.");
        pwHelper.setText("비밀번호는 최소 "+pwMinLength+"자 이상, \n" +
                "그리고 숫자, 특수기호가 포함되게 작성해주세요.");

        // Layout 연동
        basicInfoContents = (LinearLayout) findViewById(R.id.basicInfoContents);

        // idArray 가져오기
        ArrayList<String> idArray = GetIdArray();

        // 이용악관, 개인정보취급약관 Text 가져오기
        terms1 = GetTerms("Terms of Use");
        terms2 = GetTerms("Privacy Policy");

        // checkBox checkChangListener 구현
        checkAllBox.setOnClickListener(checkBoxClickListener);
        checkBox1.setOnClickListener(checkBoxClickListener);
        checkBox2.setOnClickListener(checkBoxClickListener);

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
        btnText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermsFragment terms = TermsFragment.getInstance();
                terms.show(getSupportFragmentManager(), TermsFragment.TAG_EVENT_DIALOG);
                terms.setText("이용약관", terms1);
            }
        });
        btnText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermsFragment terms = TermsFragment.getInstance();
                terms.show(getSupportFragmentManager(), TermsFragment.TAG_EVENT_DIALOG);
                terms.setText("개인정보취급약관", terms2);
            }
        });
        btnCompleteSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameInput.getText() == null) {
                    dialogMessage = "이름을 적어주세요. ";
                    MakeDialog("Error");
                    return;
                }
                else if (!CheckPhoneNum()) {
                    MakeDialog("Error");
                    return;
                }
                else if (!CheckAddress()) {
                    MakeDialog("Error");
                    return;
                }
                else if (!checkAllBox.isChecked()) {
                    dialogMessage = "약관동의를 모두 해주세요. ";
                    MakeDialog("Error");
                    return;
                }

                // 회원가입
                SignUp();

                // SignUp 창 finish
                finish();
            }
        });
    }

    View.OnClickListener checkBoxClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isChecked = ((CheckBox) v).isChecked();

            switch (v.getId()) {
                case R.id.checkAllBox:
                    if (isChecked) {
                        checkBox1.setChecked(true);
                        checkBox2.setChecked(true);
                    }
                    else {
                        checkBox1.setChecked(false);
                        checkBox2.setChecked(false);
                    }
                    break;
                case R.id.checkBox1:
                case R.id.checkBox2:
                    if (checkBox1.isChecked() && checkBox2.isChecked()) {
                        checkAllBox.setChecked(true);
                    }
                    else if (!isChecked) {
                        checkAllBox.setChecked(false);
                    }
                default:
                    break;
            }
        }
    };

    private String GetTerms(String termsName) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        String[] projection = {"termsName", "termsContent"};
        Cursor cur = db.query("terms", projection, "termsName=?", new String[]{termsName}, null, null, null);

        cur.moveToNext();
        String result = cur.getString(1);

        cur.close();
        db.close();

        return result;
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

    private boolean CheckPhoneNum() {
        String phone = phoneNumInput.getText().toString();
        if (phone.contains("-")) {
            dialogMessage = "숫자만 적어주세요";
            return false;
        }

        if (phone.length() < 9 || phone.length() > 11) {
            dialogMessage = "잘못된 전화번호입니다. ";
            return false;
        }

        return true;
    }

    private boolean CheckAddress() {
        String address = addressInput.getText().toString();

        if (address == null) {
            dialogMessage = "이메일을 적어주세요";
            return false;
        }

        if (!address.contains("@")) {
            dialogMessage = "잘못된 이메일 형식입니다. ";
            return false;
        }

        return true;
    }

    private void SignUp() {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", idInput.getText().toString());
        values.put("password", pwInput.getText().toString());
        values.put("userName", nameInput.getText().toString());
        values.put("phoneNum", phoneNumInput.getText().toString());
        values.put("address", addressInput.getText().toString());
        db.insert("userTable", null, values);

        db.close();
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