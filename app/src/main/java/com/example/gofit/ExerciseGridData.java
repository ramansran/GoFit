package com.example.gofit;

import android.graphics.Bitmap;

public class ExerciseGridData {

String Url;
String Name;

    public ExerciseGridData(String url, String name) {
        Url = url;
        Name = name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
