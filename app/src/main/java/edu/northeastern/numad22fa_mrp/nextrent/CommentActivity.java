package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.northeastern.numad22fa_mrp.ContactOwner;
import edu.northeastern.numad22fa_mrp.PropertyContentsSeeker;
import edu.northeastern.numad22fa_mrp.R;
import edu.northeastern.numad22fa_mrp.ViewLocation;

public class CommentActivity extends AppCompatActivity {
    ImageView housePic;
    TextView rent, rooms, address, country, state, description, likes, houseType, comments;
    Button like, owner, location;

    EditText commentsEt;

    String house;
    String ownerId;
    String group;
    String userId;
    String userName;
    String likesInfo;
    String roomInfo, rentInfo;

    String lat, longi;
    Double latitude, longitude;

    String descriptionInfo, addressInfo, locationInfo;

    private ArrayList<Comment> commentList;
    private AdapterComment adapter;


    boolean mProcessComment = false;
    boolean mProcessLike = false;

    DatabaseReference updateLikesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        house = intent.getStringExtra("houseId");
        ownerId = intent.getStringExtra("owner");
        group = intent.getStringExtra("group");
        userId = intent.getStringExtra("userKey");

        housePic = findViewById(R.id.commentImageview);
        rent = findViewById(R.id.commentRentTv);
        rooms = findViewById(R.id.commentRoomsTV);
        address = findViewById(R.id.commentAddressTV);
        country = findViewById(R.id.commentCountryTv);
        state = findViewById(R.id.commentStateTV);
        description = findViewById(R.id.commentDescription);
        /*likes = findViewById(R.id.commentLikesTv);
        comments = findViewById(R.id.commentsTV);
        like = findViewById(R.id.CommentLikeBtn);*/
        owner = findViewById(R.id.CommentContactOwnerBtn);
        houseType = findViewById(R.id.commentTypeTv);

        /*commentList = new ArrayList<>();
        adapter = new AdapterComment(CommentActivity.this, commentList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);*/

        loadHouseInfo();
        loadUserInfo();

        owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(CommentActivity.this, ContactOwner.class);
                intent2.putExtra("houseId", house);
                intent2.putExtra("houseDescription", descriptionInfo);
                intent2.putExtra("houseLocation", locationInfo);
                intent2.putExtra("address", addressInfo);
                intent2.putExtra("userKey",userId);
                startActivity(intent2);
            }
        });


    }

    private void loadComments() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("seekers");
        db.child(group).child("sharedHouses").child(house).child("comments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        commentList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Log.d("", snapshot.toString());
                            String commentInfo = ds.child("comment").getValue().toString();
                            String commentUser = ds.child("userName").getValue().toString();
                            String timeComment = ds.child("time").getValue().toString();
                            String commentID = ds.child("cId").getValue().toString();
                            String commentUserID = ds.child("uid").getValue().toString();

                            Comment comment = new Comment(commentUserID, commentID, timeComment,
                                    commentInfo, commentUser);

                            commentList.add(0,comment);
                            adapter.notifyItemInserted(0);
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void setLikes() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
        updateLikesRef = ref.child(group).child("sharedHouses").child(house);
        updateLikesRef.child("likedUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(userId).exists()) {
                    like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_black, 0, 0, 0);
                    like.setText("Liked");

                } else {
                    like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                    like.setText("Like");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void likePost() {
        mProcessLike = true;
        int pLikes = Integer.parseInt(likesInfo);

        updateLikesRef.child("likedUsers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(mProcessLike) {
                            if (snapshot.child(userId).exists()){
                                updateLikesRef.child("numLikes").setValue("" + (pLikes - 1));
                                updateLikesRef.child("likedUsers").child(userId).removeValue();
                                mProcessLike = false;

                            } else {
                                updateLikesRef.child("numLikes").setValue("" + (pLikes + 1));
                                updateLikesRef.child("likedUsers").child(userId).setValue("Liked");
                                mProcessLike = false;


                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
        ref.child(group).child("members")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userName = snapshot.child(userId).child("uid").getValue().toString();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void postComment() {
        String comment = commentsEt.getText().toString().trim();
        if(TextUtils.isEmpty(comment)){
            Toast.makeText(this, "Comment is Empty ...", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("groups").child(group).child("sharedHouses").child(house).child("comments");
        String times = String.valueOf(System.currentTimeMillis());

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cId", times);
        hashMap.put("comment", comment);
        hashMap.put("time", times);
        hashMap.put("uid", userId);
        hashMap.put("userName", userName);

        db.child(times).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                commentsEt.setText("");
            }
        });
    }

    private void updateCommentCount() {
        mProcessComment = true;
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("groups");
        DatabaseReference commentNumbers = dbr.child(group).child("sharedHouses").child(house);
                commentNumbers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mProcessComment){
                    String comments = "" + snapshot.child("numComments").getValue();
                    int commentBal = Integer.parseInt(comments) + 1;
                    commentNumbers.child("numComments").setValue("" + commentBal);
                    mProcessComment = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadLikes() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
        ref.child(group).child("sharedHouses").child(house)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        likesInfo = snapshot.child("numLikes").getValue().toString();
                        likes.setText(likesInfo + "Likes");
                        String commentNumInfo = snapshot.child("numComments").getValue().toString();
                        comments.setText(commentNumInfo + "Comments");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadHouseInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("houses");
        reference.child(ownerId).child(house)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        rentInfo = snapshot.child("rentPerRoom").getValue().toString();
                        addressInfo = snapshot.child("address").getValue().toString();
                        String stateInfo = snapshot.child("state").getValue().toString();
                        String countryInfo = snapshot.child("country").getValue().toString();
                        String imageInfo = snapshot.child("houseImage").getValue().toString();
                        descriptionInfo = snapshot.child("houseDescription").getValue().toString();
                        String typeInfo = snapshot.child("type").getValue().toString();
                        roomInfo = snapshot.child("noOfRoom").getValue().toString();
                        locationInfo = snapshot.child("houseLocation").getValue().toString();

                        rent.setText(rentInfo);
                        address.setText(addressInfo);
                        state.setText(stateInfo);
                        country.setText(countryInfo);
                        description.setText(descriptionInfo);
                        houseType.setText(typeInfo);
                        rooms.setText(roomInfo);
                        Glide.with(CommentActivity.this).load(imageInfo).into(housePic);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}