package com.example.sustmedicalcenter.model;

import java.util.ArrayList;

public class Appointment {


    private String appointmentUid;
    private Long date;
    private String formattedDate;
    private String time;
    private String applicantsDisplayImageUrl;
    private String doctorDisplayImageUrl;
    private String applicantsName;
    private String doctorsName;
    private String applicantsMissCount;
    private String problemTitle;
    private String problemDetails;
    private boolean isExpanded;
    private ArrayList<PrescriptionItem> prescriptionList;
    private String appointmentStatus;
    private String applicantUid;
    private String doctorUid;

    public Appointment() {
        // required empty constructor for firestore
    }




    public Appointment(String appointmentUid, Long date, String formattedDate, String time, String applicantsDisplayImageUrl, String doctorDisplayImageUrl, String applicantsName, String applicantsMissCount, String doctorsName, String problemTitle, String problemDetails, ArrayList<PrescriptionItem> prescriptionList, String applicantUid, String doctorUid, String appointmentStatus) {
        this.appointmentUid = appointmentUid;
        this.date = date;
        this.time = time;
        this.applicantsDisplayImageUrl = applicantsDisplayImageUrl;
        this.doctorDisplayImageUrl = doctorDisplayImageUrl;
        this.applicantsName = applicantsName;
        this.doctorsName = doctorsName;
        this.applicantsMissCount = applicantsMissCount;
        this.problemTitle = problemTitle;
        this.problemDetails = problemDetails;
        isExpanded = false;
        this.appointmentStatus = appointmentStatus;
        this.prescriptionList = prescriptionList;
        this.applicantUid = applicantUid;
        this.doctorUid = doctorUid;
        this.formattedDate = formattedDate;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getApplicantsDisplayImageUrl() {
        return applicantsDisplayImageUrl;
    }

    public void setApplicantsDisplayImageUrl(String applicantsDisplayImageUrl) {
        this.applicantsDisplayImageUrl = applicantsDisplayImageUrl;
    }



    public String getApplicantsName() {
        return applicantsName;
    }

    public void setApplicantsName(String applicantsName) {
        this.applicantsName = applicantsName;
    }

    public String getDoctorsName() {
        return doctorsName;
    }

    public void setDoctorsName(String doctorsName) {
        this.doctorsName = doctorsName;
    }

    public String getApplicantsMissCount() {
        return applicantsMissCount;
    }

    public void setApplicantsMissCount(String applicantsMissCount) {
        this.applicantsMissCount = applicantsMissCount;
    }

    public String getProblemTitle() {
        return problemTitle;
    }

    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
    }

    public String getProblemDetails() {
        return problemDetails;
    }

    public void setProblemDetails(String problemDetails) {
        this.problemDetails = problemDetails;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }



    public String getApplicantUid() {
        return applicantUid;
    }

    public void setApplicantUid(String applicantUid) {
        this.applicantUid = applicantUid;
    }

    public String getDoctorUid() {
        return doctorUid;
    }

    public void setDoctorUid(String doctorUid) {
        this.doctorUid = doctorUid;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public String getAppointmentUid() {
        return appointmentUid;
    }

    public void setAppointmentUid(String appointmentUid) {
        this.appointmentUid = appointmentUid;
    }

    public String getDoctorDisplayImageUrl() {
        return doctorDisplayImageUrl;
    }

    public void setDoctorDisplayImageUrl(String doctorDisplayImageUrl) {
        this.doctorDisplayImageUrl = doctorDisplayImageUrl;
    }

    public ArrayList<PrescriptionItem> getPrescriptionList() {
        return prescriptionList;
    }

    public void setPrescriptionList(ArrayList<PrescriptionItem> prescriptionList) {
        this.prescriptionList = prescriptionList;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }
}