package com.tantdyalf.csc415_pharmacyapp_test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by macadamsz1 on 2/13/16.
 */
public class RefillsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_refills, container, false);

        Activity activity = getActivity();
        activity.setTitle(R.string.refills_fragment_label);

        return view;
    }
}
