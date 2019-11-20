package com.example.gofit;

import java.util.List;

public class HoursHoldingClass {
String fullhrs;
String dividedhrs;
String url;
String Name;
String DayName;

    public HoursHoldingClass(String fullhrs, String dividedhrs, String url,String name,String dayname) {
        this.fullhrs = fullhrs;
        this.dividedhrs = dividedhrs;
        this.url = url;
        this.Name = name;
        this.DayName  = dayname;

    }

    public String getDay() {
        return DayName;
    }

    public void setDay(String day) {
        DayName = day;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFullhrs() {
        return fullhrs;
    }

    public void setFullhrs(String fullhrs) {
        this.fullhrs = fullhrs;
    }

    public String getDividedhrs() {
        return dividedhrs;
    }

    public void setDividedhrs(String dividedhrs) {
        this.dividedhrs = dividedhrs;
    }
}
