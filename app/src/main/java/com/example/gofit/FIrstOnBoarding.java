package com.example.gofit;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FIrstOnBoarding extends Fragment {


ViewPager viewPager;
Button btn_next,btn_prev;
LinearLayout dots_place;
OnboardingAdapter onboardingAdapterl;
TextView[] dots;
Context context = getContext();
int currentPage;
int pos;
Animation animation;



    public FIrstOnBoarding() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_first_on_boarding, container, false);

        viewPager = view.findViewById(R.id.viewpager);
        btn_next  = view.findViewById(R.id.nextbtn);
        btn_prev  = view.findViewById(R.id.prevbtn);
        dots_place = view.findViewById(R.id.liner);

        onboardingAdapterl = new OnboardingAdapter(getActivity());
        viewPager.setAdapter(onboardingAdapterl);


        viewPager.addOnPageChangeListener(viewListener);
        addDotIndicator(0);


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = viewPager.getCurrentItem();

                if (pos == dots.length-1)
                {


                    Fragment fragment = new AskUserTypeFrag();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    ft.replace(R.id.frame,fragment);
                    ft.commit();
                }
                else {
                    viewPager.setCurrentItem(currentPage+1);
                }
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(currentPage - 1);
            }
        });



        return view;
    }





    public void addDotIndicator(int position){

        dots = new TextView[3];
        dots_place.removeAllViews();

        for (int i = 0 ; i < dots.length ; i++)
        {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.transparent));

            dots_place.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.White));
        }
    }





    ViewPager.OnPageChangeListener  viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotIndicator(position);
            currentPage = position;

            if (currentPage == 0 )
            {
                btn_next.setEnabled(true);
                btn_prev.setEnabled(false);
                btn_prev.setVisibility(View.INVISIBLE);
                btn_next.setText("NEXT");
                btn_prev.setText("");
            }
            else if(currentPage == dots.length-1)
            {
                btn_next.setEnabled(true);
                btn_prev.setEnabled(true);
                btn_prev.setVisibility(View.VISIBLE);
                btn_next.setText("FINISH");
                btn_prev.setText("BACK");
            }
            else
                {
                    btn_next.setEnabled(true);
                    btn_prev.setEnabled(true);
                    btn_prev.setVisibility(View.VISIBLE);
                    btn_next.setText("NEXT");
                    btn_prev.setText("BACK");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };









}
