package edu.northeastern.numad22fa_mrp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    // creating a variable for Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for reference for Firebase.
    DatabaseReference databaseReference;

    public static int chosenImageId = 0;

    Bundle bundle = null;

    String chatId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // instance of the Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // get reference for the database.
        databaseReference = firebaseDatabase.getReference("");

        //get the text view in which the different usernames must be displayed.
        bundle = getIntent().getExtras();
        TextView displayUserNameTV = (TextView) findViewById(R.id.displayUserName);
        displayUserNameTV.setText(bundle.getString("userName"));


        int compare = bundle.getString("currentUserName").compareTo(bundle.getString("userName"));
        if (compare < 0){
            chatId = bundle.getString("currentUserName") + bundle.getString("userName");
        }
        else if (compare > 0) {
            chatId = bundle.getString("userName") + bundle.getString("currentUserName");
        }

        //list all the stickers in horizontal scroll view.
        addStickersList();

    }

    private void addStickersList(){
        LinearLayout sticker = findViewById(R.id.stickers);

        LayoutInflater inflater = LayoutInflater.from(this);

        //happy fox sticker
        View view1 = inflater.inflate(R.layout.sticker, sticker, false);

        ImageView imageView1 = view1.findViewById(R.id.stickerImageView);
        imageView1.setImageResource(R.drawable.happy_fox);
        int imageView1Id = getResources().getIdentifier(getApplicationContext().getPackageName()+":drawable/happy_fox" , null, null);
        imageView1.setId(imageView1Id);
        sticker.addView(view1);

        //sad fox sticker
        View view2 = inflater.inflate(R.layout.sticker, sticker, false);

        ImageView imageView2 = view2.findViewById(R.id.stickerImageView);
        imageView2.setImageResource(R.drawable.sad_fox);
        int imageView2Id = getResources().getIdentifier(getApplicationContext().getPackageName()+":drawable/sad_fox" , null, null);
        imageView2.setId(imageView2Id);

        sticker.addView(view2);

        //angry fox sticker
        View view3 = inflater.inflate(R.layout.sticker, sticker, false);

        ImageView imageView3 = view3.findViewById(R.id.stickerImageView);
        imageView3.setImageResource(R.drawable.angry_fox);
        int imageView3Id = getResources().getIdentifier(getApplicationContext().getPackageName()+":drawable/angry_fox" , null, null);
        imageView3.setId(imageView3Id);

        sticker.addView(view3);

        //hungry fox sticker
        View view4 = inflater.inflate(R.layout.sticker, sticker, false);

        ImageView imageView4 = view4.findViewById(R.id.stickerImageView);
        imageView4.setImageResource(R.drawable.hungry_fox);
        int imageView4Id = getResources().getIdentifier(getApplicationContext().getPackageName()+":drawable/hungry_fox" , null, null);
        imageView4.setId(imageView4Id);

        sticker.addView(view4);

        //love fox sticker
        View view5 = inflater.inflate(R.layout.sticker, sticker, false);

        ImageView imageView5 = view5.findViewById(R.id.stickerImageView);
        imageView5.setImageResource(R.drawable.love_fox);
        int imageView5Id = getResources().getIdentifier(getApplicationContext().getPackageName()+":drawable/love_fox" , null, null);
        imageView5.setId(imageView5Id);

        sticker.addView(view5);

        //sick fox sticker
        View view6 = inflater.inflate(R.layout.sticker, sticker, false);

        ImageView imageView6 = view6.findViewById(R.id.stickerImageView);
        imageView6.setImageResource(R.drawable.sick_fox);
        int imageView6Id = getResources().getIdentifier(getApplicationContext().getPackageName()+":drawable/sick_fox" , null, null);
        imageView6.setId(imageView6Id);

        sticker.addView(view6);
    }

    public void onStickerClick(View view){

        //get the ID of the image clicked.
        int imageID = view.getId();

        //highlight
        chosenImageId = imageID;
        System.out.println("chosenImageId" + chosenImageId);

    }

    public void sendButtonClicked(View view){

        if(chosenImageId == 0){
            Toast.makeText(MessageActivity.this, "Please choose a sticker", Toast.LENGTH_SHORT).show();
            return;
        }

        //create a chat message object.
        ChatMessage chatMessage = new ChatMessage(chosenImageId, bundle.getString("currentUserName"));

        databaseReference.child("chats").child(chatId).push().setValue(chatMessage).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MessageActivity.this, "Unable to send sticker. Please try again later", Toast.LENGTH_SHORT).show();
            }
        });

        // Attach a listener to read the data at our posts reference
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child("users").getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();

                    String currentUserName = getIntent().getExtras().getString("currentUserName");
                    if(currentUserName.equals(next.child("userName").getValue())) {

                        Map<String, Long> currentCount = (Map<String, Long>) next.child("stickerCountMap").getValue();
                        if(currentCount != null)
                        currentCount.put(chosenImageId+"", currentCount.getOrDefault(chosenImageId+"",0l)+ 1l);
                        databaseReference.child("users").child(next.getKey()).child("stickerCountMap").setValue(currentCount).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MessageActivity.this, "Unable to send sticker. Please try again later", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        Toast.makeText(MessageActivity.this, "Sticker sent", Toast.LENGTH_SHORT).show();

    }
}