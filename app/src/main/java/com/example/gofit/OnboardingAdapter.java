package com.example.gofit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class OnboardingAdapter extends PagerAdapter {

Context context;
LayoutInflater layoutInflater;


    public OnboardingAdapter(Context context)
    {
        this.context = context;
    }


public int[] OnBoardImage = {R.drawable.running_lady,R.drawable.weigthliftinglady,R.drawable.manwoman};
public  String [] Descriptions = {
            "Being healthy is all, no health is nothing.",
            "Take 30 minutes of bodybuilding every day" +
                    "to get physically fit and healthy.",
            "Bad body shape, poor sleep, lack of strength," +
                    "weight gain, weak bones, easily traumatized" +
                    "body, depressed, stressed, poor metabolism," +
                    "poor resistance"
    };




    @Override
    public int getCount() {
        return Descriptions.length;
    }



    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
    layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view =layoutInflater.inflate(R.layout.slide_layout,container,false);


        ImageView imageView =view.findViewById(R.id.person);
        TextView  textView = view.findViewById(R.id.OnBoardTextView);


        imageView.setImageResource(OnBoardImage[position]);
        textView.setText(Descriptions[position]);

        container.addView(view);





    return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
