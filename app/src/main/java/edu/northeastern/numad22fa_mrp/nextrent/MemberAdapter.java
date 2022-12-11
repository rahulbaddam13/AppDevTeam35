package edu.northeastern.numad22fa_mrp.nextrent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import edu.northeastern.numad22fa_mrp.R;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberHolder>{
    private Context context;
    private ArrayList<Member> members;
    private String groupId;

    public MemberAdapter(Context context, ArrayList<Member> members, String groupId) {
        this.context = context;
        this.members = members;
        this.groupId = groupId;
    }

    @NonNull
    @Override
    public MemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.member_item, parent, false);
        return new MemberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberHolder holder, int position) {
        Member m = members.get(position);
        holder.userName.setText(m.getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Add Member")
                        .setMessage("Add user to this group?")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                addMember(m);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

    }

    private void addMember(Member m) {
        HashMap<String, String> memberMap = new HashMap<>();
        memberMap.put("uid", m.getId());
        memberMap.put("userName", m.getUsername());

        DatabaseReference memberRef = FirebaseDatabase.getInstance()
                .getReference("groups");
        memberRef.child(groupId).child("members").child(m.getId())
                .setValue(memberMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Added user successfully!", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    class MemberHolder extends RecyclerView.ViewHolder {

        TextView userName;

        public MemberHolder(@NonNull View itemView) {
            super(itemView);
            this.userName = itemView.findViewById(R.id.memberUsername);
        }
    }
}
