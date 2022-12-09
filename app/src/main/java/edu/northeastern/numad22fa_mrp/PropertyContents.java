package edu.northeastern.numad22fa_mrp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
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

public class PropertyContents extends AppCompatActivity {

    TextView tv_houseDesc;
    ImageView iv_houseImage;
    DatabaseReference reference;
    String _rooms, _rent, _location, _state, _country;
    Button updateB;
    Button _delete;
    TextView roomsTv,typeTv, rent;
    String houseId, noOfRoom, rentPerRoom, houseDescription, houseLocation, country, type, state;
    Button bLocation;
    String lat, longi;
    Double latitude, longitude;
    Boolean updateStatus;
    TextInputEditText stateTv, countryTv,locationTv,houseDesc;
    LinearLayout updateDelete,cards,locationLinear;
    Button editButton;
    TextInputLayout roomLayout, rentLayout, typeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_contents);

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
        tv_houseDesc = findViewById(R.id.houseDescription);
        iv_houseImage = findViewById(R.id.house_image);
        updateB = findViewById(R.id.editContents);
        _delete = findViewById(R.id.deleteContents);

        houseDesc = findViewById(R.id.houseDescription);
        roomsTv = findViewById(R.id.rooms_tv);
        rent = findViewById(R.id.rent_tv);
        locationTv = findViewById(R.id.location_tv);
        stateTv = findViewById(R.id.state_tv);
        countryTv = findViewById(R.id.country_tv);
        typeTv = findViewById(R.id.type_tv);


        updateDelete = findViewById(R.id.updateDeleteLinear);
        cards = findViewById(R.id.cardLinear);
        locationLinear = findViewById(R.id.locationLinear);
        roomLayout = findViewById(R.id.roomLayout);
        rentLayout = findViewById(R.id.rentLayout);
        typeLayout = findViewById(R.id.typeLayout);

        roomLayout.setVisibility(View.GONE);
        rentLayout.setVisibility(View.GONE);
        typeLayout.setVisibility(View.GONE);
        updateDelete.setVisibility(View.GONE);

        bLocation = findViewById(R.id.getLoc);
        editButton = findViewById(R.id.editButton);

        roomsTv.setText(noOfRoom);
        rent.setText(rentPerRoom);
        locationTv.setText(houseLocation);
        stateTv.setText(state);
        countryTv.setText(country);
        typeTv.setText(type);
        tv_houseDesc.setText(houseDescription);

        locationTv.setEnabled(false);
        countryTv.setEnabled(false);
        stateTv.setEnabled(false);
        tv_houseDesc.setEnabled(false);


        _rooms = noOfRoom;
        _rent = rentPerRoom;
        _location = houseLocation;
        _state = state;
        _country = country;

        updateStatus = false;

        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(houseLocation, 1);
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

        }try {
            addressList = geocoder.getFromLocationName(houseLocation, 1);
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

        Glide.with(PropertyContents.this).load(houseImage).into(iv_houseImage);

        updateB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
                updateStatusCheck();

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
                Intent intent1 = new Intent(PropertyContents.this, ViewLocation.class);
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

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationTv.setEnabled(true);
                countryTv.setEnabled(true);
                stateTv.setEnabled(true);
                tv_houseDesc.setEnabled(true);
                cards.setVisibility(View.GONE);
                locationLinear.setVisibility(View.GONE);
                updateDelete.setVisibility(View.VISIBLE);
                roomLayout.setVisibility(View.VISIBLE);
                rentLayout.setVisibility(View.VISIBLE);
                typeLayout.setVisibility(View.VISIBLE);
            }
        });

    }

    private void deleteProperty() {

        reference.setValue(null);
        Toast.makeText(this, "Property Deleted", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void update() {

        if (!_country.equals(countryTv.getText().toString())) {
            reference.child("country").setValue(countryTv.getText().toString());
            updateStatus = true;

        }
        if (!_state.equals(stateTv.getText().toString())) {
            reference.child("state").setValue(stateTv.getText().toString());
            updateStatus = true;
        }
        if (!_location.equals(locationTv.getText().toString())) {
            reference.child("houseLocation").setValue(locationTv.getText().toString());
            updateStatus = true;
        }
        if (!_rent.equals(rent.getText().toString())) {
            reference.child("rentPerRoom").setValue(rent.getText().toString());
            updateStatus = true;
        }
        if (!_rooms.equals(roomsTv.getText().toString())) {
            reference.child("noOfRoom").setValue(roomsTv.getText().toString());
            updateStatus = true;
        }




    }

    private void updateStatusCheck() {
        if (updateStatus) {
            Toast.makeText(this, "Property Details updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PropertyList.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        } else {
            Toast.makeText(this, "No Update made", Toast.LENGTH_SHORT).show();

        }
    }

}
