package com.tantdyalf.csc415_pharmacyapp_test;

import java.util.ArrayList;

/**
 * Created by macadamsz1 on 2/16/16.
 */
public class Medication {

    private String mName;
    private int mDose;
    private ArrayList<String> mTimesToBeTaken;
    private ArrayList<String> mDaysToBeTaken;
    private int mNumRefills;
    private String mRefillDate;


    public Medication(String name, int dose, ArrayList<String> timesToBeTaken, ArrayList<String> daysToBeTaken) {

        this.mName = name;
        this.mDose = dose;
        this.mTimesToBeTaken = timesToBeTaken;
        this.mDaysToBeTaken = daysToBeTaken;
    }
}
