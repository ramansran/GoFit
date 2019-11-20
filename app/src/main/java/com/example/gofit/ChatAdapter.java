package com.example.gofit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    String UID;
    List<ChatMainData>list;
    LayoutInflater layoutInflater;
    Context context;
    View view;
    public static final int MSG_TYPE_LEFT =  0 ;
    public static final int MSG_TYPE_RIGHT = 1 ;


    public ChatAdapter(List<ChatMainData> list, Context context)
    {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT)
        {
        layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.chat_item_right,parent,false);
        return new ChatAdapter.ViewHolder(view);
        }
        else
        {
            layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.chat_item_left,parent,false);
            return new ChatAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position)
    {

        ChatMainData chatMainData = list.get(position);
        holder.message.setText(chatMainData.getMessage());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.MI);

        }
    }


    @Override
    public int getItemViewType(int position)
    {
       UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (list.get(position).getSenderId().equals(UID))
        {
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }
    }



}
