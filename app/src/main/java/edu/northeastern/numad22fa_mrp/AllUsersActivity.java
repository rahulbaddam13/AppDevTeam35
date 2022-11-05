package edu.northeastern.numad22fa_mrp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

    private RecyclerView recyclerView;

    List<User> usersList;

    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        //Instantiate the array list of websites or get from the bundle.
        if(savedInstanceState == null){
            usersList = new ArrayList<>();
        } else {
            //usersList = savedInstanceState.getParcelableArrayList("usersList");
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

        // Attach a listener to read the data at our posts reference
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child("users").getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();

                    //add users other than current user.
                    String currentUserName = getIntent().getExtras().getString("currentUserName");
                    if(!currentUserName.equals(next.child("userName").getValue())) {

                        Map<String, Long> stickerMap = new HashMap<>();
                        stickerMap.put(next.child("stickerCountMap").child("2131165308").getKey(),(Long) next.child("stickerCountMap").child("2131165308").getValue());
                        stickerMap.put(next.child("stickerCountMap").child("2131165367").getKey(),(Long) next.child("stickerCountMap").child("2131165367").getValue());
                        stickerMap.put(next.child("stickerCountMap").child("2131165271").getKey(),(Long) next.child("stickerCountMap").child("2131165271").getValue());
                        stickerMap.put(next.child("stickerCountMap").child("2131165309").getKey(),(Long) next.child("stickerCountMap").child("2131165309").getValue());
                        stickerMap.put(next.child("stickerCountMap").child("2131165325").getKey(),(Long) next.child("stickerCountMap").child("2131165325").getValue());
                        stickerMap.put(next.child("stickerCountMap").child("2131165368").getKey(),(Long) next.child("stickerCountMap").child("2131165368").getValue());

                        User user = new User(next.child("uid").getValue().toString(), next.child("userName").getValue().toString(), currentUserName, stickerMap);
                        usersList.add(user);
                    }

                    //Notify the adapter about the newly added item.
                    if(recyclerView != null && recyclerView.getAdapter() != null)
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