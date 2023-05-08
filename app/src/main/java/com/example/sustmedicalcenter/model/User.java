package com.example.sustmedicalcenter.model;

import java.io.Serializable;

public class User implements Serializable {

    private String userType;

    private String displayImageUrl, userName, email, department, registrationNo, phoneNo, bloodGroup,
            lastDayOfBloodDonation, chronicDisease, userUid;

    private String doctorDetails, doctorAppointmentCount;

    private String accountStatus, requestSentDate;




    public User() {
        //empty constructor for firebase
    }


    public User(String userType, String displayImageUrl, String userName, String email, String department, String registrationNo, String phoneNo, String bloodGroup, String lastDayOfBloodDonation, String chronicDisease, String userUid, String doctorDetails, String doctorAppointmentCount,String accountStatus, String requestSentDate) {
        this.userType = userType;
        this.displayImageUrl = displayImageUrl;
        this.userName = userName;
        this.email = email;
        this.department = department;
        this.registrationNo = registrationNo;
        this.phoneNo = phoneNo;
        this.bloodGroup = bloodGroup;
        this.lastDayOfBloodDonation = lastDayOfBloodDonation;
        this.chronicDisease = chronicDisease;
        this.userUid = userUid;
        this.doctorDetails = doctorDetails;
        this.doctorAppointmentCount = doctorAppointmentCount;
        this.accountStatus = accountStatus;
        this.requestSentDate = requestSentDate;
    }

    public String getDisplayImageUrl() {
        return displayImageUrl;
    }

    public void setDisplayImageUrl(String displayImageUrl) {
        this.displayImageUrl = displayImageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getLastDayOfBloodDonation() {
        return lastDayOfBloodDonation;
    }

    public void setLastDayOfBloodDonation(String lastDayOfBloodDonation) {
        this.lastDayOfBloodDonation = lastDayOfBloodDonation;
    }

    public String getChronicDisease() {
        return chronicDisease;
    }

    public void setChronicDisease(String chronicDisease) {
        this.chronicDisease = chronicDisease;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDoctorDetails() {
        return doctorDetails;
    }

    public void setDoctorDetails(String doctorDetails) {
        this.doctorDetails = doctorDetails;
    }

    public String getDoctorAppointmentCount() {
        return doctorAppointmentCount;
    }

    public void setDoctorAppointmentCount(String doctorAppointmentCount) {
        this.doctorAppointmentCount = doctorAppointmentCount;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getRequestSentDate() {
        return requestSentDate;
    }

    public void setRequestSentDate(String requestSentDate) {
        this.requestSentDate = requestSentDate;
    }
}
