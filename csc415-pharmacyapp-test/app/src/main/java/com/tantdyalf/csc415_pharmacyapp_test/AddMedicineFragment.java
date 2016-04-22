package com.tantdyalf.csc415_pharmacyapp_test;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by macadamsz1 on 2/13/16.
 */
public class AddMedicineFragment extends Fragment implements
        RadioGroup.OnCheckedChangeListener,
        CompoundButton.OnCheckedChangeListener {



    private Context context;

    private EditText eTxtAddMedicineName, eTxtAddMedicineDoctor, eTxtAddMedicineDose, eTxtAddMedicineRefills;
    private CheckBox  chkAddMedicineSunday, chkAddMedicineMonday, chkAddMedicineTuesday, chkAddMedicineWednesday,
                    chkAddMedicineThursday, chkAddMedicineFriday, chkAddMedicineSaturday;
    private Button btnAddMedicineSubmit, btnAddMedicineReset;
    private RadioButton rdoAddMedicineMethodPeriodically, rdoAddMedicineMethodScheduled;
    private RadioGroup rdoGroupAddMedicineMethod;

    private RelativeLayout addMedicineMethodPeriodLayout;
    private EditText eTxtPeriod;
    private Button btnAddStartTime;
    private TextView txtStartTime;

    private LinearLayout addMedicineMethodScheduledLayout;
    private Button btnAddTimes;
    private ArrayList<TextView> newTimesList = new ArrayList<>();

    private Realm medicationRealm;

    private ArrayList<Weekday> weekDays = new ArrayList<>();



    public static AddMedicineFragment newInstance() {

        return new AddMedicineFragment();
    }

    public AddMedicineFragment () {

    }



    @Override
    public void onStart() {

        super.onStart();
        context = getActivity();
        medicationRealm = Realm.getInstance(context);
    }

    @Override
    public void onStop() {

        super.onStop();
        medicationRealm.close();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        final View view = inflater.inflate(R.layout.fragment_add_medicine, container, false);

        Activity activity = getActivity();
        activity.setTitle(R.string.add_medicine_fragment_label);

        eTxtAddMedicineName = (EditText) view.findViewById(R.id.et_add_medicine_name);
        eTxtAddMedicineDoctor = (EditText) view.findViewById(R.id.et_add_medicine_doctor);
        eTxtAddMedicineDose = (EditText) view.findViewById(R.id.et_add_medicine_dose);
        eTxtAddMedicineRefills = (EditText) view.findViewById(R.id.et_add_medicine_refills);

        chkAddMedicineSunday = (CheckBox) view.findViewById(R.id.cb_add_medicine_sunday);
        chkAddMedicineMonday = (CheckBox) view.findViewById(R.id.cb_add_medicine_monday);
        chkAddMedicineTuesday = (CheckBox) view.findViewById(R.id.cb_add_medicine_tuesday);
        chkAddMedicineWednesday = (CheckBox) view.findViewById(R.id.cb_add_medicine_wednesday);
        chkAddMedicineThursday = (CheckBox) view.findViewById(R.id.cb_add_medicine_thursday);
        chkAddMedicineFriday = (CheckBox) view.findViewById(R.id.cb_add_medicine_friday);
        chkAddMedicineSaturday = (CheckBox) view.findViewById(R.id.cb_add_medicine_saturday);

        chkAddMedicineSunday.setOnCheckedChangeListener(this);
        chkAddMedicineMonday.setOnCheckedChangeListener(this);
        chkAddMedicineTuesday.setOnCheckedChangeListener(this);
        chkAddMedicineWednesday.setOnCheckedChangeListener(this);
        chkAddMedicineThursday.setOnCheckedChangeListener(this);
        chkAddMedicineFriday.setOnCheckedChangeListener(this);
        chkAddMedicineSaturday.setOnCheckedChangeListener(this);

        rdoAddMedicineMethodPeriodically = (RadioButton) view.findViewById(R.id.rdo_add_method_periodically);
        rdoAddMedicineMethodScheduled = (RadioButton) view.findViewById(R.id.rdo_add_method_scheduled);

        rdoGroupAddMedicineMethod = (RadioGroup) view.findViewById(R.id.add_method_selection_container);
        rdoGroupAddMedicineMethod.setOnCheckedChangeListener(this);

        addMedicineMethodPeriodLayout = (RelativeLayout) view.findViewById(R.id.add_medicine_method_period_layout);
        addMedicineMethodScheduledLayout = (LinearLayout) view.findViewById(R.id.add_medicine_method_scheduled_layout);

        eTxtPeriod = (EditText) view.findViewById(R.id.et_add_medicine_period);
        txtStartTime = (TextView) view.findViewById(R.id.tv_add_medicine_period_start_time);
        btnAddStartTime = (Button) view.findViewById(R.id.btn_add_medicine_period_start_time);
        btnAddStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayAddTimeDialog(txtStartTime);
            }
        });
        btnAddTimes = (Button) view.findViewById(R.id.btn_add_add_time);

        btnAddTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView newTimeView = new TextView(context);
                newTimeView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 10, 0, 0);
                params.gravity = Gravity.CENTER;

                newTimeView.setLayoutParams(params);

                newTimeView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        removeAddedTime((TextView) v);
                        newTimesList.remove(v);
                        return true;
                    }
                });

                addMedicineMethodScheduledLayout.addView(newTimeView);

                displayAddTimeDialog(newTimeView);
                newTimesList.add(newTimeView);
            }
        });

        btnAddMedicineSubmit = (Button) view.findViewById(R.id.btn_add_submit_medicine);
        btnAddMedicineReset = (Button) view.findViewById(R.id.btn_add_reset_medicine);


        btnAddMedicineSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateFields())
                {
                    medicationRealm.beginTransaction();

                    Medication newMedicine = medicationRealm.createObject(Medication.class);
                    setConstantMedicationRealmFields(newMedicine);
                    if(rdoAddMedicineMethodPeriodically.isChecked())
                    {
                        newMedicine.setMethod(MainActivity.METHOD_PERIODICAL);
                        newMedicine.setPeriod(Integer.parseInt(eTxtPeriod.getText().toString()));
                        Time startTime = medicationRealm.createObject(Time.class);
                        startTime.setTime(txtStartTime.getText().toString());
                        newMedicine.setStartTime(startTime);
                        newMedicine.setTimesToBeTaken(new RealmList<Time>());

                        medicationRealm.commitTransaction();

                        ArrayList<Calendar> alertTimes = TimeManager.getPeriodicalTimes(newMedicine);
                        TimeManager.setUpAlerts(getContext(), medicationRealm, newMedicine, alertTimes);
                    }
                    else
                    {
                        RealmList<Time> timesRealmList = new RealmList<Time>();
                        for(int i = 0; i < newTimesList.size(); i++)
                        {
                            Time time = medicationRealm.createObject(Time.class);
                            time.setTime(newTimesList.get(i).getText().toString());
                            timesRealmList.add(time);
                        }
                        newMedicine.setTimesToBeTaken(timesRealmList);
                        newMedicine.setMethod(MainActivity.METHOD_SCHEDULED);
                        Time startTime = medicationRealm.createObject(Time.class);
                        startTime.setTime("");
                        newMedicine.setStartTime(startTime);

                        medicationRealm.commitTransaction();

                        ArrayList<Calendar> alertTimes = TimeManager.getScheduledTimes(newMedicine);
                        TimeManager.setUpAlerts(getContext(), medicationRealm, newMedicine, alertTimes);
                    }

                    Log.d("Realm", "committed new medicine");
                    Fragment fragment = null;
                    try
                    {
                        fragment = MedicineListFragment.class.newInstance();
                    }
                    catch(Exception e){}

                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_content_container, fragment)
                            .commit();
                }
                else
                {
                    Snackbar.make(view.findViewById(R.id.scroll_add_medicine), R.string.add_medicine_error, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        btnAddMedicineReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetAllFields();
            }
        });
        return view;
    }



    private void removeAddedTime(TextView view) {

        addMedicineMethodScheduledLayout.removeView(view);
    }

    private void setConstantMedicationRealmFields(Medication medication) {

        medication.setName(eTxtAddMedicineName.getText().toString());
        medication.setDoctorName(eTxtAddMedicineDoctor.getText().toString());
        medication.setDose(Integer.parseInt(eTxtAddMedicineDose.getText().toString()));
        medication.setNumRefills(Integer.parseInt(eTxtAddMedicineRefills.getText().toString()));
        medication.setAlertsActive(true);

        RealmList<Weekday> weekDaysRealmList = new RealmList<Weekday>();
        for(int i = 0; i < weekDays.size(); i++)
        {
            Weekday day = medicationRealm.createObject(Weekday.class);
            day.setDay(weekDays.get(i).getDay());
            day.setDayName(weekDays.get(i).getDayName());
            weekDaysRealmList.add(day);
        }
        medication.setDaysToBeTaken(weekDaysRealmList);
    }
    private boolean validateFields() {


        if(!(validateEditText(eTxtAddMedicineName)
                && validateEditText(eTxtAddMedicineDoctor)
                && validateEditText(eTxtAddMedicineDose)
                && validateEditText(eTxtAddMedicineRefills)
                && weekDays.size() > 0))
            return false;

        if(rdoAddMedicineMethodScheduled.isChecked())
        {
            if(newTimesList.size() == 0)
                return false;
        }
        else if(rdoAddMedicineMethodPeriodically.isChecked())
        {
            if(!(validateTextView(txtStartTime) && validateEditText(eTxtPeriod)))
                return false;
        }

        return true;
    }

    private boolean validateEditText(EditText field) {

        return !field.getText().toString().equals("");
    }

    private boolean validateTextView(TextView field) {

        return !field.getText().toString().equals("");
    }

    private void resetAllFields() {

        eTxtAddMedicineName.setText("");
        eTxtAddMedicineDoctor.setText("");
        eTxtAddMedicineDose.setText("");
        eTxtAddMedicineRefills.setText("");

        eTxtPeriod.setText("");
        txtStartTime.setText("");

        resetAddedTimes();
        resetWeekdayCheckboxes();
    }

    private void resetAddedTimes() {

        for(TextView tv : newTimesList)
        {
            addMedicineMethodScheduledLayout.removeView(tv);
        }
    }

    private void resetWeekdayCheckboxes() {

        chkAddMedicineSunday.setChecked(false);
        chkAddMedicineMonday.setChecked(false);
        chkAddMedicineTuesday.setChecked(false);
        chkAddMedicineWednesday.setChecked(false);
        chkAddMedicineThursday.setChecked(false);
        chkAddMedicineFriday.setChecked(false);
        chkAddMedicineSaturday.setChecked(false);

        weekDays.clear();
    }

    private void displayAddTimeDialog(final TextView timeView) {

        Calendar currentCal = Calendar.getInstance();
        int hour = currentCal.get(Calendar.HOUR_OF_DAY);
        int minute = currentCal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                int displayHour;
                String endTimeTxt;
                if(hourOfDay > 12)
                {
                    displayHour = hourOfDay - 12;
                    endTimeTxt = "PM";
                }
                else
                {
                    displayHour = hourOfDay;
                    endTimeTxt = "AM";
                }
                String minuteStr = String.valueOf(minute);
                String displayMinute = "";
                if(minute < 10)
                {
                    displayMinute = displayMinute + "0" + minuteStr;
                }
                else
                {
                    displayMinute = minuteStr;
                }
                String timeString = displayHour + ":" + displayMinute + " " + endTimeTxt;
                timeView.setText(timeString);
            }
        }, hour, minute, false);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch(checkedId)
        {
            case R.id.rdo_add_method_periodically:

                if(rdoAddMedicineMethodPeriodically.isChecked())
                {
                    if(addMedicineMethodScheduledLayout.getVisibility() == View.VISIBLE)
                    {
                        addMedicineMethodScheduledLayout.setVisibility(View.INVISIBLE);
                    }
                    if(addMedicineMethodPeriodLayout.getVisibility() == View.INVISIBLE)
                    {
                        addMedicineMethodPeriodLayout.setVisibility(View.VISIBLE);
                    }

                }
                break;

            case R.id.rdo_add_method_scheduled:

                if(rdoAddMedicineMethodScheduled.isChecked())
                {
                    if(addMedicineMethodPeriodLayout.getVisibility() == View.VISIBLE)
                    {
                        addMedicineMethodPeriodLayout.setVisibility(View.INVISIBLE);
                    }
                    if(addMedicineMethodScheduledLayout.getVisibility() == View.INVISIBLE)
                    {
                        addMedicineMethodScheduledLayout.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        Weekday day;
        switch(buttonView.getId())
        {
            case R.id.cb_add_medicine_sunday:

                if(chkAddMedicineSunday.isChecked())
                {
                    day = new Weekday("Sunday", MainActivity.SUNDAY);
                    weekDays.add(day);
                }
                else
                {
                    for(Weekday weekday : weekDays)
                    {
                        if(weekday.getDay() == MainActivity.SUNDAY)
                        {
                            weekDays.remove(weekday);
                        }
                    }
                }
                break;

            case R.id.cb_add_medicine_monday:

                if(chkAddMedicineMonday.isChecked())
                {
                    day = new Weekday("Monday", MainActivity.MONDAY);
                    weekDays.add(day);
                }
                else
                {
                    for(Weekday weekday : weekDays)
                    {
                        if(weekday.getDay() == MainActivity.MONDAY)
                        {
                            weekDays.remove(weekday);
                        }
                    }
                }
                break;

            case R.id.cb_add_medicine_tuesday:

                if(chkAddMedicineTuesday.isChecked())
                {
                    day = new Weekday("Tuesday", MainActivity.TUESDAY);
                    weekDays.add(day);
                }
                else
                {
                    for(Weekday weekday : weekDays)
                    {
                        if(weekday.getDay() == MainActivity.TUESDAY)
                        {
                            weekDays.remove(weekday);
                        }
                    }
                }
                break;

            case R.id.cb_add_medicine_wednesday:

                if(chkAddMedicineWednesday.isChecked())
                {
                    day = new Weekday("Wednesday", MainActivity.WEDNESDAY);
                    weekDays.add(day);
                }
                else
                {
                    for(Weekday weekday : weekDays)
                    {
                        if(weekday.getDay() == MainActivity.WEDNESDAY)
                        {
                            weekDays.remove(weekday);
                        }
                    }
                }
                break;

            case R.id.cb_add_medicine_thursday:

                if(chkAddMedicineThursday.isChecked())
                {
                    day = new Weekday("Thursday", MainActivity.THURSDAY);
                    weekDays.add(day);
                }
                else
                {
                    for(Weekday weekday : weekDays)
                    {
                        if(weekday.getDay() == MainActivity.THURSDAY)
                        {
                            weekDays.remove(weekday);
                        }
                    }
                }
                break;

            case R.id.cb_add_medicine_friday:

                if(chkAddMedicineFriday.isChecked())
                {
                    day = new Weekday("Friday", MainActivity.FRIDAY);
                    weekDays.add(day);
                }
                else
                {
                    for(Weekday weekday : weekDays)
                    {
                        if(weekday.getDay() == MainActivity.FRIDAY)
                        {
                            weekDays.remove(weekday);
                        }
                    }
                }
                break;

            case R.id.cb_add_medicine_saturday:

                if(chkAddMedicineSaturday.isChecked())
                {
                    day = new Weekday("Saturday", MainActivity.SATURDAY);
                    weekDays.add(day);
                }
                else
                {
                    for(Weekday weekday : weekDays)
                    {
                        if(weekday.getDay() == MainActivity.SATURDAY)
                        {
                            weekDays.remove(weekday);
                        }
                    }
                }
                break;
        }
    }
}
