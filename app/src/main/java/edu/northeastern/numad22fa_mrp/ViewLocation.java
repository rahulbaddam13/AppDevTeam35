package edu.northeastern.numad22fa_mrp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class ViewLocation extends AppCompatActivity {

    SupportMapFragment smf_googleMap;
    FusedLocationProviderClient client;

    private static final int REQUEST_LOCATION = 100;

    Double latitude;
    Double longitude;
    String houseId, noOfRoom, rentPerRoom, houseDescription, houseLocation, add;
    Double longi,lati;
    String lo,la;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);

        Intent intent = getIntent();
        houseId = intent.getStringExtra("houseId");
        noOfRoom = intent.getStringExtra("noOfRoom");
        rentPerRoom = intent.getStringExtra("rentPerRoom");
        houseDescription = intent.getStringExtra("houseDescription");
        houseLocation = intent.getStringExtra("houseLocation");
        add = intent.getStringExtra("address");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        smf_googleMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_maps);
        client = LocationServices.getFusedLocationProviderClient(this);
        getMyLocation();
        latitude = 76.78;
        longitude = 27.89;
//        lati = intent.getDoubleExtra("latitude",13.13);
//        longi = intent.getDoubleExtra("longitude",13.13);
        la = intent.getStringExtra("latitude");
        lo = intent.getStringExtra("longitude");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(ViewLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getMyLocation();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getMyLocation();
            }
        }
    }

    private void getMyLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                smf_googleMap.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        String address = add +" ,"+houseLocation;
                        lati = Double.valueOf(la);
                        longi = Double.valueOf(lo);
                        LatLng latLng = new LatLng(lati, longi);

                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Your destination is here..");

                        googleMap.addMarker(markerOptions);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 100));
                    }
                });

            }
        });
    }

}