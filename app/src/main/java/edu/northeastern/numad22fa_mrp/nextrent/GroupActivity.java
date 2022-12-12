package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.numad22fa_mrp.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {

    ImageButton addMember;
    TextView groupName;
    String groupId;

    ArrayList<SharedProperty> shareProps;
    SharedPropAdapter adapter;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        groupId = getIntent().getExtras().getString("groupId");

        groupName = findViewById(R.id.dispGroupName);

        shareProps = new ArrayList<>();

        rv = findViewById(R.id.sharedPropRV);
        adapter = new SharedPropAdapter(GroupActivity.this, shareProps, groupId);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        loadShared();


        addMember = findViewById(R.id.addMember);
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupActivity.this, AllMembersActivity.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);

            }
        });


    }

    private void loadShared() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("groups");
        db.child(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("title").getValue().toString();
                groupName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        db.child(groupId).child("sharedHouses")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        shareProps.clear();
                        Log.d("", snapshot.toString());
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String house = ds.child("houseId").getValue().toString();
                            String owner = ds.child("ownerId").getValue().toString();
                            String likes = ds.child("numLikes").getValue().toString();
                            String comments = ds.child("numComments").getValue().toString();

                            SharedProperty prop = new SharedProperty(house, owner, likes, comments);

                            shareProps.add(prop);
                            adapter.notifyItemInserted(shareProps.size() - 1);
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}