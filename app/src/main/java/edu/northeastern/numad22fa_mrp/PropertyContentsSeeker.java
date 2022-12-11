package edu.northeastern.numad22fa_mrp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;

public class PropertyContentsSeeker extends AppCompatActivity {

    TextView tv_houseDesc;
    ImageView iv_houseImage;
    DatabaseReference reference;
    TextView roomsTv,typeTv, rent,bathTv;
    String houseId, noOfRoom, rentPerRoom, houseDescription, houseLocation, country, type, state,address,baths;
    Button bLocation;
    String lat, longi;
    Double latitude, longitude;
    Boolean updateStatus;
    TextInputEditText stateTv, countryTv,locationTv,houseDesc,addressTv;
    Button contactOwner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_contents_seeker);

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
        address = intent.getStringExtra("address");
        baths = intent.getStringExtra("baths");



//        reference = FirebaseDatabase.getInstance().getReference().child(OwnerRegister.HOUSES).child(userId).child(houseId);
        iv_houseImage = findViewById(R.id.house_image_s);

        houseDesc = findViewById(R.id.houseDescription_s);
        roomsTv = findViewById(R.id.rooms_tv_s);
        rent = findViewById(R.id.rent_tv_s);
        locationTv = findViewById(R.id.location_tv_s);
        stateTv = findViewById(R.id.state_tv_s);
        countryTv = findViewById(R.id.country_tv_s);
        typeTv = findViewById(R.id.type_tv_s);
        bLocation = findViewById(R.id.getLoc_s);
        contactOwner = findViewById(R.id.contactOwner);
        addressTv = findViewById(R.id.address_tv_s);
        bathTv = findViewById(R.id.bath_tv_s);

        locationTv.setEnabled(false);
        countryTv.setEnabled(false);
        stateTv.setEnabled(false);
        houseDesc.setEnabled(false);
        addressTv.setEnabled(false);



        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(address + "," + houseLocation, 1);
            if (addressList != null) {
                latitude = addressList.get(0).getLatitude();
                longitude = addressList.get(0).getLongitude();
                //intent1.putExtra("houseImage", houseImage);
                //tv_houseDesc.setText(String.valueOf(latitude) + " x " + String.valueOf(longitude));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            bLocation.setVisibility(View.GONE);
            //tv_houseDesc.setText("no location");
            Toast.makeText(this, "Location is Not Accurate to Track", Toast.LENGTH_SHORT).show();

        }


        Glide.with(PropertyContentsSeeker.this).load(houseImage).into(iv_houseImage);

        roomsTv.setText(noOfRoom);
        rent.setText(rentPerRoom);
        locationTv.setText(houseLocation);
        stateTv.setText(state);
        countryTv.setText(country);
        typeTv.setText(type);
        houseDesc.setText(houseDescription);
        addressTv.setText(address);
        bathTv.setText(baths);

        bLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PropertyContentsSeeker.this, ViewLocation.class);
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

        contactOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(PropertyContentsSeeker.this, ContactOwner.class);
                intent2.putExtra("houseId", houseId);
                intent2.putExtra("houseDescription", houseDescription);
                intent2.putExtra("houseLocation", houseLocation);
                intent2.putExtra("address",address);
                startActivity(intent2);

            }
        });

    }


}