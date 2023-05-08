package com.example.sustmedicalcenter.model;

public class MedicineListModel {
    String medicineName, genericName;

    public MedicineListModel(String medicineName, String genericName) {
        this.medicineName = medicineName;
        this.genericName = genericName;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }
}
