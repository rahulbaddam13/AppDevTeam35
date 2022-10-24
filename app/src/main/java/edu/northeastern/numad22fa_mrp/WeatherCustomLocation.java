package edu.northeastern.numad22fa_mrp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import edu.northeastern.numad22fa_mrp.Retrofit.ApiClient;
import edu.northeastern.numad22fa_mrp.Retrofit.ApiInterface;
import edu.northeastern.numad22fa_mrp.Retrofit.Example;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherCustomLocation extends AppCompatActivity {
    ImageView search;
    TextView tempText, descText, humidityText;
    EditText textField;
    String temperature,humidity;
    ImageView imageView;
    static String Base_Url = "https://api.flickr.com/services/rest/";
    static String key = "032be1117fdb2f5cf13a0747160895d6";
    private int currentPhotoNumber = 0;
    private Button button;
    private ProgressBar progress;
    private ObjectAnimator progressAnimator;


    public static String httpResponse(URL url) throws IOException {
        StringBuilder jsonResult = new StringBuilder();

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        Log.e("WebActivity", "url: " + url.toString());
        InputStream inputStream = connection.getInputStream();
        Log.e("WebActivity", "inputstream: " + inputStream.toString());
        InputStreamReader reader = new InputStreamReader(inputStream);
        int data = reader.read();
        while (data!=-1) {
            jsonResult.append((char) data);
            data = reader.read();
        }
        Log.d("WebActivity", "json result from background" + jsonResult);

        return jsonResult.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_custom);

        search = findViewById(R.id.search);
        tempText = findViewById(R.id.tempText);
        descText = findViewById(R.id.descText);
        humidityText = findViewById(R.id.humidityText);
        textField = findViewById(R.id.textField);
        button = (Button) findViewById(R.id.yes);
        imageView = (ImageView) findViewById(R.id.imageWeather);
        button.setVisibility(View.INVISIBLE);

        progress = findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);


            search.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    getWeatherData(textField.getText().toString().trim());

                }
            });
        }


    private void getWeatherData(String name) {

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            try {
                Call<Example> call = apiInterface.getWeatherData(name);
                call.enqueue(new Callback<Example>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<Example> call, Response<Example> response) {
                        if (response.body() == null) {
                            Toast.makeText(WeatherCustomLocation.this, "Enter Valid Location", Toast.LENGTH_SHORT).show();

                        } else {
                            humidity = response.body().getMain().getHumidity();
                            temperature = response.body().getMain().getTemp();
                            tempText.setText("Temp" + " " + response.body().getMain().getTemp() + " C");
                            descText.setText("Feels Like" + " " + response.body().getMain().getFeels_like());
                            humidityText.setText("Humidity" + " " + response.body().getMain().getHumidity());
                            button.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onFailure(Call<Example> call, Throwable t) {
                        Toast.makeText(WeatherCustomLocation.this, "Enter Location", Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (NullPointerException e)
            {
                Toast.makeText(WeatherCustomLocation.this, "Enter Valid Location", Toast.LENGTH_SHORT).show();

            }

    }

    private String constructUrl(String url) {
        String urlTag;

        if (Float.parseFloat(temperature) < 15) {
                urlTag = "cold";
            }
            if (Float.parseFloat(temperature) > 25) {
                urlTag = "scorching";
            }
            if (Float.parseFloat(temperature) < 15) {
                urlTag = "shivery";
            }
        else{
            urlTag = "raining";
            }
        url += "?method=flickr.photos.search";
        url += "&api_key=" + key;
        url += "&tags=" + urlTag;
        url += "&per_page=50";
        url += "&format=json";
        url += "&nojsoncallback=1";
        Log.d("WebActivity", "Got url: " + url);
        return url;
    }

    private WebPhoto parsePhotoInfo(JSONObject jsonPhoto) {
        WebPhoto webPhoto = null;
        try {
            String id = jsonPhoto.getString("id");
            String farm_id = jsonPhoto.getString("farm");
            String server_id = jsonPhoto.getString("server");
            String secret = jsonPhoto.getString("secret");
            webPhoto = new WebPhoto(id, farm_id, server_id, secret);
        } catch (JSONException e) {
            Log.e("WebService","JSONException");
            e.printStackTrace();
        }
        return webPhoto;
    }

    public void clickWebService(View view) {
        progress.setVisibility(View.VISIBLE);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        List<WebPhoto> listOfPhotos = new ArrayList<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                JSONObject jObject = new JSONObject();
                try {
                    Log.e("WebActivity", Base_Url);
                    Base_Url = constructUrl(Base_Url);
                    URL url = new URL(Base_Url);
                    String res = httpResponse(url);
                    jObject = new JSONObject(res);
                } catch (MalformedURLException e) {
                    Log.e("WebService","MalformedURLException");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("WebService","IOException");
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.e("WebService","JSONException");
                    e.printStackTrace();
                }
                JSONObject finalJObject = jObject;
                // parse data from json
                try {
                    JSONObject photosObject = finalJObject.getJSONObject("photos");
                    JSONArray photoArray = photosObject.getJSONArray("photo");
                    // get each photo in this list
                    for (int i = 0; i < photoArray.length(); i++) {
                        JSONObject singlePhoto = photoArray.getJSONObject(i);
                        // parse a single photo info in helper method and add to list
                        WebPhoto webPhoto = parsePhotoInfo(singlePhoto);
                        if (webPhoto != null) {
                            listOfPhotos.add(webPhoto);
                        }
                    }
                    Log.d("WebActivity", "List of photos" + listOfPhotos.size());
                } catch (JSONException e) {
                    Log.e("WebService","JSONException");
                    e.printStackTrace();
                }
                if (listOfPhotos.size() < 2) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WeatherCustomLocation.this, "Enter Location!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    return;
                }
                Random random = new Random();
                currentPhotoNumber = random.nextInt(listOfPhotos.size());
                WebPhoto firstPhoto = listOfPhotos.get(currentPhotoNumber);
                currentPhotoNumber = (currentPhotoNumber + 1) % listOfPhotos.size();
                String formImageUrl = String.format("https://farm%s.staticflickr.com/%s/%s_%s.jpg",
                        firstPhoto.getFarm_id(), firstPhoto.getServer_id(), firstPhoto.getId(), firstPhoto.getSecret());

                Bitmap photoIcon = null;
                try {
                    InputStream in = new URL(formImageUrl).openStream();
                    photoIcon = BitmapFactory.decodeStream(in);
                } catch (IOException e) {
                    Log.e("WebService","IOException");
                    e.printStackTrace();
                }

                Bitmap finalPhotoIcon = photoIcon;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(finalPhotoIcon);
                        progress.setVisibility(View.INVISIBLE);;
                    }
                });
            }
        });
    }
    public void removeImg(){
        imageView.setImageDrawable(null);
    }
}