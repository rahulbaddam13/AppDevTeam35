package edu.northeastern.numad22fa_mrp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

public class CheckAnimation extends AppCompatActivity {
    LottieAnimationView animationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_animation);
        animationView = findViewById(R.id.animation);
        animationView.playAnimation();
    }
}