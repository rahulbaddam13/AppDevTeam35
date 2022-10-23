package edu.northeastern.numad22fa_mrp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import edu.northeastern.numad22fa_mrp.R;
import edu.northeastern.numad22fa_mrp.WeatherActivity;
import edu.northeastern.numad22fa_mrp.model.Periods;
import edu.northeastern.numad22fa_mrp.model.WeatherHeader;
import edu.northeastern.numad22fa_mrp.model.WeatherInfo;

public class RVRetrofitAdapter extends RecyclerView.Adapter<RVRetrofitAdapter.RVHolderRetrofit> {
    Context context;
    WeatherInfo info;
    WeatherHeader header;

    public RVRetrofitAdapter(Context context, WeatherInfo info, WeatherHeader header) {
        this.context = context;
        this.info = info;
        this.header = header;
    }


    @NonNull
    @Override
    public RVHolderRetrofit onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.weather_city,
                    parent, false);
            return new RVHolderRetrofit(view);
        } else if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.weather_layout,
                    parent, false);
            return new RVHolderRetrofit(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.weather_info,
                    parent, false);
            return new RVHolderRetrofit(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolderRetrofit holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if (position == 1) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }


    public class RVHolderRetrofit extends RecyclerView.ViewHolder {
        TextView city;
        TextView state;
        ImageView icon;

        public RVHolderRetrofit(@NonNull View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.city);
            state = itemView.findViewById(R.id.state);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
