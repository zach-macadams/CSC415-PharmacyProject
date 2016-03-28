package com.tantdyalf.csc415_pharmacyapp_test;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Zach_macadams on 3/21/16.
 */
public class Weekday extends RealmObject implements Serializable {

    private String dayName;
    private int day;

    public Weekday() {

    }

    public Weekday(String dayName, int day) {

        this.dayName = dayName;
        this.day = day;
    }

    public String getDayName() {

        return this.dayName;
    }

    public void setDayName(String dayName) {

        this.dayName = dayName;
    }



    public int getDay() {

        return this.day;
    }

    public void setDay(int day) {

        this.day = day;
    }

    /*@Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(dayName);
        dest.writeInt(day);
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Weekday(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[0];
        }
    };

    public Weekday(Parcel source) {

        this.dayName = source.readString();
        this.day = source.readInt();
    }*/
}
