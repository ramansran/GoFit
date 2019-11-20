package com.example.gofit;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

import javax.annotation.Nullable;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HistoryAdapter_Two extends RecyclerView.Adapter<HistoryAdapter_Two.ViewHolder> {
    Context context;
    List<history_holding_class> list;
    LayoutInflater layoutInflater;
    history_holding_class history_holding_class;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    FirebaseStorage    firebaseStorage = FirebaseStorage.getInstance();
    View view;
    String ProfileUrl,Username;




    public HistoryAdapter_Two(Context context, List<history_holding_class> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HistoryAdapter_Two.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.single_history_item,parent,false);
        return new HistoryAdapter_Two.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter_Two.ViewHolder holder, int position) {
        history_holding_class history_holding_class = list.get(position);
        holder.textView1.setText(history_holding_class.getStartingDate());
        holder.textView2.setText(history_holding_class.getEndingDate());
        holder.textView3.setText(history_holding_class.getName());
        holder.textView4.setText(history_holding_class.getFullhours());
        if (history_holding_class.getShared() == true)
        {
            holder.Sharebtn.setText("Shared");
            holder.Sharebtn.setEnabled(false);
        }
        else{
            holder.Sharebtn.setText("Share");
            holder.Sharebtn.setEnabled(true);
            }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView1,textView2,textView3,textView4;
        Button Sharebtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.StratingDate);
            textView2 = itemView.findViewById(R.id.EndingDate);
            textView3 = itemView.findViewById(R.id.E_Name);
            textView4 = itemView.findViewById(R.id.AmoutOfHours);
            Sharebtn  = itemView.findViewById(R.id.sharebutton);






                      Sharebtn.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {

                              final int pos = getAdapterPosition();


                              history_holding_class = list.get(pos);
                              final Boolean SharedValue = history_holding_class.getShared();

                              Toast.makeText(context,"Shared",Toast.LENGTH_SHORT).show();

                              if (SharedValue == false)
                              {
                                  Sharebtn.setVisibility(View.INVISIBLE);

                                  Sharebtn.setEnabled(false);

                                  view.setDrawingCacheEnabled(true);
                                  view.buildDrawingCache();
                                  Bitmap bitmap = view.getDrawingCache();

                                  ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                  bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                  byte[] HistoryPic = baos.toByteArray();

                                  final Random r = new Random();
                                  final int RandomNumber = r.nextInt(1000000 - 1) + 1;

                                  final String firebaseUser_UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                  final String path = "user/" + firebaseUser_UserId + RandomNumber + "HistoryPic.png";
                                  final StorageReference storageReference = firebaseStorage.getReference(path);
                                  final UploadTask uploadTask = storageReference.putBytes(HistoryPic);

                                  database.collection("users").document(firebaseUser_UserId)
                                          .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                              @Override
                                              public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                                                  if (e != null) {
                                                      Log.w(TAG, "listen:error", e);
                                                      return;
                                                  }

                                                  if (snapshot != null && snapshot.exists())
                                                  {
                                                      Log.d(TAG, "Current data: " + snapshot.getData());

                                                      ProfileUrl = snapshot.getString("profile_pic_url");
                                                      Username   = snapshot.getString("username");

                                                  } else {
                                                      Log.d(TAG, "Current data: null");
                                                  }
                                              }
                                          });


                                  uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                      @Override
                                      public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {



                                          Map<String, Object> map = new HashMap<>();
                                          map.put("Shared", true);

                                          database.collection("Activities").document(firebaseUser_UserId)
                                                  .collection("CompletedExercises")
                                                  .document(history_holding_class.getName()).collection(history_holding_class.getName())
                                                  .document(history_holding_class.getStartingDate() + "-" + history_holding_class.getEndingDate())
                                                  .set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>()
                                          {
                                              @Override
                                              public void onSuccess(Void aVoid) {



                                                  Task<Uri> url = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                                  url.addOnSuccessListener(new OnSuccessListener<Uri>() {

                                                      @Override
                                                      public void onSuccess(Uri uri) {


                                                          String generatedFileLink = uri.toString();
                                                          Random random        = new Random();
                                                          final int randomNumber     = random.nextInt(1000 - 1) + 1;
                                                          Log.d("photoLink :", generatedFileLink);

                                                          Map<String,Object> postMap = new HashMap<>();
                                                          postMap.put("TimeStamp", FieldValue.serverTimestamp());
                                                          postMap.put("url",                  generatedFileLink);
                                                          postMap.put("UserId",             firebaseUser_UserId);
                                                          postMap.put("UserProfileUrl",              ProfileUrl);
                                                          postMap.put("Username",                      Username);
                                                          postMap.put("documentId",String.valueOf(randomNumber));



                                                          database.collection("SharedPosts").document(String.valueOf(randomNumber))
                                                                  .set(postMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                              @Override
                                                              public void onSuccess(Void aVoid)
                                                              {
                                                                  Toast.makeText(context,"url uploaded",Toast.LENGTH_SHORT).show();
                                                              }
                                                          }).addOnFailureListener(new OnFailureListener() {
                                                              @Override
                                                              public void onFailure(@NonNull Exception e) {
                                                                  Log.e("data uploaded","fasle");
                                                              }
                                                          });

                                                      }
                                                  });



                                              }
                                          }).addOnFailureListener(new OnFailureListener() {
                                              @Override
                                              public void onFailure(@NonNull Exception e) {
                                                  Log.e("merging failed", "failed");
                                              }
                                          });
                                      }
                                  }).addOnFailureListener(new OnFailureListener() {
                                      @Override
                                      public void onFailure(@NonNull Exception e) {
                                          Log.e("profilepic url uploaded", "failed");
                                      }
                                  });


                              }
                              else
                              {
                                  Sharebtn.setEnabled(false);
                              }







                          }
                      });





                }









                }





    }






   /* */
