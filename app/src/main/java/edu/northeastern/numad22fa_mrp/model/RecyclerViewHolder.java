package edu.northeastern.numad22fa_mrp.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.numad22fa_mrp.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public static class HeaderHolder extends RecyclerViewHolder{

        public TextView city;
        public TextView state;

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);

            this.city = itemView.findViewById(R.id.city);
            this.state = itemView.findViewById(R.id.state);
        }
    }

    public static class DateHolder extends RecyclerViewHolder{

        public TextView date;



        public DateHolder(@NonNull View itemView) {
            super(itemView);

            this.date = itemView.findViewById(R.id.date);
        }
    }

    public static class InfoHolder extends RecyclerViewHolder{

        public TextView name;
        public TextView temperature;
        public ImageView icon;
        public TextView description;


        public InfoHolder(@NonNull View itemView) {
            super(itemView);

            this.name = itemView.findViewById(R.id.name);
            this.temperature = itemView.findViewById(R.id.temperature);
            this.icon = itemView.findViewById(R.id.icon);
            this.description = itemView.findViewById(R.id.description);
        }
    }



}
