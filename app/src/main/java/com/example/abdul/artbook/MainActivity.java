package com.example.abdul.artbook;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(  R.menu.add_art,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        ArrayList<String> artName = new ArrayList<String>();
        ArrayList<Bitmap> bitmapsArray = new ArrayList<Bitmap>();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,artName);
        listView.setAdapter(arrayAdapter);

        try{

            Main2Activity.database = this.openOrCreateDatabase("arts" , MODE_PRIVATE , null);
            Main2Activity.database.execSQL("CREATE TABLE IF NOT EXISTS arts (name VARCHAR, image BLOB)");

            Cursor cursor = Main2Activity.database.rawQuery("SELECT * FROM arts",null);
            int nameIx = cursor.getColumnIndex("name");
            int imageIx = cursor.getColumnIndex("image");
            cursor.moveToFirst();
            while (cursor != null){
                artName.add(cursor.getString(nameIx));
                byte[] bytes = cursor.getBlob(imageIx);
                Bitmap image = BitmapFactory.decodeResource(bytes,0,bytes.length);
                bitmapsArray.add(image);
                cursor.moveToNext();
                arrayAdapter.notifyDataSetChanged();


            }

        }catch (Exception e){ e.printStackTrace();}


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_art){
            Intent ıntent = new Intent(getApplicationContext(),Main2Activity.class);

            ıntent.putExtra("info","new");
            startActivity(ıntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
