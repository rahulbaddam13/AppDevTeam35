package edu.northeastern.numad22fa_mrp.nextrent;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.northeastern.numad22fa_mrp.R;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>{
    private Context context;
    private ArrayList<favProperty> favProps;

    public FavoritesAdapter(Context context, ArrayList<favProperty> favProps) {
        this.context = context;
        this.favProps = favProps;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PropDetailsActivity.class);
                intent.putExtra("houseId",prop.getHouseID());
                intent.putExtra("ownerId", prop.getUserID());
                context.startActivity(intent);


            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreOptionsDialog(prop, holder);

            }
        });



    }

    private void moreOptionsDialog(favProperty prop, FavoritesViewHolder holder) {
        String [] options = {"Add to Group", "Share", "Apply"};
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

                        prop.setCountry(country);
                        prop.setDescription(description);
                        prop.setImage(image);
                        prop.setRent(rent);
                        prop.setLocation(address);
                        prop.setRooms(room);
                        prop.setType(type);
                        prop.setState(state);

                        String add = prop.getAddress();

                        holder.bathrooms.setText("Bathrooms: " + room); //change
                        holder.beds.setText("Bedrooms: " + room);
                        holder.housetype.setText(type);
                        holder.description.setText(description);
                        holder.address.setText(add);
                        holder.rent.setText("$" + rent);
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
        TextView rent,address, description, bathrooms, housetype, beds;
        ImageView housePic;
        ImageButton more;

        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            housePic = itemView.findViewById(R.id.fav_image);
            rent = itemView.findViewById(R.id.rentHeading);
            address = itemView.findViewById(R.id.favAddress);
            description = itemView.findViewById(R.id.favDescription);
            bathrooms = itemView.findViewById(R.id.BathroomsTv);
            housetype = itemView.findViewById(R.id.houseTypeTv);
            beds = itemView.findViewById(R.id.bedTv);
            more = itemView.findViewById(R.id.moreBtn);


        }
    }
}
