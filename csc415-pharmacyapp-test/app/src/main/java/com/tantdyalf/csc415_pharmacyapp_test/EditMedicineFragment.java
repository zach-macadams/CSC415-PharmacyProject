package com.tantdyalf.csc415_pharmacyapp_test;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
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

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmList;


public class EditMedicineFragment extends Fragment implements
    RadioGroup.OnCheckedChangeListener,
    CompoundButton.OnCheckedChangeListener {

        private Context context;

        private EditText eTxtEditMedicineName, eTxtEditMedicineDoctor, eTxtEditMedicineDose, eTxtEditMedicineRefills;
        private CheckBox chkEditMedicineSunday, chkEditMedicineMonday, chkEditMedicineTuesday, chkEditMedicineWednesday,
                chkEditMedicineThursday, chkEditMedicineFriday, chkEditMedicineSaturday;
        private Button btnEditMedicineSubmit, btnEditMedicineCancel;
        private RadioButton rdoEditMedicineMethodPeriodically, rdoEditMedicineMethodScheduled;
        private RadioGroup rdoGroupEditMedicineMethod;

        private RelativeLayout editMedicineMethodPeriodLayout;
        private EditText eTxtPeriod;
        private Button btnEditStartTime;
        private TextView txtStartTime;

        private LinearLayout editMedicineMethodScheduledLayout;
        private Button btnAddTimes;
        private ArrayList<TextView> newTimesList = new ArrayList<>();

        private Realm medicationRealm;

        private ArrayList<Weekday> weekDays = new ArrayList<>();



    public static EditMedicineFragment newInstance(Medication medicine) {

        Bundle args = new Bundle();

        args.putString(MainActivity.ARGUMENT_MEDICATION_NAME, medicine.getName());
        args.putString(MainActivity.ARGUMENT_MEDICATION_DOCTOR, medicine.getDoctorName());
        args.putInt(MainActivity.ARGUMENT_MEDICATION_DOSE, medicine.getDose());
        args.putInt(MainActivity.ARGUMENT_MEDICATION_REFILLS, medicine.getNumRefills());
        args.putInt(MainActivity.ARGUMENT_MEDICATION_METHOD, medicine.getMethod());
        ArrayList<Weekday> weekdays = new ArrayList<>();
        for(Weekday weekday : medicine.getDaysToBeTaken())
        {
            weekdays.add(weekday);
        }
        args.putParcelable(MainActivity.ARGUMENT_MEDICATION_WEEKDAYS, Parcels.wrap(weekdays));
        args.putInt(MainActivity.ARGUMENT_MEDICATION_PERIOD, medicine.getPeriod());
        args.putString(MainActivity.ARGUMENT_MEDICATION_START_TIME, medicine.getStartTime().getTime());
        ArrayList<Time> times = new ArrayList<>();
        for(Time time : medicine.getTimesToBeTaken())
        {
            times.add(time);
        }
        args.putParcelable(MainActivity.ARGUMENT_MEDICATION_SCHEDULED_TIMES, Parcels.wrap(times));

        EditMedicineFragment fragment = new EditMedicineFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public EditMedicineFragment () {

    }



    @Override
    public void onStart() {

        super.onStart();
        medicationRealm = Realm.getInstance(getContext());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        final View view = inflater.inflate(R.layout.fragment_edit_medicine, container, false);

        Activity activity = getActivity();
        activity.setTitle(R.string.edit_medicine_fragment_label);

        final Bundle args = getArguments();

        eTxtEditMedicineName = (EditText) view.findViewById(R.id.et_edit_medicine_name);
        eTxtEditMedicineDoctor = (EditText) view.findViewById(R.id.et_edit_medicine_doctor);
        eTxtEditMedicineDose = (EditText) view.findViewById(R.id.et_edit_medicine_dose);
        eTxtEditMedicineRefills = (EditText) view.findViewById(R.id.et_edit_medicine_refills);

        eTxtEditMedicineName.setText(args.getString(MainActivity.ARGUMENT_MEDICATION_NAME));
        eTxtEditMedicineDoctor.setText(args.getString(MainActivity.ARGUMENT_MEDICATION_DOCTOR));
        eTxtEditMedicineDose.setText(String.valueOf(args.getInt(MainActivity.ARGUMENT_MEDICATION_DOSE)));
        eTxtEditMedicineRefills.setText(String.valueOf(args.getInt(MainActivity.ARGUMENT_MEDICATION_REFILLS)));

        chkEditMedicineSunday = (CheckBox) view.findViewById(R.id.cb_edit_medicine_sunday);
        chkEditMedicineMonday = (CheckBox) view.findViewById(R.id.cb_edit_medicine_monday);
        chkEditMedicineTuesday = (CheckBox) view.findViewById(R.id.cb_edit_medicine_tuesday);
        chkEditMedicineWednesday = (CheckBox) view.findViewById(R.id.cb_edit_medicine_wednesday);
        chkEditMedicineThursday = (CheckBox) view.findViewById(R.id.cb_edit_medicine_thursday);
        chkEditMedicineFriday = (CheckBox) view.findViewById(R.id.cb_edit_medicine_friday);
        chkEditMedicineSaturday = (CheckBox) view.findViewById(R.id.cb_edit_medicine_saturday);

        ArrayList<Weekday> originalWeekdays = Parcels.unwrap(args.getParcelable(MainActivity.ARGUMENT_MEDICATION_WEEKDAYS));
        for(Weekday day : originalWeekdays)
        {
            switch(day.getDay())
            {
                case MainActivity.SUNDAY:
                    chkEditMedicineSunday.setChecked(true);
                    weekDays.add(day);
                    break;
                case MainActivity.MONDAY:
                    chkEditMedicineMonday.setChecked(true);
                    weekDays.add(day);
                    break;
                case MainActivity.TUESDAY:
                    chkEditMedicineTuesday.setChecked(true);
                    weekDays.add(day);
                    break;
                case MainActivity.WEDNESDAY:
                    chkEditMedicineWednesday.setChecked(true);
                    weekDays.add(day);
                    break;
                case MainActivity.THURSDAY:
                    chkEditMedicineThursday.setChecked(true);
                    weekDays.add(day);
                    break;
                case MainActivity.FRIDAY:
                    chkEditMedicineFriday.setChecked(true);
                    weekDays.add(day);
                    break;
                case MainActivity.SATURDAY:
                    chkEditMedicineSaturday.setChecked(true);
                    weekDays.add(day);
                    break;

            }
        }

        chkEditMedicineSunday.setOnCheckedChangeListener(this);
        chkEditMedicineMonday.setOnCheckedChangeListener(this);
        chkEditMedicineTuesday.setOnCheckedChangeListener(this);
        chkEditMedicineWednesday.setOnCheckedChangeListener(this);
        chkEditMedicineThursday.setOnCheckedChangeListener(this);
        chkEditMedicineFriday.setOnCheckedChangeListener(this);
        chkEditMedicineSaturday.setOnCheckedChangeListener(this);

        rdoEditMedicineMethodPeriodically = (RadioButton) view.findViewById(R.id.rdo_edit_method_periodically);
        rdoEditMedicineMethodScheduled = (RadioButton) view.findViewById(R.id.rdo_edit_method_scheduled);

        rdoGroupEditMedicineMethod = (RadioGroup) view.findViewById(R.id.edit_method_selection_container);
        rdoGroupEditMedicineMethod.setOnCheckedChangeListener(this);

        editMedicineMethodPeriodLayout = (RelativeLayout) view.findViewById(R.id.edit_medicine_method_period_layout);
        editMedicineMethodScheduledLayout = (LinearLayout) view.findViewById(R.id.edit_medicine_method_scheduled_layout);

        eTxtPeriod = (EditText) view.findViewById(R.id.et_edit_medicine_period);
        txtStartTime = (TextView) view.findViewById(R.id.tv_edit_medicine_period_start_time);
        btnEditStartTime = (Button) view.findViewById(R.id.btn_edit_medicine_period_start_time);

        btnAddTimes = (Button) view.findViewById(R.id.btn_edit_add_time);

        if(args.getInt(MainActivity.ARGUMENT_MEDICATION_METHOD) == MainActivity.METHOD_PERIODICAL)
        {
            rdoEditMedicineMethodPeriodically.setChecked(true);
            editMedicineMethodPeriodLayout.setVisibility(View.VISIBLE);
            editMedicineMethodScheduledLayout.setVisibility(View.INVISIBLE);

            eTxtPeriod.setText(String.valueOf(args.getInt(MainActivity.ARGUMENT_MEDICATION_PERIOD)));
            txtStartTime.setText(args.getString(MainActivity.ARGUMENT_MEDICATION_START_TIME));
        }
        else
        {
            rdoEditMedicineMethodScheduled.setChecked(true);
            editMedicineMethodScheduledLayout.setVisibility(View.VISIBLE);
            editMedicineMethodPeriodLayout.setVisibility(View.INVISIBLE);
            ArrayList<Time> times = Parcels.unwrap(args.getParcelable(MainActivity.ARGUMENT_MEDICATION_SCHEDULED_TIMES));
            for(Time time : times)
            {
                TextView newTimeView = new TextView(getContext());
                newTimeView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 10, 0, 0);
                params.gravity = Gravity.CENTER;

                newTimeView.setLayoutParams(params);

                newTimeView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        removeEditedTime((TextView) v);
                        newTimesList.remove(v);
                        return true;
                    }
                });

                newTimeView.setText(time.getTime());
                editMedicineMethodScheduledLayout.addView(newTimeView);
                newTimesList.add(newTimeView);
            }
        }

        btnEditStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayEditTimeDialog(txtStartTime);
            }
        });


        btnAddTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView newTimeView = new TextView(getContext());
                newTimeView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 10, 0, 0);
                params.gravity = Gravity.CENTER;

                newTimeView.setLayoutParams(params);

                newTimeView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        removeEditedTime((TextView) v);
                        newTimesList.remove(v);
                        return true;
                    }
                });

                editMedicineMethodScheduledLayout.addView(newTimeView);

                displayEditTimeDialog(newTimeView);
                newTimesList.add(newTimeView);
            }
        });

        btnEditMedicineSubmit = (Button) view.findViewById(R.id.btn_edit_submit_medicine);
        btnEditMedicineCancel = (Button) view.findViewById(R.id.btn_edit_cancel_medicine);


        btnEditMedicineSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateFields())
                {
                    Medication toUpdate = medicationRealm.where(Medication.class)
                            .equalTo("name", args.getString(MainActivity.ARGUMENT_MEDICATION_NAME))
                            .findFirst();

                    TimeManager.removeAlerts(getContext(), medicationRealm, toUpdate);

                    medicationRealm.beginTransaction();

                    setConstantMedicationRealmFields(toUpdate);
                    if(rdoEditMedicineMethodPeriodically.isChecked())
                    {
                        toUpdate.setMethod(MainActivity.METHOD_PERIODICAL);
                        toUpdate.setPeriod(Integer.parseInt(eTxtPeriod.getText().toString()));
                        Time startTime = medicationRealm.createObject(Time.class);
                        startTime.setTime(txtStartTime.getText().toString());
                        toUpdate.setStartTime(startTime);
                        toUpdate.setTimesToBeTaken(new RealmList<Time>());

                        medicationRealm.commitTransaction();

                        ArrayList<Calendar> alertTimes = TimeManager.getPeriodicalTimes(toUpdate);
                        TimeManager.setUpAlerts(getContext(), medicationRealm, toUpdate, alertTimes);
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
                        toUpdate.setTimesToBeTaken(timesRealmList);
                        toUpdate.setMethod(MainActivity.METHOD_SCHEDULED);
                        Time startTime = medicationRealm.createObject(Time.class);
                        startTime.setTime("");
                        toUpdate.setStartTime(startTime);

                        medicationRealm.commitTransaction();

                        ArrayList<Calendar> alertTimes = TimeManager.getScheduledTimes(toUpdate);
                        TimeManager.setUpAlerts(getContext(), medicationRealm, toUpdate, alertTimes);
                    }

                    Log.d("Realm", "edited medicine");
                    Fragment fragment = null;
                    try
                    {
                        fragment = MedicineListFragment.class.newInstance();
                    }
                    catch(Exception e){}

                    getFragmentManager().popBackStack();
                    getFragmentManager().popBackStack();
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_content_container, fragment)
                            .commit();
                }
                else
                {
                    Snackbar.make(view.findViewById(R.id.scroll_edit_medicine), R.string.add_medicine_error, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        btnEditMedicineCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();
            }
        });
        return view;
    }



    private void removeEditedTime(TextView view) {

        editMedicineMethodScheduledLayout.removeView(view);
    }

    private void setConstantMedicationRealmFields(Medication medication) {

        medication.setName(eTxtEditMedicineName.getText().toString());
        medication.setDoctorName(eTxtEditMedicineDoctor.getText().toString());
        medication.setDose(Integer.parseInt(eTxtEditMedicineDose.getText().toString()));
        medication.setNumRefills(Integer.parseInt(eTxtEditMedicineRefills.getText().toString()));
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


        if(!(validateEditText(eTxtEditMedicineName)
                && validateEditText(eTxtEditMedicineDoctor)
                && validateEditText(eTxtEditMedicineDose)
                && validateEditText(eTxtEditMedicineRefills)
                && weekDays.size() > 0))
            return false;

        if(rdoEditMedicineMethodScheduled.isChecked())
        {
            if(newTimesList.size() == 0)
                return false;
        }
        else if(rdoEditMedicineMethodPeriodically.isChecked())
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

    private void displayEditTimeDialog(final TextView timeView) {

        Calendar currentCal = Calendar.getInstance();
        int hour = currentCal.get(Calendar.HOUR_OF_DAY);
        int minute = currentCal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
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
            case R.id.rdo_edit_method_periodically:

                if(rdoEditMedicineMethodPeriodically.isChecked())
                {
                    if(editMedicineMethodScheduledLayout.getVisibility() == View.VISIBLE)
                    {
                        editMedicineMethodScheduledLayout.setVisibility(View.INVISIBLE);
                    }
                    if(editMedicineMethodPeriodLayout.getVisibility() == View.INVISIBLE)
                    {
                        editMedicineMethodPeriodLayout.setVisibility(View.VISIBLE);
                    }

                }
                break;

            case R.id.rdo_edit_method_scheduled:

                if(rdoEditMedicineMethodScheduled.isChecked())
                {
                    if(editMedicineMethodPeriodLayout.getVisibility() == View.VISIBLE)
                    {
                        editMedicineMethodPeriodLayout.setVisibility(View.INVISIBLE);
                    }
                    if(editMedicineMethodScheduledLayout.getVisibility() == View.INVISIBLE)
                    {
                        editMedicineMethodScheduledLayout.setVisibility(View.VISIBLE);
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
            case R.id.cb_edit_medicine_sunday:

                if(chkEditMedicineSunday.isChecked())
                {
                    day = new Weekday("Sunday", MainActivity.SUNDAY);
                    weekDays.add(day);
                }
                else
                {
                    int removePos = -1;
                    for(int i = 0; i < weekDays.size(); i++)
                    {
                        if(weekDays.get(i).getDay() == MainActivity.SUNDAY)
                        {
                            removePos = i;
                        }
                    }
                    if(removePos != -1)
                    {
                        weekDays.remove(removePos);
                    }
                }
                break;

            case R.id.cb_edit_medicine_monday:

                if(chkEditMedicineMonday.isChecked())
                {
                    day = new Weekday("Monday", MainActivity.MONDAY);
                    weekDays.add(day);
                }
                else
                {
                    int removePos = -1;
                    for(int i = 0; i < weekDays.size(); i++)
                    {
                        if(weekDays.get(i).getDay() == MainActivity.MONDAY)
                        {
                            removePos = i;
                        }
                    }
                    if(removePos != -1)
                    {
                        weekDays.remove(removePos);
                    }
                }
                break;

            case R.id.cb_edit_medicine_tuesday:

                if(chkEditMedicineTuesday.isChecked())
                {
                    day = new Weekday("Tuesday", MainActivity.TUESDAY);
                    weekDays.add(day);
                }
                else
                {
                    int removePos = -1;
                    for(int i = 0; i < weekDays.size(); i++)
                    {
                        if(weekDays.get(i).getDay() == MainActivity.TUESDAY)
                        {
                            removePos = i;
                        }
                    }
                    if(removePos != -1)
                    {
                        weekDays.remove(removePos);
                    }
                }
                break;

            case R.id.cb_edit_medicine_wednesday:

                if(chkEditMedicineWednesday.isChecked())
                {
                    day = new Weekday("Wednesday", MainActivity.WEDNESDAY);
                    weekDays.add(day);
                }
                else
                {
                    int removePos = -1;
                    for(int i = 0; i < weekDays.size(); i++)
                    {
                        if(weekDays.get(i).getDay() == MainActivity.WEDNESDAY)
                        {
                            removePos = i;
                        }
                    }
                    if(removePos != -1)
                    {
                        weekDays.remove(removePos);
                    }
                }
                break;

            case R.id.cb_edit_medicine_thursday:

                if(chkEditMedicineThursday.isChecked())
                {
                    day = new Weekday("Thursday", MainActivity.THURSDAY);
                    weekDays.add(day);
                }
                else
                {
                    int removePos = -1;
                    for(int i = 0; i < weekDays.size(); i++)
                    {
                        if(weekDays.get(i).getDay() == MainActivity.THURSDAY)
                        {
                            removePos = i;
                        }
                    }
                    if(removePos != -1)
                    {
                        weekDays.remove(removePos);
                    }
                }
                break;

            case R.id.cb_edit_medicine_friday:

                if(chkEditMedicineFriday.isChecked())
                {
                    day = new Weekday("Friday", MainActivity.FRIDAY);
                    weekDays.add(day);
                }
                else
                {
                    int removePos = -1;
                    for(int i = 0; i < weekDays.size(); i++)
                    {
                        if(weekDays.get(i).getDay() == MainActivity.FRIDAY)
                        {
                            removePos = i;
                        }
                    }
                    if(removePos != -1)
                    {
                        weekDays.remove(removePos);
                    }
                }
                break;

            case R.id.cb_edit_medicine_saturday:

                if(chkEditMedicineSaturday.isChecked())
                {
                    day = new Weekday("Saturday", MainActivity.SATURDAY);
                    weekDays.add(day);
                }
                else
                {
                    int removePos = -1;
                    for(int i = 0; i < weekDays.size(); i++)
                    {
                        if(weekDays.get(i).getDay() == MainActivity.SATURDAY)
                        {
                            removePos = i;
                        }
                    }
                    if(removePos != -1)
                    {
                        weekDays.remove(removePos);
                    }
                }
                break;
        }
    }
}

