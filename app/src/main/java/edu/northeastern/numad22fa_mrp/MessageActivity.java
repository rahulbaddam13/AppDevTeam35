package edu.northeastern.numad22fa_mrp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.northeastern.numad22fa_mrp.adapters.ChatAdapter;

public class MessageActivity extends AppCompatActivity {

    // creating a variable for Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for reference for Firebase.
    DatabaseReference databaseReference;
    DatabaseReference messages;

    //sticker chosen by the user to send.
    public static int chosenImageId = 0;

    //bundle with data from previous activity.
    Bundle bundle = null;

    //unique id which represent two users.
    String chatId = null;

    //Recycler View and list of chat
    private RecyclerView messageRecyclerView;

    MessageAdapter adapter;

    List<ChatMessage> chatMessageList;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView6;


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

        if(savedInstanceState == null){
            chatMessageList = new ArrayList<>();
        } else {
            chatMessageList = savedInstanceState.getParcelableArrayList("chatMessageList");
        }

        int compare = bundle.getString("currentUserName").compareTo(bundle.getString("userName"));
        if (compare < 0){
            chatId = bundle.getString("currentUserName") + bundle.getString("userName");
        }
        else if (compare > 0) {
            chatId = bundle.getString("userName") + bundle.getString("currentUserName");
        }

        //list all the stickers in horizontal scroll view.
        addStickersList();

        //Link to recycle view.
        messageRecyclerView = findViewById(R.id.user_recycler_view);

        //Set the layout manager for the recycle view.
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Set the custom adapter to the recycle view.
        //recyclerView.setAdapter(new ChatAdapter(chatMessageList, this));
        adapter = new MessageAdapter(this, chatMessageList);
        messageRecyclerView.setAdapter(adapter);

        //Decoration to add line after each item in the view.
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);

        messageRecyclerView.addItemDecoration(dividerItemDecoration);

      /*// Attach a listener to read the data at our messages reference
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child("chats").child(chatId).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();

                    ChatMessage chatMessage = new ChatMessage((Long)next.child("imageID").getValue(), String.valueOf(next.child("timestamp").getValue()), String.valueOf(next.child("sender").getValue()));

                    chatMessageList.add(chatMessage);

                    //Notify the adapter about the newly added item.
                    if(messageRecyclerView != null && messageRecyclerView.getAdapter() != null)
                        messageRecyclerView.getAdapter().notifyItemInserted(messageRecyclerView.getAdapter().getItemCount());


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });*/


        messages = databaseReference.child("chats").child(chatId);
        messages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                displayChatSendNotif(snapshot);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void addStickersList(){
        LinearLayout sticker = findViewById(R.id.stickers);

        LayoutInflater inflater = LayoutInflater.from(this);

        //happy fox sticker
        View view1 = inflater.inflate(R.layout.sticker, sticker, false);

        imageView1 = view1.findViewById(R.id.stickerImageView);
        imageView1.setImageResource(R.drawable.happy_fox);
        int imageView1Id = getResources().getIdentifier(getApplicationContext().getPackageName()+":drawable/happy_fox" , null, null);
        imageView1.setId(imageView1Id);
        sticker.addView(view1);

        //sad fox sticker
        View view2 = inflater.inflate(R.layout.sticker, sticker, false);

        imageView2 = view2.findViewById(R.id.stickerImageView);
        imageView2.setImageResource(R.drawable.sad_fox);
        int imageView2Id = getResources().getIdentifier(getApplicationContext().getPackageName()+":drawable/sad_fox" , null, null);
        imageView2.setId(imageView2Id);

        sticker.addView(view2);

        //angry fox sticker
        View view3 = inflater.inflate(R.layout.sticker, sticker, false);

        imageView3 = view3.findViewById(R.id.stickerImageView);
        imageView3.setImageResource(R.drawable.angry_fox);
        int imageView3Id = getResources().getIdentifier(getApplicationContext().getPackageName()+":drawable/angry_fox" , null, null);
        imageView3.setId(imageView3Id);

        sticker.addView(view3);

        //hungry fox sticker
        View view4 = inflater.inflate(R.layout.sticker, sticker, false);

        imageView4 = view4.findViewById(R.id.stickerImageView);
        imageView4.setImageResource(R.drawable.hungry_fox);
        int imageView4Id = getResources().getIdentifier(getApplicationContext().getPackageName()+":drawable/hungry_fox" , null, null);
        imageView4.setId(imageView4Id);

        sticker.addView(view4);

        //love fox sticker
        View view5 = inflater.inflate(R.layout.sticker, sticker, false);

        imageView5 = view5.findViewById(R.id.stickerImageView);
        imageView5.setImageResource(R.drawable.love_fox);
        int imageView5Id = getResources().getIdentifier(getApplicationContext().getPackageName()+":drawable/love_fox" , null, null);
        imageView5.setId(imageView5Id);

        sticker.addView(view5);

        //sick fox sticker
        View view6 = inflater.inflate(R.layout.sticker, sticker, false);

        imageView6 = view6.findViewById(R.id.stickerImageView);
        imageView6.setImageResource(R.drawable.sick_fox);
        int imageView6Id = getResources().getIdentifier(getApplicationContext().getPackageName()+":drawable/sick_fox" , null, null);
        imageView6.setId(imageView6Id);

        //setting on click listeners, change background color on click
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView1.setBackgroundColor(Color.rgb(218, 246, 169));
                imageView2.setBackgroundColor(0);
                imageView3.setBackgroundColor(0);
                imageView4.setBackgroundColor(0);
                imageView5.setBackgroundColor(0);
                imageView6.setBackgroundColor(0);

                chosenImageId = view.getId();
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView1.setBackgroundColor(0);
                imageView2.setBackgroundColor(Color.rgb(218, 246, 169));
                imageView3.setBackgroundColor(0);
                imageView4.setBackgroundColor(0);
                imageView5.setBackgroundColor(0);
                imageView6.setBackgroundColor(0);

                chosenImageId = view.getId();
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView1.setBackgroundColor(0);
                imageView2.setBackgroundColor(0);
                imageView3.setBackgroundColor(Color.rgb(218, 246, 169));
                imageView4.setBackgroundColor(0);
                imageView5.setBackgroundColor(0);
                imageView6.setBackgroundColor(0);

                chosenImageId = view.getId();
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView1.setBackgroundColor(0);
                imageView2.setBackgroundColor(0);
                imageView3.setBackgroundColor(0);
                imageView4.setBackgroundColor(Color.rgb(218, 246, 169));
                imageView5.setBackgroundColor(0);
                imageView6.setBackgroundColor(0);

                chosenImageId = view.getId();
            }
        });
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView1.setBackgroundColor(0);
                imageView2.setBackgroundColor(0);
                imageView3.setBackgroundColor(0);
                imageView4.setBackgroundColor(0);
                imageView5.setBackgroundColor(Color.rgb(218, 246, 169));
                imageView6.setBackgroundColor(0);

                chosenImageId = view.getId();
            }
        });
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView1.setBackgroundColor(0);
                imageView2.setBackgroundColor(0);
                imageView3.setBackgroundColor(0);
                imageView4.setBackgroundColor(0);
                imageView5.setBackgroundColor(0);
                imageView6.setBackgroundColor(Color.rgb(218, 246, 169));

                chosenImageId = view.getId();
            }
        });

        sticker.addView(view6);
    }

    /**
     * Method called when user clicks on send button.
     *
     * @param view current view.
     */
    public void sendButtonClicked(View view){

        if(chosenImageId == 0){
            Toast.makeText(MessageActivity.this, "Please choose a sticker", Toast.LENGTH_SHORT).show();
            return;
        }

        //create a chat message object.
        ChatMessage chatMessage = new ChatMessage(chosenImageId, bundle.getString("currentUserName"), bundle.getString("userName"), "unread");

        databaseReference.child("chats").child(chatId).push().setValue(chatMessage).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MessageActivity.this, "Unable to send sticker. Please try again later", Toast.LENGTH_SHORT).show();
            }
        });

        //update sticker counts
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
        imageView1.setBackgroundColor(0);
        imageView2.setBackgroundColor(0);
        imageView3.setBackgroundColor(0);
        imageView4.setBackgroundColor(0);
        imageView5.setBackgroundColor(0);
        imageView6.setBackgroundColor(0);
        Toast.makeText(MessageActivity.this, "Sticker sent", Toast.LENGTH_SHORT).show();
    }


    private void sendNotification(int image, String sender) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        Bitmap myBitmap;

        switch(image) {
            case 2131165308:
                myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.happy_fox);
                break;
            case 2131165368:
                myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sad_fox);
                break;
            case 2131165271:
                myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.angry_fox);
                break;
            case 2131165309:
                myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hungry_fox);
                break;
            case 2131165325:
                myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.love_fox);
                break;
            case 2131165369:
                myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sick_fox);
                break;
            default:
                myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.not_found);
                break;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "n")
                .setContentTitle("MRP")
                .setSmallIcon(R.mipmap.ic_launcher_35_round)
                .setContentText(sender + " Sent You:")
                .setLargeIcon(myBitmap)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(myBitmap)
                        .bigLargeIcon(null));

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(2, builder.build());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("chatMessageList",
                (ArrayList<? extends Parcelable>) chatMessageList);
    }

    private void displayChatSendNotif(DataSnapshot snapshot) {
        ChatMessage chatMessage = new ChatMessage((Long) snapshot.child("imageID").getValue(),
                String.valueOf(snapshot.child("timestamp").getValue()),
                String.valueOf(snapshot.child("sender").getValue()),
                String.valueOf(snapshot.child("receiver").getValue()));
        chatMessageList.add(chatMessage);
        /*String sender = snapshot.child("sender").getValue(String.class);
        int image_id = snapshot.child("imageID").getValue(int.class);
        String receive = snapshot.child("receiver").getValue(String.class);*/

        String sender = chatMessage.getSender();
        int image_id = (int) chatMessage.getImageID();
        String receive = chatMessage.getReceiver();
        String key = snapshot.getKey();
        adapter.notifyDataSetChanged();

        String current = bundle.getString("currentUserName");
        String currentStatus = snapshot.child("readStatus").getValue(String.class);

        if (receive.equalsIgnoreCase(current) && currentStatus.equalsIgnoreCase("unread")) {
            sendNotification(image_id, sender);
            messages.child(key).child("readStatus").setValue("read");
        }
    }
}