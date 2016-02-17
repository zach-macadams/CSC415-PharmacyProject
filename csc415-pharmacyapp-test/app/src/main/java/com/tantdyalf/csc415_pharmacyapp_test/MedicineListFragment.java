package com.tantdyalf.csc415_pharmacyapp_test;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by macadamsz1 on 2/13/16.
 */
public class MedicineListFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_medications, container, false);
        Snackbar.make(view.findViewById(R.id.lv_medicines), "Add medications", Snackbar.LENGTH_LONG).show();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = null;
                try
                {
                    fragment = AddMedicineFragment.class.newInstance();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_content_container, fragment).commit();
                Activity activity = getActivity();
                activity.setTitle(R.string.add_medicine_fragment_label);
            }
        });

        return view;
    }
}
