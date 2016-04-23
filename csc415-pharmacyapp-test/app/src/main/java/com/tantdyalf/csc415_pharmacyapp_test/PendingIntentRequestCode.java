package com.tantdyalf.csc415_pharmacyapp_test;

import io.realm.RealmObject;


public class PendingIntentRequestCode extends RealmObject {

    private int requestCode;


    public PendingIntentRequestCode() {}

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}
