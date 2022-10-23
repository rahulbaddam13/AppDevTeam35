package edu.northeastern.numad22fa_mrp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.numad22fa_mrp.R;
import edu.northeastern.numad22fa_mrp.model.WeatherData;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private List<WeatherData> weatherDataList;
    private Context context;

    public WeatherAdapter(List<WeatherData> weatherDataList, Context context) {
        this.weatherDataList = weatherDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public WeatherAdapter.WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.weather_info, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.WeatherViewHolder holder, int position) {

        holder.temperature.setText(weatherDataList.get(position).getDaily().getTemperature_2m_max().get(position));

    }

    @Override
    public int getItemCount() {
        return weatherDataList.size();
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView temperature;
        //private ImageView coverImage;

        WeatherViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            temperature = mView.findViewById(R.id.temperature);
            //coverImage = mView.findViewById(R.id.coverImage);
        }
    }
}
