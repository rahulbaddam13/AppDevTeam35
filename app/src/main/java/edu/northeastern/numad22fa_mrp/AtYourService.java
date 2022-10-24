package edu.northeastern.numad22fa_mrp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class AtYourService extends AppCompatActivity {

    private Button my;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_your_service);
        my = findViewById(R.id.my);
        my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMy();
            }
        });

    }

    private void openMy() {
        Intent intent = new Intent(this, WeatherCustomLocation.class);
        startActivity(intent);
    }

    public void openWeather(View view) {
        int theId = view.getId();
        if (theId == R.id.weather) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
        }
    }

}