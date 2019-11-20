package com.example.gofit;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

public class ChatList_Adapter extends RecyclerView.Adapter<ChatList_Adapter.ViewHolder> {
   LayoutInflater layoutInflater;
   Context context;
   List<ExtrasData> list;
   ExtrasData extrasData1;
    String userId;
    String Url;
    int pos;

    public ChatList_Adapter(Context context, List<ExtrasData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChatList_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_chat_item,parent,false);
        return new ChatList_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatList_Adapter.ViewHolder holder, int position) {
        ExtrasData extrasData = list.get(position);
             Url            = extrasData.getProfile_pic_url();
             userId                = extrasData.getUID();
        Log.d("userId",userId);

        Glide.with(context)
                .load(Url)
                .into(holder.imageView);

        holder.textView.setText(extrasData.getUsername());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        TextView textView1;
        TextView textView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.profile_chat_image);
            textView  = itemView.findViewById(R.id.Recipient_name);
            textView1 = itemView.findViewById(R.id.Last_msg_textview);
            textView2 = itemView.findViewById(R.id.chat_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pos = getAdapterPosition();

                    extrasData1 = list.get(pos);
                    String UID  = extrasData1.getUID();


                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    Fragment fragment = new ChatFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("OtherUserId", UID);
                    fragment.setArguments(bundle);
                    FragmentManager fm = appCompatActivity.getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.frame, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });



        }
    }
}
