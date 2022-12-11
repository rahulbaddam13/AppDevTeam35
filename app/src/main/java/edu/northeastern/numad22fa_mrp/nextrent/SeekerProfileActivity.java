package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
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
    List<String> popupLocList;
    List<String> finalPropList;
    private List<String> myPreferredHouseType;


    private ImageView userAvatar;
    private TextView userFullName, userUserName;
    private EditText userEmailET, userPhoneET, popupLocationET;
    private ChipGroup chipGroup, popupChipGroup;
    private CheckBox apartment, townhouse, condo, duplex;
    private RadioButton bed1, bed23, bed4;
    private RadioButton bath1, bath23, bath4;
    private Spinner minPrice, maxPrice;
    private String updatedMinPrice, updatedMaxPrice;
    private Button profileUpdateBtn;
    private String numOfBeds, numOfBaths;
    Context context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_profile);

        // instance of the Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // get reference for the database.
        databaseReference = firebaseDatabase.getReference("");

        bundle = getIntent().getExtras();
        userKey = bundle.getString("userKey");

        //Populate the fields
        //userAvatar= (ImageView) findViewById(R.id.userProfileAvatar);
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
        minPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updatedMinPrice = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        maxPrice = (Spinner) findViewById(R.id.profileMaximumPriceSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.max_price_array, android.R.layout.simple_spinner_item);
        maxPrice.setAdapter(adapter2);

        maxPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updatedMaxPrice = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //get number of bedroom and get number of bathroom.
        RadioGroup numberOfBedsRG  = (RadioGroup) findViewById(R.id.profileBedroomRadioGroup);
        numberOfBedsRG.setOnCheckedChangeListener((group, checkedId) -> {
            // do operations specific to this selection
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            numOfBeds = radioButton.getText().toString();
        });

        RadioGroup numberOfBathsRG = (RadioGroup) findViewById(R.id.profileBathroomRadioGroup);
        numberOfBathsRG.setOnCheckedChangeListener((group, checkedId) -> {
            // do operations specific to this selection
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            numOfBaths = radioButton.getText().toString();
        });
        popupLocList = new ArrayList<>();
        finalPropList = new ArrayList<>();
        myPreferredHouseType = new ArrayList<>();

        profileUpdateBtn = (Button) findViewById(R.id.profileUpdateBtn); 


        databaseReference.child("seekers").child(userKey).child("myPreference").addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUserPreference = snapshot.getValue(Preference.class);

                //userAvatar.setImageResource(Integer.parseInt(currentUserPreference.getAvatarID()));
                userFullName.setText(currentUserPreference.getFullName());
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
                    finalPropList.add(loc);

                    //on close of each chip
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroup.removeView(chip);
                            finalPropList.remove(loc);
                        }
                    });
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

        profileUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePreferenceInDatabase();
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
                        Intent clickIntent1 = new Intent(SeekerProfileActivity.this, PropertySeekerActivity.class);
                        clickIntent1.putExtra("userKey", userKey);
                        startActivity(clickIntent1);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.page_favorites:
                        Intent clickIntent2 = new Intent(SeekerProfileActivity.this, FavoritesActivity.class);
                        clickIntent2.putExtra("userKey", userKey);
                        startActivity(clickIntent2);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.page_chat:
                        Intent clickIntent3 = new Intent(SeekerProfileActivity.this, ChatActivity.class);
                        clickIntent3.putExtra("userKey", userKey);
                        startActivity(clickIntent3);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.page_profile:
                        return true;
                }
                return false;
            }
        });
    }

    private void updatePreferenceInDatabase() {
        //get type of house
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
        Preference myPref = new Preference(currentUserPreference.getAvatarID(), userFullName.getText().toString(), userEmailET.getText().toString(), userPhoneET.getText().toString(), currentUserPreference.getLegalSex(),
                currentUserPreference.getAge(), finalPropList, myPreferredHouseType,
                numOfBeds, numOfBaths, Integer.parseInt(updatedMinPrice.substring(1)), Integer.parseInt(updatedMaxPrice.substring(1)));

        //Update preference.
        databaseReference.child("seekers").child(userKey).child("myPreference").setValue(myPref).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SeekerProfileActivity.this, "Unable to update preference. Please try again later", Toast.LENGTH_SHORT).show();
            }
        });

        Intent clickIntent = new Intent(SeekerProfileActivity.this, PropertySeekerActivity.class);
        clickIntent.putExtra("userKey", userKey);
        startActivity(clickIntent);

    }

    /**
     * Method that is invoked on click of the floating action button which invokes a dialog box
     * to enter values.
     * @param view current view.
     */
    public void onFabPopupWindowClick(View view) {

        //Initialize an alert dialog box.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        //Initialize a view which corresponds to the dialogs layout.
        View popupView = inflater.inflate(R.layout.dialog_add_prop, null);

        //Get the website name and URL entered by the user.
        popupLocationET = (EditText) popupView.findViewById(R.id.popupEditTextTextLocation);
        popupChipGroup = (ChipGroup) popupView.findViewById(R.id.popupChipGroup);
        //Set the view to the alert builder.
        builder.setView(popupView);

        //Set action on click of "OK" button in the alert dialog.
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                addChipToMainGroup();
            }
        });

        //Set action on click of "Cancel" button in the alert dialog.
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //Show the dialog.
        builder.show();
    }

    /**
     * Method that is invoked on click of add location button, adds chips to the chip group.
     * @param view current view.
     */
    public void addChipToPopUpGroup(View view){

        if(popupLocationET != null) {
            String loc = popupLocationET.getText().toString();

            Chip chip = new Chip(this);
            chip.setText(loc);
            chip.setChipBackgroundColorResource(R.color.purple_200);
            chip.setCloseIconVisible(true);
            chip.setTextColor(getResources().getColor(R.color.white));

            //add chip into the chip group
            popupChipGroup.addView(chip);
            popupLocList.add(loc);
            finalPropList.add(loc);


            //on close of each chip
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupLocList.remove(loc);
                    popupChipGroup.removeView(chip);
                    finalPropList.remove(loc);
                }
            });
        }

    }

    /**
     * Method that is invoked on click of add location button, adds chips to the chip group.
     */
    public void addChipToMainGroup(){

        for(String loc: popupLocList){
            Chip chip = new Chip(context);
            chip.setText(loc);
            chip.setChipBackgroundColorResource(R.color.purple_200);
            chip.setCloseIconVisible(true);
            chip.setTextColor(getResources().getColor(R.color.white));

            //add chip into the chip group
            chipGroup.addView(chip);

            //on close of each chip
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipGroup.removeView(chip);
                }
            });
        }

    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(SeekerProfileActivity.this, FinalProject.class));
        finish();
    }
}