package com.example.sustmedicalcenter.model;

public class PrescriptionItem {
    private String textview3;
    private String textview4;


    public PrescriptionItem() {
        // required empty constructor for firestore
    }

    public PrescriptionItem(String textview3, String textview4) {

        this.textview3 = textview3;
        this.textview4 = textview4;

    }




    public String getTextview3() {
        return textview3;
    }

    public String getTextview4() {
        return textview4;
    }

    public void setTextview3(String textview3) {
        this.textview3 = textview3;
    }

    public void setTextview4(String textview4) {
        this.textview4 = textview4;
    }
}
