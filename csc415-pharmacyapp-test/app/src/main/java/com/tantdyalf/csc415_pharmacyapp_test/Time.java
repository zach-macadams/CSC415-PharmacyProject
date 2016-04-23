package com.tantdyalf.csc415_pharmacyapp_test;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.TimeRealmProxy;


@org.parceler.Parcel(implementations = {TimeRealmProxy.class},
                        value = Parcel.Serialization.BEAN,
                        analyze = {Time.class})
public class Time extends RealmObject {

    private String time;

    public Time() {

    }


    public String getTime() {

        return this.time;
    }

    public void setTime(String time) {

        this.time = time;
    }
}
