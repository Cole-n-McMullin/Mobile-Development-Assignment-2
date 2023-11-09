package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import com.example.assignment2.database.DBMgr;

public class MainActivity extends AppCompatActivity {

    Button getCoordinatesButton;
    Button updateDatabaseButton;
    Button deleteDatabaseButton;
    EditText addressInput;
    TextView coordinatesOutput;

    private DBMgr dbMgr;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readFile();

        dbMgr = new DBMgr(this);
        dbMgr.open();

        getCoordinatesButton = findViewById(R.id.get_coordinates_button);
        addressInput = findViewById(R.id.address_input);
        coordinatesOutput = findViewById(R.id.codordinates_output);
        updateDatabaseButton = findViewById(R.id.update_database_button);
        deleteDatabaseButton = findViewById(R.id.delete_database_button);


        updateDatabaseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Cursor cursor = dbMgr.fetchAll();
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            dbMgr.delete(cursor.getInt(0));
                        } while (cursor.moveToNext());
                    }
                }
            }
        });

        deleteDatabaseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                readFile();
            }
        });

        getCoordinatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor cursor = dbMgr.fetch(addressInput.getText().toString());
                // if this record already exists in the database then display it to the
                if (cursor != null) {
                    if (cursor.moveToFirst()) {

                        Place place = new Place();
                        place.setId(cursor.getInt(0));
                        place.setAddress(cursor.getString(1));
                        place.setLatitude(cursor.getDouble(2));
                        place.setLongitude(cursor.getDouble(3));

                        coordinatesOutput.setText("Latitude: " + place.getLatitude() + " Longitude: " + place.getLongitude());
                    }
                }
                else {
                    coordinatesOutput.setText("Address not found");
                }
            }
        });
    }

    private void readFile()
    {
        String[] data;
        File addressFile = new File("assets/","canadaAddress.txt");
        try {
            FileInputStream fis = new FileInputStream(addressFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            // this assumes that the data is all contained in a single line and in csv format
            String strLine = br.readLine();
            data = strLine.split(",");

            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.CANADA);
            List<Address> addressList = null;

            for (String str : data) {

                addressList = geocoder.getFromLocationName(addressInput.getText().toString().replace(" ","+"),1);
                Address address = addressList.get(0);

                // create a place object with the data (I know it is not strictly necessary it just helps me keep things straight in my head)

                Cursor cursor = dbMgr.fetch(str);
                // if this record already exists in the database then update it
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            Place place = new Place();
                            place.setId(cursor.getInt(0));
                            place.setAddress(cursor.getString(1));
                            place.setLatitude(cursor.getDouble(2));
                            place.setLongitude(cursor.getDouble(3));

                            dbMgr.update(cursor.getInt(0), place.getAddress(), place.getLatitude(), place.getLongitude());
                            coordinatesOutput.setText("updated: Address: " + place.getAddress() + "Latitude: " + place.getLatitude() + " Longitude: " + place.getLongitude());

                        } while (cursor.moveToNext());
                    }
                }
                // otherwise insert a new record into the database
                else {
                    Place newPlace = new Place();
                    newPlace.setId(cursor.getInt(0));
                    newPlace.setAddress(str);
                    newPlace.setLatitude(address.getLatitude());
                    newPlace.setLongitude(address.getLongitude());
                    dbMgr.insert(newPlace);
                    coordinatesOutput.setText("inserted: Address: " + newPlace.getAddress() + "Latitude: " + newPlace.getLatitude() + " Longitude: " + newPlace.getLongitude());
                }

            }

            br.close();
            in.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}