package com.example.gofit;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.media.MediaBrowserServiceCompat;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateProfile extends Fragment {
    ImageView NewProfileImage,PEYE,CPEYE;
    EditText  Username,E_mail,Password,ConfirmPassword;
    Button    Submit;
    private static final int RESULT_LOAD_IMAGE = 1;
    String userID;
    FirebaseFirestore database;
    Bitmap bitmap;
    Uri uri;
    String NAME,E_MAIL,PASSWORD,CONFIRM_PASSWORD;
    Context context;
    int SDK_INT = android.os.Build.VERSION.SDK_INT;
    String e_mail;
    String password;
    ByteArrayOutputStream baos;
    byte[] picData;
    FirebaseStorage firebaseStorage;
    Task<Uri> url;
    String generatedFileLink;



    public UpdateProfile()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);

        NewProfileImage         = view.findViewById(R.id.newprofile);
        Username                = view.findViewById(R.id.NewUsername);
        E_mail                  = view.findViewById(R.id.NewEmail);
        Password                = view.findViewById(R.id.NewPassword);
        ConfirmPassword         = view.findViewById(R.id.ConfirmNewPasssword);
        Submit                  = view.findViewById(R.id.update_profile_button);
        userID                  = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database                = FirebaseFirestore.getInstance();
        context                 = getContext();
        PEYE                    = view.findViewById(R.id.password_eye);
        CPEYE                   = view.findViewById(R.id.Cpassword_eye);
        firebaseStorage         = FirebaseStorage.getInstance();





        NewProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GallerIntent    = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(GallerIntent,RESULT_LOAD_IMAGE);
            }
        });


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NAME             = Username.getText().toString().trim();
                E_MAIL           = E_mail.getText().toString().trim();
                PASSWORD         = Password.getText().toString().trim();
                CONFIRM_PASSWORD = ConfirmPassword.getText().toString().trim();

                if (NAME.equals(""))
                {
                    Username.setError("Invalid Username");
                }
                else if (E_MAIL.equals("") || !Patterns.EMAIL_ADDRESS.matcher(E_MAIL).matches())
                {
                    E_mail.setError("Invalid E-mail");
                }
                else if (PASSWORD.equals(""))
                {
                    Password.setError("Invalid Passowrd");
                }
                else if (CONFIRM_PASSWORD.equals("") || !CONFIRM_PASSWORD.equals(PASSWORD))
                {
                    ConfirmPassword.setError("Invalid Password");
                }
                else {

                    reAuthenticate(E_MAIL,CONFIRM_PASSWORD);
                }

            }
        });

        PEYE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Password.setTransformationMethod(null);
            }
        });

        CPEYE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmPassword.setTransformationMethod(null);
            }
        });


        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            new getPreviousData().execute();

        }


        return view;
    }








    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null)
        {
            uri = data.getData();
            Glide.with(context)
                    .load(uri)
                    .into(NewProfileImage);
        }

    }






    public class getPreviousData  extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            database.collection("users").document(userID)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                            if (snapshot!=null && snapshot.exists())
                            {
                                String name             = snapshot.getString("username");
                                e_mail                  = snapshot.getString("E-Mail");
                                password                = snapshot.getString("userPassword");
                                String confirm_password = snapshot.getString("userPassword");
                                String profile_url      = snapshot.getString("profile_pic_url");

                                Username.setText(name);
                                E_mail.setText(e_mail);
                                Password.setText(password);
                                ConfirmPassword.setText(confirm_password);
                                Glide.with(context)
                                        .load(profile_url)
                                        .into(NewProfileImage);

                            }
                        }
                    });


            return null;
        }
    }


    public void reAuthenticate(final String email, final String psw)
    {


            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            AuthCredential authCredential = EmailAuthProvider.getCredential(e_mail,password);
            user.reauthenticate(authCredential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            final FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                            user1.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        user1.updatePassword(psw).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    NewProfileImage.setDrawingCacheEnabled(true);
                                                    NewProfileImage.buildDrawingCache();
                                                    bitmap        = NewProfileImage.getDrawingCache();
                                                    baos          = new ByteArrayOutputStream();
                                                    bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                                                    NewProfileImage.setDrawingCacheEnabled(false);
                                                    picData       = baos.toByteArray();


                                                    final String path = "user/" + userID  + "profilepic.png";
                                                    final StorageReference storageReference = firebaseStorage.getReference(path);
                                                    final UploadTask uploadTask              = storageReference.putBytes(picData);


                                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                            url              = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                                            url.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    generatedFileLink = uri.toString();
                                                                    saveTOFirestore(NAME,E_MAIL,CONFIRM_PASSWORD,generatedFileLink);
                                                                }
                                                            });
                                                        }
                                                    });

                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                  }




    public void saveTOFirestore(String name,String mail,String cpswd,String url)
    {

        DocumentReference documentReference = database.collection("users").document(userID);
        DocumentReference documentReference2 = database.collection("Extras").document(userID);

        documentReference.update("E-Mail",mail);
        documentReference.update("username",name);
        documentReference.update("userPassword",cpswd);
        documentReference.update("profile_pic_url",url);
        documentReference2.update("profile_pic_url",url);
        documentReference2.update("username",name);


    }








}
