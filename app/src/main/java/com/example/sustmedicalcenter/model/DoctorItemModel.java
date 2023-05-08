package com.example.sustmedicalcenter.model;

public class DoctorItemModel {
    String imageUrl;
    String name, details;

    public DoctorItemModel() {
        //empty constructor for firebase
    }

    public DoctorItemModel(String imageUrl, String name, String details){
      this.imageUrl = imageUrl;
      this.name = name;
      this.details = details;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String description) {
        this.details = description;
    }
}
