package com.example.gofit;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Chat_List_Frag extends Fragment {
RecyclerView recyclerView;
List<ExtrasData>  list;
List<String> Templist;
FirebaseFirestore database;
String UID;
LinearLayoutManager linearLayoutManager;
CircleImageView circleImageView;
TextView textView;
Context context;

    public Chat_List_Frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat__list_, container, false);

        recyclerView = view.findViewById(R.id.chats_list);
        Templist = new ArrayList();
        list  = new ArrayList<>();
        database = FirebaseFirestore.getInstance();
        UID    = FirebaseAuth.getInstance().getCurrentUser().getUid();
        circleImageView = view.findViewById(R.id.profile_image);
        textView       = view.findViewById(R.id.user_name);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        context       = getContext();





        view.findViewById(R.id.exercise_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ExerciseListFrag();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
                ft.replace(R.id.frame,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        view.findViewById(R.id.Profile_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Profile_Frag();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
                ft.replace(R.id.frame,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        view.findViewById(R.id.trainers_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Posts_Frag();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
                ft.replace(R.id.frame,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            new GetChats().execute();
            new getProfileData().execute();

        }
        return view;



    }

    public class  GetChats extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            database.collection("Chats").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e)
                        {

                            Templist.clear();
                            if (e!=null)
                            {
                                Log.w(TAG, "listen:error", e);
                                return;
                            }
                            else
                            {

                                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots)
                                  {

                                          ChatMainData chatMainData = snapshot.toObject(ChatMainData.class);
                                          if (chatMainData.getSenderId().equals(UID))
                                          {
                                              if (!Templist.contains(chatMainData.getRecieverId()))
                                              {
                                                  Templist.add(chatMainData.getRecieverId());
                                              }

                                          }
                                          if (chatMainData.getRecieverId().equals(UID))
                                          {
                                              if (!Templist.contains(chatMainData.getSenderId()))
                                              {
                                                  Templist.add(chatMainData.getSenderId());
                                              }
                                          }


                            }
                                Log.d("tempList :" , String.valueOf(Templist));
                                   readChats();
                        }}
                    });


            return null;
        }
    }



    public void readChats()
    {
        database.collection("Extras").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e)
            {
                list.clear();

                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots)
                {
                    ExtrasData extrasData = snapshot.toObject(ExtrasData.class);
                    for (String id : Templist)
                    {
                         if (extrasData.getUID().equals(id))
                         {
                             list.add(extrasData);

                         }
                    }

                }
                Log.d("new List " , String.valueOf(list));

                ChatList_Adapter chatList_adapter = new ChatList_Adapter(getActivity(),list);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(chatList_adapter);
                chatList_adapter.notifyDataSetChanged();


            }
        });
    }



    public class getProfileData extends  AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {

            database.collection("users").document(UID)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                            if (snapshot!=null && snapshot.exists())
                            {
                                String Url = snapshot.getString("profile_pic_url");
                                String name = snapshot.getString("username");
                                Glide.with(context)
                                        .load(Url)
                                        .into(circleImageView);
                                textView.setText(name);

                            }
                        }
                    });
            return null;
        }
    }









}
