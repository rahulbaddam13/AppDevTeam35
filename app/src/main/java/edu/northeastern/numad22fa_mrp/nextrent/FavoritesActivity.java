package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.northeastern.numad22fa_mrp.R;

public class FavoritesActivity extends AppCompatActivity {

    private ArrayList<favProperty> favProps;
    private FavoritesAdapter adapter;
    private RecyclerView rv;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference db;

    Bundle bundle = null;
    String userKey;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        rv = findViewById(R.id.favRecycler);

        favProps = new ArrayList<>();
        adapter = new FavoritesAdapter(FavoritesActivity.this, favProps);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        bundle = getIntent().getExtras();
        userKey = bundle.getString("userKey");

        firebaseDatabase = FirebaseDatabase.getInstance();
        db = firebaseDatabase.getReference("");

        loadFavorites();



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
                        Intent intent = new Intent(getApplicationContext(),PropertySeekerActivity.class);
                        intent.putExtra("userKey", userKey);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.page_favorites:
                        return true;
                    case R.id.page_chat:
                        Intent chatIntent = new Intent(getApplicationContext(),ChatActivity.class);
                        chatIntent.putExtra("userKey", userKey);
                        startActivity(chatIntent);
                        overridePendingTransition(0,0);
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

    private void loadFavorites() {

        db = FirebaseDatabase.getInstance().getReference("seekers");
        db.child(userKey).child("favorites")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        favProps.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String house = ds.child("propID").getValue().toString();
                            String owner = ds.child("owner").getValue().toString();

                            favProperty prop = new favProperty();
                            prop.setHouseID(house);
                            prop.setUserID(owner);

                            favProps.add(prop);
                            adapter.notifyItemInserted(favProps.size() - 1);
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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