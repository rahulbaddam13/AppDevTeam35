package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.northeastern.numad22fa_mrp.MessageAdapter;
import edu.northeastern.numad22fa_mrp.OwnerRegister;
import edu.northeastern.numad22fa_mrp.Property;
import edu.northeastern.numad22fa_mrp.R;

public class PropertySeekerActivity extends AppCompatActivity {

    //Recycler View and list of chat
    private RecyclerView propertyRecyclerView;
    ArrayList<Property> propertiesList;
    // Linear Layout Manager
    LinearLayoutManager horizontalLayout;

    // creating a variable for Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for reference for Firebase.
    DatabaseReference databaseReference;
    DatabaseReference properties;

    //bundle with data from previous activity.
    Bundle bundle = null;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_seeker);

        //Instantiate the array list of properties or get from the bundle.
        if(savedInstanceState == null){
            propertiesList = new ArrayList<>();
        } else {
            //propertiesList = savedInstanceState.getParcelableArrayList("chatMessageList");
        }

        //Link to recycle view.
        propertyRecyclerView = findViewById(R.id.property_list_recycler_view);

        //Set the layout manager for the recycle view.
        // Set Horizontal Layout Manager
        // for Recycler view
        horizontalLayout = new LinearLayoutManager(
                PropertySeekerActivity.this,
                LinearLayoutManager.HORIZONTAL,
                false);
        propertyRecyclerView.setLayoutManager(horizontalLayout);

        //Set the custom adapter to the recycle view.
        //recyclerView.setAdapter(new ChatAdapter(chatMessageList, this));
        PropertyListAdapterSeeker propertyListAdapterSeeker =
                new PropertyListAdapterSeeker(this, propertiesList);
        propertyRecyclerView.setAdapter(propertyListAdapterSeeker);

        //Decoration to add line after each item in the view.
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL);


        propertyRecyclerView.addItemDecoration(dividerItemDecoration);

        //get the user ID
        bundle = getIntent().getExtras();
        currentUserId = bundle.getString("currentUserID");

        // instance of the Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // get reference for the database.
        databaseReference = firebaseDatabase.getReference("");


        // Attach a listener to read the data at our messages reference
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child(OwnerRegister.HOUSES).getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {

                    Iterable<DataSnapshot> snapshotIterator2 = iterator.next().getChildren();
                    Iterator<DataSnapshot> iterator2 = snapshotIterator2.iterator();

                    while (iterator2.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator2.next();

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
                                String.valueOf(next.child("type").getValue()));
                        propertiesList.add(article);

                        //Notify the adapter about the newly added item.
                        if(propertyRecyclerView != null && propertyRecyclerView.getAdapter() != null)
                            propertyRecyclerView.getAdapter().notifyItemInserted(propertyRecyclerView.getAdapter().getItemCount());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP | ItemTouchHelper.DOWN) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.DOWN){
                    Toast.makeText(PropertySeekerActivity.this, "Added to favorites!",
                            Toast.LENGTH_SHORT).show();
                    //Add property to favorites.
                    String propertyID = propertiesList.get(viewHolder.getBindingAdapterPosition()).getHouseId();
                    DatabaseReference db = databaseReference.child("seekers").child(currentUserId).child("favorites").push();
                    db.setValue(propertyID).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //there was an issue
                            Toast.makeText(PropertySeekerActivity.this, "Unable to add user. Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if (direction == ItemTouchHelper.UP){
                    Toast.makeText(PropertySeekerActivity.this, "Property removed.",
                            Toast.LENGTH_SHORT).show();
                }
                //remove from list
                propertiesList.remove(viewHolder.getBindingAdapterPosition());
                if(propertyRecyclerView != null && propertyRecyclerView.getAdapter() != null) {
                    propertyRecyclerView.getAdapter().notifyItemRemoved(propertyRecyclerView.getAdapter().getItemCount());
                    propertyRecyclerView.getAdapter().notifyItemRangeChanged(viewHolder.getBindingAdapterPosition(), propertyRecyclerView.getAdapter().getItemCount());
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(propertyRecyclerView);

            properties = databaseReference.child(OwnerRegister.HOUSES);
        properties.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

        //propertyRecyclerView.setOnTouchListener();

        //Bottom navigation bar.
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Choose home by default.
        bottomNavigationView.setSelectedItemId(R.id.page_home);
        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_home:
                        return true;
                    case R.id.page_favorites:
                        startActivity(new Intent(getApplicationContext(),FavoritesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.page_chat:
                        startActivity(new Intent(getApplicationContext(),ChatActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.page_profile:
                        startActivity(new Intent(getApplicationContext(),SeekerProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(PropertySeekerActivity.this, FinalProject.class));
        finish();
    }

}