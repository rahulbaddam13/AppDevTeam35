package edu.northeastern.numad22fa_mrp.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.numad22fa_mrp.R;

public class ChatViewHolder extends RecyclerView.ViewHolder{

    public TextView sentUserName;
    public ImageView displaySticker;
    public TextView sentTimeStamp;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        this.sentUserName = itemView.findViewById(R.id.displaySentUserName);
        this.displaySticker = itemView.findViewById(R.id.displayStickers);
        this.sentTimeStamp = itemView.findViewById(R.id.displaySentTime);
    }
}
