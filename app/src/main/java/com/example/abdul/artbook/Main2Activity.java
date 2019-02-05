package com.example.abdul.artbook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Main2Activity extends AppCompatActivity {

    ImageView ımageView;
    EditText editText;
    static SQLiteDatabase database;
    Bitmap selectBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ımageView = findViewById(R.id.imageView);
        editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.button);

        Intent bring = getIntent();

        if (bring.getStringExtra("info").equalsIgnoreCase("new")){
            button.setVisibility(View.VISIBLE);
        }else { button.setVisibility(View.INVISIBLE);}

    }

    public void  save(View view){

        String artName = editText.getText().toString();
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        selectBitmap.compress(Bitmap.CompressFormat.PNG,50,arrayOutputStream);
        byte[] bytesStrim = arrayOutputStream.toByteArray();

        try {

            database = this.openOrCreateDatabase("artbook",MODE_PRIVATE,null);
            database.execSQL(" CREATE TABLE IF NOT EXISTS arts (name VRCHAT,image BLOB)");

            String sqlString = "INSERT INTO arts (name,image) VALUES (? , ?) ";
            SQLiteStatement statement = database.compileStatement(sqlString);
            statement.bindString(1,artName);
            statement.bindBlob(2,bytesStrim);
            statement.execute();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public  void  select(View view){

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);

        }else {
            Intent ıntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(ıntent, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 2){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent ıntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(ıntent, 1);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1 && requestCode == RESULT_OK && data != null){

            Uri image = data.getData();

            try {
                selectBitmap  = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
                ımageView.setImageBitmap(selectBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
