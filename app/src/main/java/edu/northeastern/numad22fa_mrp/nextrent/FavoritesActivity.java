package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import edu.northeastern.numad22fa_mrp.R;

public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        //Bottom navigation bar.
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Choose home by default.
        bottomNavigationView.setSelectedItemId(R.id.page_favorites);
        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_home:
                        startActivity(new Intent(getApplicationContext(),PropertySeekerActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.page_favorites:
                        return true;
                    case R.id.page_chat:
                        //openFragment(SubscriptionsFragment.newInstance("",""));
                        return true;
                    case R.id.page_profile:
                        startActivity(new Intent(getApplicationContext(),SeekerProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(FavoritesActivity.this, FinalProject.class));
        finish();
    }
}