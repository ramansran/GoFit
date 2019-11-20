package com.example.gofit;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
RecyclerView recyclerView;
ImageView backbutton,profilefoto;
String OTHERUSERID;
FirebaseFirestore database;
String UID;
ImageView send;
EditText messageEditText;
List<ChatMainData> list;
Context context;
TextView textView;
LinearLayoutManager linearLayoutManager;
String name;
String fotoUrl;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_chat, container, false);

        backbutton       = view.findViewById(R.id.backbtn);
        database         = FirebaseFirestore.getInstance();
        UID              = FirebaseAuth.getInstance().getCurrentUser().getUid();
        send             = view.findViewById(R.id.sendButton);
        messageEditText  = view.findViewById(R.id.keyboard_edit);
        list             = new ArrayList<>();
        recyclerView     = view.findViewById(R.id.messagesrecycler);
        context          = getContext();
        profilefoto      = view.findViewById(R.id.pImage);
        textView         = view.findViewById(R.id.usernameChat);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);


        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            OTHERUSERID = bundle.getString("OtherUserId");
        }

        database.collection("Extras").document(OTHERUSERID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e)
                    {
                        if (snapshot!=null && snapshot.exists())
                        {
                             name = snapshot.getString("username");
                             fotoUrl = snapshot.getString("profile_pic_url");

                            textView.setText(name);
                            Glide.with(context)
                                    .load(fotoUrl)
                                    .into(profilefoto);
                            ReadMessage(UID,OTHERUSERID);

                        }
                    }
                });


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Chat_List_Frag();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = messageEditText.getText().toString();
                if (!msg.equals(""))
                {
                    sendMessage(UID,OTHERUSERID,msg);
                }
                else {
                    Toast.makeText(getActivity(),"Can't Send Empty Message",Toast.LENGTH_LONG).show();
                }
                messageEditText.setText("");

            }
        });




        return view;
    }





    public void sendMessage(String SenderID,String RecieverID , String Message)
    {
        Map<String,Object> chatMap = new HashMap<>();
        chatMap.put("SenderId",SenderID);
        chatMap.put("RecieverId",RecieverID);
        chatMap.put("Message",Message);
        chatMap.put("OtherUserName",name);
        chatMap.put("OtherUserProfileUrl",fotoUrl);
        chatMap.put("TimeStamp", FieldValue.serverTimestamp());

        database.collection("Chats").document()
                .set(chatMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }



public  void  ReadMessage (final String MyID, final String OtherUserID)
{
    database.collection("Chats").orderBy("TimeStamp", Query.Direction.ASCENDING)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    list.clear();
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots)
                    {
                        ChatMainData chatMainData = snapshot.toObject(ChatMainData.class);

                        if (chatMainData.getRecieverId().equals(MyID) && chatMainData.getSenderId().equals(OtherUserID) ||
                        chatMainData.getRecieverId().equals(OtherUserID) && chatMainData.getSenderId().equals(MyID))
                        {
                            list.add(chatMainData);
                            Log.d("mainData", String.valueOf(snapshot.getData()));
                        }
                        else
                        {

                        }
                        ChatAdapter chatAdapter = new ChatAdapter(list,getActivity());
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(chatAdapter);

                        chatAdapter.notifyDataSetChanged();



                    }
                }
            });
}




}
