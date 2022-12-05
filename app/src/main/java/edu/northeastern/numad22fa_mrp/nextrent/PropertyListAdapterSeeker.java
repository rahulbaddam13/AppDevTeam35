package edu.northeastern.numad22fa_mrp.nextrent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.northeastern.numad22fa_mrp.Property;
import edu.northeastern.numad22fa_mrp.R;
import edu.northeastern.numad22fa_mrp.adapters.ChatViewHolder;

public class PropertyListAdapterSeeker extends RecyclerView.Adapter<PropertyListAdapterSeeker.PropertyListViewHolder> {

    private Context context;
    private ArrayList<Property> properties = new ArrayList<>();

    public PropertyListAdapterSeeker(Context context, ArrayList<Property> properties) {
        this.context = context;
        this.properties = properties;
    }

    @NonNull
    @Override
    public PropertyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PropertyListViewHolder(LayoutInflater.from(context).inflate(R.layout.seeker_property_card,
                null));
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyListViewHolder holder, int position) {
        Property model = properties.get(position);

        holder.location.setText(model.getHouseLocation());
        holder.rentPerRoom.setText(model.getRentPerRoom());

    }

    @Override
    public int getItemCount() {
        if(properties == null){
            return 0;
        }
        return properties.size();
    }

    static class PropertyListViewHolder extends RecyclerView.ViewHolder {
        TextView noOfRoom, rentPerRoom, location, unit, country,state;
        ImageView houseImg;

        public PropertyListViewHolder(@NonNull View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.property_address);
            rentPerRoom = itemView.findViewById(R.id.property_rent);
        }
    }
}
