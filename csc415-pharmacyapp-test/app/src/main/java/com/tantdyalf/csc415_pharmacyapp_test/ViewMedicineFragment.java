package com.tantdyalf.csc415_pharmacyapp_test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.parceler.Parcel;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;


public class ViewMedicineFragment  extends Fragment {

    private TextView txtViewMedicineName;
    private TextView txtViewMedicineDoctor;
    private TextView txtViewMedicineDose;
    private TextView txtViewMedicineRefills;
    private TextView txtViewMedicineWeekdays;
    private TextView txtViewMedicineMethod;
    private TextView txtViewMedicineStartTime;
    private TextView txtViewMedicinePeriod;
    private TextView txtViewMedicineScheduledTimes;
    private RelativeLayout layoutPeriodicalLayout;
    private RelativeLayout layoutScheduledLayout;
    private Button btnViewMedicineEdit;


    private Realm medicationRealm;



    public static ViewMedicineFragment newInstance(String medicationName, String doctorName, int dose,
                                                   int refills, int method, ArrayList<Weekday> weekdays,
                                                   int period, String startTime, ArrayList<Time> scheduledTimes)
    {
        Bundle args = new Bundle();

        args.putString(MainActivity.ARGUMENT_MEDICATION_NAME, medicationName);
        args.putString(MainActivity.ARGUMENT_MEDICATION_DOCTOR, doctorName);
        args.putInt(MainActivity.ARGUMENT_MEDICATION_DOSE, dose);
        args.putInt(MainActivity.ARGUMENT_MEDICATION_REFILLS, refills);
        args.putInt(MainActivity.ARGUMENT_MEDICATION_METHOD, method);
        args.putParcelable(MainActivity.ARGUMENT_MEDICATION_WEEKDAYS, Parcels.wrap(weekdays));
        args.putInt(MainActivity.ARGUMENT_MEDICATION_PERIOD, period);
        args.putString(MainActivity.ARGUMENT_MEDICATION_START_TIME, startTime);
        args.putParcelable(MainActivity.ARGUMENT_MEDICATION_SCHEDULED_TIMES, Parcels.wrap(scheduledTimes));

        ViewMedicineFragment fragment = new ViewMedicineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ViewMedicineFragment() {

    }


    @Override
    public void onStart() {

        super.onStart();
        medicationRealm = Realm.getInstance(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view =  inflater.inflate(R.layout.fragment_view_medicine_detail, container, false);

        txtViewMedicineName = (TextView) view.findViewById(R.id.tv_view_medicine_name);
        txtViewMedicineDoctor = (TextView) view.findViewById(R.id.tv_view_medicine_doctor);
        txtViewMedicineDose = (TextView) view.findViewById(R.id.tv_view_medicine_dose);
        txtViewMedicineRefills = (TextView) view.findViewById(R.id.tv_view_medicine_refills);
        txtViewMedicineWeekdays = (TextView) view.findViewById(R.id.tv_view_medicine_weekdays);
        txtViewMedicineMethod = (TextView) view.findViewById(R.id.tv_view_medicine_method);
        txtViewMedicinePeriod = (TextView) view.findViewById(R.id.tv_view_medicine_period);
        txtViewMedicineStartTime = (TextView) view.findViewById(R.id.tv_view_medicine_start_time);
        txtViewMedicineScheduledTimes = (TextView) view.findViewById(R.id.tv_view_medicine_scheduled_times);
        layoutPeriodicalLayout = (RelativeLayout) view.findViewById(R.id.layout_view_medicine_periodical);
        layoutScheduledLayout = (RelativeLayout) view.findViewById(R.id.layout_view_medicine_scheduled);
        btnViewMedicineEdit = (Button) view.findViewById(R.id.btn_view_edit_medicine);

        final Bundle args = getArguments();

        getActivity().setTitle(args.getString(MainActivity.ARGUMENT_MEDICATION_NAME));

        txtViewMedicineName.setText(args.getString(MainActivity.ARGUMENT_MEDICATION_NAME));
        txtViewMedicineDoctor.setText(args.getString(MainActivity.ARGUMENT_MEDICATION_DOCTOR));
        txtViewMedicineDose.setText(String.valueOf(args.getInt(MainActivity.ARGUMENT_MEDICATION_DOSE)));
        txtViewMedicineRefills.setText(String.valueOf(args.getInt(MainActivity.ARGUMENT_MEDICATION_REFILLS)));

        ArrayList<Weekday> weekdays = Parcels.unwrap(args.getParcelable(MainActivity.ARGUMENT_MEDICATION_WEEKDAYS));
        String weekdayString = "";
        if(weekdays != null)
        {
            for(int i = 0; i < weekdays.size(); i++)
            {
                weekdayString += weekdays.get(i).getDayName();
                if(i != weekdays.size() - 1)
                {
                    weekdayString += ",  ";
                }
            }
        }
        txtViewMedicineWeekdays.setText(weekdayString);

        int method = args.getInt(MainActivity.ARGUMENT_MEDICATION_METHOD);
        if(method == MainActivity.METHOD_PERIODICAL)
        {
            layoutScheduledLayout.setVisibility(View.INVISIBLE);
            txtViewMedicineMethod.setText("Periodical");
            txtViewMedicineStartTime.setText(args.getString(MainActivity.ARGUMENT_MEDICATION_START_TIME));
            txtViewMedicinePeriod.setText(String.valueOf(args.getInt(MainActivity.ARGUMENT_MEDICATION_PERIOD)));
        }
        else
        {
            txtViewMedicineMethod.setText("Scheduled");
            layoutPeriodicalLayout.setVisibility(View.INVISIBLE);
            ArrayList<Time> scheduledTimes = Parcels.unwrap(args.getParcelable(MainActivity.ARGUMENT_MEDICATION_SCHEDULED_TIMES));
            String scheduledTimesString = "";
            if(scheduledTimes != null)
            {
                for(int i = 0; i < scheduledTimes.size(); i++)
                {
                    scheduledTimesString += scheduledTimes.get(i).getTime();
                    if(i != scheduledTimes.size() - 1)
                    {
                        scheduledTimesString += ",  ";
                    }
                }
            }
            txtViewMedicineScheduledTimes.setText(scheduledTimesString);
        }

        btnViewMedicineEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Medication medicine = medicationRealm.where(Medication.class)
                                                     .equalTo("name", args.getString(MainActivity.ARGUMENT_MEDICATION_NAME))
                                                     .findFirst();
                EditMedicineFragment fragment = EditMedicineFragment.newInstance(medicine);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_content_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }
}
