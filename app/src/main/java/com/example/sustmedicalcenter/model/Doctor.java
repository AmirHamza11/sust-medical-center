package com.example.sustmedicalcenter.model;

import com.google.android.material.dialog.MaterialDialogs;

public class Doctor {

    private String doctorImageUrl;
    private String doctorName;
    private String doctorDetails;
    private String doctorAppointmentCount;


    public Doctor() {
        //required for database
    }

    public Doctor(String doctorImageUrl, String doctorName, String doctorDetails, String doctorAppointmentCount) {
        this.doctorImageUrl = doctorImageUrl;
        this.doctorName = doctorName;
        this.doctorDetails = doctorDetails;
        this.doctorAppointmentCount = doctorAppointmentCount;
    }

    public String getDoctorImageUrl() {
        return doctorImageUrl;
    }

    public void setDoctorImageUrl(String doctorImageUrl) {
        this.doctorImageUrl = doctorImageUrl;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorAppointmentCount() {
        return doctorAppointmentCount;
    }

    public void setDoctorAppointmentCount(String doctorAppointmentCount) {
        this.doctorAppointmentCount = doctorAppointmentCount;
    }

    public String getDoctorDetails() {
        return doctorDetails;
    }

    public void setDoctorDetails(String doctorDetails) {
        this.doctorDetails = doctorDetails;
    }
}
