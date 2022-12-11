package edu.northeastern.numad22fa_mrp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.L;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Objects;

public class ContactOwner extends AppCompatActivity {

    TextView ownerName,ownerEmail,phoneNumber;
    String houseId,houseDescription,location,address;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String user;
    Button send,call;
    String subject, body,number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_owner);

        //reference = FirebaseDatabase.getInstance().getReference().child("houses").child(userId).child(houseId);
        Intent intent = getIntent();
        houseId = intent.getStringExtra("houseId");
        houseDescription = intent.getStringExtra("houseDescription");
        location = intent.getStringExtra("houseLocation");
        address = intent.getStringExtra("address");
        ownerName = findViewById(R.id.ownerName);
        //ownerName.setText(houseId);
        ownerEmail = findViewById(R.id.ownerEmail);
        phoneNumber = findViewById(R.id.phoneNumber);
        send = findViewById(R.id.sendMail);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child(OwnerRegister.HOUSES).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                while (iterator.hasNext()) {

                    Iterable<DataSnapshot> snapshotIterator2 = iterator.next().getChildren();
                    //DataSnapshot next = (DataSnapshot) iterator.next();
//                    Log.v("next",next.toString());
                    Iterator<DataSnapshot> iterator2 = snapshotIterator2.iterator();


                    while (iterator2.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator2.next();
                        user = String.valueOf(next.child("houseId").getValue());
                        if(Objects.equals(user, houseId)) {
                            Log.v("The matched house id is",user);
                            String id = String.valueOf(next.child("userId").getValue());
                            Log.v("The matched user id is",id);
                            DataSnapshot d  = dataSnapshot.child("Owner").child(id);
                            String username = String.valueOf(d.child("username").getValue());
                            Log.v("The final is",username);
                            ownerName.setText(username);
                            String emailId = String.valueOf(d.child("email").getValue());
                            ownerEmail.setText(emailId);
                            String phone = String.valueOf(d.child("phoneNumber").getValue());
                            phoneNumber.setText(phone);


                        }

                        Log.v("HouseId",user);

                        //Without filtering - might have to apply filter here?
                        Property article = new Property(String.valueOf(next.child("houseId").getValue()),
                                String.valueOf(next.child("noOfRoom").getValue()),
                                String.valueOf(next.child("rentPerRoom").getValue()),
                                String.valueOf(next.child("houseDescription").getValue()),
                                String.valueOf(next.child("houseLocation").getValue()),
                                String.valueOf(next.child("houseImage").getValue()),
                                String.valueOf(next.child("userId").getValue()),
                                String.valueOf(next.child("country").getValue()),
                                String.valueOf(next.child("state").getValue()),
                                String.valueOf(next.child("type").getValue()),String.valueOf(next.child("baths")),
                                String.valueOf(next.child("address")));


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        subject = "I am interested in your property at "+ address;
        body = "It is a long established fact that a reader will be distracted" +
                " by the readable content of a page when looking at its layout." +
                " The point of using Lorem Ipsum is that it has a more-or-less normal distribution";
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:"+ownerEmail.getText().toString()+"?subject=" + Uri.encode(subject) + "&body=" + Uri.encode(body));
                intent.setData(data);
                startActivity(intent);
            }
        });

    }


}