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
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import edu.northeastern.numad22fa_mrp.adapters.RVRetrofitAdapter;
import edu.northeastern.numad22fa_mrp.model.IPlaceHolder;
import edu.northeastern.numad22fa_mrp.model.Periods;
import edu.northeastern.numad22fa_mrp.model.WeatherHeader;
import edu.northeastern.numad22fa_mrp.model.WeatherInfo;
import edu.northeastern.numad22fa_mrp.model.WeatherRecyclerViewItem;
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
    private WeatherHeader header;
    private WeatherInfo info;
    private int x;
    private int num;
    private long days;
    private int y;
    private String code;
    private final static int REQUEST_CODE = 100;
    String datetxt;
    private static final String TAG = "Weather";
    RVRetrofitAdapter rvRetrofitAdapter;
    ArrayList<WeatherRecyclerViewItem> itemList;
    ProgressBar progress;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            if (locationResult != null) {
                lat = String.format("%.4f", locationResult.getLocations().get(0).getLatitude());
                longitude = String.format("%.4f", locationResult.getLocations().get(0).getLongitude());
                Log.d(TAG, "onLocationResult: " + locationResult.getLocations().get(0).toString());

               getCity();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        datetxt = getIntent().getStringExtra("dateString");
        days = getIntent().getLongExtra("daysFuture", 0);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(WeatherActivity.this);
        locationRequest = locationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weather.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(IPlaceHolder.class);


        itemList = new ArrayList<>();
        progress = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.weatherRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvRetrofitAdapter = new RVRetrofitAdapter(this, itemList);
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
        Call<WeatherHeader> call = api.getLocation(lat + "," + longitude);
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

                StringBuffer info = new StringBuffer("X:").append(x)
                        .append("\n")
                        .append("Y:").append(y)
                        .append("\n")
                        .append("CWA:").append(code);

                Log.d(TAG, "onResponse: " + info);

                WeatherRecyclerViewItem loc = new WeatherRecyclerViewItem.Header(
                        WeatherRecyclerViewItem.LOCATION,
                        header.getProperties().getLocation().getProperties().getCity(),
                        header.getProperties().getLocation().getProperties().getState());
                if (itemList.isEmpty()) {
                    itemList.add(0, loc);
                    rvRetrofitAdapter.notifyItemInserted(0);
                }

                getDate();
            }

            @Override
            public void onFailure(Call<WeatherHeader> call, Throwable t) {
                Log.d(TAG, "Call failed!" + t.getMessage());
            }
        });



    }

    private void getDate() {
        WeatherRecyclerViewItem date = new WeatherRecyclerViewItem.Date(
                WeatherRecyclerViewItem.DATE,
                datetxt);
        if (itemList.size() == 1) {
            itemList.add(1, date);
            rvRetrofitAdapter.notifyItemInserted(1);
        }
        getWeather();
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
                LocalDate now = LocalDate.now();
                LocalDateTime start = now.atStartOfDay();
                LocalDateTime latest = start.plusHours(9);
                LocalDateTime current = LocalDateTime.now();

                Periods[] prop = info.getProperties().getPeriods();
                if (days == 0){
                    num = 0;
                }
                else if (prop[0].isDaytime()){
                    num = (int) (2 * (days));
                } else if (current.isAfter(start) && (current.isBefore(latest)) ) {
                    num = (int) (2 * (days)) + 1;
                } else {
                    num = (int)(2 * (days - 1)) + 1;
                }
                for (int i = num; i < (num + 2); i++) {
                        WeatherRecyclerViewItem data = new WeatherRecyclerViewItem.Period(
                                WeatherRecyclerViewItem.INFORMATION,
                                prop[i].getName(),
                                prop[i].getTemperature(),
                                prop[i].getIcon(),
                                prop[i].getDetailedForecast());
                        if (itemList.size() == 2 || itemList.size() == 3) {
                            itemList.add(data);
                            rvRetrofitAdapter.notifyItemInserted(itemList.size() - 1);
                        }
                }
                progress.setVisibility(View.GONE);
          }

            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {
                Log.d(TAG, "Call failed!" + t.getMessage());
            }
        });
    }


}