package edu.northeastern.numad22fa_mrp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PropertyDetails extends AppCompatActivity {
    TextView tv_houseDesc;
    ImageView iv_houseImage;
    DatabaseReference reference;
    String _rooms, _rent, _location, _state, _country;
    Button updateB;
    Button _delete;
    TextView roomsTv, stateTv, countryTv, typeTv, locationTv, rent;
    String houseId, noOfRoom, rentPerRoom, houseDescription, houseLocation, country, type, state;
    Button bLocation;
    String lat,longi;
    Double latitude,longitude;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);

        bLocation= findViewById(R.id.getLocation);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String userId = firebaseUser.getUid();

        Intent intent = getIntent();
        houseId = intent.getStringExtra("houseId");
        noOfRoom = intent.getStringExtra("noOfRoom");
        rentPerRoom = intent.getStringExtra("rentPerRoom");
        houseDescription = intent.getStringExtra("houseDescription");
        houseLocation = intent.getStringExtra("houseLocation");
        String houseImage = intent.getStringExtra("houseImage");
        String user = intent.getStringExtra("userId");
        country = intent.getStringExtra("country");
        state = intent.getStringExtra("state");
        type = intent.getStringExtra("type");

        reference = FirebaseDatabase.getInstance().getReference().child(OwnerRegister.HOUSES).child(userId).child(houseId);
        tv_houseDesc = findViewById(R.id.tv_houseDesc);
        iv_houseImage = findViewById(R.id.iv_houseImage);
        updateB = findViewById(R.id.update);
        _delete = findViewById(R.id.delete);

        roomsTv = findViewById(R.id.roomsTv);
        rent = findViewById(R.id.rentTv);
        locationTv = findViewById(R.id.locationTv);
        stateTv = findViewById(R.id.stateTv);
        countryTv = findViewById(R.id.countryTv);
        typeTv = findViewById(R.id.typeTv);


        roomsTv.setText(noOfRoom);
        rent.setText(rentPerRoom);
        locationTv.setText(houseLocation);
        stateTv.setText(state);
        countryTv.setText(country);
        typeTv.setText(type);


        _rooms = noOfRoom;
        _rent = rentPerRoom;
        _location = houseLocation;
        _state = state;
        _country = country;

        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList;

        try {
            addressList= geocoder.getFromLocationName(houseLocation,1);
            if(addressList!=null) {
                latitude = addressList.get(0).getLatitude();
                longitude = addressList.get(0).getLongitude();
                //intent1.putExtra("houseImage", houseImage);
                tv_houseDesc.setText(String.valueOf(latitude) + " x " + String.valueOf(longitude));
            }

        }

        catch (IOException e) {
            e.printStackTrace();
        }
        catch (IndexOutOfBoundsException e) {
            bLocation.setVisibility(View.GONE);
            tv_houseDesc.setText("no location");
            Toast.makeText(this, "Location is Not Accurate to Track", Toast.LENGTH_SHORT).show();

        }

        //tv_houseDesc.setText(houseDescription);
        Glide.with(PropertyDetails.this).load(houseImage).into(iv_houseImage);

        updateB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();

            }
        });

        _delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProperty();
            }
        });

        bLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PropertyDetails.this, ViewLocation.class);
                intent1.putExtra("houseId", houseId);
                intent1.putExtra("noOfRoom", noOfRoom);
                intent1.putExtra("rentPerRoom", rentPerRoom);
                intent1.putExtra("houseDescription", houseDescription);
                intent1.putExtra("houseLocation", houseLocation);
                lat = String.valueOf(latitude);
                longi = String.valueOf(longitude);
                intent1.putExtra("latitude", lat);
                intent1.putExtra("longitude", longi);
                //intent1.putExtra("houseImage", houseImage);
                //intent1.putExtra("userId", userId);
                startActivity(intent1);
            }
        });

    }




    private void deleteProperty() {

        reference.setValue(null);
        Toast.makeText(this, "Property Deleted", Toast.LENGTH_SHORT).show();
        finish();
    }

//    private void openEdit() {
//
//        Intent i = new Intent(this, PropertyEdit.class);
////        i.putExtra("houseId")
////        i.putExtra("location",data);
////        //i.putExtra("unit",unit);
////        i.putExtra("state",sData);
////        i.putExtra("country",cData);
////        i.putExtra("type",pDta);
//        i.putExtra("Disc",houseDescription);
//        startActivity(i);
//        finish();
////        Intent intent = new Intent(this, PropertyEdit.class);
//
//    }


//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openEdit();
//            }
//        });


    //
    public void update() {

        if (isRoomsChanged() || isRentChanged() || isLocationChanged() || isStateChanged() || isCountryChanged()) {
            Toast.makeText(this, "Property Details updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PropertyList.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();

        } else {
            Toast.makeText(this, "No Update made", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isCountryChanged() {
        if (!_country.equals(countryTv.getText().toString())) {
            reference.child("country").setValue(countryTv.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isStateChanged() {
        if (!_state.equals(stateTv.getText().toString())) {
            reference.child("state").setValue(stateTv.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isLocationChanged() {
        if (!_location.equals(locationTv.getText().toString())) {
            reference.child("houseLocation").setValue(locationTv.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isRentChanged() {
        if (!_rent.equals(rent.getText().toString())) {
            reference.child("rentPerRoom").setValue(rent.getText().toString());
            return true;
        } else {
            return false;
        }

    }

    private boolean isRoomsChanged() {
        if (!_rooms.equals(roomsTv.getText().toString())) {
            reference.child("noOfRoom").setValue(roomsTv.getText().toString());
            return true;
        } else {
            return false;
        }

    }


}



    //
////        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
////        assert firebaseUser != null;
////        String userId = firebaseUser.getUid();
////
////
////        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(OwnerRegister.HOUSES).child(userId);
////        String key = databaseReference.getKey();
////        HashMap<String, String> hashMap = new HashMap<>();
////        hashMap.put("noOfRoom", str);
////        databaseReference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
////            @Override
////            public void onComplete(@NonNull Task<Void> task) {
////                if (task.isSuccessful()) {
////                    Toast.makeText(PropertyDetails.this, "Success", Toast.LENGTH_SHORT).show();
////                } else {
////                    Toast.makeText(PropertyDetails.this, "Failed", Toast.LENGTH_SHORT).show();
////                }
////            }
////        });
//
//    }
//

