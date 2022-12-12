package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.numad22fa_mrp.R;

public class PropDetailsActivity extends AppCompatActivity {

    String homeID;
    String owner;

    TextView rent;
    TextView address;
    TextView type;
    TextView beds;
    TextView baths;
    TextView desc;
    ImageView img;
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prop_details);

        Intent intent = getIntent();
        homeID = intent.getStringExtra("houseId");
        owner = intent.getStringExtra("ownerId");

        rent = findViewById(R.id.rent);
        address = findViewById(R.id.addressDetail);
        beds = findViewById(R.id.bedDetails);
        type = findViewById(R.id.houseTypeDetail);
        baths = findViewById(R.id.bathroomsDetail);
        desc = findViewById(R.id.descDetails);
        img = findViewById(R.id.imgDetail);
        backBtn = findViewById(R.id.backButton);

        loadPropDetails();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadPropDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("houses");
        ref.child(owner).child(homeID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String rentInfo = snapshot.child("rentPerRoom").getValue().toString();
                        String addressInfo = snapshot.child("houseLocation").getValue().toString();
                        String stateInfo = snapshot.child("state").getValue().toString();
                        String countryInfo = snapshot.child("country").getValue().toString();
                        String imageInfo = snapshot.child("houseImage").getValue().toString();
                        String descriptionInfo = snapshot.child("houseDescription").getValue().toString();
                        String typeInfo = snapshot.child("type").getValue().toString();
                        String roomInfo = snapshot.child("noOfRoom").getValue().toString();

                        String addressFormat = addressInfo + ", " + stateInfo + ", " + countryInfo;

                        rent.setText("$" + rentInfo);
                        address.setText(addressFormat);
                        type.setText("Type: " + typeInfo);
                        beds.setText("Bedrooms: " + roomInfo);
                        baths.setText("Bathrooms: " + roomInfo);
                        desc.setText(descriptionInfo);
                        Glide.with(PropDetailsActivity.this).load(imageInfo).into(img);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}