package edu.northeastern.numad22fa_mrp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.northeastern.numad22fa_mrp.R;
import edu.northeastern.numad22fa_mrp.model.RecyclerViewHolder;
import edu.northeastern.numad22fa_mrp.model.WeatherRecyclerViewItem;

public class RVRetrofitAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private static final String TAG = "";
    private final Context context;
    private final ArrayList<WeatherRecyclerViewItem> items;

    public RVRetrofitAdapter(Context context, ArrayList<WeatherRecyclerViewItem> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case WeatherRecyclerViewItem.LOCATION:
                view = LayoutInflater.from(context).inflate(R.layout.weather_city,
                        parent, false);
                return new RecyclerViewHolder.HeaderHolder(view);
            case WeatherRecyclerViewItem.DATE:
                view = LayoutInflater.from(context).inflate(R.layout.weather_layout,
                        parent, false);
                return new RecyclerViewHolder.DateHolder(view);
            case WeatherRecyclerViewItem.INFORMATION:
                view = LayoutInflater.from(context).inflate(R.layout.weather_info,
                        parent, false);
                return new RecyclerViewHolder.InfoHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        WeatherRecyclerViewItem object = items.get(position);
        Log.d(TAG, "onBindViewHolder: " + position);
        if (object !=null) {
            if (position == 0) {
                    ((RecyclerViewHolder.HeaderHolder) holder).city.setText
                            (((WeatherRecyclerViewItem.Header) object).getCity());
                    ((RecyclerViewHolder.HeaderHolder) holder).state.setText
                            (((WeatherRecyclerViewItem.Header) object).getState());
            }
            else if (position == 1) {
                ((RecyclerViewHolder.DateHolder) holder).date.setText
                        (((WeatherRecyclerViewItem.Date) object).getDate());
            }
            else if (position > 1) {
                    ((RecyclerViewHolder.InfoHolder) holder).name.setText
                            (((WeatherRecyclerViewItem.Period) object).getName());
                    ((RecyclerViewHolder.InfoHolder) holder).temperature.setText
                            (String.valueOf(((WeatherRecyclerViewItem.Period) object).getTemperature()));
                    ((RecyclerViewHolder.InfoHolder) holder).description.setText
                            (((WeatherRecyclerViewItem.Period) object).getDescription());
                    String imageurl = ((WeatherRecyclerViewItem.Period) object).getIcon();
                    Picasso.get().load(imageurl).into(((RecyclerViewHolder.InfoHolder) holder).icon);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        switch(items.get(position).type) {
            case 0:
                return WeatherRecyclerViewItem.LOCATION;
            case 1:
                return WeatherRecyclerViewItem.DATE;
            case 2:
                return WeatherRecyclerViewItem.INFORMATION;
            default:
                return -1;

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
