package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import edu.northeastern.numad22fa_mrp.R;
import edu.northeastern.numad22fa_mrp.UserProfileActivity;

public class ChatActivity extends AppCompatActivity {

    Bundle bundle = null;
    String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        bundle = getIntent().getExtras();
        userKey = bundle.getString("userKey");

        //Bottom navigation bar.
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Choose home by default.
        bottomNavigationView.setSelectedItemId(R.id.page_chat);
        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_home:
                        Intent clickIntent1 = new Intent(ChatActivity.this, PropertySeekerActivity.class);
                        clickIntent1.putExtra("userKey", userKey);
                        startActivity(clickIntent1);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.page_favorites:
                        Intent clickIntent2 = new Intent(ChatActivity.this, FavoritesActivity.class);
                        clickIntent2.putExtra("userKey", userKey);
                        startActivity(clickIntent2);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.page_chat:
                        return true;
                    case R.id.page_profile:
                        Intent clickIntent3 = new Intent(ChatActivity.this, SeekerProfileActivity.class);
                        clickIntent3.putExtra("userKey", userKey);
                        startActivity(clickIntent3);
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
        startActivity(new Intent(ChatActivity.this, FinalProject.class));
        finish();
    }
}