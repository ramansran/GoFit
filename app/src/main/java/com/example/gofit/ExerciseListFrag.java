package com.example.gofit;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.google.api.ChangeType.MODIFIED;
import static com.google.firebase.firestore.DocumentChange.Type.ADDED;



public class ExerciseListFrag extends Fragment {
RecyclerView gridView;
List<ExerciseGridData> list;
FirebaseFirestore database;
ImageView profilepic;
TextView username;
Context context ;
LayoutInflater layoutInflaterl;
StaggeredGridLayoutManager _sGridLayoutManager;
String name;
DocumentReference documentReference;
Source source;

String CYC,RPJ,MDT,PUS,RNG,SWM,WTL,YG,JG;
int value;
TextView txtProgress;
CircularProgressBar circularProgressBar,circularProgressBarRopeJumping,circularProgressBarMeditation,circularProgressBarPushUps,circularProgressBarRunning,circularProgressBarSwimming,circularProgressBarWeightLifting,circularProgressBarYoga,circularProgressBarJogging;
int pStatus = 0;
Handler handler = new Handler();
    int animationDuration;

    public ExerciseListFrag() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);


        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.askusertypestatusbar));


        profilepic = view.findViewById(R.id.profile_image);
        username   = view.findViewById(R.id.user_name);
        context    = getContext();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onActivityCreated(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list                                   = new ArrayList<ExerciseGridData>();
        database                               = FirebaseFirestore.getInstance();
        gridView                               = getActivity().findViewById(R.id.grid_view);
        _sGridLayoutManager                    = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        circularProgressBar                    = getActivity().findViewById(R.id.progress);
        circularProgressBarRopeJumping         = getActivity().findViewById(R.id.progressRopeJumping);
        circularProgressBarMeditation          = getActivity().findViewById(R.id.progressMeditation);
        circularProgressBarRunning             = getActivity().findViewById(R.id.progressRunning);
        circularProgressBarPushUps             = getActivity().findViewById(R.id.progressPushUps);
        circularProgressBarSwimming            = getActivity().findViewById(R.id.progressSwimming);
        circularProgressBarWeightLifting       = getActivity().findViewById(R.id.progressWeightLifting);
        circularProgressBarYoga                = getActivity().findViewById(R.id.progressYoga);
        circularProgressBarJogging             = getActivity().findViewById(R.id.progressJogging);
        txtProgress                            = (TextView) getActivity().findViewById(R.id.txtProgress);
        animationDuration                      = 2500; // 2500ms = 2,5s








        getActivity().findViewById(R.id.Chat_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Chat_List_Frag();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
                ft.replace(R.id.frame,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
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

        getActivity().findViewById(R.id.trainers_screen).setOnClickListener(new View.OnClickListener() {
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
            new getProfiledata().execute();
            new getExercises().execute();
            new getProgress().execute();

        }

    }

    public class getProgress extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {

            String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            database.collection("Activities")
                    .document(UID)
                    .collection("PendingExercises")
                    .document("cycling")
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                            if (snapshot!=null && snapshot.exists())
                            {
                                final long progress = snapshot.getLong("Current_Progres");
                                final long total   = snapshot.getLong("TotalMinutes");

                                circularProgressBar.setProgressMax(total);
                                circularProgressBar.setProgressWithAnimation((int)progress, (long) animationDuration);

                            }
                        }
                    });

            database.collection("Activities")
                    .document(UID)
                    .collection("PendingExercises")
                    .document("RopeJumping")
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                            if (snapshot!=null && snapshot.exists())
                            {
                                final long progress = snapshot.getLong("Current_Progres");
                                Log.d("Ropeprogress", String.valueOf(progress));
                                final long total   = snapshot.getLong("TotalMinutes");
                                circularProgressBarRopeJumping.setProgressMax(total);
                                circularProgressBarRopeJumping.setProgressWithAnimation((int)progress, (long) animationDuration);

                            }
                        }
                    });

            database.collection("Activities")
                    .document(UID)
                    .collection("PendingExercises")
                    .document("Meditate")
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                            if (snapshot!=null && snapshot.exists())
                            {
                                final long progress = snapshot.getLong("Current_Progres");

                                final long total   = snapshot.getLong("TotalMinutes");
                                circularProgressBarMeditation.setProgressMax(total);
                                circularProgressBarMeditation.setProgressWithAnimation((int)progress, (long) animationDuration);

                            }
                        }
                    });

            database.collection("Activities")
                    .document(UID)
                    .collection("PendingExercises")
                    .document("PushUp")
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                            if (snapshot!=null && snapshot.exists())
                            {
                                final long progress = snapshot.getLong("Current_Progres");

                                final long total   = snapshot.getLong("TotalMinutes");
                                circularProgressBarPushUps.setProgressMax(total);
                                circularProgressBarPushUps.setProgressWithAnimation((int)progress, (long) animationDuration);

                            }
                        }
                    });

            database.collection("Activities")
                    .document(UID)
                    .collection("PendingExercises")
                    .document("Running")
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                            if (snapshot!=null && snapshot.exists())
                            {
                                final long progress = snapshot.getLong("Current_Progres");
                                final long total   = snapshot.getLong("TotalMinutes");

                                circularProgressBarRunning.setProgressMax(total);
                                circularProgressBarRunning.setProgressWithAnimation((int)progress, (long) animationDuration);

                            }
                        }
                    });

            database.collection("Activities")
                    .document(UID)
                    .collection("PendingExercises")
                    .document("Swimming")
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                            if (snapshot!=null && snapshot.exists())
                            {
                                final long progress = snapshot.getLong("Current_Progres");

                                final long total   = snapshot.getLong("TotalMinutes");
                                circularProgressBarSwimming.setProgressMax(total);
                                circularProgressBarSwimming.setProgressWithAnimation((int)progress, (long) animationDuration);

                            }
                        }
                    });

            database.collection("Activities")
                    .document(UID)
                    .collection("PendingExercises")
                    .document("WeightLifting")
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                            if (snapshot!=null && snapshot.exists())
                            {
                                final long progress = snapshot.getLong("Current_Progres");

                                final long total   = snapshot.getLong("TotalMinutes");
                                circularProgressBarWeightLifting.setProgressMax(total);
                                circularProgressBarWeightLifting.setProgressWithAnimation((int)progress, (long) animationDuration);

                            }
                        }
                    });

            database.collection("Activities")
                    .document(UID)
                    .collection("PendingExercises")
                    .document("Yoga")
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                            if (snapshot!=null && snapshot.exists())
                            {
                                final long progress = snapshot.getLong("Current_Progres");

                                final long total   = snapshot.getLong("TotalMinutes");
                                circularProgressBarYoga.setProgressMax(total);
                                circularProgressBarYoga.setProgressWithAnimation((int)progress, (long) animationDuration);

                            }
                        }
                    });

            database.collection("Activities")
                    .document(UID)
                    .collection("PendingExercises")
                    .document("jogging")
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                            if (snapshot!=null && snapshot.exists())
                            {
                                final long progress = snapshot.getLong("Current_Progres");

                                final long total   = snapshot.getLong("TotalMinutes");
                                circularProgressBarJogging.setProgressMax(total);
                                circularProgressBarJogging.setProgressWithAnimation((int)progress, (long) animationDuration);

                            }
                        }
                    });



            return null;
        }
    }










    public class getProfiledata  extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(final Void... voids) {
            final String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            database.collection("users").document(UID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    if (e!=null)
                    {
                        Log.w(TAG, "listen:error", e);
                        return;
                    }
                    if (snapshot!=null && snapshot.exists())
                    {
                        String url = snapshot.getString("profile_pic_url");
                        String name = snapshot.getString("username");

                        if (url != null)
                        {

                            Glide.with(context.getApplicationContext())
                                    .load(url)
                                    .into(profilepic);

                        }
                        else
                        {

                        }
                        username.setText(name);

                    }
                    else
                    {
                        Log.d(TAG,"current data null");
                    }
                }
            });



            return null;
        }
    }



    public  class getExercises extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }



        @Override
        protected Void doInBackground(Void... voids) {

            database.collection("appicons").document("AllPics")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful())
                    {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists())
                        {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                            CYC = document.getString("CyclingUrl");
                            RPJ = document.getString("RopeJumpingUrl");
                            MDT = document.getString("MeditationUrl");
                            PUS = document.getString("PushUpsUrl");
                            RNG = document.getString("RunningUrl");
                            SWM = document.getString("SwimmingUrl");
                            WTL = document.getString("WeigthLiftingUrl");
                            YG  = document.getString("YogaUrl");
                            JG  = document.getString("JoggingUrl");

                            database.collection("appicons").whereEqualTo("Fetchable",true)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException t)
                                        {
                                            if (t != null) {
                                                Log.w(TAG, "listen:error", t);
                                                return;
                                            }
                                            for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                                                switch (dc.getType()) {
                                                    case ADDED:
                                                        Log.d(TAG, "New data: " + dc.getDocument().getData());

                                                        String IconLink  = dc.getDocument().getString("Url");
                                                        name      = dc.getDocument().getString("Name");


                                                        ExerciseGridData exerciseGridData = new ExerciseGridData(IconLink,name);
                                                        list.add(exerciseGridData);

                                                        GridAdapter gridAdapter = new GridAdapter(getActivity(),list,CYC,RPJ,MDT,PUS,RNG,SWM,WTL,YG,JG);
                                                        gridView.setLayoutManager(_sGridLayoutManager);
                                                        gridView.setAdapter(gridAdapter);


                                                        break;
                                                    case MODIFIED:
                                                        Log.d(TAG, "Modified data: " + dc.getDocument().getData());
                                                        break;
                                                    case REMOVED:
                                                        Log.d(TAG, "Removed data: " + dc.getDocument().getData());
                                                        break;
                                                }
                                            }
                                        }
                                    });

                        }
                        else
                            {
                            Log.d(TAG, "No such document");
                        }


                    }

                }
            });



            return null;
        }}





}
