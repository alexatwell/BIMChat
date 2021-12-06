package com.example.myapplication.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MemoryData;
import com.example.myapplication.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<ChatList> chatlists;
    private final Context context;
    private String userMobile;


    public ChatAdapter(List<ChatList> chatlists, Context context) {
        this.chatlists = chatlists;
        this.context = context;
        this.userMobile = MemoryData.getData(context);
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        ChatList list2 = chatlists.get(position);

        if (list2.getMobile().equals(userMobile)){
            holder.user2Layout.setVisibility(View.GONE);
            holder.user1Layout.setVisibility(View.VISIBLE);
            holder.user1Msg.setText(list2.getMessage());
            holder.user1Time.setText(list2.getDate()+ "" + list2.getTime());
        }else{
            holder.user1Layout.setVisibility(View.GONE);
            holder.user2Layout.setVisibility(View.VISIBLE);
            holder.user2Msg.setText(list2.getMessage());
            holder.user2Time.setText(list2.getDate()+ "" + list2.getTime());

        }
    }

    public void updateChatList(List<ChatList> chatLists){
        this.chatlists= chatLists;
    }

    @Override
    public int getItemCount() {
        return chatlists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout user2Layout, user1Layout;
        private TextView user2Msg, user1Msg;
        private TextView user2Time, user1Time;
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            user2Layout = itemView.findViewById(R.id.u2_layout);
            user1Layout = itemView.findViewById(R.id.u1_layout);
            user2Msg = itemView.findViewById(R.id.u2_message);
            user1Msg = itemView.findViewById(R.id.u1_message);
            user2Time = itemView.findViewById(R.id.u2_msgTime);
            user1Time = itemView.findViewById(R.id.u1_msgTime);
        }
    }
}
