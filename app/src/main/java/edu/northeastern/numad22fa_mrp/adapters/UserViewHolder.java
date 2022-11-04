package edu.northeastern.numad22fa_mrp.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.numad22fa_mrp.R;

public class UserViewHolder extends RecyclerView.ViewHolder{

    public TextView userName;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        this.userName = itemView.findViewById(R.id.user_name_text_view);
    }
}
