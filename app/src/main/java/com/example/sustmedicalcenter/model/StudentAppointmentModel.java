package com.example.sustmedicalcenter.model;

import java.util.List;

public class StudentAppointmentModel {
    private int imageview1;
    private String time,formattedDate,applicantsName,problemTitle,problemDetails,drname;
   // private boolean doc_Expanded;
    private String applicantsDisplayImageUrl;
    private String doctorDisplayImageUrl;
   // boolean dueList,PastList;



    public StudentAppointmentModel() {
        // required empty constructor for firestore
    }

    public StudentAppointmentModel(int imageview1, String formattedDate, String time,String applicantsName, String problemTitle, String problemDetails, String applicantsDisplayImageUrl, String doctorDisplayImageUrl){
        this.imageview1=imageview1;
        this.formattedDate=formattedDate;
        this.time=time;
        this.applicantsName=applicantsName;
        this.problemTitle=problemTitle;
        this.problemDetails=problemDetails;
        this.applicantsDisplayImageUrl = applicantsDisplayImageUrl;
        this.doctorDisplayImageUrl = doctorDisplayImageUrl;
        //doc_Expanded = false;
        //dueList = true;

    }

    public int getImageview1() {
        return imageview1;
    }

    public String getTime() {
        return time;
    }

    public String getFormattedDate() {
        return formattedDate;
    }
    public String getApplicantsName() {
        return applicantsName;
    }

//    public boolean doc_Expanded() {
//        return doc_Expanded;
//    }
//    public void setDoc_Expanded(boolean doc_Expanded){this.doc_Expanded=doc_Expanded;}

//    public boolean dueList() {
//        return dueList;
//    }
//    public void setDueList(boolean dueList){this.dueList=dueList;}



    public String getProblemTitle() {
        return problemTitle;
    }
    public String getProblemDetails() {
        return problemDetails;
    }

    public String getApplicantsDisplayImageUrl() {
        return applicantsDisplayImageUrl;
    }

    public void setApplicantsDisplayImageUrl(String applicantsDisplayImageUrl) {
        this.applicantsDisplayImageUrl = applicantsDisplayImageUrl;
    }

    public String getDoctorDisplayImageUrl() {
        return doctorDisplayImageUrl;
    }

    public void setDoctorDisplayImageUrl(String doctorDisplayImageUrl) {
        this.doctorDisplayImageUrl = doctorDisplayImageUrl;
    }


    public void setImageview1(int imageview1) {
        this.imageview1 = imageview1;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }
    public void setApplicantsName(String applicantsName) {
        this.applicantsName = applicantsName;
    }
    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
    }
    public void setProblemDetails(String problemDetails) {
        this.problemDetails = problemDetails;
    }

}
