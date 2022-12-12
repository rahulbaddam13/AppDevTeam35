package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;

import edu.northeastern.numad22fa_mrp.R;
import edu.northeastern.numad22fa_mrp.UserProfileActivity;

public class ChatActivity extends AppCompatActivity {
    RecyclerView groupsRecyclerView;
    ArrayList<Group> groupList;
    FloatingActionButton button;

    GroupAdapter adapter;

    Bundle bundle = null;
    String userKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        groupList = new ArrayList<>();
        groupsRecyclerView = findViewById(R.id.recyclerView);

        bundle = getIntent().getExtras();
        userKey = bundle.getString("userKey");

        adapter = new GroupAdapter(this, groupList, userKey);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        groupsRecyclerView.setLayoutManager(layoutManager);
        groupsRecyclerView.setAdapter(adapter);


        loadGroups();

        button = findViewById(R.id.addGroup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent groups = new Intent(ChatActivity.this, GroupCreateActivity.class);
                groups.putExtra("userKey", userKey);
                startActivity(groups);
            }
        });

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

    private void loadGroups() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("groups");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    if(ds.child("members").child(userKey).exists()) {
                        String name = ds.child("title").getValue().toString();
                        String description = ds.child("description").getValue().toString();
                        String groupID = ds.child("groupId").getValue().toString();

                        Group g = new Group(groupID, name, description);
                        groupList.add(g);
                        adapter.notifyItemInserted(groupList.size() - 1);
                    }
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
        Intent clickIntent1 = new Intent(ChatActivity.this, PropertySeekerActivity.class);
        clickIntent1.putExtra("userKey", userKey);
        startActivity(clickIntent1);
        finish();
    }
}