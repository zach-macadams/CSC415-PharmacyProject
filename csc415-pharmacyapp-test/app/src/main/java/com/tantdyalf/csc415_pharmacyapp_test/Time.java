package com.tantdyalf.csc415_pharmacyapp_test;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Zach_macadams on 3/21/16.
 */
public class Time extends RealmObject implements Serializable{

    private String time;

    public Time() {

    }


    public String getTime() {

        return this.time;
    }

    public void setTime(String time) {

        this.time = time;
    }

    /*@Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(time);
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Time(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[0];
        }
    };

    public Time(Parcel source) {

        this.time = source.readString();
    }*/
}
