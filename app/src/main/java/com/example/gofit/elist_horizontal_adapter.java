package com.example.gofit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class elist_horizontal_adapter extends RecyclerView.Adapter<elist_horizontal_adapter.ViewHolder> {
    List<horizontal_supporteed_class> list;
    Context context;
    LayoutInflater layoutInflater;
    onClickListner onClickListner;

    public elist_horizontal_adapter(List<horizontal_supporteed_class> list, Context context) {
        this.list = list;
        this.context = context;
    }




    public void setOnClickListner(elist_horizontal_adapter.onClickListner onClickListner)
    {
        this.onClickListner = onClickListner;
    }

    public  interface  onClickListner{
        void onitemClick(String str);
    }



    @NonNull
    @Override
    public elist_horizontal_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_horizontal_item,parent,false);
        return new elist_horizontal_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final elist_horizontal_adapter.ViewHolder holder, int position) {

        final horizontal_supporteed_class hsc = list.get(position);

        holder.textView.setText(hsc.getName());



        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onClickListner.onitemClick(hsc.getName());

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.horizontal_text_view);
        }
    }
}
