package edu.northeastern.numad22fa_mrp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for user class
    User user;

    // creating variables for
    // EditText and buttons.
    private EditText userNameEdt;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_to_em);

        // initializing our edittext and button
        userNameEdt = findViewById(R.id.editTextUserName);
        loginBtn = findViewById(R.id.loginButton);

        // below line is used to get the
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("");

        // adding on click listener for our button.
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });

    }
    private void addDataToFirebase(String userName) {
        //flag to check if user was created.
        final boolean[] created = {true};
        // below 3 lines of code is used to set
        // data in our object class.
        user = new User(userName);

        // data base reference will sends data to firebase.
        databaseReference.push().setValue(user).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //there was an issue
                created[0] = false;
                Toast.makeText(StickItToEm.this, "Unable to add user. Please try again later", Toast.LENGTH_SHORT).show();
            }
        });

        if(created[0]){
            // after adding this data we are showing toast message.
            Toast.makeText(StickItToEm.this, "User added successfully", Toast.LENGTH_SHORT).show();

            //Move to next activity.
        }

    }
}