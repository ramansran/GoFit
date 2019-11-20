package com.example.gofit;

public class history_holding_class {

    String Name;
    String StartingDate;
    String EndingDate;
    String Fullhours;
    Boolean shared;


    public history_holding_class(String name, String startingDate, String endingDate, String fullhours,Boolean sh) {
        Name = name;
        StartingDate = startingDate;
        EndingDate = endingDate;
        Fullhours = fullhours;
        shared = sh;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStartingDate() {
        return StartingDate;
    }

    public void setStartingDate(String startingDate) {
        StartingDate = startingDate;
    }

    public String getEndingDate() {
        return EndingDate;
    }

    public void setEndingDate(String endingDate) {
        EndingDate = endingDate;
    }

    public String getFullhours() {
        return Fullhours;
    }

    public void setFullhours(String fullhours) {
        Fullhours = fullhours;
    }
}
