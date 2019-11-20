package com.example.gofit;


import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class MySharedItems extends Fragment {
FirebaseFirestore database;
List<MyPostData> list;
String userID;
RecyclerView recyclerView;
LinearLayoutManager linearLayoutManager;
Context context;


    public MySharedItems() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_shared_items, container, false);

        context               = getContext();
        database              = FirebaseFirestore.getInstance();
        userID                = FirebaseAuth.getInstance().getCurrentUser().getUid();
        list                  = new ArrayList<>();
        recyclerView          = view.findViewById(R.id.SharedList);
        linearLayoutManager   = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        getMyPosts();

        return view;
    }


    public void getMyPosts()
    {
        database.collection("SharedPosts").whereEqualTo("UserId",userID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null)
                {
                    Log.w(TAG,"Listen Failed" ,e);
                    return;
                }
                for (QueryDocumentSnapshot doc :queryDocumentSnapshots)
                {
                    String pic  = doc.getString("url");


                    MyPostData myPostData    = new MyPostData(pic);
                    list.add(myPostData);

                    MyPostAdapter myPostAdapter = new MyPostAdapter(list,getActivity());
                    recyclerView.setAdapter(myPostAdapter);
                    myPostAdapter.notifyDataSetChanged();

                }
            }
        });
    }

}
