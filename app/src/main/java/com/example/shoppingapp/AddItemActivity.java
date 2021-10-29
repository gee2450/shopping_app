package com.example.shoppingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class AddItemActivity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                AddItem();
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
                    }
                }
            });

    private void AddItem() {
        MySQLite mDBHelpher = new MySQLite(this);

        SQLiteDatabase db = mDBHelpher.getWritableDatabase();

        EditText clothName = (EditText) findViewById(R.id.editTextAddItemName);
        EditText clothPrice = (EditText) findViewById(R.id.editTextAddItemPrice);

        Cursor cur = db.query("items", new String[]{"itemId"}, null, null, null, null, "itemId");
        cur.moveToLast();
        int biggestId = Integer.parseInt(cur.getString(0));
        Log.d("a", ""+biggestId);

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap itemImage = drawable.getBitmap();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        itemImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] bytes = baos.toByteArray();

        // ContentValues에 item 정보 담기
        ContentValues values = new ContentValues();
        values.put("itemId", biggestId+1);
        values.put("itemName", clothName.getText().toString());
        values.put("itemPrice", Integer.parseInt(clothPrice.getText().toString()));
        values.put("itemImage", bytes);

        db.insert("items", null, values);

        db.close();
        mDBHelpher.close();
    }

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