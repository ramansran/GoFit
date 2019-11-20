package com.example.gofit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class comment_adapter extends RecyclerView.Adapter<comment_adapter.ViewHolder> {
    Context context;
    List<CommentData> list;
    LayoutInflater layoutInflater;

    public comment_adapter(Context context, List<CommentData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public comment_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(context);
        View view      = layoutInflater.inflate(R.layout.single_comment_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull comment_adapter.ViewHolder holder, int position) {

        CommentData commentData = list.get(position);

        holder.textView.setText(commentData.getComment());
        Glide.with(context)
                .load(commentData.getUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView      = itemView.findViewById(R.id.comment_image);
            textView       = itemView.findViewById(R.id.comment_message);
        }
    }
}
