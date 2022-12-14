package edu.northeastern.numad22fa_mrp.nextrent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

import edu.northeastern.numad22fa_mrp.Property;
import edu.northeastern.numad22fa_mrp.PropertyContents;
import edu.northeastern.numad22fa_mrp.PropertyContentsSeeker;
import edu.northeastern.numad22fa_mrp.R;
import edu.northeastern.numad22fa_mrp.adapters.ChatViewHolder;

public class PropertyListAdapterSeeker extends RecyclerView.Adapter<PropertyListAdapterSeeker.PropertyListViewHolder>{

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

        Glide.with(context).load(model.getHouseImage()).into(holder.houseImg);
        holder.city.setText(model.getHouseLocation());
        holder.address.setText(model.getAddress());
        holder.state.setText(model.getState());
        holder.country.setText(model.getCountry());
        holder.numberOfBeds.setText(model.getNoOfRoom());
//        holder.numberOfBaths.setText(model.getBaths());
        holder.propertyType.setText(model.getType());
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
        ImageView houseImg;
        TextView address ,city, state, country;
        TextView numberOfBeds, numberOfBaths, propertyType;
        TextView rentPerRoom;

        public PropertyListViewHolder(@NonNull View itemView) {
            super(itemView);
            houseImg = itemView.findViewById(R.id.prop_image);
            address = itemView.findViewById(R.id.property_address);
            city = itemView.findViewById(R.id.prop_city_tv);
            state = itemView.findViewById(R.id.prop_state_tv);
            country = itemView.findViewById(R.id.prop_country_tv);
            numberOfBeds = itemView.findViewById(R.id.number_of_beds);
            numberOfBaths = itemView.findViewById(R.id.number_of_baths);
            propertyType = itemView.findViewById(R.id.prop_type_tv);
            rentPerRoom = itemView.findViewById(R.id.property_rent);
        }
    }
}
