package com.example.oshan.memorableplaces;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    LatLng savedLatLng;
    String savedAddressText;
    Geocoder geocoder;
    Intent intent;
    LocationManager locationManager;
    LocationListener locationListener;
    Location prevLocation;

    String addressText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        intent = getIntent();
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            savedLatLng = (LatLng) intent.getExtras().get("addressLatLng");
            savedAddressText = (String) intent.getExtras().get("addressText");
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "Add new location", Toast.LENGTH_SHORT).show();
        }

        //        Get permission to access user location
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            String provider = LocationManager.GPS_PROVIDER;
            prevLocation = locationManager.getLastKnownLocation(provider);
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        if(savedLatLng ==null) {
            savedLatLng = new LatLng(-34, 151);
            savedAddressText = "Marker in Sydney";
        }
        mMap.addMarker(new MarkerOptions().position(savedLatLng).title(savedAddressText));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(savedLatLng,5));
        mMap.setOnMapClickListener(onMapClickListener);
        mMap.setOnMapLongClickListener(onMapLongClickListener);
    }

    GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            addMarkerOnMap(latLng);
        }
    };

    GoogleMap.OnMapLongClickListener onMapLongClickListener = new GoogleMap.OnMapLongClickListener() {
        @Override
        public void onMapLongClick(LatLng latLng) {
            addMarkerOnMap(latLng);
            goToMain(latLng);
        }
    };

    public void addMarkerOnMap(LatLng latLng){
        mMap.addMarker(new MarkerOptions().position(latLng).title("Your lat/lng is " + latLng.toString()));
    }

    public void goToMain(LatLng latLng) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("newLocLatLng", latLng);
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Log.i("Address", addressList.get(0).toString());
            addressText = addressList.get(0).getAddressLine(0);
            Log.i("Address", addressText);
            intent.putExtra("newAddress", addressText);
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "Location not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        startActivity(intent);
    }
}
