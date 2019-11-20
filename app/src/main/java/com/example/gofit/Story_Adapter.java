package com.example.gofit;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Story_Adapter extends RecyclerView.Adapter<Story_Adapter.ViewHolder> implements Filterable {
   List<PostDataHoldingClass> list;
   List<PostDataHoldingClass> newlist;
    Context context;
    LayoutInflater layoutInflater;
    View view;
    String CurrentUserId,docId,myurl,myname;
    public static final int WITH_CHAT    = 0;
    public static final int WITHOUT_CHAT = 1;
    int width = LinearLayout.LayoutParams.MATCH_PARENT;
    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
    boolean focusable = true;
    PostDataHoldingClass postDataHoldingClass;





    public Story_Adapter(Context context,List<PostDataHoldingClass> mlist) {
        this.context = context;
        list = mlist;
        newlist = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public Story_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == WITH_CHAT)
        {
            layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.row_feed, parent, false);
            return new ViewHolder(view);
        }
        else
            {
                layoutInflater = LayoutInflater.from(context);
                view = layoutInflater.inflate(R.layout.row_feed2, parent, false);
                return new ViewHolder(view);
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onBindViewHolder(@NonNull Story_Adapter.ViewHolder holder, int position) {

        final PostDataHoldingClass postDataHoldingClass = list.get(position);
        String U_Url                                    = postDataHoldingClass.getProfileUrl();
        String P_Url                                    = postDataHoldingClass.getPostUrl();
        final String UID                                = postDataHoldingClass.getUid();
        docId                                           = postDataHoldingClass.getDocumentId();
        myurl                                           = postDataHoldingClass.getMyurl();
        myname                                          = postDataHoldingClass.getMyname();


        Glide.with(context)
                .load(U_Url)
                .into(holder.profileImage);
        Glide.with(context)
                .load(P_Url)
                .into(holder.Post);
        holder.Username.setText(postDataHoldingClass.getUsername());



              holder.chatbtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {


                      AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();

                      Fragment fragment = new ChatFragment();
                      Bundle bundle = new Bundle();
                      bundle.putString("OtherUserId", UID);
                      fragment.setArguments(bundle);
                      FragmentManager fm = appCompatActivity.getSupportFragmentManager();
                      FragmentTransaction ft = fm.beginTransaction();
                      ft.replace(R.id.frame, fragment);
                      ft.addToBackStack(null);
                      ft.commit();

                  }
              });






    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private  Filter filter = new Filter()
    {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
       List<PostDataHoldingClass> filteredList = new ArrayList<>();
       if (constraint == null || constraint.length() == 0)
       {
           filteredList.addAll(newlist);
       }
       else
       {
           String filterpattern = constraint.toString().toLowerCase().trim();

           for (PostDataHoldingClass pdhc : newlist)
           {
               if (pdhc.getUsername().toLowerCase().contains(filterpattern))
               {
                   filteredList.add(pdhc);
               }
           }
       }
       FilterResults results = new FilterResults();
       results.values = filteredList ;
       return results;
    }



    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        list.clear();
        list.addAll((List)results.values);
        notifyDataSetChanged();
    }

    };








    @Override
    public int getItemViewType(int position) {
        String userid  = FirebaseAuth.getInstance().getCurrentUser().getUid();
       if(list.get(position).getUid().equals(userid))
       {
           return WITHOUT_CHAT ;
       }
        else {
            return WITH_CHAT;
       }
    }





    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView profileImage;
        ImageView Post;
        TextView Username;
        LinearLayout chatbtn;
        LinearLayout Comment_Btn;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_image);
            Post         = itemView.findViewById(R.id.imgView_postPic);
            Username     = itemView.findViewById(R.id.tv_name);
            chatbtn      = itemView.findViewById(R.id.chatbuuton);
            Comment_Btn  = itemView.findViewById(R.id.comment_btn);


            Comment_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos    =  getAdapterPosition();
                    postDataHoldingClass = list.get(pos);
                    final String DID = postDataHoldingClass.getDocumentId();


                            Log.d("docid",docId);

                            final View Comment_PopUp_View = layoutInflater.inflate(R.layout.comment_window,null);

                            final List<CommentData> list                     = new ArrayList<>();
                            final List<String> comment_list                  = new ArrayList<>();
                            CurrentUserId                              = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            final FirebaseFirestore database           = FirebaseFirestore.getInstance();
                            final EditText editText                          = Comment_PopUp_View.findViewById(R.id.comment_edit_text);
                            Button   addComment                        = Comment_PopUp_View.findViewById(R.id.comment_add_btn);
                            Button   closeWindow                       = Comment_PopUp_View.findViewById(R.id.close_comment);
                            RecyclerView recyclerView                  = Comment_PopUp_View.findViewById(R.id.comment_recycler_View);


                            final LinearLayoutManager linearLayoutManager    = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager(linearLayoutManager);

                            final comment_adapter ca                         = new comment_adapter(context,list);
                            recyclerView.setAdapter(ca);


                            final PopupWindow popupWindow = new PopupWindow(Comment_PopUp_View,width,height,focusable);
                            popupWindow.setAnimationStyle(R.style.Animation);
                            popupWindow.showAtLocation(v, Gravity.CENTER,0,10);


                            database.collection("SharedPosts").document(DID)
                                    .collection("Comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    if (e != null) {
                                        Log.w(TAG, "listen:error", e);
                                        return;
                                    }
                                    list.clear();
                                    for (DocumentSnapshot ds : queryDocumentSnapshots)
                                    {
                                        String cmnt  = ds.getString("comment");
                                        String MYURL = ds.getString("url");
                                        String NAME  = ds.getString("name");

                                        CommentData commentData = new CommentData(MYURL,cmnt,CurrentUserId);
                                        list.add(commentData);

                                    }
                                    ca.notifyDataSetChanged();
                                }
                            });



                            addComment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String comment = editText.getText().toString();
                                    /*comment_list.add(comment);*/

                                    if (comment.equals(""))
                                    {
                                        editText.setError("cant add empty comment");
                                    }

                                    Map<String,Object>  map      = new HashMap<>();
                                    map.put("comment",comment);
                                    map.put("userId",CurrentUserId);
                                    map.put("url",myurl);
                                    map.put("name",myname);


                                    database.collection("SharedPosts")
                                            .document(DID).collection("Comments").document()
                                            .set(map,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            editText.setText("");
                                        }
                                    });

                                }
                            });

                            Comment_PopUp_View.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    return true;
                                }
                            });


                            closeWindow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow.dismiss();
                                }
                            });




                        }





            });

        }
    }






}
