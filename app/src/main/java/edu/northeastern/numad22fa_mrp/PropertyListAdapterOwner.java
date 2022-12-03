package edu.northeastern.numad22fa_mrp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PropertyListAdapterOwner extends RecyclerView.Adapter<PropertyListAdapterOwner.PropertyViewHolder> {

    Context context;
    ArrayList<Property> properties = new ArrayList<>();

    public PropertyListAdapterOwner(Context context, ArrayList<Property> properties) {
        this.context = context;
        this.properties = properties;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.property_card, null, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property model = properties.get(position);

        holder.location.setText(model.getHouseLocation());
        //holder.unit.setText(model.getUnit());
        holder.rentPerRoom.setText(model.getRentPerRoom());
        holder.noOfRoom.setText(model.getNoOfRoom());
        holder.country.setText(model.getCountry());
        holder.state.setText(model.getState());
        Glide.with(context).load(model.getHouseImage()).into(holder.houseImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PropertyDetails.class);

                intent.putExtra("houseId", model.getHouseId());
                intent.putExtra("noOfRoom", model.getNoOfRoom());
                intent.putExtra("rentPerRoom", model.getRentPerRoom());
                intent.putExtra("houseDescription", model.getHouseDescription());
                intent.putExtra("houseLocation", model.getHouseLocation());
                intent.putExtra("houseImage", model.getHouseImage());
                intent.putExtra("userId", model.getUserId());
                intent.putExtra("country",model.getCountry());
                intent.putExtra("state",model.getState());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    static class PropertyViewHolder extends RecyclerView.ViewHolder {
        TextView noOfRoom, rentPerRoom, location, unit, country,state;
        ImageView houseImg;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            houseImg = itemView.findViewById(R.id.imageview);
            noOfRoom = itemView.findViewById(R.id.tv_noOfRooms);
            rentPerRoom = itemView.findViewById(R.id.tv_rentPerRoom);
            location = itemView.findViewById(R.id.tv_location);
            //unit = itemView.findViewById(R.id.tv_unit);
            country= itemView.findViewById(R.id.tv_country);
            state = itemView.findViewById(R.id.tv_state);

        }
    }
}
