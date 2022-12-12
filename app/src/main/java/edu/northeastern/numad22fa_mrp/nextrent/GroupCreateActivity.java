package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import edu.northeastern.numad22fa_mrp.R;

public class GroupCreateActivity extends AppCompatActivity {

    private EditText groupTitle, groupDesc;
    private FloatingActionButton check;
    String userKey;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        userKey = getIntent().getStringExtra("userKey");



        groupTitle = findViewById(R.id.addGroupName);
        groupDesc = findViewById(R.id.addGroupDescription);
        check = findViewById(R.id.createGroupBtn);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreatingGroup();
            }
        });

    }

    private void startCreatingGroup() {
        String titleName = groupTitle.getText().toString().trim();
        String desc = groupDesc.getText().toString().trim();
        String time = String.valueOf(System.currentTimeMillis());

        if (TextUtils.isEmpty(titleName)) {
            Toast.makeText(this, "Please enter group tilte...", Toast.LENGTH_SHORT).show();
            return;
        }
            HashMap<String, String> groupMap = new HashMap<>();
            groupMap.put("groupId", time);
            groupMap.put("title", titleName);
            groupMap.put("description", desc);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
            ref.child(time).setValue(groupMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("seekers");
                            db.child(userKey).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    name = snapshot.child("userName").getValue().toString();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            HashMap<String, String> memberMap = new HashMap<>();
                            memberMap.put("uid", userKey);
                            memberMap.put("userName", name);

                            DatabaseReference memberRef = FirebaseDatabase.getInstance()
                                    .getReference("groups");
                            memberRef.child(time).child("members").child(userKey)
                                    .setValue(memberMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(GroupCreateActivity.this,
                                                    "Group created", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        }
                    });
    }
}