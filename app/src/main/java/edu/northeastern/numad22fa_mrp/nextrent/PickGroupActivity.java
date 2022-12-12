package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.northeastern.numad22fa_mrp.R;

public class PickGroupActivity extends AppCompatActivity {

    RecyclerView rv;
    String owner, house, current;
    ArrayList<Group> groups;
    FavoriteGroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_group);

        rv = findViewById(R.id.allGroupsRecycler);
        owner = getIntent().getExtras().getString("owner");
        house = getIntent().getExtras().getString("house");
        current = getIntent().getExtras().getString("userKey");

        groups = new ArrayList<>();

        adapter = new FavoriteGroupAdapter(this, groups, house, owner);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);



        loadGroupOptions();
    }

    private void loadGroupOptions() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("groups");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groups.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    if(ds.child("members").child(current).exists()) {
                        String name = ds.child("title").getValue().toString();
                        String description = ds.child("description").getValue().toString();
                        String groupID = ds.child("groupId").getValue().toString();

                        Group g = new Group(groupID, name, description);
                        groups.add(g);
                        adapter.notifyItemInserted(groups.size() - 1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}