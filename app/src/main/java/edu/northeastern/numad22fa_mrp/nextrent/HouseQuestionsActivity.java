package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.api.SystemParameterOrBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.northeastern.numad22fa_mrp.MessageActivity;
import edu.northeastern.numad22fa_mrp.OwnerRegister;
import edu.northeastern.numad22fa_mrp.R;

public class HouseQuestionsActivity extends AppCompatActivity {

    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    private EditText location;
    private ChipGroup chipGroup;
    private Button addLocation;
    private CheckBox apartment, townhouse, condo, duplex;
    private String numOfBeds, numOfBaths;
    private String minPrice, maxPrice;

    private List<String> myPreferredLocations;
    private List<String> myPreferredHouseType;

    //bundle with data from previous activity.
    Bundle bundle = null;
    String userKey, avatarId, seekerFullName, seekerEmailId, seekerPhone, legalSex, age;

    // creating a variable for Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for reference for Firebase.
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_questions);

        // instance of the Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // get reference for the database.
        databaseReference = firebaseDatabase.getReference("");

        //Get data from previous.
        bundle = getIntent().getExtras();
        userKey = bundle.getString("userKey");
        avatarId = bundle.getString("avatarId");
        seekerFullName = bundle.getString("seekerFullName");
        seekerEmailId = bundle.getString("seekerEmailId");
        seekerPhone = bundle.getString("seekerPhone");
        legalSex = bundle.getString("legalSex");
        age = bundle.getString("age");
        if(age == null){
            age = "18";
        }


        location = (EditText) findViewById(R.id.editTextTextLocation);
        addLocation = (Button) findViewById(R.id.addLocationBtn);
        chipGroup = findViewById(R.id.locationChipGroup);

        apartment = (CheckBox)findViewById(R.id.apartment_chkbox);
        townhouse = (CheckBox)findViewById(R.id.townhouse_chkbox);
        condo = (CheckBox)findViewById(R.id.condo_chkbox);
        duplex = (CheckBox)findViewById(R.id.duplex_chkbox);

        RadioGroup numberOfBedsRG  = (RadioGroup) findViewById(R.id.bedroomRadioGroup);
        numberOfBedsRG.setOnCheckedChangeListener((group, checkedId) -> {
            // do operations specific to this selection
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            numOfBeds = radioButton.getText().toString();
        });

        RadioGroup numberOfBathsRG = (RadioGroup) findViewById(R.id.bathroomRadioGroup);
        numberOfBathsRG.setOnCheckedChangeListener((group, checkedId) -> {
            // do operations specific to this selection
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            numOfBaths = radioButton.getText().toString();
        });

        Spinner minimumPriceSpinner = (Spinner) findViewById(R.id.minimumPriceSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.min_price_array, android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        minimumPriceSpinner.setAdapter(adapter1);
        minimumPriceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                minPrice = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                minPrice = "0";
            }
        });

        Spinner maximumPriceSpinner = (Spinner) findViewById(R.id.maximumPriceSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.max_price_array, android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        maximumPriceSpinner.setAdapter(adapter2);
        maximumPriceSpinner.setSelection(9);
        maximumPriceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                maxPrice = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                maxPrice = "5000";
            }
        });

            myPreferredLocations = new ArrayList<>();
            myPreferredHouseType = new ArrayList<>();
    }

    /**
     * Method that is invoked on click of add location button, adds chips to the chip group.
     * @param view current view.
     */
    public void addChipToGroup(View view){

        String loc = location.getText().toString();

        Chip chip = new Chip(this);
        chip.setText(loc);
        chip.setChipBackgroundColorResource(R.color.purple_200);
        chip.setCloseIconVisible(true);
        chip.setTextColor(getResources().getColor(R.color.white));

        //add chip into the chip group
        chipGroup.addView(chip);
        myPreferredLocations.add(location.getText().toString());

        //on close of each chip
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPreferredLocations.remove(loc);
                chipGroup.removeView(chip);
            }
        });

    }

    /**
     * Method called on submitting the preference.
     * @param view current view.
     */
    public void submitPreferenceBtn(View view){

        //Determine the checkboxes checked for type of house
        if(apartment.isChecked()){
            myPreferredHouseType.add("Apartment");
        }
        if(townhouse.isChecked()){
            myPreferredHouseType.add("Townhouse");
        }
        if(condo.isChecked()){
            myPreferredHouseType.add("Condo");
        }
        if(duplex.isChecked()){
            myPreferredHouseType.add("Duplex");
        }

        //Create preference object.
        Preference myPref = new Preference(avatarId, seekerFullName, seekerEmailId, seekerPhone, legalSex,
                Integer.parseInt(age), myPreferredLocations, myPreferredHouseType,
                numOfBeds, numOfBaths, Integer.parseInt(minPrice.substring(1)), Integer.parseInt(maxPrice.substring(1)));

        //Update preference.
        databaseReference.child("seekers").child(userKey).child("myPreference").setValue(myPref).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HouseQuestionsActivity.this, "Unable to update preference. Please try again later", Toast.LENGTH_SHORT).show();
            }
        });

        Intent clickIntent = new Intent(HouseQuestionsActivity.this, PropertySeekerActivity.class);
        clickIntent.putExtra("userKey", userKey);
        startActivity(clickIntent);

    }

}