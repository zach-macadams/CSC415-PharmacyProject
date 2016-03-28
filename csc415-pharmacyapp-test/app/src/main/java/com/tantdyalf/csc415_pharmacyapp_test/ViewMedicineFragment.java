package com.tantdyalf.csc415_pharmacyapp_test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import io.realm.RealmList;

/**
 * Created by Zach_macadams on 3/20/16.
 */
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

    private static final String ARGUMENT_MEDICATION_NAME = "medicationName";
    private static final String ARGUMENT_MEDICATION_DOCTOR = "medicationDoctor";
    private static final String ARGUMENT_MEDICATION_DOSE = "medicationDose";
    private static final String ARGUMENT_MEDICATION_REFILLS = "medicationRefills";
    private static final String ARGUMENT_MEDICATION_WEEKDAYS = "medicationWeekdays";
    private static final String ARGUMENT_MEDICATION_METHOD = "medicationMethod";
    private static final String ARGUMENT_MEDICATION_PERIOD = "medicationPeriod";
    private static final String ARGUMENT_MEDICATION_START_TIME = "medicationStartTime";
    private static final String ARGUMENT_MEDICATION_SCHEDULED_TIMES = "medicationScheduledTimes";




    public static ViewMedicineFragment newInstance(String medicationName, String doctorName, int dose,
                                                   int refills, int method, ArrayList<Weekday> weekdays,
                                                   int period, String startTime, ArrayList<Time> scheduledTimes)
    {
        Bundle args = new Bundle();

        args.putString(ARGUMENT_MEDICATION_NAME, medicationName);
        args.putString(ARGUMENT_MEDICATION_DOCTOR, doctorName);
        args.putInt(ARGUMENT_MEDICATION_DOSE, dose);
        args.putInt(ARGUMENT_MEDICATION_REFILLS, refills);
        args.putInt(ARGUMENT_MEDICATION_METHOD, method);
        args.putSerializable(ARGUMENT_MEDICATION_WEEKDAYS, weekdays);
        args.putInt(ARGUMENT_MEDICATION_PERIOD, period);
        args.putString(ARGUMENT_MEDICATION_START_TIME, startTime);
        args.putSerializable(ARGUMENT_MEDICATION_SCHEDULED_TIMES, scheduledTimes);

        ViewMedicineFragment fragment = new ViewMedicineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ViewMedicineFragment() {

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

        Bundle args = getArguments();

        txtViewMedicineName.setText(args.getString(ARGUMENT_MEDICATION_NAME));
        txtViewMedicineDoctor.setText(args.getString(ARGUMENT_MEDICATION_DOCTOR));
        txtViewMedicineDose.setText(String.valueOf(args.getInt(ARGUMENT_MEDICATION_DOSE)));
        txtViewMedicineRefills.setText(String.valueOf(args.getInt(ARGUMENT_MEDICATION_REFILLS)));

        ArrayList<Weekday> weekdays = (ArrayList<Weekday>) args.getSerializable(ARGUMENT_MEDICATION_WEEKDAYS);
        String weekdayString = "";
        if(weekdays != null)
        {
            for(Weekday day : weekdays)
            {
                weekdayString += day.getDayName() + " ";
            }
        }
        txtViewMedicineWeekdays.setText(weekdayString);

        int method = args.getInt(ARGUMENT_MEDICATION_METHOD);
        if(method == MainActivity.METHOD_PERIODICAL)
        {
            layoutScheduledLayout.setVisibility(View.INVISIBLE);
            txtViewMedicineMethod.setText("Periodical");
            txtViewMedicineStartTime.setText(args.getString(ARGUMENT_MEDICATION_START_TIME));
            txtViewMedicinePeriod.setText(String.valueOf(args.getInt(ARGUMENT_MEDICATION_PERIOD)));
        }
        else
        {
            txtViewMedicineMethod.setText("Scheduled");
            layoutPeriodicalLayout.setVisibility(View.INVISIBLE);
            ArrayList<Time> scheduledTimes = (ArrayList<Time>) args.getSerializable(ARGUMENT_MEDICATION_SCHEDULED_TIMES);
            String scheduledTimesString = "";
            if(scheduledTimes != null)
            {
                for(Time time : scheduledTimes)
                {
                    scheduledTimesString += time.getTime() + " ";
                }
            }
            txtViewMedicineScheduledTimes.setText(scheduledTimesString);
        }
        return view;
    }
}