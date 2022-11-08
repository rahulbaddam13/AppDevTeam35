package edu.northeastern.numad22fa_mrp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.northeastern.numad22fa_mrp.R;
import edu.northeastern.numad22fa_mrp.SentItem;

public class SentAdapter extends RecyclerView.Adapter<SentViewHolder> {

    private static final String TAG = "";
    Context context;
    ArrayList<SentItem> sentItems;

    public SentAdapter(Context context, ArrayList<SentItem> sentItems) {
        this.context = context;
        this.sentItems = sentItems;
    }

    @NonNull
    @Override
    public SentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SentViewHolder(LayoutInflater.from(context).inflate(R.layout.sent_number_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull SentViewHolder holder, int position) {
        SentItem sent = sentItems.get(position);
        holder.count.setText(sent.getCount());
        Log.d(TAG, "Image:" + sent.getImageID());

        switch(sent.getImageID()) {
            case "2131165308":
                holder.sticker.setImageResource(R.drawable.happy_fox);
                break;
            case "2131165368":
                holder.sticker.setImageResource(R.drawable.sad_fox);
                break;
            case "2131165271":
                holder.sticker.setImageResource(R.drawable.angry_fox);
                break;
            case "2131165309":
                holder.sticker.setImageResource(R.drawable.hungry_fox);
                break;
            case "2131165325":
                holder.sticker.setImageResource(R.drawable.love_fox);
                break;
            case "2131165369":
                holder.sticker.setImageResource(R.drawable.sick_fox);
                break;
            default:
                holder.sticker.setImageResource(R.drawable.not_found);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return sentItems.size();
    }
}
