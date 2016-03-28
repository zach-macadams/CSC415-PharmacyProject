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
public class AlertListFragment extends Fragment {

    public static AlertListFragment newInstance() {

        Bundle args = new Bundle();

        AlertListFragment fragment = new AlertListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public AlertListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_alerts, container, false);

        Activity activity = getActivity();
        activity.setTitle(R.string.alerts_list_fragment_label);

        return view;
    }
}
