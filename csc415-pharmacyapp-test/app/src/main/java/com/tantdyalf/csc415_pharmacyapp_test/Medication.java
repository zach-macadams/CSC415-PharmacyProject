package com.tantdyalf.csc415_pharmacyapp_test;

import io.realm.RealmList;
import io.realm.RealmObject;


public class Medication extends RealmObject {

    private String name;
    private String doctorName;
    private int dose;
    private int numRefills;
    private int method;
    private int period;
    private boolean alertsActive;
    private Time startTime;
    private RealmList<Time> timesToBeTaken;
    private RealmList<Weekday> daysToBeTaken;
    private RealmList<PendingIntentRequestCode> resultCodes;


    public Medication() {

    }


    public String getName() {

        return this.name;
    }

    public void setName(String name) {

        this.name = name;
    }



    public String getDoctorName() {

        return this.doctorName;
    }

    public void setDoctorName(String doctorName) {

        this.doctorName = doctorName;
    }



    public int getDose() {

        return this.dose;
    }

    public void setDose(int dose) {

        this.dose = dose;
    }



    public RealmList<Weekday> getDaysToBeTaken() {

        return this.daysToBeTaken;
    }

    public void setDaysToBeTaken(RealmList<Weekday> daysToBeTaken) {

        this.daysToBeTaken = daysToBeTaken;
    }



    public int getNumRefills() {

        return this.numRefills;
    }

    public void setNumRefills(int numRefills) {

        this.numRefills = numRefills;
    }



    public int getMethod() {

        return this.method;
    }

    public void setMethod(int method) {

        this.method = method;
    }



    public RealmList<Time> getTimesToBeTaken() {

        return this.timesToBeTaken;
    }

    public void setTimesToBeTaken(RealmList<Time> timesToBeTaken) {

        this.timesToBeTaken = timesToBeTaken;
    }



    public int getPeriod() {

        return this.period;
    }

    public void setPeriod(int period) {

        this.period = period;
    }



    public Time getStartTime() {

        return this.startTime;
    }

    public void setStartTime(Time startTime) {

        this.startTime = startTime;
    }


    public boolean getAlertsActive() {
        return alertsActive;
    }

    public void setAlertsActive(boolean alertsActive) {
        this.alertsActive = alertsActive;
    }

    public RealmList<PendingIntentRequestCode> getResultCodes() {
        return resultCodes;
    }

    public void setResultCodes(RealmList<PendingIntentRequestCode> resultCodes) {
        this.resultCodes = resultCodes;
    }
}
