package com.example.gofit;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class DemandProfilePictureFrag extends Fragment {
Button NEXTBTN,UPLOADBTN;
private static final int RESULT_LOAD_IMAGE = 1 ;
Uri uri;
Task<Uri> url;
ImageView profile_ImageView;
Bitmap bitmap;
ByteArrayOutputStream baos;
byte[] PicData;
SignUp signUp;
private FirebaseStorage firebaseStorage;
String firebaseUser_UserId,generatedFileLink;

private FirebaseFirestore database;


    public DemandProfilePictureFrag() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_demand_profile_picture, container, false);
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.askusertypestatusbar));



       NEXTBTN            = view.findViewById(R.id.nextbtn);
       profile_ImageView  = view.findViewById(R.id.Demand_Profile_ImageView);
       UPLOADBTN          = view.findViewById(R.id.button6);
       signUp             = new SignUp();
       firebaseStorage    = FirebaseStorage.getInstance();
       database           = FirebaseFirestore.getInstance();

       NEXTBTN.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               addProfilePic(profile_ImageView);
               Fragment fragment = new LogIn();
               FragmentManager fm = getActivity().getSupportFragmentManager();
               FragmentTransaction ft = fm.beginTransaction();
               ft.replace(R.id.frame,fragment);
               ft.addToBackStack(null);
               ft.commit();
           }
       });


       UPLOADBTN.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent GalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               startActivityForResult(GalleryIntent,RESULT_LOAD_IMAGE);
           }
       });



       return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode== RESULT_OK && data != null)
        {
            uri = data.getData();
            profile_ImageView.setImageURI(uri);
        }
    }


     public void addProfilePic(ImageView circleImageView)
     {
         circleImageView.setDrawingCacheEnabled(true);
         circleImageView.buildDrawingCache();
         bitmap    = circleImageView.getDrawingCache();
         baos      = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
         circleImageView.setDrawingCacheEnabled(false);
         PicData   = baos.toByteArray();

         firebaseUser_UserId   = FirebaseAuth.getInstance().getCurrentUser().getUid();
         final String path = "user/" + firebaseUser_UserId  + "profilepic.png";
         final  StorageReference storageReference = firebaseStorage.getReference(path);
         final UploadTask uploadTask              = storageReference.putBytes(PicData);





         uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {


                 url = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                 url.addOnSuccessListener(new OnSuccessListener<Uri>() {
                     @Override
                     public void onSuccess(Uri uri) {

                         generatedFileLink = uri.toString();
                         Log.d("photoLink :", generatedFileLink);



                         final Map<String,Object> map = new HashMap<>();
                         map.put("profile_pic_url",String.valueOf(generatedFileLink));
                         database.collection("users").document(firebaseUser_UserId)
                                 .set(map, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                 Log.d("profilepic url uploaded","done");

                                 database.collection("Extras").document(firebaseUser_UserId)
                                         .set(map,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                         Log.d("Extras Written","done");
                                     }
                                 }).addOnFailureListener(new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {
                                         Log.d("Extras Written","fail");
                                     }
                                 });

                             }
                         }).addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Log.e("profilepic url uploaded","failed");
                             }
                         });



                     }
                 });







             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {

             }
         });



     }


}
