package com.example.gofit;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogIn extends Fragment {
TextView LTEXTVIEW,STEXTVIEW,FrgtPasswrd;
AlphaAnimation fadein,fadeout;
EditText e_mail,psswrd;
Button logInBtn;
FirebaseAuth Lmauth;
FirebaseUser firebaseUser,firebaseUser2;
FirebaseAuth.AuthStateListener authStateListener;

    public LogIn() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.askusertypestatusbar));


        LTEXTVIEW = view.findViewById(R.id.login_text_view);
        STEXTVIEW = view.findViewById(R.id.signup_text_view);
        e_mail    = view.findViewById(R.id.loginEditText);
        FrgtPasswrd= view.findViewById(R.id.forgot_psswrd);
        psswrd    = view.findViewById(R.id.loginEditText2);
        logInBtn  = view.findViewById(R.id.LogInBtn);
        Lmauth    = FirebaseAuth.getInstance();



        fadein = new AlphaAnimation(0.3f,1.0f);
        fadeout = new AlphaAnimation(1.0f , 0.3f);

        STEXTVIEW.startAnimation(fadeout);
        fadeout.setDuration(1200);
        fadeout.setFillAfter(true);


        STEXTVIEW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new SignUp();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
                ft.replace(R.id.frame,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserEmail   = e_mail.getText().toString().trim();
                String UserPasswrd = psswrd.getText().toString().trim();

                if (UserEmail.equals(""))
                {
                    e_mail.setError("InValid E-mail");
                }
                else if (UserPasswrd.equals(""))
                {
                    psswrd.setError("Invalid Password");
                }

                else {

                    LogInUser(UserEmail,UserPasswrd);
                }
            }
        });

        FrgtPasswrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new resetpasswrd();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
                ft.replace(R.id.frame,fragment);
                ft.commit();


            }
        });

        return view;
    }



    public void LogInUser(String UE, String Up)
    {
        Lmauth.signInWithEmailAndPassword(UE,Up)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getActivity(), "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();
                            firebaseUser = Lmauth.getCurrentUser();

                            Fragment fragment = new ExerciseListFrag();
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
                            ft.replace(R.id.frame,fragment);
                            ft.addToBackStack(null);
                            ft.commit();

                        }
                        else{
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }



}
