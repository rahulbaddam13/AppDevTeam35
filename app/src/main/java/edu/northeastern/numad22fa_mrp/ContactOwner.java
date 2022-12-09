package edu.northeastern.numad22fa_mrp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactOwner extends AppCompatActivity {

    TextView ownerName,ownerEmail,description;
    String houseId,houseDescription;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_owner);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String userId = firebaseUser.getUid();
//        reference = FirebaseDatabase.getInstance().getReference().child("houses").child(userId).child(houseId);
//        Log.v("Rahul",reference.toString());
        Intent intent = getIntent();
        houseId = intent.getStringExtra("houseId");
        houseDescription = intent.getStringExtra("houseDescription");
        Log.v("RahulD",houseDescription);
        ownerName = findViewById(R.id.ownerName);
        ownerName.setText(houseId);



    }
}