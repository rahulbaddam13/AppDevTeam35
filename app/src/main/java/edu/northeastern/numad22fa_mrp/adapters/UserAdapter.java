package edu.northeastern.numad22fa_mrp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.numad22fa_mrp.AllUsersActivity;
import edu.northeastern.numad22fa_mrp.MessageActivity;
import edu.northeastern.numad22fa_mrp.R;
import edu.northeastern.numad22fa_mrp.StickItToEm;
import edu.northeastern.numad22fa_mrp.User;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder>{

    private final List<User> users;
    private final Context context;

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
        return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user, null));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.userName.setText(users.get(position).getUserName());

        //on click of each item
        holder.itemView.setOnClickListener(view -> {
            //Move to next activity.
            Bundle bundle = new Bundle();
            bundle.putString("uid", users.get(position).getUID());
            bundle.putString("userName", users.get(position).getUserName());
            bundle.putString("currentUserName", users.get(position).getCurrentUserName());

            Intent clickIntent = new Intent(context, MessageActivity.class);
            clickIntent.putExtras(bundle);
            context.startActivity(clickIntent);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
