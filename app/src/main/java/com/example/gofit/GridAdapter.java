package com.example.gofit;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static android.view.View.Y;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class GridAdapter  extends RecyclerView.Adapter<GridAdapter.ViewHolder>   {
Context context;
List<ExerciseGridData> Nlist;
LayoutInflater layoutInflater;
int pos;
ExerciseGridData exerciseGridDataclickedItem;
NumberPicker numberPicker;
Button CalenderButton,Exercisesend,CyclingExercisesend;
TextView startdate_textView,enddate_TextView;
Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy");
int width = LinearLayout.LayoutParams.MATCH_PARENT;
int height = LinearLayout.LayoutParams.WRAP_CONTENT;
boolean focusable = true;
FirebaseFirestore database = FirebaseFirestore.getInstance();
String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
WriteBatch writeBatch = database.batch();
String startDate;
String endDate;
String url1,url2,url3,url4,url5,url6,url7,url8,url9;
ArrayList dataList =  new ArrayList();
boolean CyclingClicked = true;
int DOM,M,Y;
Boolean CYCLING,ROPE_JUMPINMG,MEDITATE,PUSH_UP,RUNNING,SWIMMING,WEIGHT_LIFTING,YOGA,JOGGING;





    public GridAdapter(Context context, List<ExerciseGridData> list, String CYC, String RPJ, String MDT, String PUS, String RNG, String SWM, String WTL, String YG, String JG){
        this.context = context;
        this.Nlist = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.url1 = CYC;
        this.url2 = RPJ;
        this.url3 = MDT;
        this.url4 = PUS;
        this.url5 = RNG;
        this.url6 = SWM;
        this.url7 = WTL;
        this.url8 = YG;
        this.url9 = JG;

    }



    @NonNull
    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = layoutInflater.inflate(R.layout.single_item,parent,false);
       return new GridAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridAdapter.ViewHolder holder, int position) {

     ExerciseGridData exerciseGridData = Nlist.get(position);

     String url = exerciseGridData.getUrl();

        Glide.with(context)
                .load(url)
                .into(holder.iconPlace);

     holder.iconNamePlace.setText(exerciseGridData.getName());



    }



    @Override
    public int getItemCount() {
        return Nlist.size();
    }




    public class ViewHolder extends  RecyclerView.ViewHolder {
            ImageView iconPlace;
            TextView  iconNamePlace;
       public ViewHolder(@NonNull final View itemView) {
           super(itemView);



             iconPlace = itemView.findViewById(R.id.set_icon);
             iconNamePlace = itemView.findViewById(R.id.set_text);


             final DocumentReference documentReferenceV1 = database.collection("users").document(UID);

             documentReferenceV1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                 @Override
                 public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                     if (e != null) {
                         Log.w(TAG, "Listen failed.", e);
                         return;
                     }

                     if (snapshot != null && snapshot.exists())
                     {
                        CYCLING          = snapshot.getBoolean("cycling");
                        ROPE_JUMPINMG    = snapshot.getBoolean("RopeJumping");
                        MEDITATE         = snapshot.getBoolean("Meditate");
                        PUSH_UP          = snapshot.getBoolean("PushUp");
                        RUNNING          = snapshot.getBoolean("Running");
                        SWIMMING         = snapshot.getBoolean("Swimming");
                        WEIGHT_LIFTING   = snapshot.getBoolean("WeightLifting");
                        YOGA             = snapshot.getBoolean("Yoga");
                        JOGGING          = snapshot.getBoolean("jogging");

                     }
                     else
                         {
                         Log.d(TAG, "Current data: null");
                     }
                 }
             });

              itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                      pos = getAdapterPosition();
                      if (pos != RecyclerView.NO_POSITION)
                     {
                         exerciseGridDataclickedItem =  Nlist.get(pos);




                         // IF CLICKED ON CYCLING

                         if (exerciseGridDataclickedItem.getName().equals("cycling") && !CYCLING )
                         {



                             // INFLATING "cycling_popup.xml"
                                 final View Cycling_popUpView = layoutInflater.inflate(R.layout.cycling_popup,null);

                                 CyclingExercisesend  = Cycling_popUpView.findViewById(R.id.exercise_upload_button);
                                 numberPicker = Cycling_popUpView.findViewById(R.id.number_picker);
                                 numberPicker.setMinValue(1);
                                 numberPicker.setMaxValue(70);
                                 numberPicker.setWrapSelectorWheel(true);
                                 CalenderButton = Cycling_popUpView.findViewById(R.id.calenderbb);
                                 startdate_textView = Cycling_popUpView.findViewById(R.id.plan_start_text);
                                 enddate_TextView   = Cycling_popUpView.findViewById(R.id.plan_ends_text);




                                 final PopupWindow popupWindow = new PopupWindow(Cycling_popUpView,width,height,focusable);
                                 popupWindow.setAnimationStyle(R.style.Animation);
                                 popupWindow.showAtLocation(v, Gravity.CENTER,0,-155);


                                 Cycling_popUpView.setOnTouchListener(new View.OnTouchListener() {
                                     @Override
                                     public boolean onTouch(View v, MotionEvent event) {

                                         return  true;
                                     }
                                 });

                                 // OPENING CALENDER AND PICKING DATE
                                 CalenderButton.setOnClickListener(new View.OnClickListener() {

                                     @RequiresApi(api = Build.VERSION_CODES.N)
                                     @Override
                                     public void onClick(View v) {
                                         DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                                             @Override
                                             public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                 startdate_textView.setText(new StringBuilder().append(dayOfMonth).append(":").append(month+1)
                                                 .append(":").append(year));
                                                 DOM = dayOfMonth;
                                                 M   = (month+1);
                                                 Y   = year;
                                                 calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                                 calendar.set(Calendar.MONTH,month);
                                                 calendar.set(Calendar.YEAR,year);

                                                 calendar.add(Calendar.DAY_OF_MONTH,7);

                                                 enddate_TextView.setText(new StringBuilder().append(format.format(calendar.getTime())));
                                             }
                                         };

                                         DatePickerDialog dpDialog = new DatePickerDialog(context,dateSetListener,
                                                 calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));

                                         dpDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000   );
                                         dpDialog.show();
                                     }
                                 });

                                 // SENDING DATA TO FIRESTORE

                                 CyclingExercisesend.setOnClickListener(new View.OnClickListener()
                                 {
                                     @Override
                                     public void onClick(View v) {
                                         popupWindow.dismiss();

                                         startDate = startdate_textView.getText().toString().trim();
                                         endDate   = enddate_TextView.getText().toString();
                                         if (startDate.equals(""))
                                         {
                                             Toast.makeText(context,"Please select start date from calender",Toast.LENGTH_LONG).show();
                                         }

                                         else {

                                       int numberpickervalue = numberPicker.getValue();
                                       double seconds           = numberpickervalue*3600;

                                       int minutes           = numberpickervalue * 60 ;
                                       int  dividedMinutes    =  (minutes / 7);
                                       int    finalMinutes      = (dividedMinutes * 7);



                                       double dividedValue1 =     seconds / 7;
                                       Log.d("splited seconds ", String.valueOf(dividedValue1));

                                             int day = (int) (dividedValue1 / (24 * 3600));
                                             int dividedValue2 = (int) (dividedValue1 % (24 * 3600));
                                             int hour = (int) (dividedValue2 / 3600);
                                             dividedValue2 %= 3600;
                                             int minutestwo = dividedValue2 / 60 ;

                                             String SplitedTime = hour+" hours - "+minutestwo + " minutes" ;


                                       Log.d("alldata", SplitedTime);






                                       DocumentReference Doc = database.collection("Activities").document(UID)
                                               .collection("PendingExercises").document("cycling");


                                             final Map<String,Object> Map     = new HashMap<>();
                                             Map.put("TimeStamp", FieldValue.serverTimestamp());
                                             Map.put("ExerciseName" ,exerciseGridDataclickedItem.getName());
                                             Map.put("StartingDate",startDate);
                                             Map.put("End_Date",endDate);
                                             Map.put("DayOfMonth",DOM);
                                             Map.put("Month",M);
                                             Map.put("Year",Y);
                                             Map.put("total_hours_for_week",String.valueOf(numberpickervalue)+": hours");
                                             Map.put("hours_per_day",SplitedTime);
                                             Map.put("TotalMinutes",finalMinutes);
                                             Map.put("PerDayMinutes",dividedMinutes);
                                             Map.put("Current_Progres",0);








                                             //Batch Start
                                             final WriteBatch CompletedBatch = database.batch();
                                             final WriteBatch batch = database.batch();


                                             // Day1 Map

                                             final  DocumentReference CDoc1 = database.collection("Activities").document(UID)
                                                     .collection("PendingExercises").document("cycling")
                                                     .collection("cycling").document("Day1");


                                             final Map<String,Object> CD1M = new HashMap<>();
                                             CD1M.put("Day","Day1");
                                             CD1M.put("ExerciseName" ,exerciseGridDataclickedItem.getName());
                                             CD1M.put("hours_per_day",SplitedTime);
                                             CD1M.put("FullHours",String.valueOf(numberpickervalue)+ ": hours");
                                             CD1M.put("SecondsUploaded",false);
                                             CD1M.put("PicLink",url1);
                                             CD1M.put("TotalMinutes",finalMinutes);
                                             CD1M.put("PerDayMinutes",dividedMinutes);
                                             CD1M.put("CompletedMinutes",0);

                                             batch.set(CDoc1,CD1M);


                                             /////////////////////////////////////// Completed Batch
                                             final  DocumentReference CompletedCDoc1 = database.collection("Activities")
                                                     .document(UID).collection("CompletedExercises").document("cycling");

                                             final Map<String,Object> CompletedCDoc1M = new HashMap<>();
                                             CompletedCDoc1M.put("Day1",false);
                                             CompletedBatch.set(CompletedCDoc1,CompletedCDoc1M);
                                             //////////////////////////////////////



                                             //Day2 Map
                                             final DocumentReference CDoc2 = database.collection("Activities").document(UID)
                                                     .collection("PendingExercises").document("cycling")
                                                     .collection("cycling").document("Day2");

                                             final Map<String,Object> CD2M = new HashMap<>();
                                             CD2M.put("Day","Day2");
                                             CD2M.put("ExerciseName" ,exerciseGridDataclickedItem.getName());
                                             CD2M.put("hours_per_day",SplitedTime);
                                             CD2M.put("FullHours",String.valueOf(numberpickervalue)+ ": hours");
                                             CD2M.put("SecondsUploaded",false);
                                             CD2M.put("PicLink",url1);
                                             CD2M.put("TotalMinutes",finalMinutes);
                                             CD2M.put("PerDayMinutes",dividedMinutes);
                                             CD2M.put("CompletedMinutes",0);
                                             batch.set(CDoc2,CD2M);


                                            //* /////////////////////////////////////// Completed Batch
                                             final  DocumentReference CompletedCDoc2 = database.collection("Activities")
                                                     .document(UID).collection("CompletedExercises").document("cycling");

                                             final Map<String,Object> CompletedCDoc2M = new HashMap<>();
                                             CompletedCDoc1M.put("Day2",false);
                                             CompletedBatch.set(CompletedCDoc2,CompletedCDoc2M);


                                             //////////////////////////////////////*//*



                                             //Day3 Map
                                             final  DocumentReference CDoc3 = database.collection("Activities").document(UID)
                                                     .collection("PendingExercises").document("cycling")
                                                     .collection("cycling").document("Day3");

                                             final Map<String,Object> CD3M = new HashMap<>();
                                             CD3M.put("Day","Day3");
                                             CD3M.put("ExerciseName" ,exerciseGridDataclickedItem.getName());
                                             CD3M.put("hours_per_day",SplitedTime);
                                             CD3M.put("FullHours",String.valueOf(numberpickervalue)+ ": hours");
                                             CD3M.put("SecondsUploaded",false);
                                             CD3M.put("PicLink",url1);
                                             CD3M.put("TotalMinutes",finalMinutes);
                                             CD3M.put("PerDayMinutes",dividedMinutes);
                                             CD3M.put("CompletedMinutes",0);
                                             batch.set(CDoc3,CD3M);


                                            //* /////////////////////////////////////// Completed Batch
                                             final  DocumentReference CompletedCDoc3 = database.collection("Activities")
                                                     .document(UID).collection("CompletedExercises").document("cycling");

                                             final Map<String,Object> CompletedCDoc3M = new HashMap<>();
                                             CompletedCDoc3M.put("Day3",false);
                                             CompletedBatch.set(CompletedCDoc3,CompletedCDoc3M);
                                             //////////////////////////////////////*//*


                                             //Day4 Map
                                             final  DocumentReference CDoc4 = database.collection("Activities").document(UID)
                                                     .collection("PendingExercises").document("cycling")
                                                     .collection("cycling").document("Day4");

                                             final Map<String,Object> CD4M = new HashMap<>();
                                             CD4M.put("Day","Day4");
                                             CD4M.put("ExerciseName" ,exerciseGridDataclickedItem.getName());
                                             CD4M.put("hours_per_day",SplitedTime);
                                             CD4M.put("FullHours",String.valueOf(numberpickervalue)+ ": hours");
                                             CD4M.put("SecondsUploaded",false);
                                             CD4M.put("PicLink",url1);
                                             CD4M.put("TotalMinutes",finalMinutes);
                                             CD4M.put("PerDayMinutes",dividedMinutes);
                                             CD4M.put("CompletedMinutes",0);
                                             batch.set(CDoc4,CD4M);

                                            //* /////////////////////////////////////// Completed Batch
                                             final  DocumentReference CompletedCDoc4 = database.collection("Activities")
                                                     .document(UID).collection("CompletedExercises").document("cycling");

                                             final Map<String,Object> CompletedCDoc4M = new HashMap<>();
                                             CompletedCDoc4M.put("Day4",false);
                                             CompletedBatch.set(CompletedCDoc4,CompletedCDoc4M);
                                             //////////////////////////////////////*//*



                                             //Day5 Map
                                             final  DocumentReference CDoc5 = database.collection("Activities").document(UID)
                                                     .collection("PendingExercises").document("cycling")
                                                     .collection("cycling").document("Day5");

                                             final Map<String,Object> CD5M = new HashMap<>();
                                             CD5M.put("Day","Day5");
                                             CD5M.put("ExerciseName" ,exerciseGridDataclickedItem.getName());
                                             CD5M.put("hours_per_day",SplitedTime);
                                             CD5M.put("FullHours",String.valueOf(numberpickervalue)+ ": hours");
                                             CD5M.put("SecondsUploaded",false);
                                             CD5M.put("PicLink",url1);
                                             CD5M.put("TotalMinutes",finalMinutes);
                                             CD5M.put("PerDayMinutes",dividedMinutes);
                                             CD5M.put("CompletedMinutes",0);
                                             batch.set(CDoc5,CD5M);

                                             /////////////////////////////////////// Completed Batch
                                             final  DocumentReference CompletedCDoc5 = database.collection("Activities")
                                                     .document(UID).collection("CompletedExercises").document("cycling");

                                             final Map<String,Object> CompletedCDoc5M = new HashMap<>();
                                             CompletedCDoc5M.put("Day5",false);
                                             CompletedBatch.set(CompletedCDoc5,CompletedCDoc5M);
                                             //////////////////////////////////////*//*

                                             //Day6 Map
                                             final  DocumentReference CDoc6 = database.collection("Activities").document(UID)
                                                     .collection("PendingExercises").document("cycling")
                                                     .collection("cycling").document("Day6");

                                             final Map<String,Object> CD6M = new HashMap<>();
                                             CD6M.put("Day","Day6");
                                             CD6M.put("ExerciseName" ,exerciseGridDataclickedItem.getName());
                                             CD6M.put("hours_per_day",SplitedTime);
                                             CD6M.put("FullHours",String.valueOf(numberpickervalue)+ ": hours");
                                             CD6M.put("SecondsUploaded",false);
                                             CD6M.put("PicLink",url1);
                                             CD6M.put("TotalMinutes",finalMinutes);
                                             CD6M.put("PerDayMinutes",dividedMinutes);
                                             CD6M.put("CompletedMinutes",0);
                                             batch.set(CDoc6,CD6M);


                                            //* /////////////////////////////////////// Completed Batch
                                             final  DocumentReference CompletedCDoc6 = database.collection("Activities")
                                                     .document(UID).collection("CompletedExercises").document("cycling");

                                             final Map<String,Object> CompletedCDoc6M = new HashMap<>();
                                             CompletedCDoc6M.put("Day6",false);
                                             CompletedBatch.set(CompletedCDoc6,CompletedCDoc6M);
                                             //////////////////////////////////////*//*

                                             //Day6 Map
                                             final  DocumentReference CDoc7 = database.collection("Activities").document(UID)
                                                     .collection("PendingExercises").document("cycling")
                                                     .collection("cycling").document("Day7");

                                             final Map<String,Object> CD7M = new HashMap<>();
                                             CD7M.put("Day","Day7");
                                             CD7M.put("ExerciseName" ,exerciseGridDataclickedItem.getName());
                                             CD7M.put("hours_per_day",SplitedTime);
                                             CD7M.put("FullHours",String.valueOf(numberpickervalue)+ ": hours");
                                             CD7M.put("SecondsUploaded",false);
                                             CD7M.put("PicLink",url1);
                                             CD7M.put("TotalMinutes",finalMinutes);
                                             CD7M.put("PerDayMinutes",dividedMinutes);
                                             CD7M.put("CompletedMinutes",0);
                                             batch.set(CDoc7,CD7M);

                                             /////////////////////////////////////// Completed Batch
                                             final  DocumentReference CompletedCDoc = database.collection("Activities")
                                                     .document(UID).collection("CompletedExercises").document("cycling");

                                             final Map<String,Object> CompletedCDocM = new HashMap<>();
                                             CompletedCDocM.put("Day1",false);
                                             CompletedCDocM.put("Day2",false);
                                             CompletedCDocM.put("Day3",false);
                                             CompletedCDocM.put("Day4",false);
                                             CompletedCDocM.put("Day5",false);
                                             CompletedCDocM.put("Day6",false);
                                             CompletedCDocM.put("Day7",false);
                                             CompletedCDocM.put("Fail",false);
                                             CompletedCDocM.put("Skip1",false);
                                             CompletedCDocM.put("Skip2",false);
                                             CompletedCDocM.put("Skip3",false);
                                             CompletedCDocM.put("Skip4",false);
                                             CompletedCDocM.put("Skip5",false);
                                             CompletedCDocM.put("Skip6",false);
                                             CompletedCDocM.put("Skip7",false);







                                             CompletedBatch.set(CompletedCDoc,CompletedCDocM);
                                             //////////////////////////////////////

                                             //Batch End 



                                           

                                             Doc.set(Map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                 @Override
                                                 public void onSuccess(Void aVoid) {
                                                     Log.d(TAG, "DocumentSnapshot successfully written!");

                                                         CompletedBatch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task) {

                                                                 documentReferenceV1.update("cycling",true);
                                                                 Toast.makeText(context,"data added",Toast.LENGTH_LONG).show();


                                                                 batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                     @Override
                                                                     public void onComplete(@NonNull Task<Void> task)
                                                                     {


                                                                     }
                                                                 });
                                                             }
                                                         });
                                                 }
                                             }).addOnFailureListener(new OnFailureListener() {
                                                 @Override
                                                 public void onFailure(@NonNull Exception e) {
                                                     Log.w(TAG, "Error writing document", e);
                                                     Toast.makeText(context,"data not added",Toast.LENGTH_LONG).show();
                                                 }
                                             });


                                     }}
                                 });

                         }
                         else if(exerciseGridDataclickedItem.getName().equals("cycling") && CYCLING)
                         {
                             Toast.makeText(context,"You already set target for cycling , COMPLETE it or DELETE it",Toast.LENGTH_LONG).show();
                         }














                         // IF CLICKED ON ROPE JUMPING

                         else if (exerciseGridDataclickedItem.getName().equals("RopeJumping") && !ROPE_JUMPINMG)
                         {
                             // INFLATING rope_jumping_popup.xml

                             final View Rope_Jumping_popUpView = layoutInflater.inflate(R.layout.rope_jumping_popup,null);

                             Exercisesend  = Rope_Jumping_popUpView.findViewById(R.id.exercise_upload_button);
                             numberPicker = Rope_Jumping_popUpView.findViewById(R.id.number_picker);
                             numberPicker.setMinValue(1);
                             numberPicker.setMaxValue(70);
                             numberPicker.setWrapSelectorWheel(true);
                             CalenderButton = Rope_Jumping_popUpView.findViewById(R.id.calenderbb);
                             startdate_textView = Rope_Jumping_popUpView.findViewById(R.id.plan_start_text);
                             enddate_TextView   = Rope_Jumping_popUpView.findViewById(R.id.plan_ends_text);





                             final PopupWindow popupWindow = new PopupWindow(Rope_Jumping_popUpView,width,height,focusable);
                             popupWindow.setAnimationStyle(R.style.Animation);
                             popupWindow.showAtLocation(v, Gravity.CENTER,0,-155);


                             Rope_Jumping_popUpView.setOnTouchListener(new View.OnTouchListener() {
                                 @Override
                                 public boolean onTouch(View v, MotionEvent event) {

                                     return  true;
                                 }
                             });

                             // OPENING CALENDER AND SETTING DATE

                             CalenderButton.setOnClickListener(new View.OnClickListener() {

                                 @RequiresApi(api = Build.VERSION_CODES.N)
                                 @Override
                                 public void onClick(View v) {
                                     DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                                         @Override
                                         public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                             startdate_textView.setText(new StringBuilder().append(dayOfMonth).append(":").append(month+1)
                                                     .append(":").append(year));

                                             calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                             calendar.set(Calendar.MONTH,month);
                                             calendar.set(Calendar.YEAR,year);

                                             calendar.add(Calendar.DAY_OF_MONTH,7);

                                             enddate_TextView.setText(new StringBuilder().append(format.format(calendar.getTime())));
                                         }
                                     };

                                     DatePickerDialog dpDialog = new DatePickerDialog(context,dateSetListener,
                                             calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));

                                     dpDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000   );
                                     dpDialog.show();
                                 }
                             });


                             // SENDING DATA TO FIRESTORE

                             Exercisesend.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     popupWindow.dismiss();
                                     startDate = startdate_textView.getText().toString().trim();
                                     endDate   = enddate_TextView.getText().toString();
                                     if (startDate.equals(""))
                                     {
                                         Toast.makeText(context,"Please select start date from calender",Toast.LENGTH_LONG).show();
                                     }

                                     else {
                                         int numberpickervalue = numberPicker.getValue();
                                         double seconds           = numberpickervalue*3600;

                                         int minutes           = numberpickervalue * 60 ;
                                         int  dividedMinutes    =  (minutes / 7);
                                         int    finalMinutes      = (dividedMinutes * 7);



                                         double dividedValue1 =     seconds / 7;
                                         Log.d("splited seconds ", String.valueOf(dividedValue1));

                                         int day = (int) (dividedValue1 / (24 * 3600));
                                         int dividedValue2 = (int) (dividedValue1 % (24 * 3600));
                                         int hour = (int) (dividedValue2 / 3600);
                                         dividedValue2 %= 3600;
                                         int minutestwo = dividedValue2 / 60 ;

                                         String SplitedTime = hour+" hours - "+minutestwo + " minutes" ;

                                         Log.d("alldata", SplitedTime);





                                         DocumentReference Doc = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("RopeJumping");

                                         Map<String,Object> Map     = new HashMap<>();
                                         Map.put("TimeStamp", FieldValue.serverTimestamp());
                                         Map.put("ExerciseName" ,exerciseGridDataclickedItem.getName());
                                         Map.put("StartingDate",startDate);
                                         Map.put("End_Date",endDate);
                                         Map.put("DayOfMonth",DOM);
                                         Map.put("Month",M);
                                         Map.put("Year",Y);
                                         Map.put("total_hours_for_week",String.valueOf(numberpickervalue)+": hours");
                                         Map.put("hours_per_day",SplitedTime);
                                         Map.put("TotalMinutes",finalMinutes);
                                         Map.put("PerDayMinutes",dividedMinutes);
                                         Map.put("Current_Progres",0);


                                         //Batch Start

                                         final WriteBatch batch2 = database.batch();
                                         final WriteBatch Completedbacth2 = database.batch();
                                         // Day1 Map
                                         final  DocumentReference RJDoc1 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("RopeJumping")
                                                 .collection("RopeJumping").document("Day1");

                                         final Map<String,Object> RJD1M = new HashMap<>();
                                         RJD1M.put("Day","Day1");
                                         RJD1M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         RJD1M.put("SplitDay",SplitedTime);
                                         RJD1M.put("FullHours",String.valueOf(numberpickervalue));
                                         RJD1M.put("Seconds",dividedValue1);
                                         RJD1M.put("SecondsUploaded",false);
                                         RJD1M.put("PicLink",url2);
                                         batch2.set(RJDoc1,RJD1M);


                                         //Day2 Map
                                         final DocumentReference RJDoc2 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("RopeJumping")
                                                 .collection("RopeJumping").document("Day2");

                                         final Map<String,Object> RJD2M = new HashMap<>();
                                         RJD2M.put("Day","Day2");
                                         RJD2M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         RJD2M.put("SplitDay",SplitedTime);
                                         RJD2M.put("FullHours",String.valueOf(numberpickervalue));
                                         RJD2M.put("Seconds",dividedValue1);
                                         RJD2M.put("SecondsUploaded",false);
                                         RJD2M.put("PicLink",url2);
                                         batch2.set(RJDoc2,RJD2M);

                                         //Day3 Map
                                         final  DocumentReference RJDoc3 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("RopeJumping")
                                                 .collection("RopeJumping").document("Day3");

                                         final Map<String,Object> RJD3M = new HashMap<>();
                                         RJD3M.put("Day","Day3");
                                         RJD3M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         RJD3M.put("SplitDay",SplitedTime);
                                         RJD3M.put("FullHours",String.valueOf(numberpickervalue));
                                         RJD3M.put("Seconds",dividedValue1);
                                         RJD3M.put("SecondsUploaded",false);
                                         RJD3M.put("PicLink",url2);
                                         batch2.set(RJDoc3,RJD3M);


                                         //Day4 Map
                                         final  DocumentReference RJDoc4 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("RopeJumping")
                                                 .collection("RopeJumping").document("Day4");

                                         final Map<String,Object> RJD4M = new HashMap<>();
                                         RJD4M.put("Day","Day4");
                                         RJD4M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         RJD4M.put("SplitDay",SplitedTime);
                                         RJD4M.put("FullHours",String.valueOf(numberpickervalue));
                                         RJD4M.put("Seconds",dividedValue1);
                                         RJD4M.put("SecondsUploaded",false);
                                         RJD4M.put("PicLink",url2);
                                         batch2.set(RJDoc4,RJD4M);

                                         //Day5 Map
                                         final  DocumentReference RJDoc5 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("RopeJumping")
                                                 .collection("RopeJumping").document("Day5");

                                         final Map<String,Object> RJD5M = new HashMap<>();
                                         RJD5M.put("Day","Day5");
                                         RJD5M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         RJD5M.put("SplitDay",SplitedTime);
                                         RJD5M.put("FullHours",String.valueOf(numberpickervalue));
                                         RJD5M.put("Seconds",dividedValue1);
                                         RJD5M.put("SecondsUploaded",false);
                                         RJD5M.put("PicLink",url2);
                                         batch2.set(RJDoc5,RJD5M);

                                         //Day6 Map
                                         final  DocumentReference RJDoc6 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("RopeJumping")
                                                 .collection("RopeJumping").document("Day6");

                                         final Map<String,Object> RJD6M = new HashMap<>();
                                         RJD6M.put("Day","Day6");
                                         RJD6M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         RJD6M.put("SplitDay",SplitedTime);
                                         RJD6M.put("FullHours",String.valueOf(numberpickervalue));
                                         RJD6M.put("Seconds",dividedValue1);
                                         RJD6M.put("SecondsUploaded",false);
                                         RJD6M.put("PicLink",url2);
                                         batch2.set(RJDoc6,RJD6M);

                                         //Day7 Map
                                         final  DocumentReference RJDoc7 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("RopeJumping")
                                                 .collection("RopeJumping").document("Day7");

                                         final Map<String,Object> RJD7M = new HashMap<>();
                                         RJD7M.put("Day","Day7");
                                         RJD7M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         RJD7M.put("SplitDay",SplitedTime);
                                         RJD7M.put("FullHours",String.valueOf(numberpickervalue));
                                         RJD7M.put("Seconds",dividedValue1);
                                         RJD7M.put("SecondsUploaded",false);
                                         RJD7M.put("PicLink",url2);
                                         batch2.set(RJDoc7,RJD7M);


                                         /////////////////////////////////////// Completed Batch
                                         final  DocumentReference CompletedRJDoc = database.collection("Activities")
                                                 .document(UID).collection("CompletedExercises").document("RopeJumping");

                                         final Map<String,Object> CompletedRJDocM = new HashMap<>();
                                         CompletedRJDocM.put("Day1",false);
                                         CompletedRJDocM.put("Day2",false);
                                         CompletedRJDocM.put("Day3",false);
                                         CompletedRJDocM.put("Day4",false);
                                         CompletedRJDocM.put("Day5",false);
                                         CompletedRJDocM.put("Day6",false);
                                         CompletedRJDocM.put("Day7",false);
                                         CompletedRJDocM.put("Fail",false);
                                         CompletedRJDocM.put("Skip1",false);
                                         CompletedRJDocM.put("Skip2",false);
                                         CompletedRJDocM.put("Skip3",false);
                                         CompletedRJDocM.put("Skip4",false);
                                         CompletedRJDocM.put("Skip5",false);
                                         CompletedRJDocM.put("Skip6",false);
                                         CompletedRJDocM.put("Skip7",false);


                                         Completedbacth2.set(CompletedRJDoc,CompletedRJDocM);
                                         //////////////////////////////////////

                                         // Batch Ends





                                         Doc.set(Map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                             @Override
                                             public void onSuccess(Void aVoid) {
                                                 Log.d(TAG, "DocumentSnapshot successfully written!");

                                                 Completedbacth2.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {

                                                         batch2.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task)
                                                             {
                                                                 documentReferenceV1.update("RopeJumping",true);
                                                                 Toast.makeText(context,"batch added succesfully",Toast.LENGTH_LONG).show();
                                                             }
                                                         });
                                                     }
                                                 });



                                             }
                                         }).addOnFailureListener(new OnFailureListener() {
                                             @Override
                                             public void onFailure(@NonNull Exception e) {
                                                 Log.w(TAG, "Error writing document", e);
                                                 Toast.makeText(context,"data not added",Toast.LENGTH_LONG).show();
                                             }
                                         });
                                     }}
                             });
                         }
                         else if (exerciseGridDataclickedItem.getName().equals("RopeJumping") && ROPE_JUMPINMG)
                         {
                             Toast.makeText(context,"You already set target for Rope Jumping , COMPLETE it or DELETE it",Toast.LENGTH_LONG).show();
                         }


                         // IF CLICKED ON MEDITATE

                         else if (exerciseGridDataclickedItem.getName().equals("Meditate") && !MEDITATE)
                         {
                             // INFLATING meditate_popup.xml

                             final View Meditate_popUpView = layoutInflater.inflate(R.layout.meditate_popup,null);

                             Exercisesend  = Meditate_popUpView.findViewById(R.id.exercise_upload_button);
                             numberPicker  = Meditate_popUpView.findViewById(R.id.number_picker);
                             numberPicker.setMinValue(1);
                             numberPicker.setMaxValue(70);
                             numberPicker.setWrapSelectorWheel(true);
                             CalenderButton = Meditate_popUpView.findViewById(R.id.calenderbb);
                             startdate_textView = Meditate_popUpView.findViewById(R.id.plan_start_text);
                             enddate_TextView   = Meditate_popUpView.findViewById(R.id.plan_ends_text);





                             final PopupWindow popupWindow = new PopupWindow(Meditate_popUpView,width,height,focusable);
                             popupWindow.setAnimationStyle(R.style.Animation);
                             popupWindow.showAtLocation(v, Gravity.CENTER,0,-155);


                             Meditate_popUpView.setOnTouchListener(new View.OnTouchListener() {
                                 @Override
                                 public boolean onTouch(View v, MotionEvent event) {

                                     return  true;
                                 }
                             });

                             // OPENING CALENDER AND SETTING DATE

                             CalenderButton.setOnClickListener(new View.OnClickListener() {

                                 @RequiresApi(api = Build.VERSION_CODES.N)
                                 @Override
                                 public void onClick(View v) {
                                     DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                                         @Override
                                         public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                             startdate_textView.setText(new StringBuilder().append(dayOfMonth).append(":").append(month+1)
                                                     .append(":").append(year));

                                             calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                             calendar.set(Calendar.MONTH,month);
                                             calendar.set(Calendar.YEAR,year);

                                             calendar.add(Calendar.DAY_OF_MONTH,7);

                                             enddate_TextView.setText(new StringBuilder().append(format.format(calendar.getTime())));
                                         }
                                     };

                                     DatePickerDialog dpDialog = new DatePickerDialog(context,dateSetListener,
                                             calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));

                                     dpDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000   );
                                     dpDialog.show();
                                 }
                             });


                             // SENDING DATA TO FIRESTORE

                             Exercisesend.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     popupWindow.dismiss();
                                     startDate = startdate_textView.getText().toString().trim();
                                     endDate   = enddate_TextView.getText().toString();
                                     if (startDate.equals(""))
                                     {
                                         Toast.makeText(context,"Please select start date from calender",Toast.LENGTH_LONG).show();
                                     }

                                     else {
                                         int numberpickervalue = numberPicker.getValue();
                                         double seconds           = numberpickervalue*3600;

                                         int minutes           = numberpickervalue * 60 ;
                                         int  dividedMinutes    =  (minutes / 7);
                                         int    finalMinutes      = (dividedMinutes * 7);



                                         double dividedValue1 =     seconds / 7;
                                         Log.d("splited seconds ", String.valueOf(dividedValue1));

                                         int day = (int) (dividedValue1 / (24 * 3600));
                                         int dividedValue2 = (int) (dividedValue1 % (24 * 3600));
                                         int hour = (int) (dividedValue2 / 3600);
                                         dividedValue2 %= 3600;
                                         int minutestwo = dividedValue2 / 60 ;

                                         String SplitedTime = hour+" hours - "+minutestwo + " minutes" ;

                                         Log.d("alldata", SplitedTime);




                                         DocumentReference Doc = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Meditate");

                                         Map<String,Object> Map     = new HashMap<>();
                                         Map.put("TimeStamp", FieldValue.serverTimestamp());
                                         Map.put("ExerciseName" ,exerciseGridDataclickedItem.getName());
                                         Map.put("StartingDate",startDate);
                                         Map.put("End_Date",endDate);
                                         Map.put("DayOfMonth",DOM);
                                         Map.put("Month",M);
                                         Map.put("Year",Y);
                                         Map.put("total_hours_for_week",String.valueOf(numberpickervalue)+": hours");
                                         Map.put("hours_per_day",SplitedTime);
                                         Map.put("TotalMinutes",finalMinutes);
                                         Map.put("PerDayMinutes",dividedMinutes);
                                         Map.put("Current_Progres",0);



                                         //Batch Start

                                         final WriteBatch batch3 = database.batch();
                                         final WriteBatch Completedbacth3 = database.batch();

                                         // Day1 Map
                                         final  DocumentReference MTDoc1 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Meditate")
                                                 .collection("Meditate").document("Day1");

                                         final Map<String,Object> MTD1M = new HashMap<>();
                                         MTD1M.put("Day","Day1");
                                         MTD1M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         MTD1M.put("SplitDay",SplitedTime);
                                         MTD1M.put("FullHours",String.valueOf(numberpickervalue));
                                         MTD1M.put("Seconds",dividedValue1);
                                         MTD1M.put("SecondsUploaded",false);
                                         MTD1M.put("PicLink",url3);
                                         batch3.set(MTDoc1,MTD1M);


                                         //Day2 Map
                                         final DocumentReference MTDoc2 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Meditate")
                                                 .collection("Meditate").document("Day2");

                                         final Map<String,Object> MTD2M = new HashMap<>();
                                         MTD2M.put("Day","Day2");
                                         MTD2M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         MTD2M.put("SplitDay",SplitedTime);
                                         MTD2M.put("FullHours",String.valueOf(numberpickervalue));
                                         MTD2M.put("Seconds",dividedValue1);
                                         MTD2M.put("SecondsUploaded",false);
                                         MTD2M.put("PicLink",url3);
                                         batch3.set(MTDoc2,MTD2M);


                                         //Day3 Map
                                         final  DocumentReference MTDoc3 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Meditate")
                                                 .collection("Meditate").document("Day3");

                                         final Map<String,Object> MTD3M = new HashMap<>();
                                         MTD3M.put("Day","Day3");
                                         MTD3M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         MTD3M.put("SplitDay",SplitedTime);
                                         MTD3M.put("FullHours",String.valueOf(numberpickervalue));
                                         MTD3M.put("Seconds",dividedValue1);
                                         MTD3M.put("SecondsUploaded",false);
                                         MTD3M.put("PicLink",url3);
                                         batch3.set(MTDoc3,MTD3M);


                                         //Day4 Map
                                         final  DocumentReference MTDoc4 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Meditate")
                                                 .collection("Meditate").document("Day4");

                                         final Map<String,Object> MTD4M = new HashMap<>();
                                         MTD4M.put("Day","Day4");
                                         MTD4M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         MTD4M.put("SplitDay",SplitedTime);
                                         MTD4M.put("FullHours",String.valueOf(numberpickervalue));
                                         MTD4M.put("Seconds",dividedValue1);
                                         MTD4M.put("SecondsUploaded",false);
                                         MTD4M.put("PicLink",url3);
                                         batch3.set(MTDoc4,MTD4M);


                                         //Day5 Map
                                         final  DocumentReference MTDoc5 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Meditate")
                                                 .collection("Meditate").document("Day5");

                                         final Map<String,Object> MTD5M = new HashMap<>();
                                         MTD5M.put("Day","Day5");
                                         MTD5M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         MTD5M.put("SplitDay",SplitedTime);
                                         MTD5M.put("FullHours",String.valueOf(numberpickervalue));
                                         MTD5M.put("Seconds",dividedValue1);
                                         MTD5M.put("SecondsUploaded",false);
                                         MTD5M.put("PicLink",url3);
                                         batch3.set(MTDoc5,MTD5M);

                                         //Day6 Map
                                         final  DocumentReference MTDoc6 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Meditate")
                                                 .collection("Meditate").document("Day6");

                                         final Map<String,Object> MTD6M = new HashMap<>();
                                         MTD6M.put("Day","Day6");
                                         MTD6M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         MTD6M.put("SplitDay",SplitedTime);
                                         MTD6M.put("FullHours",String.valueOf(numberpickervalue));
                                         MTD6M.put("Seconds",dividedValue1);
                                         MTD6M.put("SecondsUploaded",false);
                                         MTD6M.put("PicLink",url3);
                                         batch3.set(MTDoc6,MTD6M);

                                         //Day7 Map
                                         final  DocumentReference MTDoc7 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Meditate")
                                                 .collection("Meditate").document("Day7");

                                         final Map<String,Object> MTD7M = new HashMap<>();
                                         MTD7M.put("Day","Day7");
                                         MTD7M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         MTD7M.put("SplitDay",SplitedTime);
                                         MTD7M.put("FullHours",String.valueOf(numberpickervalue));
                                         MTD7M.put("Seconds",dividedValue1);
                                         MTD7M.put("SecondsUploaded",false);
                                         MTD7M.put("PicLink",url3);
                                         batch3.set(MTDoc7,MTD7M);


                                         // Batch Ends



                                         /////////////////////////////////////// Completed Batch
                                         final  DocumentReference CompletedMTDoc = database.collection("Activities")
                                                 .document(UID).collection("CompletedExercises").document("Meditate");

                                         final Map<String,Object> CompletedMTDocM = new HashMap<>();
                                         CompletedMTDocM.put("Day1",false);
                                         CompletedMTDocM.put("Day2",false);
                                         CompletedMTDocM.put("Day3",false);
                                         CompletedMTDocM.put("Day4",false);
                                         CompletedMTDocM.put("Day5",false);
                                         CompletedMTDocM.put("Day6",false);
                                         CompletedMTDocM.put("Day7",false);
                                         CompletedMTDocM.put("Fail",false);
                                         CompletedMTDocM.put("Skip1",false);
                                         CompletedMTDocM.put("Skip2",false);
                                         CompletedMTDocM.put("Skip3",false);
                                         CompletedMTDocM.put("Skip4",false);
                                         CompletedMTDocM.put("Skip5",false);
                                         CompletedMTDocM.put("Skip6",false);
                                         CompletedMTDocM.put("Skip7",false);



                                         Completedbacth3.set(CompletedMTDoc,CompletedMTDocM);
                                         //////////////////////////////////////







                                         Doc.set(Map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                             @Override
                                             public void onSuccess(Void aVoid) {
                                                 Log.d(TAG, "DocumentSnapshot successfully written!");

                                                 Completedbacth3.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {
                                                         batch3.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task)
                                                             {
                                                                 documentReferenceV1.update("Meditate",true);
                                                                 Toast.makeText(context,"data added succesfully",Toast.LENGTH_LONG).show();
                                                             }
                                                         });
                                                     }
                                                 });
                                             }
                                         }).addOnFailureListener(new OnFailureListener() {
                                             @Override
                                             public void onFailure(@NonNull Exception e) {
                                                 Log.w(TAG, "Error writing document", e);
                                                 Toast.makeText(context,"data not added",Toast.LENGTH_LONG).show();
                                             }
                                         });
                                     }}
                             });
                         }


                         else if (exerciseGridDataclickedItem.getName().equals("Meditate") && MEDITATE)
                         {
                             Toast.makeText(context,"You already set target for Meditation , COMPLETE it or DELETE it",Toast.LENGTH_LONG).show();
                         }



                         // IF CLICKED ON PUSH UPS
                         else if (exerciseGridDataclickedItem.getName().equals("PushUp") && !PUSH_UP)
                         {
                             // INFLATING push_up_popup.xml

                             final View PushUp_popup = layoutInflater.inflate(R.layout.push_up_popup,null);

                             Exercisesend  = PushUp_popup.findViewById(R.id.exercise_upload_button);
                             numberPicker  = PushUp_popup.findViewById(R.id.number_picker);
                             numberPicker.setMinValue(1);
                             numberPicker.setMaxValue(70);
                             numberPicker.setWrapSelectorWheel(true);
                             CalenderButton = PushUp_popup.findViewById(R.id.calenderbb);
                             startdate_textView = PushUp_popup.findViewById(R.id.plan_start_text);
                             enddate_TextView   = PushUp_popup.findViewById(R.id.plan_ends_text);





                             final PopupWindow popupWindow = new PopupWindow(PushUp_popup,width,height,focusable);
                             popupWindow.setAnimationStyle(R.style.Animation);
                             popupWindow.showAtLocation(v, Gravity.CENTER,0,-155);


                             PushUp_popup.setOnTouchListener(new View.OnTouchListener() {
                                 @Override
                                 public boolean onTouch(View v, MotionEvent event) {

                                     return  true;
                                 }
                             });

                             // OPENING CALENDER AND SETTING DATE

                             CalenderButton.setOnClickListener(new View.OnClickListener() {

                                 @RequiresApi(api = Build.VERSION_CODES.N)
                                 @Override
                                 public void onClick(View v) {
                                     DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                                         @Override
                                         public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                             startdate_textView.setText(new StringBuilder().append(dayOfMonth).append(":").append(month+1)
                                                     .append(":").append(year));

                                             calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                             calendar.set(Calendar.MONTH,month);
                                             calendar.set(Calendar.YEAR,year);

                                             calendar.add(Calendar.DAY_OF_MONTH,7);

                                             enddate_TextView.setText(new StringBuilder().append(format.format(calendar.getTime())));
                                         }
                                     };

                                     DatePickerDialog dpDialog = new DatePickerDialog(context,dateSetListener,
                                             calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));

                                     dpDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000   );
                                     dpDialog.show();
                                 }
                             });


                             // SENDING DATA TO FIRESTORE

                             Exercisesend.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     popupWindow.dismiss();
                                     startDate = startdate_textView.getText().toString().trim();
                                     endDate   = enddate_TextView.getText().toString();
                                     if (startDate.equals(""))
                                     {
                                         Toast.makeText(context,"Please select start date from calender",Toast.LENGTH_LONG).show();
                                     }

                                     else {

                                         int numberpickervalue = numberPicker.getValue();
                                         double seconds           = numberpickervalue*3600;

                                         int minutes           = numberpickervalue * 60 ;
                                         int  dividedMinutes    =  (minutes / 7);
                                         int    finalMinutes      = (dividedMinutes * 7);



                                         double dividedValue1 =     seconds / 7;
                                         Log.d("splited seconds ", String.valueOf(dividedValue1));

                                         int day = (int) (dividedValue1 / (24 * 3600));
                                         int dividedValue2 = (int) (dividedValue1 % (24 * 3600));
                                         int hour = (int) (dividedValue2 / 3600);
                                         dividedValue2 %= 3600;
                                         int minutestwo = dividedValue2 / 60 ;

                                         String SplitedTime = hour+" hours - "+minutestwo + " minutes" ;



                                         DocumentReference Doc = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("PushUp");

                                         Map<String,Object> Map     = new HashMap<>();
                                         Map.put("TimeStamp", FieldValue.serverTimestamp());
                                         Map.put("ExerciseName" ,exerciseGridDataclickedItem.getName());
                                         Map.put("StartingDate",startDate);
                                         Map.put("End_Date",endDate);
                                         Map.put("DayOfMonth",DOM);
                                         Map.put("Month",M);
                                         Map.put("Year",Y);
                                         Map.put("total_hours_for_week",String.valueOf(numberpickervalue)+": hours");
                                         Map.put("hours_per_day",SplitedTime);
                                         Map.put("TotalMinutes",finalMinutes);
                                         Map.put("PerDayMinutes",dividedMinutes);
                                         Map.put("Current_Progres",0);



                                         //Batch Start

                                         final WriteBatch batch4 = database.batch();
                                         final WriteBatch Completedbacth4 = database.batch();

                                         // Day1 Map
                                         final  DocumentReference PUDoc1 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("PushUp")
                                                 .collection("PushUp").document("Day1");

                                         final Map<String,Object> PUD1M = new HashMap<>();
                                         PUD1M.put("Day","Day1");
                                         PUD1M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         PUD1M.put("SplitDay",SplitedTime);
                                         PUD1M.put("FullHours",String.valueOf(numberpickervalue));
                                         PUD1M.put("PicLink",url4);
                                         batch4.set(PUDoc1,PUD1M);


                                         //Day2 Map
                                         final DocumentReference PUDoc2 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("PushUp")
                                                 .collection("PushUp").document("Day2");

                                         final Map<String,Object> PUD2M = new HashMap<>();
                                         PUD2M.put("Day","Day2");
                                         PUD2M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         PUD2M.put("SplitDay",SplitedTime);
                                         PUD2M.put("FullHours",String.valueOf(numberpickervalue));
                                         PUD2M.put("PicLink",url4);
                                         batch4.set(PUDoc2,PUD2M);


                                         //Day3 Map
                                         final  DocumentReference PUDoc3 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("PushUp")
                                                 .collection("PushUp").document("Day3");

                                         final Map<String,Object> PUD3M = new HashMap<>();
                                         PUD3M.put("Day","Day3");
                                         PUD3M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         PUD3M.put("SplitDay",SplitedTime);
                                         PUD3M.put("FullHours",String.valueOf(numberpickervalue));
                                         PUD3M.put("PicLink",url4);
                                         batch4.set(PUDoc3,PUD3M);


                                         //Day4 Map
                                         final  DocumentReference PUDoc4 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("PushUp")
                                                 .collection("PushUp").document("Day4");

                                         final Map<String,Object> PUD4M = new HashMap<>();
                                         PUD4M.put("Day","Day4");
                                         PUD4M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         PUD4M.put("SplitDay",SplitedTime);
                                         PUD4M.put("FullHours",String.valueOf(numberpickervalue));
                                         PUD4M.put("PicLink",url4);
                                         batch4.set(PUDoc4,PUD4M);


                                         //Day5 Map
                                         final  DocumentReference PUDoc5 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("PushUp")
                                                 .collection("PushUp").document("Day5");

                                         final Map<String,Object> PUD5M = new HashMap<>();
                                         PUD5M.put("Day","Day5");
                                         PUD5M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         PUD5M.put("SplitDay",SplitedTime);
                                         PUD5M.put("FullHours",String.valueOf(numberpickervalue));
                                         PUD5M.put("PicLink",url4);
                                         batch4.set(PUDoc5,PUD5M);


                                         //Day6 Map
                                         final  DocumentReference PUDoc6 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("PushUp")
                                                 .collection("PushUp").document("Day6");

                                         final Map<String,Object> PUD6M = new HashMap<>();
                                         PUD6M.put("Day","Day6");
                                         PUD6M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         PUD6M.put("SplitDay",SplitedTime);
                                         PUD6M.put("FullHours",String.valueOf(numberpickervalue));
                                         PUD6M.put("PicLink",url4);
                                         batch4.set(PUDoc6,PUD6M);


                                         //Day7 Map
                                         final  DocumentReference PUDoc7 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("PushUp")
                                                 .collection("PushUp").document("Day7");

                                         final Map<String,Object> PUD7M = new HashMap<>();
                                         PUD7M.put("Day","Day7");
                                         PUD7M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         PUD7M.put("SplitDay",SplitedTime);
                                         PUD7M.put("FullHours",String.valueOf(numberpickervalue));
                                         PUD7M.put("PicLink",url4);
                                         batch4.set(PUDoc7,PUD7M);


                                         // Batch Ends



                                         /////////////////////////////////////// Completed Batch
                                         final  DocumentReference CompletedPUDoc = database.collection("Activities")
                                                 .document(UID).collection("CompletedExercises").document("PushUp");

                                         final Map<String,Object> CompletedPUDocM = new HashMap<>();
                                         CompletedPUDocM.put("Day1",false);
                                         CompletedPUDocM.put("Day2",false);
                                         CompletedPUDocM.put("Day3",false);
                                         CompletedPUDocM.put("Day4",false);
                                         CompletedPUDocM.put("Day5",false);
                                         CompletedPUDocM.put("Day6",false);
                                         CompletedPUDocM.put("Day7",false);
                                         CompletedPUDocM.put("Fail",false);
                                         CompletedPUDocM.put("Skip1",false);
                                         CompletedPUDocM.put("Skip2",false);
                                         CompletedPUDocM.put("Skip3",false);
                                         CompletedPUDocM.put("Skip4",false);
                                         CompletedPUDocM.put("Skip5",false);
                                         CompletedPUDocM.put("Skip6",false);
                                         CompletedPUDocM.put("Skip7",false);


                                         Completedbacth4.set(CompletedPUDoc,CompletedPUDocM);
                                         //////////////////////////////////////





                                         Doc.set(Map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                             @Override
                                             public void onSuccess(Void aVoid) {
                                                 Log.d(TAG, "DocumentSnapshot successfully written!");


                                                 Completedbacth4.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {
                                                         batch4.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task)
                                                             {
                                                                 documentReferenceV1.update("PushUp",true);
                                                                 Toast.makeText(context,"data added succesfully",Toast.LENGTH_LONG).show();
                                                             }
                                                         });
                                                     }
                                                 });
                                             }
                                         }).addOnFailureListener(new OnFailureListener() {
                                             @Override
                                             public void onFailure(@NonNull Exception e) {
                                                 Log.w(TAG, "Error writing document", e);
                                                 Toast.makeText(context,"data not added",Toast.LENGTH_LONG).show();
                                             }
                                         });
                                     }}
                             });
                         }


                         else if (exerciseGridDataclickedItem.getName().equals("PushUp") && PUSH_UP)
                         {
                             Toast.makeText(context,"You already set target for Push Ups , COMPLETE it or DELETE it",Toast.LENGTH_LONG).show();
                         }




                       // IF CLICKED ON RUNNING
                         else if (exerciseGridDataclickedItem.getName().equals("Running") && !RUNNING)
                         {
                             // INFLATING running_popup.xml

                             final View Running_popup = layoutInflater.inflate(R.layout.running_popup,null);

                             Exercisesend  = Running_popup.findViewById(R.id.exercise_upload_button);
                             numberPicker  = Running_popup.findViewById(R.id.number_picker);
                             numberPicker.setMinValue(1);
                             numberPicker.setMaxValue(70);
                             numberPicker.setWrapSelectorWheel(true);
                             CalenderButton = Running_popup.findViewById(R.id.calenderbb);
                             startdate_textView = Running_popup.findViewById(R.id.plan_start_text);
                             enddate_TextView   = Running_popup.findViewById(R.id.plan_ends_text);





                             final PopupWindow popupWindow = new PopupWindow(Running_popup,width,height,focusable);
                             popupWindow.setAnimationStyle(R.style.Animation);
                             popupWindow.showAtLocation(v, Gravity.CENTER,0,-155);


                             Running_popup.setOnTouchListener(new View.OnTouchListener() {
                                 @Override
                                 public boolean onTouch(View v, MotionEvent event) {

                                     return  true;
                                 }
                             });

                             // OPENING CALENDER AND SETTING DATE

                             CalenderButton.setOnClickListener(new View.OnClickListener() {

                                 @RequiresApi(api = Build.VERSION_CODES.N)
                                 @Override
                                 public void onClick(View v) {
                                     DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                                         @Override
                                         public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                             startdate_textView.setText(new StringBuilder().append(dayOfMonth).append(":").append(month+1)
                                                     .append(":").append(year));

                                             calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                             calendar.set(Calendar.MONTH,month);
                                             calendar.set(Calendar.YEAR,year);

                                             calendar.add(Calendar.DAY_OF_MONTH,7);

                                             enddate_TextView.setText(new StringBuilder().append(format.format(calendar.getTime())));
                                         }
                                     };

                                     DatePickerDialog dpDialog = new DatePickerDialog(context,dateSetListener,
                                             calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));

                                     dpDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000   );
                                     dpDialog.show();
                                 }
                             });


                             // SENDING DATA TO FIRESTORE

                             Exercisesend.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     popupWindow.dismiss();
                                     startDate = startdate_textView.getText().toString().trim();
                                     endDate   = enddate_TextView.getText().toString();
                                     if (startDate.equals(""))
                                     {
                                         Toast.makeText(context,"Please select start date from calender",Toast.LENGTH_LONG).show();
                                     }

                                     else {

                                         int numberpickervalue = numberPicker.getValue();
                                         double seconds           = numberpickervalue*3600;

                                         int minutes           = numberpickervalue * 60 ;
                                         int  dividedMinutes    =  (minutes / 7);
                                         int    finalMinutes      = (dividedMinutes * 7);



                                         double dividedValue1 =     seconds / 7;
                                         Log.d("splited seconds ", String.valueOf(dividedValue1));

                                         int day = (int) (dividedValue1 / (24 * 3600));
                                         int dividedValue2 = (int) (dividedValue1 % (24 * 3600));
                                         int hour = (int) (dividedValue2 / 3600);
                                         dividedValue2 %= 3600;
                                         int minutestwo = dividedValue2 / 60 ;

                                         String SplitedTime = hour+" hours - "+minutestwo + " minutes" ;

                                         Log.d("alldata", SplitedTime);


                                         DocumentReference Doc = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Running");

                                         Map<String,Object> Map     = new HashMap<>();
                                         Map.put("TimeStamp", FieldValue.serverTimestamp());
                                         Map.put("ExerciseName" ,exerciseGridDataclickedItem.getName());
                                         Map.put("StartingDate",startDate);
                                         Map.put("End_Date",endDate);
                                         Map.put("DayOfMonth",DOM);
                                         Map.put("Month",M);
                                         Map.put("Year",Y);
                                         Map.put("total_hours_for_week",String.valueOf(numberpickervalue)+": hours");
                                         Map.put("hours_per_day",SplitedTime);
                                         Map.put("TotalMinutes",finalMinutes);
                                         Map.put("PerDayMinutes",dividedMinutes);
                                         Map.put("Current_Progres",0);


                                         //Batch Start

                                         final WriteBatch batch5          = database.batch();
                                         final WriteBatch Completedbacth5 = database.batch();

                                         // Day1 Map
                                         final  DocumentReference RUDoc1 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Running")
                                                 .collection("Running").document("Day1");

                                         final Map<String,Object> RUD1M = new HashMap<>();
                                         RUD1M.put("Day","Day1");
                                         RUD1M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         RUD1M.put("SplitDay",SplitedTime);
                                         RUD1M.put("FullHours",String.valueOf(numberpickervalue));
                                         RUD1M.put("Seconds",dividedValue1);
                                         RUD1M.put("SecondsUploaded",false);
                                         RUD1M.put("PicLink",url5);
                                         batch5.set(RUDoc1,RUD1M);


                                         //Day2 Map
                                         final DocumentReference RUDoc2 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Running")
                                                 .collection("Running").document("Day2");

                                         final Map<String,Object> RUD2M = new HashMap<>();
                                         RUD2M.put("Day","Day2");
                                         RUD2M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         RUD2M.put("SplitDay",SplitedTime);
                                         RUD2M.put("FullHours",String.valueOf(numberpickervalue));
                                         RUD2M.put("Seconds",dividedValue1);
                                         RUD2M.put("SecondsUploaded",false);
                                         RUD2M.put("PicLink",url5);
                                         batch5.set(RUDoc2,RUD2M);


                                         //Day3 Map
                                         final  DocumentReference RUDoc3 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Running")
                                                 .collection("Running").document("Day3");

                                         final Map<String,Object> RUD3M = new HashMap<>();
                                         RUD3M.put("Day","Day3");
                                         RUD3M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         RUD3M.put("SplitDay",SplitedTime);
                                         RUD3M.put("FullHours",String.valueOf(numberpickervalue));
                                         RUD3M.put("Seconds",dividedValue1);
                                         RUD3M.put("SecondsUploaded",false);
                                         RUD3M.put("PicLink",url5);
                                         batch5.set(RUDoc3,RUD3M);


                                         //Day4 Map
                                         final  DocumentReference RUDoc4 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Running")
                                                 .collection("Running").document("Day4");

                                         final Map<String,Object> RUD4M = new HashMap<>();
                                         RUD4M.put("Day","Day4");
                                         RUD4M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         RUD4M.put("SplitDay",SplitedTime);
                                         RUD4M.put("FullHours",String.valueOf(numberpickervalue));
                                         RUD4M.put("Seconds",dividedValue1);
                                         RUD4M.put("SecondsUploaded",false);
                                         RUD4M.put("PicLink",url5);
                                         batch5.set(RUDoc4,RUD4M);


                                         //Day5 Map
                                         final  DocumentReference RUDoc5 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Running")
                                                 .collection("Running").document("Day5");

                                         final Map<String,Object> RUD5M = new HashMap<>();
                                         RUD5M.put("Day","Day5");
                                         RUD5M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         RUD5M.put("SplitDay",SplitedTime);
                                         RUD5M.put("FullHours",String.valueOf(numberpickervalue));
                                         RUD5M.put("Seconds",dividedValue1);
                                         RUD5M.put("SecondsUploaded",false);
                                         RUD5M.put("PicLink",url5);
                                         batch5.set(RUDoc5,RUD5M);


                                         //Day6 Map
                                         final  DocumentReference RUDoc6 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Running")
                                                 .collection("Running").document("Day6");

                                         final Map<String,Object> RUD6M = new HashMap<>();
                                         RUD6M.put("Day","Day6");
                                         RUD6M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         RUD6M.put("SplitDay",SplitedTime);
                                         RUD6M.put("FullHours",String.valueOf(numberpickervalue));
                                         RUD6M.put("Seconds",dividedValue1);
                                         RUD6M.put("SecondsUploaded",false);
                                         RUD6M.put("PicLink",url5);
                                         batch5.set(RUDoc6,RUD6M);


                                         //Day7 Map
                                         final  DocumentReference RUDoc7 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Running")
                                                 .collection("Running").document("Day7");

                                         final Map<String,Object> RUD7M = new HashMap<>();
                                         RUD7M.put("Day","Day7");
                                         RUD7M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         RUD7M.put("SplitDay",SplitedTime);
                                         RUD7M.put("FullHours",String.valueOf(numberpickervalue));
                                         RUD7M.put("Seconds",dividedValue1);
                                         RUD7M.put("SecondsUploaded",false);
                                         RUD7M.put("PicLink",url5);
                                         batch5.set(RUDoc7,RUD7M);


                                         // Batch Ends



                                         /////////////////////////////////////// Completed Batch
                                         final  DocumentReference CompletedRUDoc = database.collection("Activities")
                                                 .document(UID).collection("CompletedExercises").document("Running");

                                         final Map<String,Object> CompletedRUDocM = new HashMap<>();
                                         CompletedRUDocM.put("Day1",false);
                                         CompletedRUDocM.put("Day2",false);
                                         CompletedRUDocM.put("Day3",false);
                                         CompletedRUDocM.put("Day4",false);
                                         CompletedRUDocM.put("Day5",false);
                                         CompletedRUDocM.put("Day6",false);
                                         CompletedRUDocM.put("Day7",false);
                                         CompletedRUDocM.put("Fail",false);
                                         CompletedRUDocM.put("Skip1",false);
                                         CompletedRUDocM.put("Skip2",false);
                                         CompletedRUDocM.put("Skip3",false);
                                         CompletedRUDocM.put("Skip4",false);
                                         CompletedRUDocM.put("Skip5",false);
                                         CompletedRUDocM.put("Skip6",false);
                                         CompletedRUDocM.put("Skip7",false);


                                         Completedbacth5.set(CompletedRUDoc,CompletedRUDocM);
                                         //////////////////////////////////////



                                         Doc.set(Map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                             @Override
                                             public void onSuccess(Void aVoid) {

                                                 Completedbacth5.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {
                                                         batch5.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task)
                                                             {
                                                                 documentReferenceV1.update("Running",true);
                                                                 Toast.makeText(context,"data added succesfully",Toast.LENGTH_LONG).show();
                                                             }
                                                         });
                                                     }
                                                 });



                                             }
                                         }).addOnFailureListener(new OnFailureListener() {
                                             @Override
                                             public void onFailure(@NonNull Exception e) {
                                                 Log.w(TAG, "Error writing document", e);
                                                 Toast.makeText(context,"data not added",Toast.LENGTH_LONG).show();
                                             }
                                         });
                                     }}
                             });
                         }



                         else if (exerciseGridDataclickedItem.getName().equals("Running") && RUNNING)
                         {
                             Toast.makeText(context,"You already set target for Running , COMPLETE it or DELETE it",Toast.LENGTH_LONG).show();
                         }






                         // IF CLICKED ON SWIMMING
                         else if (exerciseGridDataclickedItem.getName().equals("Swimming") && !SWIMMING)
                         {
                             // INFLATING swimming_popup.xml

                             final View Swimming_popup = layoutInflater.inflate(R.layout.swimming_popup,null);

                             Exercisesend  = Swimming_popup.findViewById(R.id.exercise_upload_button);
                             numberPicker  = Swimming_popup.findViewById(R.id.number_picker);
                             numberPicker.setMinValue(1);
                             numberPicker.setMaxValue(40);
                             numberPicker.setWrapSelectorWheel(true);
                             CalenderButton = Swimming_popup.findViewById(R.id.calenderbb);
                             startdate_textView = Swimming_popup.findViewById(R.id.plan_start_text);
                             enddate_TextView   = Swimming_popup.findViewById(R.id.plan_ends_text);





                             final PopupWindow popupWindow = new PopupWindow(Swimming_popup,width,height,focusable);
                             popupWindow.setAnimationStyle(R.style.Animation);
                             popupWindow.showAtLocation(v, Gravity.CENTER,0,-155);


                             Swimming_popup.setOnTouchListener(new View.OnTouchListener() {
                                 @Override
                                 public boolean onTouch(View v, MotionEvent event) {

                                     return  true;
                                 }
                             });

                             // OPENING CALENDER AND SETTING DATE

                             CalenderButton.setOnClickListener(new View.OnClickListener() {

                                 @RequiresApi(api = Build.VERSION_CODES.N)
                                 @Override
                                 public void onClick(View v) {
                                     DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                                         @Override
                                         public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                             startdate_textView.setText(new StringBuilder().append(dayOfMonth).append(":").append(month+1)
                                                     .append(":").append(year));

                                             calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                             calendar.set(Calendar.MONTH,month);
                                             calendar.set(Calendar.YEAR,year);

                                             calendar.add(Calendar.DAY_OF_MONTH,7);

                                             enddate_TextView.setText(new StringBuilder().append(format.format(calendar.getTime())));
                                         }
                                     };

                                     DatePickerDialog dpDialog = new DatePickerDialog(context,dateSetListener,
                                             calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));

                                     dpDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000   );
                                     dpDialog.show();
                                 }
                             });


                             // SENDING DATA TO FIRESTORE

                             Exercisesend.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     popupWindow.dismiss();
                                     startDate = startdate_textView.getText().toString().trim();
                                     endDate   = enddate_TextView.getText().toString();
                                     if (startDate.equals(""))
                                     {
                                         Toast.makeText(context,"Please select start date from calender",Toast.LENGTH_LONG).show();
                                     }

                                     else {

                                         int numberpickervalue = numberPicker.getValue();
                                         double seconds           = numberpickervalue*3600;

                                         int minutes           = numberpickervalue * 60 ;
                                         int  dividedMinutes    =  (minutes / 7);
                                         int    finalMinutes      = (dividedMinutes * 7);



                                         double dividedValue1 =     seconds / 7;
                                         Log.d("splited seconds ", String.valueOf(dividedValue1));

                                         int day = (int) (dividedValue1 / (24 * 3600));
                                         int dividedValue2 = (int) (dividedValue1 % (24 * 3600));
                                         int hour = (int) (dividedValue2 / 3600);
                                         dividedValue2 %= 3600;
                                         int minutestwo = dividedValue2 / 60 ;

                                         String SplitedTime = hour+" hours - "+minutestwo + " minutes" ;

                                         Log.d("alldata", SplitedTime);




                                         DocumentReference Doc = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Swimming");

                                         Map<String,Object> Map     = new HashMap<>();
                                         Map.put("TimeStamp", FieldValue.serverTimestamp());
                                         Map.put("ExerciseName" ,exerciseGridDataclickedItem.getName());
                                         Map.put("StartingDate",startDate);
                                         Map.put("End_Date",endDate);
                                         Map.put("DayOfMonth",DOM);
                                         Map.put("Month",M);
                                         Map.put("Year",Y);
                                         Map.put("total_hours_for_week",String.valueOf(numberpickervalue)+": hours");
                                         Map.put("hours_per_day",SplitedTime);
                                         Map.put("TotalMinutes",finalMinutes);
                                         Map.put("PerDayMinutes",dividedMinutes);
                                         Map.put("Current_Progres",0);






                                         //Batch Start

                                         final WriteBatch batch6          = database.batch();
                                         final WriteBatch Completedbacth6 = database.batch();

                                         // Day1 Map
                                         final  DocumentReference SWDoc1 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Swimming")
                                                 .collection("Swimming").document("Day1");

                                         final Map<String,Object> SWD1M = new HashMap<>();
                                         SWD1M.put("Day","Day1");
                                         SWD1M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         SWD1M.put("SplitDay",SplitedTime);
                                         SWD1M.put("FullHours",String.valueOf(numberpickervalue));
                                         SWD1M.put("Seconds",dividedValue1);
                                         SWD1M.put("SecondsUploaded",false);
                                         SWD1M.put("PicLink",url6);
                                         batch6.set(SWDoc1,SWD1M);



                                         //Day2 Map
                                         final DocumentReference SWDoc2 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Swimming")
                                                 .collection("Swimming").document("Day2");

                                         final Map<String,Object> SWD2M = new HashMap<>();
                                         SWD2M.put("Day","Day2");
                                         SWD2M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         SWD2M.put("SplitDay",SplitedTime);
                                         SWD2M.put("FullHours",String.valueOf(numberpickervalue));
                                         SWD2M.put("Seconds",dividedValue1);
                                         SWD2M.put("SecondsUploaded",false);
                                         SWD2M.put("PicLink",url6);
                                         batch6.set(SWDoc2,SWD2M);


                                         //Day3 Map
                                         final  DocumentReference SWDoc3 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Swimming")
                                                 .collection("Swimming").document("Day3");

                                         final Map<String,Object> SWD3M = new HashMap<>();
                                         SWD3M.put("Day","Day3");
                                         SWD3M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         SWD3M.put("SplitDay",SplitedTime);
                                         SWD3M.put("FullHours",String.valueOf(numberpickervalue));
                                         SWD3M.put("Seconds",dividedValue1);
                                         SWD3M.put("SecondsUploaded",false);
                                         SWD3M.put("PicLink",url6);
                                         batch6.set(SWDoc3,SWD3M);


                                         //Day4 Map
                                         final  DocumentReference SWDoc4 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Swimming")
                                                 .collection("Swimming").document("Day4");

                                         final Map<String,Object> SWD4M = new HashMap<>();
                                         SWD4M.put("Day","Day4");
                                         SWD4M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         SWD4M.put("SplitDay",SplitedTime);
                                         SWD4M.put("FullHours",String.valueOf(numberpickervalue));
                                         SWD4M.put("Seconds",dividedValue1);
                                         SWD4M.put("SecondsUploaded",false);
                                         SWD4M.put("PicLink",url6);
                                         batch6.set(SWDoc4,SWD4M);


                                         //Day5 Map
                                         final  DocumentReference SWDoc5 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Swimming")
                                                 .collection("Swimming").document("Day5");

                                         final Map<String,Object> SWD5M = new HashMap<>();
                                         SWD5M.put("Day","Day5");
                                         SWD5M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         SWD5M.put("SplitDay",SplitedTime);
                                         SWD5M.put("FullHours",String.valueOf(numberpickervalue));
                                         SWD5M.put("Seconds",dividedValue1);
                                         SWD5M.put("SecondsUploaded",false);
                                         SWD5M.put("PicLink",url6);
                                         batch6.set(SWDoc5,SWD5M);


                                         //Day6 Map
                                         final  DocumentReference SWDoc6 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Swimming")
                                                 .collection("Swimming").document("Day6");

                                         final Map<String,Object> SWD6M = new HashMap<>();
                                         SWD6M.put("Day","Day6");
                                         SWD6M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         SWD6M.put("SplitDay",SplitedTime);
                                         SWD6M.put("FullHours",String.valueOf(numberpickervalue));
                                         SWD6M.put("Seconds",dividedValue1);
                                         SWD6M.put("SecondsUploaded",false);
                                         SWD6M.put("PicLink",url6);
                                         batch6.set(SWDoc6,SWD6M);


                                         //Day7 Map
                                         final  DocumentReference SWDoc7 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Swimming")
                                                 .collection("Swimming").document("Day7");

                                         final Map<String,Object> SWD7M = new HashMap<>();
                                         SWD7M.put("Day","Day7");
                                         SWD7M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         SWD7M.put("SplitDay",SplitedTime);
                                         SWD7M.put("FullHours",String.valueOf(numberpickervalue));
                                         SWD7M.put("Seconds",dividedValue1);
                                         SWD7M.put("SecondsUploaded",false);
                                         SWD7M.put("PicLink",url6);
                                         batch6.set(SWDoc7,SWD7M);


                                         // Batch Ends



                                         /////////////////////////////////////// Completed Batch
                                         final  DocumentReference CompletedSWDoc = database.collection("Activities")
                                                 .document(UID).collection("CompletedExercises").document("Swimming");

                                         final Map<String,Object> CompletedSWDocM = new HashMap<>();
                                         CompletedSWDocM.put("Day1",false);
                                         CompletedSWDocM.put("Day2",false);
                                         CompletedSWDocM.put("Day3",false);
                                         CompletedSWDocM.put("Day4",false);
                                         CompletedSWDocM.put("Day5",false);
                                         CompletedSWDocM.put("Day6",false);
                                         CompletedSWDocM.put("Day7",false);
                                         CompletedSWDocM.put("Fail",false);
                                         CompletedSWDocM.put("Skip1",false);
                                         CompletedSWDocM.put("Skip2",false);
                                         CompletedSWDocM.put("Skip3",false);
                                         CompletedSWDocM.put("Skip4",false);
                                         CompletedSWDocM.put("Skip5",false);
                                         CompletedSWDocM.put("Skip6",false);
                                         CompletedSWDocM.put("Skip7",false);


                                         Completedbacth6.set(CompletedSWDoc,CompletedSWDocM);
                                         //////////////////////////////////////



                                         Doc.set(Map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                             @Override
                                             public void onSuccess(Void aVoid) {

                                                 Completedbacth6.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {

                                                         batch6.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task)
                                                             {
                                                                 documentReferenceV1.update("Swimming",true);
                                                                 Toast.makeText(context,"data added succesfully",Toast.LENGTH_LONG).show();
                                                             }
                                                         });
                                                     }
                                                 });
                                             }
                                         }).addOnFailureListener(new OnFailureListener() {
                                             @Override
                                             public void onFailure(@NonNull Exception e) {
                                                 Log.w(TAG, "Error writing document", e);
                                                 Toast.makeText(context,"data not added",Toast.LENGTH_LONG).show();
                                             }
                                         });
                                     }}
                             });
                                   }

                         else if (exerciseGridDataclickedItem.getName().equals("Swimming") && SWIMMING)
                         {
                             Toast.makeText(context,"You already set target for Swimming , COMPLETE it or DELETE it",Toast.LENGTH_LONG).show();
                         }










                         // IF CLICKED ON WEIGHT LIFITNG
                         else if (exerciseGridDataclickedItem.getName().equals("WeightLifting") && !WEIGHT_LIFTING)
                         {
                             // INFLATING weight_lifting_popup.xml

                             final View WeightLifting_popup = layoutInflater.inflate(R.layout.weight_lifting_popup,null);

                             Exercisesend  = WeightLifting_popup.findViewById(R.id.exercise_upload_button);
                             numberPicker  = WeightLifting_popup.findViewById(R.id.number_picker);
                             numberPicker.setMinValue(1);
                             numberPicker.setMaxValue(70);
                             numberPicker.setWrapSelectorWheel(true);
                             CalenderButton = WeightLifting_popup.findViewById(R.id.calenderbb);
                             startdate_textView = WeightLifting_popup.findViewById(R.id.plan_start_text);
                             enddate_TextView   = WeightLifting_popup.findViewById(R.id.plan_ends_text);





                             final PopupWindow popupWindow = new PopupWindow(WeightLifting_popup,width,height,focusable);
                             popupWindow.setAnimationStyle(R.style.Animation);
                             popupWindow.showAtLocation(v, Gravity.CENTER,0,-155);


                             WeightLifting_popup.setOnTouchListener(new View.OnTouchListener() {
                                 @Override
                                 public boolean onTouch(View v, MotionEvent event) {

                                     return  true;

                                 }
                             });

                             // OPENING CALENDER AND SETTING DATE

                             CalenderButton.setOnClickListener(new View.OnClickListener() {

                                 @RequiresApi(api = Build.VERSION_CODES.N)
                                 @Override
                                 public void onClick(View v) {
                                     DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                                         @Override
                                         public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                             startdate_textView.setText(new StringBuilder().append(dayOfMonth).append(":").append(month+1)
                                                     .append(":").append(year));

                                             calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                             calendar.set(Calendar.MONTH,month);
                                             calendar.set(Calendar.YEAR,year);

                                             calendar.add(Calendar.DAY_OF_MONTH,7);

                                             enddate_TextView.setText(new StringBuilder().append(format.format(calendar.getTime())));
                                         }
                                     };

                                     DatePickerDialog dpDialog = new DatePickerDialog(context,dateSetListener,
                                             calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));

                                     dpDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000   );
                                     dpDialog.show();
                                 }
                             });


                             // SENDING DATA TO FIRESTORE

                             Exercisesend.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     popupWindow.dismiss();
                                     startDate = startdate_textView.getText().toString().trim();
                                     endDate   = enddate_TextView.getText().toString();
                                     if (startDate.equals(""))
                                     {
                                         Toast.makeText(context,"Please select start date from calender",Toast.LENGTH_LONG).show();
                                     }

                                     else {

                                         int numberpickervalue = numberPicker.getValue();
                                         double seconds           = numberpickervalue*3600;

                                         int minutes           = numberpickervalue * 60 ;
                                         int  dividedMinutes    =  (minutes / 7);
                                         int    finalMinutes      = (dividedMinutes * 7);



                                         double dividedValue1 =     seconds / 7;
                                         Log.d("splited seconds ", String.valueOf(dividedValue1));

                                         int day = (int) (dividedValue1 / (24 * 3600));
                                         int dividedValue2 = (int) (dividedValue1 % (24 * 3600));
                                         int hour = (int) (dividedValue2 / 3600);
                                         dividedValue2 %= 3600;
                                         int minutestwo = dividedValue2 / 60 ;

                                         String SplitedTime = hour+" hours - "+minutestwo + " minutes" ;



                                         DocumentReference Doc = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("WeightLifting");

                                         Map<String,Object> Map     = new HashMap<>();
                                         Map.put("TimeStamp", FieldValue.serverTimestamp());
                                         Map.put("ExerciseName" ,exerciseGridDataclickedItem.getName());
                                         Map.put("StartingDate",startDate);
                                         Map.put("End_Date",endDate);
                                         Map.put("DayOfMonth",DOM);
                                         Map.put("Month",M);
                                         Map.put("Year",Y);
                                         Map.put("total_hours_for_week",String.valueOf(numberpickervalue)+": hours");
                                         Map.put("hours_per_day",SplitedTime);
                                         Map.put("TotalMinutes",finalMinutes);
                                         Map.put("PerDayMinutes",dividedMinutes);
                                         Map.put("Current_Progres",0);







                                         //Batch Start

                                         final WriteBatch batch7          = database.batch();
                                         final WriteBatch Completedbacth7 = database.batch();

                                         // Day1 Map
                                         final  DocumentReference WLDoc1 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("WeightLifting")
                                                 .collection("WeightLifting").document("Day1");

                                         final Map<String,Object> WLD1M = new HashMap<>();
                                         WLD1M.put("Day","Day1");
                                         WLD1M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         WLD1M.put("SplitDay",SplitedTime);
                                         WLD1M.put("FullHours",String.valueOf(numberpickervalue));
                                         WLD1M.put("PicLink",url7);

                                         batch7.set(WLDoc1,WLD1M);


                                         //Day2 Map
                                         final DocumentReference WLDoc2 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("WeightLifting")
                                                 .collection("WeightLifting").document("Day2");

                                         final Map<String,Object> WLD2M = new HashMap<>();
                                         WLD2M.put("Day","Day2");
                                         WLD2M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         WLD2M.put("SplitDay",SplitedTime);
                                         WLD2M.put("FullHours",String.valueOf(numberpickervalue));
                                         WLD2M.put("PicLink",url7);
                                         batch7.set(WLDoc2,WLD2M);



                                         //Day3 Map
                                         final  DocumentReference WLDoc3 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("WeightLifting")
                                                 .collection("WeightLifting").document("Day3");

                                         final Map<String,Object> WLD3M = new HashMap<>();
                                         WLD3M.put("Day","Day3");
                                         WLD3M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         WLD3M.put("SplitDay",SplitedTime);
                                         WLD3M.put("FullHours",String.valueOf(numberpickervalue));
                                         WLD3M.put("PicLink",url7);
                                         batch7.set(WLDoc3,WLD3M);


                                         //Day4 Map
                                         final  DocumentReference WLDoc4 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("WeightLifting")
                                                 .collection("WeightLifting").document("Day4");

                                         final Map<String,Object> WLD4M = new HashMap<>();
                                         WLD4M.put("Day","Day4");
                                         WLD4M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         WLD4M.put("SplitDay",SplitedTime);
                                         WLD4M.put("FullHours",String.valueOf(numberpickervalue));
                                         WLD4M.put("PicLink",url7);
                                         batch7.set(WLDoc4,WLD4M);


                                         //Day5 Map
                                         final  DocumentReference WLDoc5 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("WeightLifting")
                                                 .collection("WeightLifting").document("Day5");

                                         final Map<String,Object> WLD5M = new HashMap<>();
                                         WLD5M.put("Day","Day5");
                                         WLD5M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         WLD5M.put("SplitDay",SplitedTime);
                                         WLD5M.put("FullHours",String.valueOf(numberpickervalue));
                                         WLD5M.put("PicLink",url7);
                                         batch7.set(WLDoc5,WLD5M);


                                         //Day6 Map
                                         final  DocumentReference WLDoc6 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("WeightLifting")
                                                 .collection("WeightLifting").document("Day6");

                                         final Map<String,Object> WLD6M = new HashMap<>();
                                         WLD6M.put("Day","Day6");
                                         WLD6M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         WLD6M.put("SplitDay",SplitedTime);
                                         WLD6M.put("FullHours",String.valueOf(numberpickervalue));
                                         WLD6M.put("PicLink",url7);
                                         batch7.set(WLDoc6,WLD6M);


                                         //Day7 Map
                                         final  DocumentReference WLDoc7 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("WeightLifting")
                                                 .collection("WeightLifting").document("Day7");

                                         final Map<String,Object> WLD7M = new HashMap<>();
                                         WLD7M.put("Day","Day7");
                                         WLD7M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         WLD7M.put("SplitDay",SplitedTime);
                                         WLD7M.put("FullHours",String.valueOf(numberpickervalue));
                                         WLD7M.put("PicLink",url7);
                                         batch7.set(WLDoc7,WLD7M);


                                         // Batch Ends



                                         /////////////////////////////////////// Completed Batch
                                         final  DocumentReference CompletedWLDoc = database.collection("Activities")
                                                 .document(UID).collection("CompletedExercises").document("WeightLifting");

                                         final Map<String,Object> CompletedWLDocM = new HashMap<>();
                                         CompletedWLDocM.put("Day1",false);
                                         CompletedWLDocM.put("Day2",false);
                                         CompletedWLDocM.put("Day3",false);
                                         CompletedWLDocM.put("Day4",false);
                                         CompletedWLDocM.put("Day5",false);
                                         CompletedWLDocM.put("Day6",false);
                                         CompletedWLDocM.put("Day7",false);
                                         CompletedWLDocM.put("Fail",false);
                                         CompletedWLDocM.put("Skip1",false);
                                         CompletedWLDocM.put("Skip2",false);
                                         CompletedWLDocM.put("Skip3",false);
                                         CompletedWLDocM.put("Skip4",false);
                                         CompletedWLDocM.put("Skip5",false);
                                         CompletedWLDocM.put("Skip6",false);
                                         CompletedWLDocM.put("Skip7",false);


                                         Completedbacth7.set(CompletedWLDoc,CompletedWLDocM);
                                         //////////////////////////////////////





                                         Doc.set(Map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                             @Override
                                             public void onSuccess(Void aVoid) {

                                                 Completedbacth7.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {

                                                         batch7.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task)
                                                             {
                                                                 documentReferenceV1.update("WeightLifting",true);
                                                                 Toast.makeText(context,"data added succesfully",Toast.LENGTH_LONG).show();
                                                             }
                                                         });
                                                     }
                                                 });
                                             }
                                         }).addOnFailureListener(new OnFailureListener() {
                                             @Override
                                             public void onFailure(@NonNull Exception e) {
                                                 Log.w(TAG, "Error writing document", e);
                                                 Toast.makeText(context,"data not added",Toast.LENGTH_LONG).show();
                                             }
                                         });
                                     }}
                             });
                                   }


                         else if (exerciseGridDataclickedItem.getName().equals("WeightLifting") && WEIGHT_LIFTING)
                         {
                             Toast.makeText(context,"You already set target for Weight Lifting , COMPLETE it or DELETE it",Toast.LENGTH_LONG).show();
                         }





                         // IF CLICKED ON yOGA
                         else if (exerciseGridDataclickedItem.getName().equals("Yoga") && !YOGA)
                         {
                             // INFLATING weight_lifting_popup.xml

                             final View Yoga_popup = layoutInflater.inflate(R.layout.yoga_popup,null);

                             Exercisesend  = Yoga_popup.findViewById(R.id.exercise_upload_button);
                             numberPicker  = Yoga_popup.findViewById(R.id.number_picker);
                             numberPicker.setMinValue(1);
                             numberPicker.setMaxValue(70);
                             numberPicker.setWrapSelectorWheel(true);
                             CalenderButton = Yoga_popup.findViewById(R.id.calenderbb);
                             startdate_textView = Yoga_popup.findViewById(R.id.plan_start_text);
                             enddate_TextView   = Yoga_popup.findViewById(R.id.plan_ends_text);





                             final PopupWindow popupWindow = new PopupWindow(Yoga_popup,width,height,focusable);
                             popupWindow.setAnimationStyle(R.style.Animation);
                             popupWindow.showAtLocation(v, Gravity.CENTER,0,-155);


                             Yoga_popup.setOnTouchListener(new View.OnTouchListener() {
                                 @Override
                                 public boolean onTouch(View v, MotionEvent event) {

                                     return  true;
                                 }
                             });

                             // OPENING CALENDER AND SETTING DATE

                             CalenderButton.setOnClickListener(new View.OnClickListener() {

                                 @RequiresApi(api = Build.VERSION_CODES.N)
                                 @Override
                                 public void onClick(View v) {

                                     DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                                         @Override
                                         public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                             startdate_textView.setText(new StringBuilder().append(dayOfMonth).append(":").append(month+1)
                                                     .append(":").append(year));

                                             calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                             calendar.set(Calendar.MONTH,month);
                                             calendar.set(Calendar.YEAR,year);

                                             calendar.add(Calendar.DAY_OF_MONTH,7);

                                             enddate_TextView.setText(new StringBuilder().append(format.format(calendar.getTime())));
                                         }
                                     };

                                     DatePickerDialog dpDialog = new DatePickerDialog(context,dateSetListener,
                                             calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));

                                     dpDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000   );
                                     dpDialog.show();
                                 }
                             });



                             // SENDING DATA TO FIRESTORE

                             Exercisesend.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     popupWindow.dismiss();
                                     startDate = startdate_textView.getText().toString().trim();
                                     endDate   = enddate_TextView.getText().toString();
                                     if (startDate.equals(""))
                                     {
                                         Toast.makeText(context,"Please select start date from calender",Toast.LENGTH_LONG).show();
                                     }

                                     else {

                                         int numberpickervalue = numberPicker.getValue();
                                         double seconds           = numberpickervalue*3600;

                                         int minutes           = numberpickervalue * 60 ;
                                         int  dividedMinutes    =  (minutes / 7);
                                         int    finalMinutes      = (dividedMinutes * 7);



                                         double dividedValue1 =     seconds / 7;
                                         Log.d("splited seconds ", String.valueOf(dividedValue1));

                                         int day = (int) (dividedValue1 / (24 * 3600));
                                         int dividedValue2 = (int) (dividedValue1 % (24 * 3600));
                                         int hour = (int) (dividedValue2 / 3600);
                                         dividedValue2 %= 3600;
                                         int minutestwo = dividedValue2 / 60 ;

                                         String SplitedTime = hour+" hours - "+minutestwo + " minutes" ;

                                         Log.d("alldata", SplitedTime);



                                         DocumentReference Doc = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Yoga");

                                         Map<String,Object> Map     = new HashMap<>();
                                         Map.put("TimeStamp", FieldValue.serverTimestamp());
                                         Map.put("ExerciseName" ,exerciseGridDataclickedItem.getName());
                                         Map.put("StartingDate",startDate);
                                         Map.put("End_Date",endDate);
                                         Map.put("DayOfMonth",DOM);
                                         Map.put("Month",M);
                                         Map.put("Year",Y);
                                         Map.put("total_hours_for_week",String.valueOf(numberpickervalue)+": hours");
                                         Map.put("hours_per_day",SplitedTime);
                                         Map.put("TotalMinutes",finalMinutes);
                                         Map.put("PerDayMinutes",dividedMinutes);
                                         Map.put("Current_Progres",0);




                                         //Batch Start

                                         final WriteBatch batch8          = database.batch();
                                         final WriteBatch Completedbacth8 = database.batch();

                                         // Day1 Map
                                         final  DocumentReference YDoc1 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Yoga")
                                                 .collection("Yoga").document("Day1");

                                         final Map<String,Object> YD1M = new HashMap<>();
                                         YD1M.put("Day","Day1");
                                         YD1M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         YD1M.put("SplitDay",SplitedTime);
                                         YD1M.put("FullHours",String.valueOf(numberpickervalue));
                                         YD1M.put("Seconds",dividedValue1);
                                         YD1M.put("SecondsUploaded",false);
                                         YD1M.put("PicLink",url8);
                                         batch8.set(YDoc1,YD1M);


                                         //Day2 Map
                                         final DocumentReference YDoc2 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Yoga")
                                                 .collection("Yoga").document("Day2");

                                         final Map<String,Object> YD2M = new HashMap<>();
                                         YD2M.put("Day","Day2");
                                         YD2M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         YD2M.put("SplitDay",SplitedTime);
                                         YD2M.put("FullHours",String.valueOf(numberpickervalue));
                                         YD2M.put("Seconds",dividedValue1);
                                         YD2M.put("SecondsUploaded",false);
                                         YD2M.put("PicLink",url8);
                                         batch8.set(YDoc2,YD2M);


                                         //Day3 Map
                                         final  DocumentReference YDoc3 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Yoga")
                                                 .collection("Yoga").document("Day3");

                                         final Map<String,Object> YD3M = new HashMap<>();
                                         YD3M.put("Day","Day3");
                                         YD3M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         YD3M.put("SplitDay",SplitedTime);
                                         YD3M.put("FullHours",String.valueOf(numberpickervalue));
                                         YD3M.put("Seconds",dividedValue1);
                                         YD3M.put("SecondsUploaded",false);
                                         YD3M.put("PicLink",url8);
                                         batch8.set(YDoc3,YD3M);


                                         //Day4 Map
                                         final  DocumentReference YDoc4 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Yoga")
                                                 .collection("Yoga").document("Day4");

                                         final Map<String,Object> YD4M = new HashMap<>();
                                         YD4M.put("Day","Day4");
                                         YD4M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         YD4M.put("SplitDay",SplitedTime);
                                         YD4M.put("FullHours",String.valueOf(numberpickervalue));
                                         YD4M.put("Seconds",dividedValue1);
                                         YD4M.put("SecondsUploaded",false);
                                         YD4M.put("PicLink",url8);
                                         batch8.set(YDoc4,YD4M);


                                         //Day5 Map
                                         final  DocumentReference YDoc5 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Yoga")
                                                 .collection("Yoga").document("Day5");

                                         final Map<String,Object> YD5M = new HashMap<>();
                                         YD5M.put("Day","Day5");
                                         YD5M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         YD5M.put("SplitDay",SplitedTime);
                                         YD5M.put("FullHours",String.valueOf(numberpickervalue));
                                         YD5M.put("Seconds",dividedValue1);
                                         YD5M.put("SecondsUploaded",false);
                                         YD5M.put("PicLink",url8);
                                         batch8.set(YDoc5,YD5M);


                                         //Day6 Map
                                         final  DocumentReference YDoc6 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Yoga")
                                                 .collection("Yoga").document("Day6");

                                         final Map<String,Object> YD6M = new HashMap<>();
                                         YD6M.put("Day","Day6");
                                         YD6M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         YD6M.put("SplitDay",SplitedTime);
                                         YD6M.put("FullHours",String.valueOf(numberpickervalue));
                                         YD6M.put("Seconds",dividedValue1);
                                         YD6M.put("SecondsUploaded",false);
                                         YD6M.put("PicLink",url8);
                                         batch8.set(YDoc6,YD6M);


                                         //Day7 Map
                                         final  DocumentReference YDoc7 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("Yoga")
                                                 .collection("Yoga").document("Day7");

                                         final Map<String,Object> YD7M = new HashMap<>();
                                         YD7M.put("Day","Day7");
                                         YD7M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         YD7M.put("SplitDay",SplitedTime);
                                         YD7M.put("FullHours",String.valueOf(numberpickervalue));
                                         YD7M.put("Seconds",dividedValue1);
                                         YD7M.put("SecondsUploaded",false);
                                         YD7M.put("PicLink",url8);
                                         batch8.set(YDoc7,YD7M);


                                         // Batch Ends



                                         /////////////////////////////////////// Completed Batch
                                         final  DocumentReference CompletedYDoc = database.collection("Activities")
                                                 .document(UID).collection("CompletedExercises").document("Yoga");

                                         final Map<String,Object> CompletedYDocM = new HashMap<>();
                                         CompletedYDocM.put("Day1",false);
                                         CompletedYDocM.put("Day2",false);
                                         CompletedYDocM.put("Day3",false);
                                         CompletedYDocM.put("Day4",false);
                                         CompletedYDocM.put("Day5",false);
                                         CompletedYDocM.put("Day6",false);
                                         CompletedYDocM.put("Day7",false);
                                         CompletedYDocM.put("Fail",false);
                                         CompletedYDocM.put("Skip1",false);
                                         CompletedYDocM.put("Skip2",false);
                                         CompletedYDocM.put("Skip3",false);
                                         CompletedYDocM.put("Skip4",false);
                                         CompletedYDocM.put("Skip5",false);
                                         CompletedYDocM.put("Skip6",false);
                                         CompletedYDocM.put("Skip7",false);


                                         Completedbacth8.set(CompletedYDoc,CompletedYDocM);
                                         //////////////////////////////////////





                                         Doc.set(Map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                             @Override
                                             public void onSuccess(Void aVoid) {

                                                 Completedbacth8.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {
                                                         batch8.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task) {
                                                                 documentReferenceV1.update("Yoga",true);
                                                                 Toast.makeText(context,"data added succesfully",Toast.LENGTH_LONG).show();
                                                             }
                                                         });
                                                     }
                                                 });
                                             }
                                         }).addOnFailureListener(new OnFailureListener() {
                                             @Override
                                             public void onFailure(@NonNull Exception e) {
                                                 Log.w(TAG, "Error writing document", e);
                                                 Toast.makeText(context,"data not added",Toast.LENGTH_LONG).show();
                                             }
                                         });
                                     }}
                             });
                         }




                         else if (exerciseGridDataclickedItem.getName().equals("Yoga") && YOGA)
                         {
                             Toast.makeText(context,"You already set target for Yoga , COMPLETE it or DELETE it",Toast.LENGTH_LONG).show();
                         }




                         // IF CLICKED ON JOGGING
                         else if (exerciseGridDataclickedItem.getName().equals("jogging") && !JOGGING)
                         {
                             // INFLATING weight_lifting_popup.xml

                             final View Jogging_popup = layoutInflater.inflate(R.layout.jogging,null);

                             Exercisesend  = Jogging_popup.findViewById(R.id.exercise_upload_button);
                             numberPicker  = Jogging_popup.findViewById(R.id.number_picker);
                             numberPicker.setMinValue(1);
                             numberPicker.setMaxValue(70);
                             numberPicker.setWrapSelectorWheel(true);
                             CalenderButton = Jogging_popup.findViewById(R.id.calenderbb);
                             startdate_textView = Jogging_popup.findViewById(R.id.plan_start_text);
                             enddate_TextView   = Jogging_popup.findViewById(R.id.plan_ends_text);





                             final PopupWindow popupWindow = new PopupWindow(Jogging_popup,width,height,focusable);
                             popupWindow.setAnimationStyle(R.style.Animation);
                             popupWindow.showAtLocation(v, Gravity.CENTER,0,-155);


                             Jogging_popup.setOnTouchListener(new View.OnTouchListener() {
                                 @Override
                                 public boolean onTouch(View v, MotionEvent event) {

                                     return  true;
                                 }
                             });

                             // OPENING CALENDER AND SETTING DATE

                             CalenderButton.setOnClickListener(new View.OnClickListener() {

                                 @RequiresApi(api = Build.VERSION_CODES.N)
                                 @Override
                                 public void onClick(View v) {
                                     DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                                         @Override
                                         public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                             startdate_textView.setText(new StringBuilder().append(dayOfMonth).append(":").append(month+1)
                                                     .append(":").append(year));

                                             calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                             calendar.set(Calendar.MONTH,month);
                                             calendar.set(Calendar.YEAR,year);

                                             calendar.add(Calendar.DAY_OF_MONTH,7);

                                             enddate_TextView.setText(new StringBuilder().append(format.format(calendar.getTime())));
                                         }
                                     };

                                     DatePickerDialog dpDialog = new DatePickerDialog(context,dateSetListener,
                                             calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));

                                     dpDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000   );
                                     dpDialog.show();
                                 }
                             });


                             // SENDING DATA TO FIRESTORE

                             Exercisesend.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     popupWindow.dismiss();
                                     startDate = startdate_textView.getText().toString().trim();
                                     endDate   = enddate_TextView.getText().toString();
                                     if (startDate.equals(""))
                                     {
                                         Toast.makeText(context,"Please select start date from calender",Toast.LENGTH_LONG).show();
                                     }

                                     else {

                                         int numberpickervalue = numberPicker.getValue();
                                         double seconds           = numberpickervalue*3600;

                                         int minutes           = numberpickervalue * 60 ;
                                         int  dividedMinutes    =  (minutes / 7);
                                         int    finalMinutes      = (dividedMinutes * 7);



                                         double dividedValue1 =     seconds / 7;
                                         Log.d("splited seconds ", String.valueOf(dividedValue1));

                                         int day = (int) (dividedValue1 / (24 * 3600));
                                         int dividedValue2 = (int) (dividedValue1 % (24 * 3600));
                                         int hour = (int) (dividedValue2 / 3600);
                                         dividedValue2 %= 3600;
                                         int minutestwo = dividedValue2 / 60 ;

                                         String SplitedTime = hour+" hours - "+minutestwo + " minutes" ;

                                         Log.d("alldata", SplitedTime);



                                         DocumentReference Doc = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("jogging");

                                         Map<String,Object> Map     = new HashMap<>();
                                         Map.put("TimeStamp", FieldValue.serverTimestamp());
                                         Map.put("ExerciseName" ,exerciseGridDataclickedItem.getName());
                                         Map.put("StartingDate",startDate);
                                         Map.put("End_Date",endDate);
                                         Map.put("DayOfMonth",DOM);
                                         Map.put("Month",M);
                                         Map.put("Year",Y);
                                         Map.put("total_hours_for_week",String.valueOf(numberpickervalue)+": hours");
                                         Map.put("hours_per_day",SplitedTime);
                                         Map.put("TotalMinutes",finalMinutes);
                                         Map.put("PerDayMinutes",dividedMinutes);
                                         Map.put("Current_Progres",0);



                                         //Batch Start

                                         final WriteBatch batch9          = database.batch();
                                         final WriteBatch Completedbacth9 = database.batch();

                                         // Day1 Map
                                         final  DocumentReference JGDoc1 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("jogging")
                                                 .collection("jogging").document("Day1");

                                         final Map<String,Object> JGD1M = new HashMap<>();
                                         JGD1M.put("Day","Day1");
                                         JGD1M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         JGD1M.put("SplitDay",SplitedTime);
                                         JGD1M.put("FullHours",String.valueOf(numberpickervalue));
                                         JGD1M.put("Seconds",dividedValue1);
                                         JGD1M.put("SecondsUploaded",false);
                                         JGD1M.put("PicLink",url9);
                                         batch9.set(JGDoc1,JGD1M);


                                         //Day2 Map
                                         final DocumentReference JGDoc2 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("jogging")
                                                 .collection("jogging").document("Day2");

                                         final Map<String,Object> JGD2M = new HashMap<>();
                                         JGD2M.put("Day","Day2");
                                         JGD2M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         JGD2M.put("SplitDay",SplitedTime);
                                         JGD2M.put("FullHours",String.valueOf(numberpickervalue));
                                         JGD2M.put("Seconds",dividedValue1);
                                         JGD2M.put("SecondsUploaded",false);
                                         JGD2M.put("PicLink",url9);
                                         batch9.set(JGDoc2,JGD2M);


                                         //Day3 Map
                                         final  DocumentReference JGDoc3 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("jogging")
                                                 .collection("jogging").document("Day3");

                                         final Map<String,Object> JGD3M = new HashMap<>();
                                         JGD3M.put("Day","Day3");
                                         JGD3M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         JGD3M.put("SplitDay",SplitedTime);
                                         JGD3M.put("FullHours",String.valueOf(numberpickervalue));
                                         JGD3M.put("Seconds",dividedValue1);
                                         JGD3M.put("SecondsUploaded",false);
                                         JGD3M.put("PicLink",url9);
                                         batch9.set(JGDoc3,JGD3M);


                                         //Day4 Map
                                         final  DocumentReference JGDoc4 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("jogging")
                                                 .collection("jogging").document("Day4");

                                         final Map<String,Object> JGD4M = new HashMap<>();
                                         JGD4M.put("Day","Day4");
                                         JGD4M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         JGD4M.put("SplitDay",SplitedTime);
                                         JGD4M.put("FullHours",String.valueOf(numberpickervalue));
                                         JGD4M.put("Seconds",dividedValue1);
                                         JGD4M.put("SecondsUploaded",false);
                                         JGD4M.put("PicLink",url9);
                                         batch9.set(JGDoc4,JGD4M);


                                         //Day5 Map
                                         final  DocumentReference JGDoc5 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("jogging")
                                                 .collection("jogging").document("Day5");

                                         final Map<String,Object> JGD5M = new HashMap<>();
                                         JGD5M.put("Day","Day5");
                                         JGD5M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         JGD5M.put("SplitDay",SplitedTime);
                                         JGD5M.put("FullHours",String.valueOf(numberpickervalue));
                                         JGD5M.put("Seconds",dividedValue1);
                                         JGD5M.put("SecondsUploaded",false);
                                         JGD5M.put("PicLink",url9);
                                         batch9.set(JGDoc5,JGD5M);


                                         //Day6 Map
                                         final  DocumentReference JGDoc6 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("jogging")
                                                 .collection("jogging").document("Day6");

                                         final Map<String,Object> JGD6M = new HashMap<>();
                                         JGD6M.put("Day","Day6");
                                         JGD6M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         JGD6M.put("SplitDay",SplitedTime);
                                         JGD6M.put("FullHours",String.valueOf(numberpickervalue));
                                         JGD6M.put("Seconds",dividedValue1);
                                         JGD6M.put("SecondsUploaded",false);
                                         JGD6M.put("PicLink",url9);
                                         batch9.set(JGDoc6,JGD6M);


                                         //Day7 Map
                                         final  DocumentReference JGDoc7 = database.collection("Activities").document(UID)
                                                 .collection("PendingExercises").document("jogging")
                                                 .collection("jogging").document("Day7");

                                         final Map<String,Object> JGD7M = new HashMap<>();
                                         JGD7M.put("Day","Day7");
                                         JGD7M.put("EName" ,exerciseGridDataclickedItem.getName());
                                         JGD7M.put("SplitDay",SplitedTime);
                                         JGD7M.put("FullHours",String.valueOf(numberpickervalue));
                                         JGD7M.put("Seconds",dividedValue1);
                                         JGD7M.put("SecondsUploaded",false);
                                         JGD7M.put("PicLink",url9);
                                         batch9.set(JGDoc7,JGD7M);


                                         // Batch Ends



                                         /////////////////////////////////////// Completed Batch
                                         final  DocumentReference CompletedJGDoc = database.collection("Activities")
                                                 .document(UID).collection("CompletedExercises").document("jogging");

                                         final Map<String,Object> CompletedJGDocM = new HashMap<>();
                                         CompletedJGDocM.put("Day1",false);
                                         CompletedJGDocM.put("Day2",false);
                                         CompletedJGDocM.put("Day3",false);
                                         CompletedJGDocM.put("Day4",false);
                                         CompletedJGDocM.put("Day5",false);
                                         CompletedJGDocM.put("Day6",false);
                                         CompletedJGDocM.put("Day7",false);
                                         CompletedJGDocM.put("Fail",false);
                                         CompletedJGDocM.put("Skip1",false);
                                         CompletedJGDocM.put("Skip2",false);
                                         CompletedJGDocM.put("Skip3",false);
                                         CompletedJGDocM.put("Skip4",false);
                                         CompletedJGDocM.put("Skip5",false);
                                         CompletedJGDocM.put("Skip6",false);
                                         CompletedJGDocM.put("Skip7",false);


                                         Completedbacth9.set(CompletedJGDoc,CompletedJGDocM);

                                         //////////////////////////////////////




                                         Doc.set(Map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                             @Override
                                             public void onSuccess(Void aVoid) {

                                                 Completedbacth9.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {

                                                         batch9.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task) {

                                                                documentReferenceV1.update("jogging",true);
                                                                 Toast.makeText(context,"data added succesfully",Toast.LENGTH_LONG).show();
                                                             }
                                                         });
                                                     }
                                                 });
                                             }
                                         }).addOnFailureListener(new OnFailureListener() {
                                             @Override
                                             public void onFailure(@NonNull Exception e) {
                                                 Log.w(TAG, "Error writing document", e);
                                                 Toast.makeText(context,"data not added",Toast.LENGTH_LONG).show();
                                             }
                                         });
                                     }}
                             });
                         }
                         else if (exerciseGridDataclickedItem.getName().equals("jogging") && JOGGING)
                         {
                             Toast.makeText(context,"You already set target for Jogging , COMPLETE it or DELETE it",Toast.LENGTH_LONG).show();
                         }


                     }}});

       }


    }

}
