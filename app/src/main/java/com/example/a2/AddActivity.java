package com.example.a2;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    Button searchBtn, addBtn;
    EditText longitude, latitude;
    TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        longitude = findViewById(R.id.longitude_input);
        latitude = findViewById(R.id.latitude_input);
        address = findViewById(R.id.address_output);
        searchBtn = findViewById(R.id.check_location);
        addBtn = findViewById(R.id.add_to_db);
//Geo coder object
        Geocoder geocoder;
        geocoder = new Geocoder(this);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String lng_input = longitude.getText().toString();
                String lat_input = latitude.getText().toString();

                if (lng_input.isEmpty() || lat_input.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Longitude or Latitude cannot be empty!", Toast.LENGTH_SHORT).show();
                } else {

                    float lng = Float.parseFloat(lng_input);
                    float lat = Float.parseFloat(lat_input);

                    try {
// use geocoder to get address. Set to variables and concatenate
                        List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
                        String adr = addressList.get(0).getAddressLine(0);
                        String city = addressList.get(0).getLocality();
                        String state = addressList.get(0).getAdminArea();
                        String country = addressList.get(0).getCountryName();

                        address.setText(adr + " " + city + " " + state + " " + country);
// check for erros. -180<Latitude<180 , -90<Longitude<90
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("error", String.valueOf(e));
                    } catch (IndexOutOfBoundsException e) {
                        Toast.makeText(getApplicationContext(), "Location not found, please check your coordinates!", Toast.LENGTH_SHORT).show();
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(getApplicationContext(), "Longitude or Latitude value is illegal!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

//        Try and catch for adding entry Need to make sure values are legal
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lng_input = longitude.getText().toString();
                String lat_input = latitude.getText().toString();
                String adr = address.getText().toString().trim();

                if (adr.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Address is empty, Search for a new address", Toast.LENGTH_SHORT).show();

                } else {
                    float lng = Float.parseFloat(lng_input);
                    float lat = Float.parseFloat(lat_input);
                    adr = address.getText().toString().trim();
                    MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                    myDB.addLocation(lat, lng, adr);

                }
//navigate back to main screen after adding
                try {
                    Thread.sleep(250);
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}