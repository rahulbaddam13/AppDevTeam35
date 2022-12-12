package edu.northeastern.numad22fa_mrp.nextrent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.northeastern.numad22fa_mrp.R;

public class SharedPropAdapter extends RecyclerView.Adapter<SharedPropAdapter.SharedPropHolder>{

    Context context;
    ArrayList<SharedProperty> shared;
    String groupId;
    Boolean mProcessLike = false;
    DatabaseReference sharedHouseRef;
    String houseId;
    String userId;

    public SharedPropAdapter(Context context, ArrayList<SharedProperty> shared, String groupId, String userID) {
        this.context = context;
        this.shared = shared;
        this.groupId = groupId;
        this.userId = userID;
    }

    @NonNull
    @Override
    public SharedPropHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shared_property_card,
                parent, false);
        return new SharedPropHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SharedPropHolder holder, int position) {
        SharedProperty sharedProp = shared.get(position);
        loadPropertyDetails(sharedProp, holder);

        /*String pLikes = sharedProp.getLikes();
        holder.likes.setText(pLikes + " Likes");
        String pComments = sharedProp.getComments();
        holder.comments.setText(pComments + " Comments");*/


//        holder.like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int pLikes = Integer.parseInt(sharedProp.getLikes());
//                mProcessLike = true;
//                houseId = sharedProp.getHouseId();
//
//                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
//                sharedHouseRef = ref.child(groupId).child("sharedHouses");
//                        sharedHouseRef.child(houseId).child("likedUsers")
//                                .addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if(mProcessLike) {
//                                    if (snapshot.child(userId).exists()){
//                                        sharedHouseRef.child(houseId).child("numLikes").setValue("" + (pLikes - 1));
//                                        sharedHouseRef.child(houseId).child("likedUsers").child(userId).removeValue();
//                                        mProcessLike = false;
//                                    } else {
//                                        sharedHouseRef.child(houseId).child("numLikes").setValue("" + (pLikes + 1));
//                                        sharedHouseRef.child(houseId).child("likedUsers").child(userId).setValue("Liked");
//                                        mProcessLike = false;
//
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//            }
//        });

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(context, CommentActivity.class);
               intent.putExtra("houseId", sharedProp.getHouseId());
               intent.putExtra("owner", sharedProp.getOwnerId());
               intent.putExtra("group", groupId);
               intent.putExtra("userKey", userId);
               context.startActivity(intent);
           }
       });

    }


    private void loadPropertyDetails(SharedProperty sharedProp, SharedPropHolder holder) {
        String owner = sharedProp.getOwnerId();
        String house = sharedProp.getHouseId();
        Log.d("", owner);
        Log.d("", house);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("houses");
        reference.child(owner).child(house)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String rent = snapshot.child("rentPerRoom").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();
                        String state = snapshot.child("state").getValue().toString();
                        String country = snapshot.child("country").getValue().toString();
                        String image = snapshot.child("houseImage").getValue().toString();
                        String description = snapshot.child("houseDescription").getValue().toString();
                        String type = snapshot.child("type").getValue().toString();
                        String room = snapshot.child("noOfRoom").getValue().toString();


                        holder.beds.setText(room);
                        holder.houseType.setText(type);
                        holder.address.setText(address);
                        holder.state.setText(state);
                        holder.country.setText(country);
                        holder.rent.setText(rent);
                        Glide.with(context).load(image).into(holder.housePic);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return shared.size();
    }

    class SharedPropHolder extends RecyclerView.ViewHolder {
        TextView rent,address, houseType, beds, state, country, likes, comments;
        ImageView housePic;
        Button like;


        public SharedPropHolder(@NonNull View itemView) {
            super(itemView);
            this.rent = itemView.findViewById(R.id.sharedRentTv);
            this.address = itemView.findViewById(R.id.sharedAddressTV);
            this.state = itemView.findViewById(R.id.sharedStateTV);
            this.country = itemView.findViewById(R.id.sharedCountryTv);
            this.houseType = itemView.findViewById(R.id.sharedTypeTv);
            this.beds = itemView.findViewById(R.id.sharedRoomsTV);
            this.housePic = itemView.findViewById(R.id.sharedImageview);
           /* this.likes = itemView.findViewById(R.id.likesTv);
            this.comments = itemView.findViewById(R.id.shareCommentsTV);
            this.like = itemView.findViewById(R.id.likeBtn);*/
        }
    }
}
