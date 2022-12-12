package edu.northeastern.numad22fa_mrp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    String _rooms, _rent, _location, _state, _country,_address,_type,_baths,_houseDec;
    Button updateB;
    Button _delete;
    TextView roomsTv,typeTv, rent,bathTv;
    String houseId, noOfRoom, rentPerRoom, houseDescription, houseLocation, country, type, state,address,baths;
    Button bLocation;
    String lat, longi;
    Double latitude, longitude;
    Boolean updateStatus;
    TextInputEditText stateTv, countryTv,locationTv,houseDesc,addressTv;
    TextInputEditText room_t,rent_t,type_t,bath_t;
    LinearLayout updateDelete,cards,locationLinear;
    Button editButton;
    TextInputLayout roomLayout, rentLayout, typeLayout,bathLayout;

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
        address = intent.getStringExtra("address");
        baths = intent.getStringExtra("baths");


        reference = FirebaseDatabase.getInstance().getReference().child(OwnerRegister.HOUSES).child(userId).child(houseId);
        tv_houseDesc = findViewById(R.id.houseDescription);
        iv_houseImage = findViewById(R.id.house_image);
        updateB = findViewById(R.id.editContents);
        _delete = findViewById(R.id.deleteContents);

//        houseDesc = findViewById(R.id.houseDescription);
        roomsTv = findViewById(R.id.rooms_tv);
        rent = findViewById(R.id.rent_tv);
        locationTv = findViewById(R.id.location_tv);
        stateTv = findViewById(R.id.state_tv);
        countryTv = findViewById(R.id.country_tv);
        typeTv = findViewById(R.id.type_tv);
        addressTv = findViewById(R.id.address_tv);
        bathTv = findViewById(R.id.bath_tv);
        room_t = findViewById(R.id.rooms_t);
        rent_t = findViewById(R.id.rent_t);
        type_t = findViewById(R.id.type_t);
        bath_t = findViewById(R.id.bath_t);


        updateDelete = findViewById(R.id.updateDeleteLinear);
        cards = findViewById(R.id.cardLinear);
        locationLinear = findViewById(R.id.locationLinear);
        roomLayout = findViewById(R.id.roomLayout);
        rentLayout = findViewById(R.id.rentLayout);
        typeLayout = findViewById(R.id.typeLayout);
        bathLayout =findViewById(R.id.bathLayout);

        roomLayout.setVisibility(View.GONE);
        rentLayout.setVisibility(View.GONE);
        typeLayout.setVisibility(View.GONE);
        updateDelete.setVisibility(View.GONE);
        bathLayout.setVisibility(View.GONE);

        bLocation = findViewById(R.id.getLoc);
        editButton = findViewById(R.id.editButton);

        roomsTv.setText(noOfRoom);
        rent.setText(rentPerRoom);
        locationTv.setText(houseLocation);
        stateTv.setText(state);
        countryTv.setText(country);
        typeTv.setText(type);
        tv_houseDesc.setText(houseDescription);
        addressTv.setText(address);
        bathTv.setText(baths);

        room_t.setText(noOfRoom);
        rent_t.setText("$" + rentPerRoom);
        type_t.setText(type);
        bath_t.setText(baths);

        locationTv.setEnabled(false);
        countryTv.setEnabled(false);
        stateTv.setEnabled(false);
        tv_houseDesc.setEnabled(false);
        addressTv.setEnabled(false);


        _rooms = noOfRoom;
        _rent = rentPerRoom;
        _location = houseLocation;
        _state = state;
        _country = country;
        _address = address;
        _type = type;
        _baths = baths;
        _houseDec = houseDescription;

        updateStatus = false;

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
//        try {
//            addressList = geocoder.getFromLocationName(houseLocation, 1);
//            if (addressList != null) {
//                latitude = addressList.get(0).getLatitude();
//                longitude = addressList.get(0).getLongitude();
//                //intent1.putExtra("houseImage", houseImage);
//                //tv_houseDesc.setText(String.valueOf(latitude) + " x " + String.valueOf(longitude));
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (IndexOutOfBoundsException e) {
//            bLocation.setVisibility(View.GONE);
//            //tv_houseDesc.setText("no location");
//            Toast.makeText(this, "Location is Not Accurate to Track", Toast.LENGTH_SHORT).show();
//
//        }

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
                intent1.putExtra("address", address);
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
                addressTv.setEnabled(true);
                cards.setVisibility(View.GONE);
                locationLinear.setVisibility(View.GONE);
                updateDelete.setVisibility(View.VISIBLE);
                roomLayout.setVisibility(View.VISIBLE);
                rentLayout.setVisibility(View.VISIBLE);
                typeLayout.setVisibility(View.VISIBLE);
                bathLayout.setVisibility(View.VISIBLE);
            }
        });

    }

    private void deleteProperty() {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("Delete?");
        build.setMessage("Do you wanna delete property")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reference.setValue(null);
                        Toast.makeText(PropertyContents.this, "Property Deleted", Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = build.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        int width = (int) (getResources().getDisplayMetrics().widthPixels*0.7);
        int height = (int) (getResources().getDisplayMetrics().heightPixels*0.32);
        alertDialog.getWindow().setLayout(width,height);

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
        if (!_rent.equals(rent_t.getText().toString())) {
            reference.child("rentPerRoom").setValue(rent_t.getText().toString());
            updateStatus = true;
        }
        if (!_rooms.equals(room_t.getText().toString())) {
            reference.child("noOfRoom").setValue(room_t.getText().toString());
            updateStatus = true;
        }
        if (!_address.equals(addressTv.getText().toString())) {
            reference.child("address").setValue(addressTv.getText().toString());
            updateStatus = true;
        }
        if (!_type.equals(type_t.getText().toString())) {
            reference.child("type").setValue(type_t.getText().toString());
            updateStatus = true;
        }
        if (!_baths.equals(bath_t.getText().toString())) {
            reference.child("baths").setValue(bath_t.getText().toString());
            updateStatus = true;
        }
        if (!_houseDec.equals(tv_houseDesc.getText().toString())) {
            reference.child("houseDescription").setValue(tv_houseDesc.getText().toString());
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
