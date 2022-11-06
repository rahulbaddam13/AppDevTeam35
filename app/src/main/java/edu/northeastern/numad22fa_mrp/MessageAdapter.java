package edu.northeastern.numad22fa_mrp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {
    Context context;
    List<ChatMessage> chats;
    int sendFlag = 1;
    int receiveFlag = 2;

    public MessageAdapter(Context context, List<ChatMessage> chats) {
        this.context = context;
        this.chats = chats;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == sendFlag) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_chat_layout, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_chat_layout, parent, false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getClass() == SenderViewHolder.class) {
            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
            senderViewHolder.sUserName.setText(chats.get(position).getSender());
            senderViewHolder.senderDate.setText(chats.get(position).getTimestamp());
            long imageID = chats.get(position).getImageID();
            System.out.println("imageID "+imageID);
            if (imageID == 2131165308) {
                senderViewHolder.displaySticker.setImageResource(R.drawable.happy_fox);
            } else if (imageID == 2131165367) {
                senderViewHolder.displaySticker.setImageResource(R.drawable.sad_fox);
            } else if (imageID == 2131165271) {
                senderViewHolder.displaySticker.setImageResource(R.drawable.angry_fox);
            } else if (imageID == 2131165309) {
                senderViewHolder.displaySticker.setImageResource(R.drawable.hungry_fox);
            } else if (imageID == 2131165325) {
                senderViewHolder.displaySticker.setImageResource(R.drawable.love_fox);
            } else if (imageID == 2131165368) {
                senderViewHolder.displaySticker.setImageResource(R.drawable.sick_fox);
            } else {
                senderViewHolder.displaySticker.setImageResource(R.drawable.not_found);
            }
        }

        if (holder.getClass() == ReceiverViewHolder.class) {
            ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
            receiverViewHolder.rUserName.setText(chats.get(position).getSender());
            receiverViewHolder.receiverDate.setText(chats.get(position).getTimestamp());
            long imageID = chats.get(position).getImageID();
            if (imageID == 2131165308) {
                receiverViewHolder.displaySticker.setImageResource(R.drawable.happy_fox);
            } else if (imageID == 2131165367) {
                receiverViewHolder.displaySticker.setImageResource(R.drawable.sad_fox);
            } else if (imageID == 2131165271) {
                receiverViewHolder.displaySticker.setImageResource(R.drawable.angry_fox);
            } else if (imageID == 2131165309) {
                receiverViewHolder.displaySticker.setImageResource(R.drawable.hungry_fox);
            } else if (imageID == 2131165325) {
                receiverViewHolder.displaySticker.setImageResource(R.drawable.love_fox);
            } else if (imageID == 2131165368) {
                receiverViewHolder.displaySticker.setImageResource(R.drawable.sick_fox);
            }  else {
                receiverViewHolder.displaySticker.setImageResource(R.drawable.not_found);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        Intent intent = ((Activity) context).getIntent();
        ChatMessage message = chats.get(position);
        if (intent.getExtras().getString("currentUserName").equals(message.getSender())) {
            return sendFlag;
        } else {
            return receiveFlag;
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView sUserName;
        TextView senderDate;
        ImageView displaySticker;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            sUserName = itemView.findViewById(R.id.sender);
            displaySticker = itemView.findViewById(R.id.senderImage);
            senderDate = itemView.findViewById(R.id.sender_date);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView rUserName;
        TextView receiverDate;
        ImageView displaySticker;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            rUserName = itemView.findViewById(R.id.receive);
            displaySticker = itemView.findViewById(R.id.receiverImage);
            receiverDate = itemView.findViewById(R.id.receiver_date);
        }
    }
}