package edu.northeastern.numad22fa_mrp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.os.Parcelable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.northeastern.numad22fa_mrp.adapters.UserAdapter;

public class AllUsersActivity extends AppCompatActivity {

    // creating a variable for Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for reference for Firebase.
    DatabaseReference databaseReference;
    DatabaseReference messages;

    private RecyclerView recyclerView;

    List<User> usersList;

    ConstraintLayout constraintLayout;
    String userKey;
    String currentUserName;
    String userName;

    private final int NOTIFICATION_UNIQUE_ID = 13;
    private static int notigen = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        userKey = getIntent().getExtras().getString("userKey");
        currentUserName = getIntent().getExtras().getString("currentUserName");

        //Instantiate the array list of websites or get from the bundle.
        if (savedInstanceState == null) {
            usersList = new ArrayList<>();
        } else {
            usersList = savedInstanceState.getParcelableArrayList("usersList");
        }

        // below line is used to get the
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("");

        //Link to recycle view.
        recyclerView = findViewById(R.id.user_recycler_view);

        //Set the layout manager for the recycle view.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Set the custom adapter to the recycle view.
        recyclerView.setAdapter(new UserAdapter(usersList, this));

        //Decoration to add line after each item in the view.
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(dividerItemDecoration);

        //Initialize a constraint layout to display the snack bar.
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        if (savedInstanceState == null) {
            // Attach a listener to read the data at our posts reference
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child("users").getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();

                    //add users other than current user.
                    if(!currentUserName.equals(next.child("userName").getValue())) {

                        Map<String, Long> stickerMap = new HashMap<>();
                        stickerMap.put(next.child("stickerCountMap").child("2131165308").getKey(), (Long) next.child("stickerCountMap").child("2131165308").getValue());
                        stickerMap.put(next.child("stickerCountMap").child("2131165368").getKey(), (Long) next.child("stickerCountMap").child("2131165368").getValue());
                        stickerMap.put(next.child("stickerCountMap").child("2131165271").getKey(), (Long) next.child("stickerCountMap").child("2131165271").getValue());
                        stickerMap.put(next.child("stickerCountMap").child("2131165309").getKey(), (Long) next.child("stickerCountMap").child("2131165309").getValue());
                        stickerMap.put(next.child("stickerCountMap").child("2131165325").getKey(), (Long) next.child("stickerCountMap").child("2131165325").getValue());
                        stickerMap.put(next.child("stickerCountMap").child("2131165369").getKey(), (Long) next.child("stickerCountMap").child("2131165369").getValue());

                        User user = new User(next.child("uid").getValue().toString(), next.child("userName").getValue().toString(), currentUserName, stickerMap);
                        usersList.add(user);

                        userName = next.child("userName").getValue(String.class);
                        String chatId = "";

                        int compare = currentUserName.compareTo(userName);

                        if (compare < 0) {
                            chatId = currentUserName + userName;
                        } else if (compare > 0) {
                            chatId = userName + currentUserName;
                        }

                        messages = databaseReference.child("chats").child(chatId);
                            messages.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    sendNotif(snapshot);
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    sendNotif(snapshot);


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

                        //Notify the adapter about the newly added item.
                        if (recyclerView != null && recyclerView.getAdapter() != null)
                            recyclerView.getAdapter().notifyItemInserted(recyclerView.getAdapter().getItemCount());

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
    }

    private void sendNotif(DataSnapshot snapshot) {
        String receive = snapshot.child("receiver").getValue(String.class);
        String key = snapshot.getKey();
        String currentStatus = snapshot.child("readStatus").getValue(String.class);
        if (receive != null && receive.equalsIgnoreCase(currentUserName) && currentStatus.equalsIgnoreCase("unread")) {
            String sender = snapshot.child("sender").getValue(String.class);
            int image_id = snapshot.child("imageID").getValue(int.class);
            sendNotification(image_id, sender);
            messages.child(key).child("readStatus").setValue("read");
        }
    }

    private void sendNotification(int image_id, String sender) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        Bitmap myBitmap;

        switch(image_id) {
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

        Intent resultingIntent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        bundle.putString("currentUserName", currentUserName);
        resultingIntent.putExtras(bundle);
        PendingIntent pendingIntenet = PendingIntent.getActivity(this,
                (int) System.currentTimeMillis(),
                resultingIntent,
                PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "n")
                .setContentTitle("MRP Sticker Notified!")
                .setSmallIcon(R.mipmap.ic_launcher_35_round)
                .setContentText(sender + " Sent You:")
                .setLargeIcon(myBitmap)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(myBitmap)
                        .bigLargeIcon(null))
                .setAutoCancel(true)
                .setContentIntent(pendingIntenet);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_UNIQUE_ID+ notigen, builder.build());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("usersList",
                (ArrayList<? extends Parcelable>) usersList);
    }

    public void openProfile(View view) {
        int theId = view.getId();
        if (theId == R.id.profile) {
            Intent intent = new Intent(AllUsersActivity.this, UserProfileActivity.class);
            intent.putExtra("userKey", userKey);
            intent.putExtra("username", currentUserName);
            startActivity(intent);
        }
    }
}