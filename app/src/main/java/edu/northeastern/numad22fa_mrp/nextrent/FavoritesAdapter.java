package edu.northeastern.numad22fa_mrp.nextrent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.northeastern.numad22fa_mrp.PropertyContentsSeeker;
import edu.northeastern.numad22fa_mrp.R;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>{
    private Context context;
    private ArrayList<favProperty> favProps;
    private String userKey;

    public FavoritesAdapter(Context context, ArrayList<favProperty> favProps, String userKey) {
        this.context = context;
        this.favProps = favProps;
        this.userKey = userKey;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_property_card,
                parent, false);
        return new FavoritesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        favProperty prop = favProps.get(position);

        loadPropertyDetails(prop, holder);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, PropDetailsActivity.class);
//                intent.putExtra("houseId",prop.getHouseID());
//                intent.putExtra("ownerId", prop.getUserID());
//                context.startActivity(intent);
//
//
//            }
//        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(context, PropertyEditDetails.class);
                Intent intent = new Intent(context, PropertyContentsSeeker.class);
                intent.putExtra("houseId", prop.getHouseID());
                intent.putExtra("noOfRoom", prop.getRooms());
                intent.putExtra("rentPerRoom", prop.getRent());
                intent.putExtra("houseDescription", prop.getDescription());
                intent.putExtra("houseLocation", prop.getLocation());
                intent.putExtra("houseImage", prop.getImage());
                intent.putExtra("userId", prop.getUserID());
                intent.putExtra("country",prop.getCountry());
                intent.putExtra("state",prop.getState());
                intent.putExtra("type",prop.getType());
                intent.putExtra("address",prop.getAddress());
                intent.putExtra("baths",prop.getBaths());
                intent.putExtra("userKey",userKey);
                context.startActivity(intent);
            }
        });



        /*holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PickGroupActivity.class);
                intent.putExtra("owner", prop.getUserID());
                intent.putExtra("house",prop.getHouseID());
                intent.putExtra("userKey",userKey);
                context.startActivity(intent);
            }
        });*/




    }

    private void loadPropertyDetails(favProperty prop, FavoritesViewHolder holder) {
        String ownerId = prop.getUserID();
        String house = prop.getHouseID();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("houses");
        reference.child(ownerId).child(house)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String rent = snapshot.child("rentPerRoom").getValue().toString();
                        String address = snapshot.child("houseLocation").getValue().toString();
                        String state = snapshot.child("state").getValue().toString();
                        String country = snapshot.child("country").getValue().toString();
                        String image = snapshot.child("houseImage").getValue().toString();
                        String description = snapshot.child("houseDescription").getValue().toString();
                        String type = snapshot.child("type").getValue().toString();
                        String room = snapshot.child("noOfRoom").getValue().toString();
                        String addressLine = snapshot.child("address").getValue().toString();
                        String baths = snapshot.child("baths").getValue().toString();

                        prop.setCountry(country);
                        prop.setDescription(description);
                        prop.setImage(image);
                        prop.setRent(rent);
                        prop.setLocation(address);
                        prop.setRooms(room);
                        prop.setType(type);
                        prop.setState(state);
                        prop.setAddress(addressLine);
                        prop.setBaths(baths);

                        holder.beds.setText(room);
                        holder.housetype.setText(type);
                        holder.address.setText(address);
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
        return favProps.size();
    }


    class FavoritesViewHolder extends RecyclerView.ViewHolder {
        TextView rent,address, housetype, beds;
        ImageView housePic;
        ImageButton more;

        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            housePic = itemView.findViewById(R.id.fav_image);
            rent = itemView.findViewById(R.id.rentHeading);
            address = itemView.findViewById(R.id.favAddress);
            housetype = itemView.findViewById(R.id.houseTypeTv);
            beds = itemView.findViewById(R.id.bedTv);
            //more = itemView.findViewById(R.id.moreBtn);


        }
    }
}
