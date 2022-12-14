package edu.northeastern.numad22fa_mrp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StickItToEm extends AppCompatActivity {

    // creating a variable for Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for user class
    User user;

    // creating variables for
    // EditText and buttons.
    private EditText userNameEdt;
    String userKey;
    private static String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_to_em);

        // initializing our edittext and button
        userNameEdt = findViewById(R.id.editTextUserName);

        // instance of the Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // get reference for the database.
        databaseReference = firebaseDatabase.getReference("");


    }

    public void openLogin(View view){
        int theId = view.getId();
        if (theId == R.id.loginButton) {
            // getting text from our edittext fields.
            String userName = userNameEdt.getText().toString();

            // below line is for checking whether the
            // edittext fields are empty or not.
            if (TextUtils.isEmpty(userName)) {
                // if the text fields are empty
                // then show the below message.
                Toast.makeText(StickItToEm.this, "Please enter user name.",
                        Toast.LENGTH_SHORT).show();
            } else {
                // else call the method to add
                // data to our database.
                addDataToFirebase(userName);
            }
        }
    }
    private void addDataToFirebase(String userName) {
        //flag to check if user was created.
        final boolean[] created = {true};
        final boolean[] exists = {false};
        // below 3 lines of code is used to set
        // data in our object class.
        user = new User(userName);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.child("users").getChildren()) {
                    if (userName.equalsIgnoreCase(data.child("userName").getValue().toString())) {
                        //user name exists
                        userKey = data.getKey();
                        Log.d(TAG, "Userkey: " + userKey);
                        exists[0] = true;
                        break;
                    }
                }

                if(!exists[0]) {
                    //user name does not exists, create new
                    // data base reference will sends data to firebase.
                    DatabaseReference db = databaseReference.child("users").push();
                    userKey = db.getKey();
                    Log.d(TAG, "Userkey: " + userKey);
                    db.setValue(user).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //there was an issue
                            created[0] = false;
                            Toast.makeText(StickItToEm.this, "Unable to add user. Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    created[0] = false;
                }

                if(created[0]) {
                    // after adding this data we are showing toast message.
                    Toast.makeText(StickItToEm.this, "New user added!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StickItToEm.this, "User logged in successfully!", Toast.LENGTH_SHORT).show();
                }
                //Move to next activity.
                Intent clickIntent = new Intent(StickItToEm.this, AllUsersActivity.class);
                clickIntent.putExtra("currentUserName", userName);
                clickIntent.putExtra("userKey", userKey);
                startActivity(clickIntent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });


    }
}