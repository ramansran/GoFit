package com.example.gofit;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


/**
 * A simple {@link Fragment} subclass.
 */
public class resetpasswrd extends Fragment {
    Button btn;
    EditText enterEmail;
    Context context;


    public resetpasswrd() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View view = inflater.inflate(R.layout.fragment_resetpasswrd, container, false);

     btn =  view.findViewById(R.id.buttonS);
     enterEmail = view.findViewById(R.id.editTextS);
     context   = getContext();

     btn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             String  UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
             FirebaseFirestore database = FirebaseFirestore.getInstance();
             String email = enterEmail.getText().toString();
            if (email.equals(""))
            {
                enterEmail.setError("please enter email");
            }
            else {


                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("message", "Email sent.");
                            Toast.makeText(context,"e-mail sent",Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }

         }
     });



     return view;






    }



}
