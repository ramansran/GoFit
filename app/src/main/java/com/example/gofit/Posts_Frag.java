package com.example.gofit;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.collection.CircularIntArray;
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
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Posts_Frag extends Fragment {
    SearchView seachbox;
    TextView Uname;
    Button searchbtn;
    RecyclerView recyclerViewM;
    LinearLayout linearLayout;
    Story_Adapter story_adapter;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String posturl, UserId, profileurl, username;
    List<PostDataHoldingClass> list;
    CircleImageView circleImageView;
    Context context;
    private boolean isScrolling = false;
    private boolean isLastItemReached = false;
    private DocumentSnapshot lastVisible;
    private int limit = 5;
    String docId;
    String Url;
    String name;


    public Posts_Frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_posts_, container, false);

        recyclerViewM = view.findViewById(R.id.stories_recycler_view);
        recyclerViewM.setLayoutManager(new LinearLayoutManager(getActivity()));
        circleImageView = view.findViewById(R.id.profile_image);
        context = getContext();
        seachbox = view.findViewById(R.id.searchEditText);

        Uname = view.findViewById(R.id.user_name);
        list = new ArrayList<>();
        story_adapter       = new Story_Adapter(getActivity(),list);
        recyclerViewM.setAdapter(story_adapter);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            new getProfileData().execute();
            new getPosts().execute();

        }


        view.findViewById(R.id.exercise_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment      = new ExerciseListFrag();
                FragmentManager fm     = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
                ft.replace(R.id.frame,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        view.findViewById(R.id.Chat_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment      = new Chat_List_Frag();
                FragmentManager fm     = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
                ft.replace(R.id.frame,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        view.findViewById(R.id.Profile_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment      = new Profile_Frag();
                FragmentManager fm     = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
                ft.replace(R.id.frame,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;


    }

    public class getProfileData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            database.collection("users").document(UID)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                            if (snapshot != null && snapshot.exists()) {
                                Url = snapshot.getString("profile_pic_url");
                                name = snapshot.getString("username");
                                Glide.with(context)
                                        .load(Url)
                                        .into(circleImageView);
                                Uname.setText(name);

                            }
                        }
                    });
            return null;
        }
    }


    public class getPosts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            database.collection("SharedPosts").orderBy("TimeStamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "listen:error", e);
                        return;
                    }


                    for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                Log.d(TAG, "data : " + dc.getDocument().getData());

                                posturl    = dc.getDocument().getString("url");
                                UserId     = dc.getDocument().getString("UserId");
                                profileurl = dc.getDocument().getString("UserProfileUrl");
                                username   = dc.getDocument().getString("Username");
                                docId      = dc.getDocument().getString("documentId");;





                                PostDataHoldingClass postDataHoldingClass = new PostDataHoldingClass(username, posturl
                                        , UserId, profileurl,docId,Url,name);

                                list.add(postDataHoldingClass);
                                story_adapter = new Story_Adapter(getActivity(), list);
                                recyclerViewM.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerViewM.setAdapter(story_adapter);
                                story_adapter.notifyDataSetChanged();

                                seachbox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        story_adapter.getFilter().filter(newText);
                                        return false;
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







    /*public class getPosts2 extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {

            Query first = database.collection("SharedPosts").limit(limit);

            first.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful())
                    {
                        for (DocumentSnapshot dc : task.getResult())
                        {
                            posturl       = dc.getString("url");
                            UserId        = dc.getString("UserId");
                            profileurl    = dc.getString("UserProfileUrl");
                            username      = dc.getString("Username");



                            PostDataHoldingClass postDataHoldingClass = new PostDataHoldingClass(username,posturl
                                    ,UserId,profileurl);

                            list.add(postDataHoldingClass);

                        }

                        story_adapter.notifyDataSetChanged();
                        seachbox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                story_adapter.getFilter().filter(newText);
                                return false;
                            }
                        });




                        lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);

                        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                                {
                                    isScrolling = true;
                                }
                            }

                            @Override
                            public void onScrolled(@NonNull final RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                                assert linearLayoutManager != null;
                                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                                int visibleItemCount = linearLayoutManager.getChildCount();
                                int totalItemCount = linearLayoutManager.getItemCount();

                                if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached)
                                {
                                    isScrolling = false;
                                    Query next = database.collection("SharedPosts").startAfter(lastVisible).limit(limit);

                                    next.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                            if (t.isSuccessful())
                                            {
                                                for (DocumentSnapshot d : t.getResult())
                                                {
                                                    posturl       = d.getString("url");
                                                    UserId        = d.getString("UserId");
                                                    profileurl    = d.getString("UserProfileUrl");
                                                    username      = d.getString("Username");



                                                    PostDataHoldingClass postDataHoldingClass = new PostDataHoldingClass(username,posturl
                                                            ,UserId,profileurl);

                                                    list.add(postDataHoldingClass);
                                                }
                                                story_adapter.notifyDataSetChanged();

                                                seachbox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                                    @Override
                                                    public boolean onQueryTextSubmit(String query) {
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onQueryTextChange(String newText) {
                                                        story_adapter.getFilter().filter(newText);
                                                        return false;
                                                    }
                                                });


                                                if (t.getResult().size()<limit)
                                                {
                                                    isLastItemReached = true;
                                                }
                                            }
                                        }
                                    });
                                }

                            }
                        };
                        recyclerViewM.addOnScrollListener(onScrollListener);
                    }


                }



            });

            return null;
        }
    }


*/


}


























