package com.tantdyalf.csc415_pharmacyapp_test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by macadamsz1 on 2/13/16.
 */
public class AlertListFragment extends Fragment {

    private Realm medicationRealm;
    private RealmResults<Medication> medicationResults;
    private RecyclerView alertListRecyclerView;



    public static AlertListFragment newInstance() {

        return new AlertListFragment();
    }

    public AlertListFragment() {

    }


    @Override
    public void onResume() {
        super.onResume();

        Context context = getActivity();
        medicationRealm = Realm.getInstance(context);
        medicationResults = medicationRealm.where(Medication.class).findAll();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        medicationRealm.close();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_alerts, container, false);

        Activity activity = getActivity();
        activity.setTitle(R.string.alerts_list_fragment_label);

        alertListRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_alerts);
        alertListRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        alertListRecyclerView.setAdapter(new AlertListAdapter(activity));

        return view;
    }


    class AlertListAdapter extends RecyclerView.Adapter<AlertListViewHolder> {

        private LayoutInflater mLayoutInflater;

        public AlertListAdapter(Context context) {

            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public AlertListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new AlertListViewHolder(mLayoutInflater.inflate(R.layout.alert_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(AlertListViewHolder holder, int position) {

            final String medicationName = medicationResults.get(position).getName();
            final boolean alertsActive = medicationResults.get(position).getAlertsActive();

            holder.setData(medicationName, alertsActive);
        }


        @Override
        public int getItemCount() {
            return medicationResults.size();
        }
    }


    class AlertListViewHolder extends RecyclerView.ViewHolder {

        private TextView txtMedicineName;
        private Switch alertsSwitch;

        public AlertListViewHolder(View itemView) {
            super(itemView);

            txtMedicineName = (TextView) itemView.findViewById(R.id.tv_alert_medicine_name);
            alertsSwitch = (Switch) itemView.findViewById(R.id.switch_alerts_active);
        }

        private void setData(final String medicineName, boolean checked) {

            txtMedicineName.setText(medicineName);
            alertsSwitch.setChecked(checked);

            alertsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(buttonView.getId() == R.id.switch_alerts_active)
                    {
                        Medication toEdit = medicationRealm.where(Medication.class).equalTo("name", medicineName).findFirst();

                        if(alertsSwitch.isChecked())
                        {
                            if(toEdit.getMethod() == MainActivity.METHOD_PERIODICAL)
                            {
                                ArrayList<Calendar> alertTimes = TimeManager.getPeriodicalTimes(toEdit);
                                TimeManager.setUpAlerts(getContext(), medicationRealm, toEdit, alertTimes);
                            }
                            else
                            {
                                ArrayList<Calendar> alertTimes = TimeManager.getScheduledTimes(toEdit);
                                TimeManager.setUpAlerts(getContext(), medicationRealm, toEdit, alertTimes);
                            }
                            medicationRealm.beginTransaction();
                            toEdit.setAlertsActive(true);
                            medicationRealm.commitTransaction();
                        }
                        else
                        {
                            TimeManager.removeAlerts(getContext(), medicationRealm, toEdit);
                            medicationRealm.beginTransaction();
                            toEdit.setAlertsActive(false);
                            medicationRealm.commitTransaction();
                        }
                    }
                }
            });
        }
    }
}
