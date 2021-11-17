package com.example.a2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;

    MyDatabaseHelper myDB;
    ArrayList<String> location_id;
    ArrayList<String> location_lat;
    ArrayList<String> location_lng;
    ArrayList<String> location_adr;
    ArrayList<Location> location;
    CustomAdapter customAdapter;

    EditText searchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        searchBar = findViewById(R.id.search);


        recyclerView = findViewById(R.id.recyclyerView);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(MainActivity.this);
        location_id = new ArrayList<>();
        location_lat = new ArrayList<>();
        location_lng = new ArrayList<>();
        location_adr = new ArrayList<>();
        location = new ArrayList<Location>();

        storeDataInArrays();

//        Recycle View uses adapter to display data
        customAdapter = new CustomAdapter(MainActivity.this, this, location);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

// Search bar, if text changed we call search locations function to display result data
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (location.size() != 0) {
                    customAdapter.searchLocations(s.toString());
                }
            }
        });

    }

    // refresh screen when Main activty is created, this is to view new database entries
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
            Log.d("Test", "Recreated");
        }
    }

    void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                location.add(new Location(cursor.getInt(0), cursor.getFloat(1), cursor.getFloat(2), cursor.getString(3)));
            }
        }
    }
}