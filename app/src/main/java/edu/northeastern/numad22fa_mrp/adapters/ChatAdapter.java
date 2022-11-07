package edu.northeastern.numad22fa_mrp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.numad22fa_mrp.ChatMessage;
import edu.northeastern.numad22fa_mrp.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder>{

    private final List<ChatMessage> chats;
    private final Context context;

    public ChatAdapter(List<ChatMessage> chats, Context context) {
        this.chats = chats;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat,
                null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        holder.sentUserName.setText(chats.get(position).getSender());
        holder.sentTimeStamp.setText(chats.get(position).getTimestamp());

        long imageID = chats.get(position).getImageID();
        if(imageID == 2131165308){
            holder.displaySticker.setImageResource(R.drawable.happy_fox);
        } else if(imageID == 2131165368){
            holder.displaySticker.setImageResource(R.drawable.sad_fox);
        } else if(imageID == 2131165271){
            holder.displaySticker.setImageResource(R.drawable.angry_fox);
        } else if(imageID == 2131165309){
            holder.displaySticker.setImageResource(R.drawable.hungry_fox);
        } else if (imageID == 2131165325){
            holder.displaySticker.setImageResource(R.drawable.love_fox);
        } else if(imageID == 2131165369){
            holder.displaySticker.setImageResource(R.drawable.sick_fox);
        } else {
            holder.displaySticker.setImageResource(R.drawable.not_found);
        }
    }

    @Override
    public int getItemCount() {
        if(chats == null){
            return 0;
        }
        return chats.size();
    }
}
