package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.numad22fa_mrp.AllUsersActivity;
import edu.northeastern.numad22fa_mrp.OwnerRegister;
import edu.northeastern.numad22fa_mrp.R;
import edu.northeastern.numad22fa_mrp.StickItToEm;
import edu.northeastern.numad22fa_mrp.User;
import edu.northeastern.numad22fa_mrp.WeatherActivity;

public class FinalProject extends AppCompatActivity {

    // creating a variable for Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for user class
    NextRentUser user;

    // creating variables for
    // EditText and buttons.
    private EditText userNameEdt;
    String userKey;

    private final static int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_project);

        // initializing our edittext and button
        userNameEdt = findViewById(R.id.edittext_username);

        // instance of the Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // get reference for the database.
        databaseReference = firebaseDatabase.getReference("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(
                FinalProject.this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_DENIED) {
            askLocationPermissions();
        }
    }

    /**
     * A public method that is called when button is clicked in the Main Activity.
     *
     * @param view current view.
     */
    public void onClickTypeOfUser(View view) {

        // getting text from our edittext fields.
        String userName = userNameEdt.getText().toString();

        // below line is for checking whether the
        // edittext fields are empty or not.
        if (TextUtils.isEmpty(userName)) {
            // if the text fields are empty
            // then show the below message.
            Toast.makeText(FinalProject.this, "Please enter user name!",
                    Toast.LENGTH_SHORT).show();
        } else {
            //get the ID of the button clicked in the view.
            int buttonId = view.getId();

            if (buttonId == R.id.seekerButton) {

                // create a user object of seeker type.
                user = new NextRentUser(userName, "seekers");

                addDataToFirebase(user);

            }  else if (buttonId == R.id.managerButton) {

                // create a user object of manager type.
                user = new NextRentUser(userName, "managers");

                Intent clickIntent = new Intent(FinalProject.this, OwnerRegister.class);
                clickIntent.putExtra("currentUserName", user.getUserName());
                startActivity(clickIntent);
            }
        }

    }

    /**
     * method to add user objects into the database under "nextrentusers", based on the type of user.
     *
     * @param user user object which indicates the UID, name and type of user.
     */
    private void addDataToFirebase(NextRentUser user) {
        //flag to check if user was created.
        boolean[] created = {true};
        boolean[] exists = {false};

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.child(user.getUserType()).getChildren()) {
                    if (user.getUserName().equalsIgnoreCase(data.child("userName").getValue().toString())) {
                        //user name exists
                        userKey = data.getKey();
                        exists[0] = true;
                        break;
                    }
                }

                if (!exists[0]) {
                    //user name does not exists, create new
                    // data base reference will sends data to firebase.
                    DatabaseReference db = databaseReference.child(user.getUserType()).push();
                    userKey = db.getKey();
                    db.setValue(user).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //there was an issue
                            created[0] = false;
                            Toast.makeText(FinalProject.this, "Unable to add user. Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    created[0] = false;
                }

                if (created[0]) {
                    // after adding this data we are showing toast message.
                    Toast.makeText(FinalProject.this, "New user added!", Toast.LENGTH_SHORT).show();

                    if("seekers".equalsIgnoreCase(user.getUserType())){
                        //New user, navigate to survey page.
                        Intent clickIntent = new Intent(FinalProject.this, BasicQuestionsActivity.class);
                        clickIntent.putExtra("userKey", userKey);
                        startActivity(clickIntent);
                    }

                } else {
                    Toast.makeText(FinalProject.this, "User logged in successfully!", Toast.LENGTH_SHORT).show();

                    if("seekers".equalsIgnoreCase(user.getUserType())){
                        //Already registered user, navigate to property lists page.
                        Intent clickIntent = new Intent(FinalProject.this, PropertySeekerActivity.class);
                        clickIntent.putExtra("userKey", userKey);
                        startActivity(clickIntent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });

    }

    /**
     * Method to request location permission from user.
     */
    private void askLocationPermissions() {
        // Check Permissions
        if (ContextCompat.checkSelfPermission(
                this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(FinalProject.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission to access this feature")
                        .setPositiveButton("OK", (dialogInterface, i) -> ActivityCompat.requestPermissions(FinalProject.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                REQUEST_CODE))
                        .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(FinalProject.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //checkSettingsAndStartLocationUpdates();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give location permission to access this feature")
                        .setPositiveButton("OK", (dialogInterface, i) -> ActivityCompat.requestPermissions(FinalProject.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                REQUEST_CODE))
                        .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                        .create()
                        .show();
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
