package com.example.myapplication.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
//import com.google.firebase.database.core.Context;
import android.content.Context;
import android.widget.Toast;

import com.example.myapplication.Register;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

    private final List<MessagesList> messagesLists;
    private final Context context;
    private Picasso picasso = null;

    public MessagesAdapter(List<MessagesList> messagesLists, Context context) {
        this.messagesLists = messagesLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MyViewHolder holder, int position) {
        MessagesList list2 = messagesLists.get(position);

        if (list2.getProfilePic().isEmpty()){
           // picasso.get().load(list2.getProfilePic()).into(holder.profilePic); // THIS IS WHERE THE ERROR IS
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }

        holder.name.setText(list2.getName());
        holder.lastMessage.setText(list2.getLastMessage());

        if (!(list2.getUnseenMessages() > 0)) {
            holder.unseenMessages.setVisibility(View.GONE);
        } else {
            holder.unseenMessages.setVisibility(View.VISIBLE);

        }
    }

    public int getItemCount() {
        return messagesLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profilePic;
        private TextView name;
        private TextView lastMessage;
        private TextView unseenMessages;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profilePic);
            name = itemView.findViewById(R.id.name);
            lastMessage =itemView.findViewById(R.id.lastMessage);
            unseenMessages =itemView.findViewById(R.id.unseenMessages);
        }
    }
}
