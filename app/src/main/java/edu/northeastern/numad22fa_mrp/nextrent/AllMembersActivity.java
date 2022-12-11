package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.numad22fa_mrp.R;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllMembersActivity extends AppCompatActivity {

    RecyclerView membersRV;
    String groupId;
    ArrayList<Member> members;
    ArrayList<String> currentMembers;
    ArrayList<Member> current;

    MemberAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_members);

        membersRV = findViewById(R.id.membersRecycler);
        groupId = getIntent().getExtras().getString("groupId");

        members = new ArrayList<>();
        currentMembers = new ArrayList<>();
        current = new ArrayList<>();
        membersRV = findViewById(R.id.membersRecycler);

        adapter = new MemberAdapter(this, members, groupId);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        membersRV.setLayoutManager(layoutManager);
        membersRV.setAdapter(adapter);



        loadCurrentMembers();

        loadMembers();
    }

    private void loadCurrentMembers() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("groups");
        db.child(groupId).child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentMembers.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    String id = ds.child("uid").getValue().toString();
                    currentMembers.add(id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadMembers() {
         DatabaseReference db = FirebaseDatabase.getInstance().getReference("seekers");
         db.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 members.clear();
                 for (DataSnapshot ds: snapshot.getChildren()){
                     String id = ds.child("uid").getValue().toString();
                     String name = ds.child("userName").getValue().toString();
                     if (!currentMembers.contains(id)){
                         Member mem = new Member(name, id);
                         members.add(mem);
                         adapter.notifyItemInserted(members.size()-1);
                     }

                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });

    }
}