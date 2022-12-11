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

        adapter = new GroupAdapter(this, groupList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        groupsRecyclerView.setLayoutManager(layoutManager);
        groupsRecyclerView.setAdapter(adapter);

        loadGroups();

        button = findViewById(R.id.addGroup);
        button.setOnClickListener((view) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
            builder.setTitle("Create New Group");

            bundle = getIntent().getExtras();
            userKey = bundle.getString("userKey");

            View groupDialog = getLayoutInflater().inflate(R.layout.add_group_dialog, null);

            EditText eName = groupDialog.findViewById(R.id.addGroupName);
            EditText eDesc = groupDialog.findViewById(R.id.addGroupDescription);
            String gName = eName.getText().toString().trim();
            String gDesc = eDesc.getText().toString().trim();
            String time = String.valueOf(System.currentTimeMillis());



            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (TextUtils.isEmpty(gName)){
                        Toast.makeText(ChatActivity.this,
                                "Please enter group title...",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Group newGroup = new Group(time, gName, gDesc);
                     if(groupList.contains(newGroup)) {
                         Snackbar.make(view, "Not added: Duplicate entry", Snackbar.LENGTH_LONG)
                                 .setAction("Action",null).show();
                     }
                     else {
                         groupList.add(newGroup);
                         HashMap<String, String> groupMap = new HashMap<>();
                         groupMap.put("groupId", time);
                         groupMap.put("title", gName);
                         groupMap.put("description", gDesc);

                         DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
                         ref.child(time).setValue(groupMap)
                                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                                     @Override
                                     public void onSuccess(Void unused) {
                                         HashMap<String, String> memberMap = new HashMap<>();
                                         memberMap.put("uid", userKey);

                                         DatabaseReference memberRef = FirebaseDatabase.getInstance()
                                                 .getReference("groups");
                                         memberRef.child(time).child("members").child(userKey)
                                                 .setValue(memberMap)
                                                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                     @Override
                                                     public void onSuccess(Void unused) {

                                                     }
                                                 });

                                     }
                                          });
                         adapter.notifyItemInserted(groupList.size()-1);
                         Snackbar.make(view, "Not added: Duplicate entry", Snackbar.LENGTH_LONG)
                                 .setAction("Action",null).show();
                     }

                }
            });

            builder.setNegativeButton("Cancel", (dialog, i) -> {
                Snackbar.make(view, "Group was not added", Snackbar.LENGTH_LONG)
                        .setAction("Action",null).show();
            });

            builder.setView(groupDialog);
            AlertDialog dialog = builder.create();
            dialog.show();
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
                        startActivity(new Intent(getApplicationContext(),PropertySeekerActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.page_favorites:
                        startActivity(new Intent(getApplicationContext(),FavoritesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.page_chat:
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

    private void loadGroups() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("groups");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    if(ds.child("members").child(userKey).exists()) {
                        Group g = ds.getValue(Group.class);
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
        startActivity(new Intent(ChatActivity.this, FinalProject.class));
        finish();
    }
}