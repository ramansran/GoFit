package com.example.gofit;


import android.os.AsyncTask;
import android.os.Bundle;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class History extends Fragment {
    RecyclerView recyclerView1,recyclerView2;
    HistoryAdapter_One historyAdapter_one;
    String ENAME;
    private FirebaseFirestore databse = FirebaseFirestore.getInstance();
    private String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    List<history_holding_class> list ;
    String[] Exercises =
             {
                     "cycling",
                     "RopeJumping",
                     "Meditate",
                     "PushUp",
                     "Running",
                     "Swimming",
                     "WeightLifting",
                     "Yoga",
                     "jogging"
            };




    public History() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);


        recyclerView1  = view.findViewById(R.id.E_Select);
        recyclerView2  = view.findViewById(R.id.E_History);
        list           = new ArrayList<>();
        historyAdapter_one = new HistoryAdapter_One(getActivity(),Exercises);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView1.setAdapter(historyAdapter_one);
        historyAdapter_one.notifyDataSetChanged();

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        recyclerView2.addItemDecoration(itemDecorator);


        view.findViewById(R.id.Chat_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Chat_List_Frag();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
                ft.replace(R.id.frame,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
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

        view.findViewById(R.id.trainers_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Posts_Frag();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
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
            new getHistory().execute();


        }

        return  view;
    }



    public class  getHistory extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {


            historyAdapter_one.setOnItemClickListner(new HistoryAdapter_One.onItemClickListner() {
                @Override
                public void onClick(String str) {
                    Log.d("str ,", str);

                    list.clear();

                    databse.collection("Activities").document(UID).collection("CompletedExercises")
                            .document(str).collection(str).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e!=null)
                            {
                                Log.w(TAG, "listen:error", e);
                                return;
                            }

                            for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges())
                            {
                                switch (dc.getType())
                                {
                                    case ADDED:
                                        Log.d(TAG, "history data: " + dc.getDocument().getData());

                                        String NAME          =  dc.getDocument().getString("Name_of_Exercise");
                                        String STARTING_DATE =  dc.getDocument().getString("Starting_Date");
                                        String ENDING_DATE   = dc.getDocument().getString("Ending_Date");
                                        String FULL_HOURS    = dc.getDocument().getString("Total_hours");
                                        Boolean sharedornot  = (Boolean) dc.getDocument().get("Shared");


                                        history_holding_class history_holding_class = new history_holding_class(NAME,STARTING_DATE,ENDING_DATE,FULL_HOURS,sharedornot);
                                        list.add(history_holding_class);

                                        HistoryAdapter_Two historyAdapter_two = new HistoryAdapter_Two(getActivity(),list);
                                        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));

                                        recyclerView2.setAdapter(historyAdapter_two);
                                        historyAdapter_two.notifyDataSetChanged();

                                        break;
                                    case MODIFIED:
                                        break;
                                    case REMOVED:
                                        break;
                                }

                            }










                            }
                    });

                }
            });


            return null;
        }
    }
















}
