package edu.northeastern.numad22fa_mrp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import edu.northeastern.numad22fa_mrp.adapters.RVRetrofitAdapter;
import edu.northeastern.numad22fa_mrp.model.IPlaceHolder;
import edu.northeastern.numad22fa_mrp.model.Periods;
import edu.northeastern.numad22fa_mrp.model.WeatherHeader;
import edu.northeastern.numad22fa_mrp.model.WeatherInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private IPlaceHolder api;
    private String lat;
    private String longitude;
    RecyclerView recyclerView;
    private int x;
    private int y;
    private String code;
    private final static int REQUEST_CODE = 100;
    private static final String TAG = "Weather";
    RVRetrofitAdapter rvRetrofitAdapter;
    WeatherHeader header;
    WeatherInfo info;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for(Location location: locationResult.getLocations()){
                lat = String.format("%.4f", location.getLatitude());
                longitude = String.format("%.4f", location.getLongitude());
                Log.d(TAG, "onLocationResult: " + location.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = locationRequest.create();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weather.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(IPlaceHolder.class);

        getCity();
        recyclerView = findViewById(R.id.weatherRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvRetrofitAdapter = new RVRetrofitAdapter(this, info, header);
        recyclerView.setAdapter(rvRetrofitAdapter);
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(
                WeatherActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            checkSettingsAndStartLocationUpdates();
        } else {
            askLocationPermissions();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void checkSettingsAndStartLocationUpdates() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();

        SettingsClient client = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();

            }
        });
        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(WeatherActivity.this, 1001);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                    locationCallback, Looper.getMainLooper());
        }
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

    }

    private void askLocationPermissions() {
        // Check Permissions
        if (ContextCompat.checkSelfPermission(
                this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(WeatherActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission to access this feature")
                        .setPositiveButton("OK", (dialogInterface, i) -> ActivityCompat.requestPermissions(WeatherActivity.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                REQUEST_CODE))
                        .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(WeatherActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkSettingsAndStartLocationUpdates();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give location permission to access this feature")
                        .setPositiveButton("OK", (dialogInterface, i) -> ActivityCompat.requestPermissions(WeatherActivity.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                REQUEST_CODE))
                        .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                        .create()
                        .show();
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void getCity() {
//        Call<WeatherHeader> call = api.getLocation(lat + "," + longitude);

        Call<WeatherHeader> call = api.getLocation("40.5902,-74.3494");
        call.enqueue(new Callback<WeatherHeader>() {
            @Override
            public void onResponse(Call<WeatherHeader> call, Response<WeatherHeader> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "Call failed!" + response.code());
                    return;
                }

                Log.d(TAG, "Call Success!");
                header = response.body();
                x = header.getProperties().getGridX();
                y = header.getProperties().getGridY();
                code = header.getProperties().getCwa();
                getWeather();

                StringBuffer str = new StringBuffer();
                str.append("X:")
                        .append(header.getProperties().getGridX())
                        .append("\n")
                        .append("Y:")
                        .append(header.getProperties().getGridY())
                        .append("\n")
                        .append("City:")
                        .append(header.getProperties().getLocation().getProperties().getCity())
                        .append("\n");

                Log.d(TAG, str.toString());

            }

            @Override
            public void onFailure(Call<WeatherHeader> call, Throwable t) {
                Log.d(TAG, "Call failed!" + t.getMessage());
            }
        });



    }

    private void getWeather() {
       Call<WeatherInfo> call = api.getWeatherInfo(code,x,y);
        call.enqueue(new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "Call did not work!" + response.code());
                    return;
                }

                Log.d(TAG, "Call Success!");
                info = response.body();
                rvRetrofitAdapter.notifyDataSetChanged();

                Periods[] prop = info.getProperties().getPeriods();
                int num = 0;
                if (num < 13) {
                    for (int i = num; i < (num + 2); i++) {
                        StringBuffer str = new StringBuffer();
                        str.append("Name:")
                                .append(prop[i].getName())
                                .append("\n")
                                .append(prop[i].getTemperature());

                        Log.d(TAG, str.toString());
                    }
                } else {
                    prop[num].getName();
                    prop[num].getTemperature();
               }
          }

            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {
                Log.d(TAG, "Call failed!" + t.getMessage());
            }
        });
    }


}