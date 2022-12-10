package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.northeastern.numad22fa_mrp.OwnerRegister;
import edu.northeastern.numad22fa_mrp.R;

public class SeekerProfileActivity extends AppCompatActivity {

    // creating a variable for Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for reference for Firebase.
    DatabaseReference databaseReference;

    //bundle with data from previous activity.
    Bundle bundle = null;
    String userKey;

    Preference currentUserPreference;

    private ImageView userAvatar;
    private TextView userFullName, userUserName;
    private EditText userEmailET, userPhoneET;
    private ChipGroup chipGroup;
    private CheckBox apartment, townhouse, condo, duplex;
    private RadioButton bed1, bed23, bed4;
    private RadioButton bath1, bath23, bath4;
    private Spinner minPrice, maxPrice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_profile);

        Context context = this;
        // instance of the Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // get reference for the database.
        databaseReference = firebaseDatabase.getReference("");

        bundle = getIntent().getExtras();
        userKey = bundle.getString("userKey");

        //Populate the fields
        userAvatar= (ImageView) findViewById(R.id.userProfileAvatar);
        userFullName = (TextView) findViewById(R.id.profileFullNameDisplay) ;
        userUserName = (TextView) findViewById(R.id.profileUserNameDisplay);
        userEmailET = (EditText) findViewById(R.id.profileEmailET);
        userPhoneET = (EditText) findViewById(R.id.profilePhoneET);
        chipGroup = (ChipGroup) findViewById(R.id.profileLocationCG);
        apartment = (CheckBox) findViewById(R.id.profile_apartment_chkbox);
        townhouse = (CheckBox) findViewById(R.id.profile_townhouse_chkbox);
        condo = (CheckBox) findViewById(R.id.profile_condo_chkbox);
        duplex = (CheckBox) findViewById(R.id.profile_duplex_chkbox);
        bed1 = (RadioButton) findViewById(R.id.profileOneBedroom);
        bed23 = (RadioButton) findViewById(R.id.profileTwoToThreeBedrooms);
        bed4 = (RadioButton) findViewById(R.id.profileMoreBedrooms);
        bath1 = (RadioButton) findViewById(R.id.profileOneBathroom);
        bath23 = (RadioButton) findViewById(R.id.profileTwoToThreeBathrooms);
        bath4 = (RadioButton) findViewById(R.id.profileMoreBathrooms);
        minPrice = (Spinner) findViewById(R.id.profileMinimumPriceSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.min_price_array, android.R.layout.simple_spinner_item);
        minPrice.setAdapter(adapter1);

        maxPrice = (Spinner) findViewById(R.id.profileMaximumPriceSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.max_price_array, android.R.layout.simple_spinner_item);
        maxPrice.setAdapter(adapter2);


        databaseReference.child("seekers").child(userKey).child("myPreference").addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUserPreference = snapshot.getValue(Preference.class);

                //userAvatar.setImageResource(Integer.parseInt(currentUserPreference.getAvatarID()));
                userFullName.setText(currentUserPreference.getFullName());
                //userUserName.setText();
                userEmailET.setText(currentUserPreference.getEmailID());
                userPhoneET.setText(currentUserPreference.getPhoneNumber());

                List<String> myLocs = currentUserPreference.getLocations();
                for(String loc : myLocs){
                    Chip chip = new Chip(context);
                    chip.setText(loc);
                    chip.setChipBackgroundColorResource(R.color.purple_200);
                    chip.setCloseIconVisible(true);
                    chip.setTextColor(getResources().getColor(R.color.white));

                    //add chip into the chip group
                    chipGroup.addView(chip);
                }

                List<String> type = currentUserPreference.getTypeOfHouse();
                for(String houseType : type){
                    switch (houseType){
                        case "Apartment":
                            apartment.setChecked(true);
                            break;
                        case "Townhouse":
                            townhouse.setChecked(true);
                            break;
                        case "Condo":
                            condo.setChecked(true);
                            break;
                        case "Duplex":
                            duplex.setChecked(true);
                            break;
                    }
                }

                String numBed = currentUserPreference.getNumberOfBedrooms();
                if("1".equalsIgnoreCase(numBed)){
                    bed1.setChecked(true);
                } else if("2 - 3".equalsIgnoreCase(numBed)){
                    bed23.setChecked(true);
                } else if("> 4".equalsIgnoreCase(numBed)){
                    bed4.setChecked(true);
                }

                String numBath = currentUserPreference.getNumberOfBathrooms();
                if("1".equalsIgnoreCase(numBath)){
                    bath1.setChecked(true);
                } else if("2 - 3".equalsIgnoreCase(numBath)){
                    bath23.setChecked(true);
                } else if("> 4".equalsIgnoreCase(numBath)){
                    bath4.setChecked(true);
                }

                minPrice.setSelection(currentUserPreference.getMinimumPrice()/200);
                if(currentUserPreference.getMaximumPrice() <= 3000){
                    maxPrice.setSelection((currentUserPreference.getMaximumPrice() - 2000)/200);
                } else {
                    maxPrice.setSelection((currentUserPreference.getMaximumPrice()/500)-1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });

        //Bottom navigation bar.
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Choose home by default.
        bottomNavigationView.setSelectedItemId(R.id.page_profile);
        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_home:
                        startActivity(new Intent(getApplicationContext(),PropertySeekerActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.page_favorites:
                        startActivity(new Intent(getApplicationContext(),FavoritesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.page_chat:
                        startActivity(new Intent(getApplicationContext(),ChatActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.page_profile:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(SeekerProfileActivity.this, FinalProject.class));
        finish();
    }
}