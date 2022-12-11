package edu.northeastern.numad22fa_mrp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class PropertyList extends AppCompatActivity {

    private RecyclerView rv;
    private PropertyListAdapterOwner adapter;
    private ArrayList<Property> propertyList = new ArrayList<>();

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FloatingActionButton floating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_list);

        rv = findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PropertyList.this,LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        floating = findViewById(R.id.floatingAdd);
        getAllProperties();
        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PropertyList.this, AddPropertyAddress.class));
                overridePendingTransition( android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });

    }

    private void getAllProperties() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String userId = firebaseUser.getUid();
        firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child(OwnerRegister.HOUSES).child(userId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                propertyList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Property article = dataSnapshot.getValue(Property.class);
                    propertyList.add(article);
                    Collections.reverse(propertyList);

                }
                adapter = new PropertyListAdapterOwner(PropertyList.this, propertyList);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}