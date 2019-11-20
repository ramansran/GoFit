package com.example.gofit;


import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.provider.Contacts;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUp extends Fragment  {
TextView LOGINTEXTVIEW,SIGNUPTEXTVIEW,GENDER_ERROR;
AlphaAnimation fadein,fadeout;
TextView textView;
Button SIGNUP_BUTTON;
EditText USER_NAME_EDIT_TEXT,EMIAL_EDIT_TEXT,PASSWORD_EDIT_TEXT,CONFIRM_P_EDIT_TEXT;
RadioGroup RADIO_GROUP;
RadioButton RADIO_BTN;
String UserName,E_Mail,PPassword,ConfirmPassword;
private FirebaseAuth  mAuth;
FirebaseUser firebaseUser;
FirebaseFirestore database ;
String UID;
int genderId;
String gender;


    public SignUp() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.askusertypestatusbar));

        LOGINTEXTVIEW       = view.findViewById(R.id.login_text_view);
        SIGNUPTEXTVIEW      = view.findViewById(R.id.signup_text_view);
        USER_NAME_EDIT_TEXT = view.findViewById(R.id.username_editText);
        EMIAL_EDIT_TEXT     = view.findViewById(R.id.email_editText);
        PASSWORD_EDIT_TEXT  = view.findViewById(R.id.psswrd_editText);
        CONFIRM_P_EDIT_TEXT = view.findViewById(R.id.C_psswrd_editText);
        SIGNUP_BUTTON       = view.findViewById(R.id.goButton);
        RADIO_GROUP         = view.findViewById(R.id.radio);
        textView            = view.findViewById(R.id.gender_text);
        mAuth               =  FirebaseAuth.getInstance();
        database            = FirebaseFirestore.getInstance();


        fadeout = new AlphaAnimation(1.0f , 0.3f);
        LOGINTEXTVIEW.startAnimation(fadeout);
        fadeout.setDuration(1200);
        fadeout.setFillAfter(true);


       LOGINTEXTVIEW.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Fragment fragment = new LogIn();
               FragmentManager fm = getActivity().getSupportFragmentManager();
               FragmentTransaction ft = fm.beginTransaction();
               ft.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
               ft.replace(R.id.frame,fragment);
               ft.addToBackStack(null);
               ft.commit();
           }
       });


       SIGNUP_BUTTON.setOnClickListener(new View.OnClickListener() {
           @Override
               public void onClick(View view)

           {
                UserName            = USER_NAME_EDIT_TEXT.getText().toString().trim();
                E_Mail              = EMIAL_EDIT_TEXT.getText().toString().trim();
                PPassword           = PASSWORD_EDIT_TEXT.getText().toString().trim();
                ConfirmPassword     = CONFIRM_P_EDIT_TEXT.getText().toString().trim();
                genderId            = RADIO_GROUP.getCheckedRadioButtonId();
                RADIO_BTN           = getActivity().findViewById(genderId);


              if (UserName.equals(""))
               {
                   USER_NAME_EDIT_TEXT.setError("Username should'nt be empty");
                   USER_NAME_EDIT_TEXT.requestFocus();
               }
               else if (E_Mail.equals("") || !Patterns.EMAIL_ADDRESS.matcher(E_Mail).matches())
               {
                   EMIAL_EDIT_TEXT.setError("Please enter a valid e-mail");
                   EMIAL_EDIT_TEXT.requestFocus();
               }
               else if (PPassword.equals("") || PPassword.length()<6)
               {
                PASSWORD_EDIT_TEXT.setError("Password length must not be less than 6");
                PASSWORD_EDIT_TEXT.requestFocus();
              }
              else  if (ConfirmPassword.equals("") || !ConfirmPassword.equals(PPassword))
               {
                 CONFIRM_P_EDIT_TEXT.setError("Password does'nt match");
                 CONFIRM_P_EDIT_TEXT.requestFocus();
              }
              else if (RADIO_GROUP.getCheckedRadioButtonId()== -1)
              {
                 Toast.makeText(getActivity(),"please choose gender",Toast.LENGTH_SHORT).show();
              }

              else{


                  SigIn(E_Mail,PPassword);




               }
           }
       });


        return view;
    }


    public void SigIn(final String Email, final String Password)
    {

        mAuth.createUserWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getActivity(),"Sign In Success",Toast.LENGTH_LONG).show();
                             Log.d(TAG,"createUserWithEmail: Success");
                             UID =  FirebaseAuth.getInstance().getCurrentUser().getUid();
                             Log.e("id:" , UID);


                            gender = RADIO_BTN.getText().toString();

                             saveToFirestore(UserName,E_Mail,PPassword,gender);

                            Fragment fragment = new DemandProfilePictureFrag();
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
                            ft.replace(R.id.frame,fragment);
                            ft.addToBackStack(null);
                            ft.commit();

                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Sign In Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


     public void saveToFirestore(String Username,String emailId, String UserPassword, String Gender){



         Map<String,Object> userMap = new HashMap<>();
         Map<String,Object> userMapExtras = new HashMap<>();
         userMap.put("username",Username);
         userMap.put("E-Mail",emailId);
         userMap.put("userPassword",UserPassword);
         userMap.put("userGender",Gender);
         userMap.put("cycling",false);
         userMap.put("RopeJumping",false);
         userMap.put("Meditate",false);
         userMap.put("PushUp",false);
         userMap.put("Running",false);
         userMap.put("Swimming",false);
         userMap.put("WeightLifting",false);
         userMap.put("Yoga",false);
         userMap.put("jogging",false);

         userMapExtras.put("username",Username);
         userMapExtras.put("UID",UID);


         database.collection("users").document(UID).set(userMap)
                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         Log.d(TAG, "DocumentSnapshot successfully written!");
                     }
                 }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                         Log.w(TAG, "Error writing document", e);
             }
         });


         database.collection("Extras").document(UID).set(userMapExtras).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 Log.d(TAG, "Extras successfully written!");
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Log.w(TAG, "Error writing Extras", e);
             }
         });



     }






}
