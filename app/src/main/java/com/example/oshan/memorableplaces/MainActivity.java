package com.example.oshan.memorableplaces;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView placesListView;
    ArrayList<String> placeNames = new ArrayList<>();
    ArrayList<LatLng> savedLocations = new ArrayList<>();
    Intent mapsActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        add a header to the list view. this add extra space on top which refines the look of the app.
        addListViewHeader();

//        add array adapter to the list view. We only have on item at the start.
//        as user saves more location we keep adding more items
        placeNames.add("add a favorite place");

        Context context = getApplicationContext();

//        get intent when returning from maps.
        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            Bundle inputFromMap = intent.getExtras();
            savedLocations = (ArrayList<LatLng>) inputFromMap.get("newLocLatLng");
            placeNames = (ArrayList<String>) inputFromMap.get("newAddress");
            intent.replaceExtras(new Bundle());
            intent.setAction("");
            intent.setData(null);
            intent.setFlags(0);
        }


        ArrayAdapter placesAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, placeNames);
        placesListView.setAdapter(placesAdapter);

        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToMaps(i);
            }
        });

    }

    public void goToMaps(int idx){
//        Log.i("item idx", Integer.toString(idx));
        mapsActivityIntent = new Intent(getApplicationContext(),MapsActivity.class);
//        if(idx !=1) {
        Log.i("index, lengthPlaces, lenglocations", Integer.toString(idx) + Integer.toString(placeNames.size()) + Integer.toString(savedLocations.size()));
        mapsActivityIntent.putExtra("addressText", placeNames);
        mapsActivityIntent.putExtra("addressLatLng", savedLocations);
        mapsActivityIntent.putExtra("idx", idx);
//        }
        startActivity(mapsActivityIntent);
    }

    public void addListViewHeader(){
        placesListView = findViewById(R.id.placesListView);
        TextView headerTextView;
        headerTextView = new TextView(this);
        headerTextView.setText("Your saved places \n");
        headerTextView.setTextSize(22);
        headerTextView.setClickable(true);
        headerTextView.setAllCaps(true);
        placesListView.addHeaderView(headerTextView);
    }

}

