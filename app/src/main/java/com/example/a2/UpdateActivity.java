package com.example.a2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UpdateActivity extends AppCompatActivity {

    EditText lat_input, lng_input;
    TextView adr_output;
    Button deleteBtn, updateBtn;

    String id, address, latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        lat_input = findViewById(R.id.latitude_input2);
        lng_input = findViewById(R.id.longitude_input2);
        adr_output = findViewById(R.id.address_output2);
        deleteBtn = findViewById(R.id.delete_btn);
        updateBtn = findViewById(R.id.add_to_db2);

        Geocoder geocoder;
        geocoder = new Geocoder(this);

        getAndSetData();

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Update Location");
        }

// same functionality as add activty. Get lng and lat and find new address. Check for erros
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                latitude = lat_input.getText().toString().trim();
                longitude = lng_input.getText().toString().trim();

                if (latitude.isEmpty() || longitude.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Longitude or Latitude cannot be empty!", Toast.LENGTH_SHORT).show();
                } else {

                    List<Address> addressList = null;
                    try {
                        addressList = geocoder.getFromLocation(Float.parseFloat(latitude), Float.parseFloat(longitude), 1);
                        String adr = addressList.get(0).getAddressLine(0);
                        String city = addressList.get(0).getLocality();
                        String state = addressList.get(0).getAdminArea();
                        String country = addressList.get(0).getCountryName();
                        adr_output.setText(adr + " " + city + " " + state + " " + country);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (IndexOutOfBoundsException e) {
                        Toast.makeText(getApplicationContext(), "Location not found, please check your coordinates!", Toast.LENGTH_SHORT).show();
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(getApplicationContext(), "Longitude or Latitude value is illegal!", Toast.LENGTH_SHORT).show();
                    }

                    address = adr_output.getText().toString().trim();
                    myDB.updateData(id, Float.parseFloat(latitude), Float.parseFloat(longitude), address);
                }
                try {
                    Thread.sleep(250);
                    Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                    startActivity(intent);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                confirmDialog();
            }
        });


    }

    // get and set our database value from intent for recycler view. if no data, toast will be displayed
    void getAndSetData() {

        if (getIntent().hasExtra("id") && getIntent().hasExtra("latitude") && getIntent().hasExtra("longitude") && getIntent().hasExtra("address")) {
            id = getIntent().getStringExtra("id");
            latitude = getIntent().getStringExtra("latitude");
            longitude = getIntent().getStringExtra("longitude");
            address = getIntent().getStringExtra("address");

            lat_input.setText(latitude);
            lng_input.setText(longitude);
            adr_output.setText(address);
        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }

    }

    //    Confirm dialoge to delete our entry. If User presses positive button, use deleteRow function
    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete this location?");
        builder.setMessage("Are you sure you want to delete this location from the database?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

}