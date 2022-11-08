package edu.northeastern.numad22fa_mrp.adapters;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import edu.northeastern.numad22fa_mrp.MessageActivity;
import edu.northeastern.numad22fa_mrp.R;
import edu.northeastern.numad22fa_mrp.User;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder>{

    private final List<User> users;
    private final Context context;
    String currentUsername;
    DatabaseReference messages;

    /**
     * Constructs a UserAdapter with the array list of user objects.
     * @param users arraylist of user object.
     * @param context context of the activity.
     */
    public UserAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user_card, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.userName.setText(users.get(position).getUserName());

        String currentUsername =  users.get(position).getCurrentUserName();
        String username = users.get(position).getUserName();

        //on click of each item
        holder.itemView.setOnClickListener(view -> {
            //Move to next activity.
            Bundle bundle = new Bundle();
            bundle.putString("uid", users.get(position).getUID());
            bundle.putString("userName", username);
            bundle.putString("currentUserName", currentUsername);

            Intent clickIntent = new Intent(context, MessageActivity.class);
            clickIntent.putExtras(bundle);
            context.startActivity(clickIntent);
        });
    }


    @Override
    public int getItemCount() {
        if(users == null)
            return 0;
        return users.size();
    }
}
