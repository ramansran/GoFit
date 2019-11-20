package com.example.gofit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class weekAdapter extends RecyclerView.Adapter<weekAdapter.ViewHolder> {
    List<Exercise_Data_Hold_Class> list;
    List<horizontal_supporteed_class> newlist = new ArrayList<>();
    Context context;
    Exercise_Data_Hold_Class edhc;
    int pos;
    String DD;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    onItemClickListner onItemClickListner;

    public weekAdapter(List<Exercise_Data_Hold_Class> list, Context context) {
        this.list = list;
        this.context = context;

    }

    public weekAdapter() {
    }

    @NonNull
    @Override
    public weekAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.weeklist_single_item,parent,false);
        return new weekAdapter.ViewHolder(view);
    }

    public void setOnItemClickListner(weekAdapter.onItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public interface onItemClickListner{
        void onClick(String str);//pass your object types.
    }
    @Override
    public void onBindViewHolder(@NonNull weekAdapter.ViewHolder holder, int position) {

        final Exercise_Data_Hold_Class exercise_data_hold_class = list.get(position);

        holder.textView.setText(exercise_data_hold_class.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListner.onClick(exercise_data_hold_class.getDate());
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

            textView = itemView.findViewById(R.id.weekSet);


        }
    }
}
