package com.example.gofit;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Days_Frag extends Fragment {
    View view;
NumberPicker numberPickerDay1Hour,numberPickerDay1minutes,numberPickerDay1seconds;
NumberPicker numberPickerDay2Hour,numberPickerDay2minutes;
NumberPicker numberPickerDay3Hour,numberPickerDay3minutes;
NumberPicker numberPickerDay4Hour,numberPickerDay4minutes;
NumberPicker numberPickerDay5Hour,numberPickerDay5minutes;
NumberPicker numberPickerDay6Hour,numberPickerDay6minutes;
NumberPicker numberPickerDay7Hour,numberPickerDay7minutes;

Button       SkipDay1,SkipDay2,SkipDay3,SkipDay4,SkipDay5,SkipDay6,SkipDay7;
    Button close;
Boolean    SD1 = false;
Boolean    SD2 = false;
Boolean    SD3 = false;
Boolean    SD4 = false;
Boolean    SD5 = false;
Boolean    SD6 = false;
Boolean    SD7 = false;

Button       DoneDay1,DoneDay2,DoneDay3,DoneDay4,DoneDay5,DoneDay6,DoneDay7;
Boolean    DD1 = false;
Boolean    DD2 = false;
Boolean    DD3 = false;
Boolean    DD4 = false;
Boolean    DD5 = false;
Boolean    DD6 = false;
Boolean    DD7 = false;

TextView EXERCISENAME;
TextView EXERCISEHOURS;
ProgressBar progressBar;
LinearLayout linearLayoutDay1,linearLayoutDay2,linearLayoutDay3,linearLayoutDay4,linearLayoutDay5,linearLayoutDay6,linearLayoutDay7;
Context context;
LayoutInflater layoutInflater;
int width = LinearLayout.LayoutParams.MATCH_PARENT;
int height = LinearLayout.LayoutParams.WRAP_CONTENT;
boolean focusable = true;

    String EndDate;
    String StartDate;
    long CurrentProgress;
    String HoursPerDay;
    String ExerciseName;
    String DayN;
    long PerDayMinutes;
    long TotalMinutes;
    String  TotalHoursOfWeek;
    int numberPickerHours;
    int numberPickerMinutes;
    Boolean d7 = false;

    Map<String,Object> historyMap ;

RecyclerView Horizontal_recyclerView;
weekAdapter wA;
RecyclerView WeekRecycler;
FirebaseFirestore database;
List<Exercise_Data_Hold_Class> Edlist;
List<horizontal_supporteed_class> newlist;
List<String> normalList;
List<horizontal_supporteed_class> hlist;
List<HoursHoldingClass> hhclist;
String UID,spinnerItem;
Exercise_Data_Hold_Class ehc;
horizontal_supporteed_class hsc;
elist_horizontal_adapter eha;
String horizontalString;
Boolean value1,value2,value3,value4,value5,value6,value7,Fail,skip1,skip2,skip3,skip4,skip5,skip6,skip7;






    public Days_Frag() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_days_, container, false);




        return view;
    }

    @Override
    public void onActivityCreated(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        context                 = getContext();
        Edlist                  = new ArrayList<>();
        normalList              = new ArrayList<String>();
        newlist                 = new ArrayList<>();
        hhclist                 = new ArrayList<>();
        database                = FirebaseFirestore.getInstance();
        Horizontal_recyclerView = getActivity().findViewById(R.id.h_recyclerview);
        WeekRecycler            = getActivity().findViewById(R.id.weekList);
        hlist                   = new ArrayList<horizontal_supporteed_class>();
        UID                     = FirebaseAuth.getInstance().getCurrentUser().getUid();
        EXERCISEHOURS           = getActivity().findViewById(R.id.Exercise_Splited_Hours);
        EXERCISENAME            = getActivity().findViewById(R.id.Exercise_Name);
        progressBar             = getActivity().findViewById(R.id.progressSingle);


        SkipDay1                = getActivity().findViewById(R.id.skipButtonDay1);
        SkipDay2                = getActivity().findViewById(R.id.skipButtonDay2);
        SkipDay2.setEnabled(false);
        SkipDay3                = getActivity().findViewById(R.id.skipButtonDay3);
        SkipDay3.setEnabled(false);
        SkipDay4                = getActivity().findViewById(R.id.skipButtonDay4);
        SkipDay4.setEnabled(false);
        SkipDay5                = getActivity().findViewById(R.id.skipButtonDay5);
        SkipDay5.setEnabled(false);
        SkipDay6                = getActivity().findViewById(R.id.skipButtonDay6);
        SkipDay6.setEnabled(false);
        SkipDay7                = getActivity().findViewById(R.id.skipButtonDay7);
        SkipDay7.setEnabled(false);

        DoneDay1                = getActivity().findViewById(R.id.doneButtonDay1);
        DoneDay2                = getActivity().findViewById(R.id.doneButtonDay2);
        DoneDay2.setEnabled(false);
        DoneDay3                = getActivity().findViewById(R.id.doneButtonDay3);
        DoneDay3.setEnabled(false);
        DoneDay4                = getActivity().findViewById(R.id.doneButtonDay4);
        DoneDay4.setEnabled(false);
        DoneDay5                = getActivity().findViewById(R.id.doneButtonDay5);
        DoneDay5.setEnabled(false);
        DoneDay6                = getActivity().findViewById(R.id.doneButtonDay6);
        DoneDay6.setEnabled(false);
        DoneDay7                = getActivity().findViewById(R.id.doneButtonDay7);
        DoneDay7.setEnabled(false);


        linearLayoutDay1        = getActivity().findViewById(R.id.day1);
        linearLayoutDay1.setVisibility(View.INVISIBLE);
        linearLayoutDay2        = getActivity().findViewById(R.id.day2);
        linearLayoutDay2.setVisibility(View.INVISIBLE);
        linearLayoutDay3        = getActivity().findViewById(R.id.day3);
        linearLayoutDay3.setVisibility(View.INVISIBLE);
        linearLayoutDay4        = getActivity().findViewById(R.id.day4);
        linearLayoutDay4.setVisibility(View.INVISIBLE);
        linearLayoutDay5        = getActivity().findViewById(R.id.day5);
        linearLayoutDay5.setVisibility(View.INVISIBLE);
        linearLayoutDay6        = getActivity().findViewById(R.id.day6);
        linearLayoutDay6.setVisibility(View.INVISIBLE);
        linearLayoutDay7        = getActivity().findViewById(R.id.day7);
        linearLayoutDay7.setVisibility(View.INVISIBLE);



        ////////////////                 NUMBER PICKERS        /////////////////////////////


        //// DAY1
        numberPickerDay1Hour     = getActivity().findViewById(R.id.hoursPickerDay1);
        numberPickerDay1Hour.setMaxValue(24);
        numberPickerDay1Hour.setMinValue(0);

        numberPickerDay1minutes = getActivity().findViewById(R.id.minutesPickerDay1);
        numberPickerDay1minutes.setMinValue(0);
        numberPickerDay1minutes.setMaxValue(60);



        //// DAY2

        numberPickerDay2Hour  = getActivity().findViewById(R.id.hoursPickerDay2);
        numberPickerDay2Hour.setMaxValue(24);
        numberPickerDay2Hour.setMinValue(0);

        numberPickerDay2minutes = getActivity().findViewById(R.id.minutesPickerDay2);
        numberPickerDay2minutes.setMinValue(0);
        numberPickerDay2minutes.setMaxValue(60);


        //// DAY3

        numberPickerDay3Hour  = getActivity().findViewById(R.id.hoursPickerDay3);
        numberPickerDay3Hour.setMaxValue(24);
        numberPickerDay3Hour.setMinValue(0);

        numberPickerDay3minutes = getActivity().findViewById(R.id.minutesPickerDay3);
        numberPickerDay3minutes.setMinValue(0);
        numberPickerDay3minutes.setMaxValue(60);


        //// DAY4

        numberPickerDay4Hour  = getActivity().findViewById(R.id.hoursPickerDay4);
        numberPickerDay4Hour.setMaxValue(24);
        numberPickerDay4Hour.setMinValue(0);

        numberPickerDay4minutes = getActivity().findViewById(R.id.minutesPickerDay4);
        numberPickerDay4minutes.setMinValue(0);
        numberPickerDay4minutes.setMaxValue(60);


        //// DAY5

        numberPickerDay5Hour  = getActivity().findViewById(R.id.hoursPickerDay5);
        numberPickerDay5Hour.setMaxValue(24);
        numberPickerDay5Hour.setMinValue(0);

        numberPickerDay5minutes = getActivity().findViewById(R.id.minutesPickerDay5);
        numberPickerDay5minutes.setMinValue(0);
        numberPickerDay5minutes.setMaxValue(60);


        //// DAY6

        numberPickerDay6Hour  = getActivity().findViewById(R.id.hoursPickerDay6);
        numberPickerDay6Hour.setMaxValue(24);
        numberPickerDay6Hour.setMinValue(0);

        numberPickerDay6minutes = getActivity().findViewById(R.id.minutesPickerDay6);
        numberPickerDay6minutes.setMinValue(0);
        numberPickerDay6minutes.setMaxValue(60);



       //// DAY6

        numberPickerDay7Hour  = getActivity().findViewById(R.id.hoursPickerDay7);
        numberPickerDay7Hour.setMaxValue(24);
        numberPickerDay7Hour.setMinValue(0);

        numberPickerDay7minutes = getActivity().findViewById(R.id.minutesPickerDay7);
        numberPickerDay7minutes.setMinValue(0);
        numberPickerDay7minutes.setMaxValue(60);

        historyMap = new HashMap<>();


        getActivity().findViewById(R.id.Profile_screen).setOnClickListener(new View.OnClickListener() {
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



        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            new getExerciseData().execute();



        }
    }



    public class getExerciseData extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(final Void... voids) {


            //////////////////  GETING  STARTING  DATES    ////////////////////


            database.collection("Activities").document(UID).collection("PendingExercises")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null)
                            {
                                Log.w(TAG, "listen:error", e);
                                return;
                            }

                            for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges())
                            {
                                switch (dc.getType())
                                {
                                    case ADDED:
                                        Log.d(TAG, "New data: " + dc.getDocument().getData());
                                        String  Sdate = dc.getDocument().getString("StartingDate");


                                         Exercise_Data_Hold_Class ehc = new Exercise_Data_Hold_Class(Sdate);
                                         Edlist.add(ehc);

                                         if (Edlist==null)
                                         {
                                             Toast.makeText(getActivity(),"list is null",Toast.LENGTH_SHORT).show();
                                         }
                                         wA = new weekAdapter(Edlist, getActivity());


                                         WeekRecycler.setAdapter(wA);
                                         WeekRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                                         wA.notifyDataSetChanged();



                           ///////////////////////      GETTING   EXERCISE   NAMES   TO   SET   IN   HORIZONTAL   LIST    /////////////////////////

                                         wA.setOnItemClickListner(new weekAdapter.onItemClickListner() {
                                             @Override
                                             public void onClick(String str) {




                                                 newlist.clear();
                                                 database.collection("Activities").document(UID).collection("PendingExercises")
                                                         .whereEqualTo("StartingDate",str).addSnapshotListener(new EventListener<QuerySnapshot>()
                                                 {
                                                     @Override
                                                     public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e)
                                                     {
                                                         if (e != null)
                                                         {
                                                             Log.w(TAG, "listen:error", e);
                                                             return;
                                                         }

                                                         for (DocumentChange dc :queryDocumentSnapshots.getDocumentChanges())
                                                         {
                                                             switch (dc.getType())
                                                             {
                                                                 case ADDED:
                                                                     Log.d(TAG, "Horizontal Data: " + dc.getDocument().getData());
                                                                     String ename = dc.getDocument().getString("ExerciseName");


                                                                     horizontal_supporteed_class hsc = new horizontal_supporteed_class(ename);
                                                                     newlist.add(hsc);

                                                                     elist_horizontal_adapter eha = new elist_horizontal_adapter(newlist,getActivity());


                                                                     Horizontal_recyclerView.setAdapter(eha);
                                                                     Horizontal_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                                                                     eha.notifyDataSetChanged();


                                                 //////////////////       GGETTING    EXERCISE   DATA      ////////////////////

                                                                     eha.setOnClickListner(new elist_horizontal_adapter.onClickListner()
                                                                     {
                                                                         @Override
                                                                         public void onitemClick(final String str2)
                                                                         {
                                                                             database.collection("Activities").document(UID)
                                                                                     .collection("CompletedExercises")
                                                                                     .document(str2).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                                                 @Override
                                                                                 public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                                                                                     if (snapshot!=null && snapshot.exists())
                                                                                     {

                                                                                         Fail = snapshot.getBoolean("Fail");

                                                                                         if (Fail)
                                                                                         {
                                                                                             linearLayoutDay1.setVisibility(View.INVISIBLE);
                                                                                             linearLayoutDay2.setVisibility(View.INVISIBLE);
                                                                                             linearLayoutDay3.setVisibility(View.INVISIBLE);
                                                                                             linearLayoutDay4.setVisibility(View.INVISIBLE);
                                                                                             linearLayoutDay5.setVisibility(View.INVISIBLE);
                                                                                             linearLayoutDay6.setVisibility(View.INVISIBLE);
                                                                                             linearLayoutDay7.setVisibility(View.INVISIBLE);

                                                                                             EXERCISENAME.setText(ExerciseName);
                                                                                             EXERCISENAME.setBackgroundColor(context.getResources().getColor(R.color.colorLightBlue));
                                                                                             EXERCISEHOURS.setText("You Failed This Task");
                                                                                         }



                                                                                         skip1 = snapshot.getBoolean("Skip1");
                                                                                         if (skip1)
                                                                                         {
                                                                                             SkipDay2.setEnabled(true);
                                                                                             DoneDay2.setEnabled(true);
                                                                                             linearLayoutDay1.setBackgroundColor(context.getResources().getColor(R.color.fill1));
                                                                                             DoneDay1.setVisibility(View.INVISIBLE);
                                                                                             SkipDay1.setText("Skiped");
                                                                                             SkipDay1.setTextColor(context.getResources().getColor(R.color.White));
                                                                                             SkipDay1.setBackground(context.getResources().getDrawable(R.drawable.doneback));
                                                                                             DoneDay1.setVisibility(View.INVISIBLE);
                                                                                         }
                                                                                         else if (!skip1)
                                                                                         {
                                                                                             linearLayoutDay1.setBackgroundColor(context.getResources().getColor(R.color.grey));
                                                                                             SkipDay1.setText("skip");
                                                                                             SkipDay1.setTextColor(context.getResources().getColor(R.color.black));
                                                                                             SkipDay1.setBackground(context.getResources().getDrawable(R.drawable.back_for_share_button));
                                                                                             DoneDay1.setVisibility(View.VISIBLE);
                                                                                         }






                                                                                         value1 = snapshot.getBoolean("Day1");
                                                                                         if (value1)
                                                                                         {
                                                                                             SkipDay2.setEnabled(true);
                                                                                             DoneDay2.setEnabled(true);
                                                                                             linearLayoutDay1.setBackgroundColor(context.getResources().getColor(R.color.fill1));
                                                                                             DoneDay1.setText("Completed");
                                                                                             DoneDay1.setTextColor(context.getResources().getColor(R.color.White));
                                                                                             DoneDay1.setBackground(context.getResources().getDrawable(R.drawable.doneback));
                                                                                             SkipDay1.setVisibility(View.INVISIBLE);
                                                                                         }
                                                                                         else if (!value1){
                                                                                             linearLayoutDay1.setBackgroundColor(context.getResources().getColor(R.color.grey));
                                                                                             DoneDay1.setText("Done");
                                                                                             DoneDay1.setTextColor(context.getResources().getColor(R.color.black));
                                                                                             DoneDay1.setBackground(context.getResources().getDrawable(R.drawable.back_for_share_button));
                                                                                             SkipDay1.setVisibility(View.VISIBLE);
                                                                                         }





                                                                                         skip2 = snapshot.getBoolean("Skip2");
                                                                                         if (skip2)
                                                                                         {
                                                                                             SkipDay3.setEnabled(true);
                                                                                             DoneDay3.setEnabled(true);
                                                                                             linearLayoutDay2.setBackgroundColor(context.getResources().getColor(R.color.fill1));
                                                                                             DoneDay2.setVisibility(View.INVISIBLE);
                                                                                             SkipDay2.setText("Skiped");
                                                                                             SkipDay2.setTextColor(context.getResources().getColor(R.color.White));
                                                                                             SkipDay2.setBackground(context.getResources().getDrawable(R.drawable.doneback));
                                                                                             DoneDay2.setVisibility(View.INVISIBLE);
                                                                                         }
                                                                                         else if (!skip2)
                                                                                         {
                                                                                             linearLayoutDay2.setBackgroundColor(context.getResources().getColor(R.color.grey));
                                                                                             SkipDay2.setText("skip");
                                                                                             SkipDay2.setTextColor(context.getResources().getColor(R.color.black));
                                                                                             SkipDay2.setBackground(context.getResources().getDrawable(R.drawable.back_for_share_button));
                                                                                             DoneDay2.setVisibility(View.VISIBLE);
                                                                                         }











                                                                                         value2 = snapshot.getBoolean("Day2");
                                                                                         if (value2)
                                                                                         {
                                                                                             SkipDay3.setEnabled(true);
                                                                                             DoneDay3.setEnabled(true);
                                                                                             linearLayoutDay2.setBackgroundColor(context.getResources().getColor(R.color.fill1));
                                                                                             DoneDay2.setText("Completed");
                                                                                             DoneDay2.setTextColor(context.getResources().getColor(R.color.White));
                                                                                             DoneDay2.setBackground(context.getResources().getDrawable(R.drawable.doneback));
                                                                                             SkipDay2.setVisibility(View.INVISIBLE);
                                                                                         }
                                                                                         else if (!value2){
                                                                                             linearLayoutDay2.setBackgroundColor(context.getResources().getColor(R.color.grey));
                                                                                             DoneDay2.setText("Done");
                                                                                             DoneDay2.setTextColor(context.getResources().getColor(R.color.black));
                                                                                             DoneDay2.setBackground(context.getResources().getDrawable(R.drawable.back_for_share_button));
                                                                                             SkipDay2.setVisibility(View.VISIBLE);
                                                                                         }





                                                                                         skip3 = snapshot.getBoolean("Skip3");
                                                                                         if (skip3)
                                                                                         {
                                                                                             SkipDay4.setEnabled(true);
                                                                                             DoneDay4.setEnabled(true);
                                                                                             linearLayoutDay3.setBackgroundColor(context.getResources().getColor(R.color.fill1));
                                                                                             DoneDay3.setVisibility(View.INVISIBLE);
                                                                                             SkipDay3.setText("Skiped");
                                                                                             SkipDay3.setTextColor(context.getResources().getColor(R.color.White));
                                                                                             SkipDay3.setBackground(context.getResources().getDrawable(R.drawable.doneback));
                                                                                             DoneDay3.setVisibility(View.INVISIBLE);
                                                                                         }
                                                                                         else if (!skip3)
                                                                                         {
                                                                                             linearLayoutDay3.setBackgroundColor(context.getResources().getColor(R.color.grey));
                                                                                             SkipDay3.setText("skip");
                                                                                             SkipDay3.setTextColor(context.getResources().getColor(R.color.black));
                                                                                             SkipDay3.setBackground(context.getResources().getDrawable(R.drawable.back_for_share_button));
                                                                                             DoneDay3.setVisibility(View.VISIBLE);
                                                                                         }







                                                                                         value3 = snapshot.getBoolean("Day3");
                                                                                         if (value3)
                                                                                         {
                                                                                             SkipDay4.setEnabled(true);
                                                                                             DoneDay4.setEnabled(true);
                                                                                             linearLayoutDay3.setBackgroundColor(context.getResources().getColor(R.color.fill1));
                                                                                             DoneDay3.setText("Completed");
                                                                                             DoneDay3.setTextColor(context.getResources().getColor(R.color.White));
                                                                                             DoneDay3.setBackground(context.getResources().getDrawable(R.drawable.doneback));
                                                                                             SkipDay3.setVisibility(View.INVISIBLE);
                                                                                         }
                                                                                         else if (!value3){
                                                                                             linearLayoutDay3.setBackgroundColor(context.getResources().getColor(R.color.grey));
                                                                                             DoneDay3.setText("Done");
                                                                                             DoneDay3.setTextColor(context.getResources().getColor(R.color.black));
                                                                                             DoneDay3.setBackground(context.getResources().getDrawable(R.drawable.back_for_share_button));
                                                                                             SkipDay3.setVisibility(View.VISIBLE);
                                                                                         }









                                                                                         skip4 = snapshot.getBoolean("Skip4");
                                                                                         if (skip4)
                                                                                         {
                                                                                             SkipDay5.setEnabled(true);
                                                                                             DoneDay5.setEnabled(true);
                                                                                             linearLayoutDay4.setBackgroundColor(context.getResources().getColor(R.color.fill1));
                                                                                             DoneDay4.setVisibility(View.INVISIBLE);
                                                                                             SkipDay4.setText("Skiped");
                                                                                             SkipDay4.setTextColor(context.getResources().getColor(R.color.White));
                                                                                             SkipDay4.setBackground(context.getResources().getDrawable(R.drawable.doneback));
                                                                                             DoneDay4.setVisibility(View.INVISIBLE);
                                                                                         }
                                                                                         else if (!skip4)
                                                                                         {
                                                                                             linearLayoutDay4.setBackgroundColor(context.getResources().getColor(R.color.grey));
                                                                                             SkipDay4.setText("skip");
                                                                                             SkipDay4.setTextColor(context.getResources().getColor(R.color.black));
                                                                                             SkipDay4.setBackground(context.getResources().getDrawable(R.drawable.back_for_share_button));
                                                                                             DoneDay4.setVisibility(View.VISIBLE);
                                                                                         }







                                                                                         value4 = snapshot.getBoolean("Day4");
                                                                                         if (value4)
                                                                                         {
                                                                                             SkipDay5.setEnabled(true);
                                                                                             DoneDay5.setEnabled(true);
                                                                                             linearLayoutDay4.setBackgroundColor(context.getResources().getColor(R.color.fill1));
                                                                                             DoneDay4.setText("Completed");
                                                                                             DoneDay4.setTextColor(context.getResources().getColor(R.color.White));
                                                                                             DoneDay4.setBackground(context.getResources().getDrawable(R.drawable.doneback));
                                                                                             SkipDay4.setVisibility(View.INVISIBLE);
                                                                                         }
                                                                                         else if (!value4){
                                                                                             linearLayoutDay4.setBackgroundColor(context.getResources().getColor(R.color.grey));
                                                                                             DoneDay4.setText("Done");
                                                                                             DoneDay4.setTextColor(context.getResources().getColor(R.color.black));
                                                                                             DoneDay4.setBackground(context.getResources().getDrawable(R.drawable.back_for_share_button));
                                                                                             SkipDay4.setVisibility(View.VISIBLE);
                                                                                         }





                                                                                         skip5 = snapshot.getBoolean("Skip5");
                                                                                         if (skip5)
                                                                                         {
                                                                                             SkipDay6.setEnabled(true);
                                                                                             DoneDay6.setEnabled(true);
                                                                                             linearLayoutDay5.setBackgroundColor(context.getResources().getColor(R.color.fill1));
                                                                                             DoneDay5.setVisibility(View.INVISIBLE);
                                                                                             SkipDay5.setText("Skiped");
                                                                                             SkipDay5.setTextColor(context.getResources().getColor(R.color.White));
                                                                                             SkipDay5.setBackground(context.getResources().getDrawable(R.drawable.doneback));
                                                                                             DoneDay5.setVisibility(View.INVISIBLE);
                                                                                         }
                                                                                         else if (!skip5)
                                                                                         {
                                                                                             linearLayoutDay5.setBackgroundColor(context.getResources().getColor(R.color.grey));
                                                                                             SkipDay5.setText("skip");
                                                                                             SkipDay5.setTextColor(context.getResources().getColor(R.color.black));
                                                                                             SkipDay5.setBackground(context.getResources().getDrawable(R.drawable.back_for_share_button));
                                                                                             DoneDay5.setVisibility(View.VISIBLE);
                                                                                         }










                                                                                         value5 = snapshot.getBoolean("Day5");
                                                                                         if (value5)
                                                                                         {
                                                                                             SkipDay6.setEnabled(true);
                                                                                             DoneDay6.setEnabled(true);
                                                                                             linearLayoutDay5.setBackgroundColor(context.getResources().getColor(R.color.fill1));
                                                                                             DoneDay5.setText("Completed");
                                                                                             DoneDay5.setTextColor(context.getResources().getColor(R.color.White));
                                                                                             DoneDay5.setBackground(context.getResources().getDrawable(R.drawable.doneback));
                                                                                             SkipDay5.setVisibility(View.INVISIBLE);
                                                                                         }
                                                                                         else if (!value5){
                                                                                             linearLayoutDay5.setBackgroundColor(context.getResources().getColor(R.color.grey));
                                                                                             DoneDay5.setText("Done");
                                                                                             DoneDay5.setTextColor(context.getResources().getColor(R.color.black));
                                                                                             DoneDay5.setBackground(context.getResources().getDrawable(R.drawable.back_for_share_button));
                                                                                             SkipDay5.setVisibility(View.VISIBLE);
                                                                                         }







                                                                                         skip6 = snapshot.getBoolean("Skip6");
                                                                                         if (skip6)
                                                                                         {
                                                                                             SkipDay7.setEnabled(true);
                                                                                             DoneDay7.setEnabled(true);
                                                                                             linearLayoutDay6.setBackgroundColor(context.getResources().getColor(R.color.fill1));
                                                                                             DoneDay6.setVisibility(View.INVISIBLE);
                                                                                             SkipDay6.setText("Skiped");
                                                                                             SkipDay6.setTextColor(context.getResources().getColor(R.color.White));
                                                                                             SkipDay6.setBackground(context.getResources().getDrawable(R.drawable.doneback));
                                                                                             DoneDay6.setVisibility(View.INVISIBLE);
                                                                                         }
                                                                                         else if (!skip6)
                                                                                         {
                                                                                             linearLayoutDay6.setBackgroundColor(context.getResources().getColor(R.color.grey));
                                                                                             SkipDay6.setText("skip");
                                                                                             SkipDay6.setTextColor(context.getResources().getColor(R.color.black));
                                                                                             SkipDay6.setBackground(context.getResources().getDrawable(R.drawable.back_for_share_button));
                                                                                             DoneDay6.setVisibility(View.VISIBLE);
                                                                                         }







                                                                                         value6 = snapshot.getBoolean("Day6");
                                                                                         if (value6)
                                                                                         {
                                                                                             SkipDay7.setEnabled(true);
                                                                                             DoneDay7.setEnabled(true);
                                                                                             linearLayoutDay6.setBackgroundColor(context.getResources().getColor(R.color.fill1));
                                                                                             DoneDay6.setText("Completed");
                                                                                             DoneDay6.setTextColor(context.getResources().getColor(R.color.White));
                                                                                             DoneDay6.setBackground(context.getResources().getDrawable(R.drawable.doneback));
                                                                                             SkipDay6.setVisibility(View.INVISIBLE);
                                                                                         }
                                                                                         else if (!value6){
                                                                                             linearLayoutDay6.setBackgroundColor(context.getResources().getColor(R.color.grey));
                                                                                             DoneDay6.setText("Done");
                                                                                             DoneDay6.setTextColor(context.getResources().getColor(R.color.black));
                                                                                             DoneDay6.setBackground(context.getResources().getDrawable(R.drawable.back_for_share_button));
                                                                                             SkipDay6.setVisibility(View.VISIBLE);
                                                                                         }






                                                                                         skip7 = snapshot.getBoolean("Skip7");
                                                                                         if (skip7)
                                                                                         {
                                                                                             linearLayoutDay7.setBackgroundColor(context.getResources().getColor(R.color.fill1));
                                                                                             DoneDay7.setVisibility(View.INVISIBLE);
                                                                                             SkipDay7.setText("Skiped");
                                                                                             SkipDay7.setTextColor(context.getResources().getColor(R.color.White));
                                                                                             SkipDay7.setBackground(context.getResources().getDrawable(R.drawable.doneback));
                                                                                             DoneDay7.setVisibility(View.INVISIBLE);
                                                                                         }
                                                                                         else if (!skip7)
                                                                                         {
                                                                                             linearLayoutDay7.setBackgroundColor(context.getResources().getColor(R.color.grey));
                                                                                             SkipDay7.setText("skip");
                                                                                             SkipDay7.setTextColor(context.getResources().getColor(R.color.black));
                                                                                             SkipDay7.setBackground(context.getResources().getDrawable(R.drawable.back_for_share_button));
                                                                                             DoneDay7.setVisibility(View.VISIBLE);
                                                                                         }









                                                                                         value7 = snapshot.getBoolean("Day7");
                                                                                         if (value7)
                                                                                         {

                                                                                             linearLayoutDay7.setBackgroundColor(context.getResources().getColor(R.color.fill1));
                                                                                             DoneDay7.setText("Completed");
                                                                                             DoneDay7.setTextColor(context.getResources().getColor(R.color.White));
                                                                                             DoneDay7.setBackground(context.getResources().getDrawable(R.drawable.doneback));
                                                                                             SkipDay7.setVisibility(View.INVISIBLE);
                                                                                         }
                                                                                         else if (!value7){
                                                                                             linearLayoutDay7.setBackgroundColor(context.getResources().getColor(R.color.grey));
                                                                                             DoneDay7.setText("Done");
                                                                                             DoneDay7.setTextColor(context.getResources().getColor(R.color.black));
                                                                                             DoneDay7.setBackground(context.getResources().getDrawable(R.drawable.back_for_share_button));
                                                                                             SkipDay7.setVisibility(View.VISIBLE);
                                                                                         }

                                                                                     }
                                                                                 }
                                                                             });



                                                                             hhclist.clear();
                                                                             horizontalString = str2;

                                                                          database.collection("Activities")
                                                                          .document(UID)
                                                                          .collection("PendingExercises")
                                                                          .document(str2)
                                                                          .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                                              @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                                                              @Override
                                                                              public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {

                                                                           if (snapshot!=null && snapshot.exists())
                                                                           {
                                                                               StartDate       =  snapshot.getString("StartingDate");
                                                                               EndDate         =  snapshot.getString("End_Date");
                                                                               CurrentProgress = snapshot.getLong("Current_Progres");
                                                                               HoursPerDay     = snapshot.getString("hours_per_day");
                                                                               TotalHoursOfWeek = snapshot.getString("total_hours_for_week");
                                                                               PerDayMinutes   = snapshot.getLong("PerDayMinutes");
                                                                               TotalMinutes    = snapshot.getLong("TotalMinutes");
                                                                               ExerciseName    = snapshot.getString("ExerciseName");

                                                                               historyMap.put("Starting_Date",StartDate);
                                                                               historyMap.put("Ending_Date",EndDate);
                                                                               historyMap.put("Name_of_Exercise",ExerciseName);
                                                                               historyMap.put("Per_Day_Hours",HoursPerDay);
                                                                               historyMap.put("Total_hours",TotalHoursOfWeek);
                                                                               historyMap.put("Completed_TimeStamp", FieldValue.serverTimestamp());
                                                                               historyMap.put("Complete_Date_Valaue",StartDate+"-"+EndDate);
                                                                               historyMap.put("Shared",false);

                                                                               if (CurrentProgress >= TotalMinutes)
                                                                               {
                                                                                   layoutInflater = LayoutInflater.from(context);
                                                                                   final View Complete_pop_up_view = layoutInflater.inflate(R.layout.complete_pop_up,null);

                                                                                   close = Complete_pop_up_view.findViewById(R.id.closepopup);
                                                                                   final PopupWindow popupWindow = new PopupWindow(Complete_pop_up_view,width,height,focusable);
                                                                                   popupWindow.setAnimationStyle(R.style.Animation);
                                                                                   popupWindow.showAtLocation(view,Gravity.CENTER,0,-155);

                                                                                   close.setOnClickListener(new View.OnClickListener() {
                                                                                       @Override
                                                                                       public void onClick(View v) {
                                                                                           popupWindow.dismiss();
                                                                                       }
                                                                                   });
                                                                                   Complete_pop_up_view.setOnTouchListener(new View.OnTouchListener() {
                                                                                       @Override
                                                                                       public boolean onTouch(View v, MotionEvent event) {

                                                                                           return  true;
                                                                                       }
                                                                                   });


                                                                                   progressBar.setVisibility(View.VISIBLE);
                                                                                   progressBar.setMax((int) TotalMinutes);
                                                                                   progressBar.setProgress((int) CurrentProgress);
                                                                                   EXERCISENAME.setText(ExerciseName);
                                                                                   EXERCISENAME.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                                                                                   EXERCISEHOURS.setText("You Completed This Task");

                                                                                   database.collection("users").document(UID).update(ExerciseName,false);

                                                                                   linearLayoutDay1.setVisibility(View.INVISIBLE);
                                                                                   linearLayoutDay2.setVisibility(View.INVISIBLE);
                                                                                   linearLayoutDay3.setVisibility(View.INVISIBLE);
                                                                                   linearLayoutDay4.setVisibility(View.INVISIBLE);
                                                                                   linearLayoutDay5.setVisibility(View.INVISIBLE);
                                                                                   linearLayoutDay6.setVisibility(View.INVISIBLE);
                                                                                   linearLayoutDay7.setVisibility(View.INVISIBLE);


                                                                                   database.collection("Activities")
                                                                                           .document(UID).collection("CompletedExercises")
                                                                                           .document(ExerciseName).collection(ExerciseName).document(StartDate+"-"+EndDate)
                                                                                           .set(historyMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                       @Override
                                                                                       public void onSuccess(Void aVoid) {

                                                                                       }
                                                                                   });


                                                                               }

                                                                              else if (CurrentProgress <= TotalMinutes && Fail)
                                                                               {

                                                                                   linearLayoutDay1.setVisibility(View.INVISIBLE);
                                                                                   linearLayoutDay2.setVisibility(View.INVISIBLE);
                                                                                   linearLayoutDay3.setVisibility(View.INVISIBLE);
                                                                                   linearLayoutDay4.setVisibility(View.INVISIBLE);
                                                                                   linearLayoutDay5.setVisibility(View.INVISIBLE);
                                                                                   linearLayoutDay6.setVisibility(View.INVISIBLE);
                                                                                   linearLayoutDay7.setVisibility(View.INVISIBLE);

                                                                                   EXERCISENAME.setText(ExerciseName);
                                                                                   EXERCISENAME.setBackgroundColor(context.getResources().getColor(R.color.colorLightBlue));
                                                                                   EXERCISEHOURS.setText("You Failed This Task");

                                                                               }

                                                                               else if(CurrentProgress <= TotalMinutes && !Fail)
                                                                               {


                                                                                   EXERCISENAME.setText(ExerciseName);
                                                                                   EXERCISENAME.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                                                                                   EXERCISEHOURS.setText("Per Day : "+HoursPerDay);
                                                                                   progressBar.setVisibility(View.VISIBLE);
                                                                                   progressBar.setMax((int) TotalMinutes);
                                                                                   progressBar.setProgress((int) CurrentProgress);



                                                                                   linearLayoutDay1.setVisibility(View.VISIBLE);
                                                                                   linearLayoutDay2.setVisibility(View.VISIBLE);
                                                                                   linearLayoutDay3.setVisibility(View.VISIBLE);
                                                                                   linearLayoutDay4.setVisibility(View.VISIBLE);
                                                                                   linearLayoutDay5.setVisibility(View.VISIBLE);
                                                                                   linearLayoutDay6.setVisibility(View.VISIBLE);
                                                                                   linearLayoutDay7.setVisibility(View.VISIBLE);


                                                                                   SkipDay1.setOnClickListener(new View.OnClickListener() {
                                                                                       @Override
                                                                                       public void onClick(View v) {


                                                                                        database.collection("Activities").document(UID)
                                                                                                .collection("CompletedExercises")
                                                                                                .document(ExerciseName).update("Skip1",true);
                                                                                       }
                                                                                   });

                                                                                   SkipDay2.setOnClickListener(new View.OnClickListener() {
                                                                                       @Override
                                                                                       public void onClick(View v) {


                                                                                           database.collection("Activities").document(UID)
                                                                                                   .collection("CompletedExercises")
                                                                                                   .document(ExerciseName).update("Skip2",true);
                                                                                       }
                                                                                   });

                                                                                   SkipDay3.setOnClickListener(new View.OnClickListener() {
                                                                                       @Override
                                                                                       public void onClick(View v) {


                                                                                           database.collection("Activities").document(UID)
                                                                                                   .collection("CompletedExercises")
                                                                                                   .document(ExerciseName).update("Skip3",true);
                                                                                       }
                                                                                   });

                                                                                   SkipDay4.setOnClickListener(new View.OnClickListener() {
                                                                                       @Override
                                                                                       public void onClick(View v) {

                                                                                           database.collection("Activities").document(UID)
                                                                                                   .collection("CompletedExercises")
                                                                                                   .document(ExerciseName).update("Skip4",true);
                                                                                       }
                                                                                   });

                                                                                   SkipDay4.setOnClickListener(new View.OnClickListener() {
                                                                                       @Override
                                                                                       public void onClick(View v) {

                                                                                           database.collection("Activities").document(UID)
                                                                                                   .collection("CompletedExercises")
                                                                                                   .document(ExerciseName).update("Skip4",true);
                                                                                       }
                                                                                   });

                                                                                   SkipDay5.setOnClickListener(new View.OnClickListener() {
                                                                                       @Override
                                                                                       public void onClick(View v) {

                                                                                           database.collection("Activities").document(UID)
                                                                                                   .collection("CompletedExercises")
                                                                                                   .document(ExerciseName).update("Skip5",true);
                                                                                       }
                                                                                   });

                                                                                   SkipDay6.setOnClickListener(new View.OnClickListener() {
                                                                                   @Override
                                                                                   public void onClick(View v) {

                                                                                       database.collection("Activities").document(UID)
                                                                                               .collection("CompletedExercises")
                                                                                               .document(ExerciseName).update("Skip6",true);
                                                                                   }
                                                                               });

                                                                                   SkipDay7.setOnClickListener(new View.OnClickListener() {
                                                                                       @Override
                                                                                       public void onClick(View v) {

                                                                                           database.collection("users").document(UID).update(ExerciseName,false);

                                                                                           database.collection("Activities").document(UID)
                                                                                                   .collection("CompletedExercises")
                                                                                                   .document(ExerciseName).update("Skip7",true);

                                                                                           database.collection("Activities").document(UID)
                                                                                                   .collection("CompletedExercises")
                                                                                                   .document(ExerciseName).update("Fail",true);
                                                                                       }
                                                                                   });



                                                                                   DoneDay1.setOnClickListener(new View.OnClickListener() {
                                                                                       @Override
                                                                                       public void onClick(View v) {
                                                                                           numberPickerHours   = numberPickerDay1Hour.getValue();
                                                                                           numberPickerMinutes = numberPickerDay1minutes.getValue();


                                                                                           int firstFinal = numberPickerHours *60;
                                                                                           int secondFinal = firstFinal+numberPickerMinutes;


                                                                                           if (!value1)
                                                                                           {
                                                                                               database.collection("Activities")
                                                                                                       .document(UID)
                                                                                                       .collection("PendingExercises")
                                                                                                       .document(ExerciseName).update("Current_Progres", FieldValue.increment(secondFinal));



                                                                                           }
                                                                                           database.collection("Activities").document(UID)
                                                                                                   .collection("CompletedExercises")
                                                                                                   .document(ExerciseName).update("Day1",true);


                                                                                       }
                                                                                   });

                                                                                   

                                                                                   DoneDay2.setOnClickListener(new View.OnClickListener() {
                                                                                       @Override
                                                                                       public void onClick(View v) {
                                                                                           numberPickerHours   = numberPickerDay2Hour.getValue();
                                                                                           numberPickerMinutes = numberPickerDay2minutes.getValue();

                                                                                           int firstFinal = numberPickerHours *60;
                                                                                           int secondFinal = firstFinal+numberPickerMinutes;

                                                                                           if (!value2)
                                                                                           {
                                                                                               database.collection("Activities")
                                                                                                       .document(UID)
                                                                                                       .collection("PendingExercises")
                                                                                                       .document(ExerciseName).update("Current_Progres", FieldValue.increment(secondFinal));


                                                                                           }
                                                                                           database.collection("Activities").document(UID)
                                                                                                   .collection("CompletedExercises")
                                                                                                   .document(ExerciseName).update("Day2",true);


                                                                                       }
                                                                                   });
                                                                                   DoneDay3.setOnClickListener(new View.OnClickListener() {
                                                                                       @Override
                                                                                       public void onClick(View v) {
                                                                                           numberPickerHours   = numberPickerDay3Hour.getValue();
                                                                                           numberPickerMinutes = numberPickerDay3minutes.getValue();



                                                                                           int firstFinal = numberPickerHours *60;
                                                                                           int secondFinal = firstFinal+numberPickerMinutes;

                                                                                           if (!value3)
                                                                                           {
                                                                                               database.collection("Activities")
                                                                                                       .document(UID)
                                                                                                       .collection("PendingExercises")
                                                                                                       .document(ExerciseName).update("Current_Progres", FieldValue.increment(secondFinal));




                                                                                           }
                                                                                           database.collection("Activities").document(UID)
                                                                                                   .collection("CompletedExercises")
                                                                                                   .document(ExerciseName).update("Day3",true);


                                                                                       }
                                                                                   });
                                                                                   DoneDay4.setOnClickListener(new View.OnClickListener() {
                                                                                       @Override
                                                                                       public void onClick(View v) {
                                                                                           numberPickerHours   = numberPickerDay4Hour.getValue();
                                                                                           numberPickerMinutes = numberPickerDay4minutes.getValue();



                                                                                           int firstFinal = numberPickerHours *60;
                                                                                           int secondFinal = firstFinal+numberPickerMinutes;

                                                                                           if (!value4)
                                                                                           {
                                                                                               database.collection("Activities")
                                                                                                       .document(UID)
                                                                                                       .collection("PendingExercises")
                                                                                                       .document(ExerciseName).update("Current_Progres", FieldValue.increment(secondFinal));


                                                                                           }
                                                                                           database.collection("Activities").document(UID)
                                                                                                   .collection("CompletedExercises")
                                                                                                   .document(ExerciseName).update("Day4",true);


                                                                                       }
                                                                                   });
                                                                                   DoneDay5.setOnClickListener(new View.OnClickListener() {
                                                                                       @Override
                                                                                       public void onClick(View v) {
                                                                                           numberPickerHours   = numberPickerDay5Hour.getValue();
                                                                                           numberPickerMinutes = numberPickerDay5minutes.getValue();


                                                                                           int firstFinal = numberPickerHours *60;
                                                                                           int secondFinal = firstFinal+numberPickerMinutes;

                                                                                           if (!value5)
                                                                                           {
                                                                                               database.collection("Activities")
                                                                                                       .document(UID)
                                                                                                       .collection("PendingExercises")
                                                                                                       .document(ExerciseName).update("Current_Progres", FieldValue.increment(secondFinal));



                                                                                           }
                                                                                           database.collection("Activities").document(UID)
                                                                                                   .collection("CompletedExercises")
                                                                                                   .document(ExerciseName).update("Day5",true);


                                                                                       }
                                                                                   });
                                                                                   DoneDay6.setOnClickListener(new View.OnClickListener() {
                                                                                       @Override
                                                                                       public void onClick(View v) {
                                                                                           numberPickerHours   = numberPickerDay6Hour.getValue();
                                                                                           numberPickerMinutes = numberPickerDay6minutes.getValue();


                                                                                           int firstFinal = numberPickerHours *60;
                                                                                           int secondFinal = firstFinal+numberPickerMinutes;

                                                                                           if (!value6)
                                                                                           {
                                                                                               database.collection("Activities")
                                                                                                       .document(UID)
                                                                                                       .collection("PendingExercises")
                                                                                                       .document(ExerciseName).update("Current_Progres", FieldValue.increment(secondFinal));



                                                                                           }
                                                                                           database.collection("Activities").document(UID)
                                                                                                   .collection("CompletedExercises")
                                                                                                   .document(ExerciseName).update("Day6",true);


                                                                                       }
                                                                                   });
                                                                                   DoneDay7.setOnClickListener(new View.OnClickListener() {
                                                                                       @Override
                                                                                       public void onClick(View v) {
                                                                                           d7 = true;


                                                                                           numberPickerHours   = numberPickerDay7Hour.getValue();
                                                                                           numberPickerMinutes = numberPickerDay7minutes.getValue();


                                                                                           int firstFinal = numberPickerHours *60;
                                                                                           int secondFinal = firstFinal+numberPickerMinutes;

                                                                                           if (!value7 )
                                                                                           {
                                                                                               database.collection("users").document(UID).update(ExerciseName,false);

                                                                                               database.collection("Activities")
                                                                                                       .document(UID)
                                                                                                       .collection("PendingExercises")
                                                                                                       .document(ExerciseName).update("Current_Progres", FieldValue.increment(secondFinal));


                                                                                           }
                                                                                           database.collection("Activities").document(UID)
                                                                                                   .collection("CompletedExercises")
                                                                                                   .document(ExerciseName).update("Day7",true);

                                                                                           database.collection("Activities")
                                                                                                   .document(UID)
                                                                                                   .collection("PendingExercises")
                                                                                                   .document(ExerciseName).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                                                               @Override
                                                                                               public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {

                                                                                                   if (snapshot!=null && snapshot.exists())
                                                                                                   {
                                                                                                       Long cp = snapshot.getLong("Current_Progres");
                                                                                                       Long tm = snapshot.getLong("TotalMinutes");


                                                                                                      if (cp < tm && d7  )
                                                                                                       {

                                                                                                           database.collection("Activities").document(UID)
                                                                                                                   .collection("CompletedExercises")
                                                                                                                   .document(ExerciseName).update("Fail",true);
                                                                                                           d7 = false;
                                                                                                       }
                                                                                                   }
                                                                                               }
                                                                                           });





                                                                                       }
                                                                                   });





                                                                               }



                                                                           }



                                                                              }
                                                                          }) ;


                                                                         }
                                                                     });
















                                                                     break;
                                                                 case MODIFIED:
                                                                     break;
                                                                 case REMOVED:
                                                                     Edlist.clear();
                                                                     break;
                                                             }
                                                         }
                                                     }
                                                 });


                                             }
                                         });
                                         break;

                                    case MODIFIED:
                                        break;
                                    case REMOVED:
                                        break;
                                }
                            }
                        }
                    });

            return null;
        }
    }






}













