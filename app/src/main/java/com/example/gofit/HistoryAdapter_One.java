package com.example.gofit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter_One extends RecyclerView.Adapter<HistoryAdapter_One.ViewHolder> {
    Context context;
    String[] Exercises;
    LayoutInflater layoutInflater;
    onItemClickListner onItemClickListner;

    public HistoryAdapter_One(Context context, String[] exercises) {
        this.context = context;
        Exercises = exercises;
    }


    //InterFace

    public interface onItemClickListner{
        void onClick(String str);//pass your object types.
    }



    public void setOnItemClickListner(HistoryAdapter_One.onItemClickListner onItemClickListner)
    {
        this.onItemClickListner = onItemClickListner;
    }




    @NonNull
    @Override
    public HistoryAdapter_One.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_horizontal_e_select,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryAdapter_One.ViewHolder holder, final int position) {

        holder.textView.setText(Exercises[position]);
        final String name = holder.textView.getText().toString();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListner.onClick(name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Exercises.length;
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.horizontal_text_view);
        }
    }
}
