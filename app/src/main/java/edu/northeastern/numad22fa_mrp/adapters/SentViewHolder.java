package edu.northeastern.numad22fa_mrp.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.numad22fa_mrp.R;

public class SentViewHolder extends RecyclerView.ViewHolder {
    TextView count;
    ImageView sticker;

    public SentViewHolder(@NonNull View itemView) {
        super(itemView);
        this.count = itemView.findViewById(R.id.sent_sticker_count);
        this.sticker = itemView.findViewById(R.id.sent_sticker);
    }
}
