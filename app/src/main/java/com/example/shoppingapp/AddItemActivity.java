package com.example.shoppingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class AddItemActivity extends AppCompatActivity {
    private ImageView imageView;

    ClothAdapter adapter;

    EditText clothName;
    EditText clothPrice;

    boolean isSelectImage = false;

    String dialogMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        adapter = HomeFragment.instance.adapter;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        clothName = (EditText) findViewById(R.id.editTextAddItemName);
        clothPrice = (EditText) findViewById(R.id.editTextAddItemPrice);

        imageView = (ImageView) findViewById(R.id.itemImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                registerLauncher.launch(intent);
            }
        });

        Button btnComplete = (Button) findViewById(R.id.btnAddItem);
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Check()) {
                    MakeDialog("Error");
                    return;
                }
                AddItem();
                finish();
            }
        });
    }

    ActivityResultLauncher<Intent> registerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        Uri uri = intent.getData();
                        imageView.setImageURI(uri);
                        isSelectImage = true;
                    }
                }
            });

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // android.R.id.home : toolbar ???back
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean Check() {
        if (!isSelectImage) {
            dialogMessage = "???????????? ??????????????????. ";
            return false;
        }
        else if (clothName.getText().toString().equals("")) {
            dialogMessage = "?????? ????????? ???????????????. ";
            return false;
        }
        else if (clothPrice.getText().toString().equals("")) {
            dialogMessage = "?????? ????????? ???????????????. ";
            return false;
        }
        return true;
    }

    private void MakeDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddItemActivity.this);
        builder.setTitle(title);
        builder.setMessage(dialogMessage);
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

    private void AddItem() {
        SQLiteDatabase db = MySQLite.instance.getWritableDatabase();

        // ?????? ??? itemId ???????????? ?????? SQLite?????? ?????? ????????? id ????????????
        Cursor cur = db.query("items", new String[]{"itemId"}, null, null, null, null, "itemId");
        cur.moveToLast();
        int biggestId = Integer.parseInt(cur.getString(0));
        Log.d("a", ""+biggestId);

        // ????????? ??????
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap itemImage = drawable.getBitmap();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        itemImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] bytes = baos.toByteArray();

        // ????????? ?????? ??????
        int newItemId = biggestId+1;
        String newItemName = clothName.getText().toString();
        int newItemPrice = Integer.parseInt(clothPrice.getText().toString());

        // ContentValues??? item ?????? ??????
        ContentValues values = new ContentValues();
        values.put("itemId", newItemId);
        values.put("itemName", newItemName);
        values.put("itemPrice", newItemPrice);
        values.put("itemImage", bytes);

        db.insert("items", null, values);

        db.close();

        adapter.addItem(new SampleData(newItemId, itemImage, newItemName, ""+newItemPrice));
        adapter.notifyDataSetChanged();
    }
}