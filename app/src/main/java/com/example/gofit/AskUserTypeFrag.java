package com.example.gofit;


import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class AskUserTypeFrag extends Fragment {
Button btn_n_signup;
Button btn_t_signup;
Button btn_log_in ;
Animation animation;



    public AskUserTypeFrag() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        animation = AnimationUtils.loadAnimation(getActivity(),R.anim.shake);
        View view = inflater.inflate(R.layout.fragment_ask_user_type, container, false);

        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.askusertypestatusbar));


        btn_n_signup = view.findViewById(R.id.normalSignup);

        btn_log_in   = view.findViewById(R.id.LogInBtn);


        btn_n_signup.startAnimation(animation);



        final Handler new2handler = new Handler();
        new2handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               btn_log_in.startAnimation(animation);
            }
        }, 2000);



        btn_n_signup.setOnClickListener(new View.OnClickListener() {
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


        btn_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new LogIn();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
                ft.replace(R.id.frame,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });




       return view;
    }

}
