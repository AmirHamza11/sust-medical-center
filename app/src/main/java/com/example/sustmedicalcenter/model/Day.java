package com.example.sustmedicalcenter.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Day {

    private String date;
    private ArrayList<String> availableTimes;

    public Day() {
        // required empty constructor for firestore
    }

    private ArrayList<Boolean> isAvailable;

    public Day(String date, ArrayList<String> availableTimes, ArrayList<Boolean> isAvailable) {
        this.date = date;
        this.availableTimes = availableTimes;
        this.isAvailable = isAvailable;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getAvailableTimes() {
        return availableTimes;
    }


    public void setAvailableTimes(ArrayList<String> availableTimes) {
        this.availableTimes = availableTimes;
    }

    public ArrayList<Boolean> getIsAvailable() {
       return isAvailable;


    }

    public void setIsAvailable(ArrayList<Boolean> isAvailable) {
        this.isAvailable = isAvailable;
    }
}
