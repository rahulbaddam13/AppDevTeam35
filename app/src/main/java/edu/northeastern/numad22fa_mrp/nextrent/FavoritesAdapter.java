package edu.northeastern.numad22fa_mrp.nextrent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                List<String> groups = new ArrayList<String>();
                boolean[] checkedItems = new boolean[groups.size()];
                HashMap<String, String> groupIds = new HashMap<>();

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                assert firebaseUser != null;
                String userId = firebaseUser.getUid();

                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Groups");
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        groups.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            if(ds.child("members").child(userId).exists()) {
                                String gName = ds.child("title").getValue().toString();
                                String gId = ds.child("groupId").getValue().toString();
                                groupIds.put(gName, gId);
                                groups.add(gName);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                CharSequence[] chars = groups.toArray(new CharSequence[groups.size()]);


                builder.setTitle("Add Property to Group");
                builder.setMultiChoiceItems(chars, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        checkedItems[i] = b;
                        String current = groups.get(i);
                    }
                });

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i=0; i<checkedItems.length; i++){
                            boolean checked = checkedItems[i];
                            HashMap<String, String> houses = new HashMap<>();
                            houses.put("houseId",prop.getHouseID());
                            houses.put("ownerId", prop.getUserID());
                            houses.put("numLikes", "0");
                            houses.put("numComments", "0");
                            if (checked) {
                                String groupID = groupIds.get(groups.get(i));
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
                                ref.child(groupID).child("sharedHouses").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot ds: snapshot.getChildren()){
                                            if (ds.child(prop.getHouseID()).exists()){
                                                Toast.makeText(context, "Property already exists in group", Toast.LENGTH_SHORT).show();
                                            } else {
                                                ref.child(groupID).child("sharedHouses").child(prop.getHouseID())
                                                        .setValue(houses).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                            }
                                                        });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                        }

                    }
                });
            }
        });



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

                        holder.beds.setText(room);
                        holder.housetype.setText(type);
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
        return favProps.size();
    }


    class FavoritesViewHolder extends RecyclerView.ViewHolder {
        TextView rent,address, housetype, beds, state, country;
        ImageView housePic;
        ImageButton more;

        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            housePic = itemView.findViewById(R.id.fav_image);
            rent = itemView.findViewById(R.id.rentHeading);
            address = itemView.findViewById(R.id.favAddress);
            housetype = itemView.findViewById(R.id.houseTypeTv);
            beds = itemView.findViewById(R.id.bedTv);
            more = itemView.findViewById(R.id.moreBtn);
            state = itemView.findViewById(R.id.favCountryTv);
            country = itemView.findViewById(R.id.favStateTV);


        }
    }
}
