package edu.northeastern.numad22fa_mrp.nextrent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.northeastern.numad22fa_mrp.R;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.CommentHolder> {
    Context context;
    ArrayList<Comment> comments;

    public AdapterComment(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,
                parent, false);
        return new CommentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        Comment c = comments.get(position);

        String timeStamp = c.getTime();

        DateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy hh:mm:ss");
        Date date = new Date(Long.parseLong(timeStamp));
        String time = dateFormat.format(date);

        holder.comment.setText(c.getComment());
        holder.name.setText(c.getUserName());
        holder.time.setText(time);

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        TextView name, comment, time;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.commentUsername);
            this.comment = itemView.findViewById(R.id.commentMessage);
            this.time = itemView.findViewById(R.id.commentTime);
        }
    }
}
