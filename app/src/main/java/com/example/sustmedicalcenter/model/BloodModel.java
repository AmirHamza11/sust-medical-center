package com.example.sustmedicalcenter.model;

public class BloodModel {
    private String Name,Phone_no, last_day_of_donation;
    private int profile_pic;

    public BloodModel(String name, String phone_no, String last_day_of_donation, int profile_pic) {
        this.Name = name;
        this.Phone_no = phone_no;
        this.last_day_of_donation = last_day_of_donation;
        this.profile_pic = profile_pic;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone_no() {
        return Phone_no;
    }

    public void setPhone_no(String phone_no) {
        Phone_no = phone_no;
    }

    public String getLast_day_of_donation() {
        return last_day_of_donation;
    }

    public void setLast_day_of_donation(String last_day_of_donation) {
        this.last_day_of_donation = last_day_of_donation;
    }

    public int getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(int profile_pic) {
        this.profile_pic = profile_pic;
    }
}
