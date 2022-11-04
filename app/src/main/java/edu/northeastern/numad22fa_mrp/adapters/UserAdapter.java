package edu.northeastern.numad22fa_mrp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.numad22fa_mrp.R;
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
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
