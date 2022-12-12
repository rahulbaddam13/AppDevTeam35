package edu.northeastern.numad22fa_mrp.nextrent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import edu.northeastern.numad22fa_mrp.R;

public class FavoriteGroupAdapter extends RecyclerView.Adapter<FavoriteGroupAdapter.FavoriteGroupHolder> {
    private Context context;
    private ArrayList<Group> favGroups;
    String houseId, ownerId;

    public FavoriteGroupAdapter(Context context, ArrayList<Group> favGroups, String houseId, String ownerId) {
        this.context = context;
        this.favGroups = favGroups;
        this.houseId = houseId;
        this.ownerId = ownerId;
    }

    @NonNull
    @Override
    public FavoriteGroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_group_item, parent, false);
        return new FavoriteGroupHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteGroupHolder holder, int position) {
        Group g = favGroups.get(position);
        holder.groups.setText(g.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Add Property")
                        .setMessage("Add current Property to Shared group?")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                addGroup(g);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

    }

    private void checkAlready(Group g) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
        ref.child(g.getGroupID()).child("sharedHouses").child(houseId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Toast.makeText(context,
                                    "This property already exists in group",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            addGroup(g);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void addGroup(Group g) {
        HashMap<String, String> houses = new HashMap<>();
        houses.put("houseId",houseId);
        houses.put("ownerId", ownerId);
        houses.put("numLikes", "0");
        houses.put("numComments", "0");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
        ref.child(g.getGroupID()).child("sharedHouses").child(houseId).setValue(houses)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Added to Group!", Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    @Override
    public int getItemCount() {
        return favGroups.size();
    }

    class FavoriteGroupHolder extends RecyclerView.ViewHolder{
        TextView groups;

        public FavoriteGroupHolder(@NonNull View itemView) {
            super(itemView);
            this.groups = itemView.findViewById(R.id.favGroup);

        }
    }

}
