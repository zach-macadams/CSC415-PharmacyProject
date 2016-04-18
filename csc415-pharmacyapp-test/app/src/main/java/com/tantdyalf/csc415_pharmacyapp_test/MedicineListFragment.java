package com.tantdyalf.csc415_pharmacyapp_test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by macadamsz1 on 2/13/16.
 */
public class MedicineListFragment extends Fragment {

    private Realm medicationRealm;
    private RealmResults<Medication> medicationResults;
    private OnMedicationSelectedListener medicationListener;
    private RecyclerView medicineListRecyclerView;


    public static MedicineListFragment newInstance() {

        return new MedicineListFragment();
    }

    public MedicineListFragment() {

    }


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        if(context instanceof OnMedicationSelectedListener)
        {
            medicationListener = (OnMedicationSelectedListener) context;
        }
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

        View view = inflater.inflate(R.layout.fragment_medications, container, false);

        Activity activity = getActivity();
        activity.setTitle(R.string.medicine_list_fragment_label);

        Snackbar.make(view.findViewById(R.id.recycler_medicines), "Add medications", Snackbar.LENGTH_LONG).show();
        
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
                fragmentManager.beginTransaction().replace(R.id.fragment_content_container, fragment).addToBackStack(null).commit();
            }
        });

        medicineListRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_medicines);
        medicineListRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        medicineListRecyclerView.setAdapter(new MedicineListAdapter(activity));

        return view;
    }


    class MedicineListAdapter extends RecyclerView.Adapter<MedicineListViewHolder> {

        private LayoutInflater mLayoutInflater;

        public MedicineListAdapter(Context context) {

            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public MedicineListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new MedicineListViewHolder(mLayoutInflater.inflate(R.layout.medicine_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(MedicineListViewHolder holder, final int position) {

            final String medicationName = medicationResults.get(position).getName();
            final String medicationDoctor = medicationResults.get(position).getDoctorName();
            final int medicationDose = medicationResults.get(position).getDose();
            final int medicationRefills = medicationResults.get(position).getNumRefills();

            RealmList<Weekday> medicationsRealmWeekDays = medicationResults.get(position).getDaysToBeTaken();
            final ArrayList<Weekday> medicationWeekDays = new ArrayList<>();
            for(Weekday weekday : medicationsRealmWeekDays)
            {
                medicationWeekDays.add(weekday);
            }
            final int medicationMethod = medicationResults.get(position).getMethod();

            final int medicationPeriod = medicationResults.get(position).getPeriod();
            final String medicationStartTime = medicationResults.get(position).getStartTime();

            RealmList<Time> medicationRealmScheduledTimes = medicationResults.get(position).getTimesToBeTaken();
            final ArrayList<Time> medicationScheduledTimes = new ArrayList<>();
            for(Time time : medicationRealmScheduledTimes)
            {
                medicationScheduledTimes.add(time);
            }

            holder.setData(medicationName, medicationDoctor);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    medicationListener.onMedicationSelected(medicationName, medicationDoctor, medicationDose,
                            medicationRefills, medicationMethod, medicationWeekDays, medicationPeriod,
                            medicationStartTime, medicationScheduledTimes);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    removeMedicine(position);
                    medicineListRecyclerView.getAdapter().notifyDataSetChanged();
                    return true;
                }
            });
        }

        private void removeMedicine(int position) {

            medicationRealm.beginTransaction();
            medicationResults.remove(position);
            medicationRealm.commitTransaction();
        }


        @Override
        public int getItemCount() {
            return medicationResults.size();
        }
    }

    class MedicineListViewHolder extends RecyclerView.ViewHolder {

        private TextView txtMedicineName;
        private TextView txtDoctorName;

        public MedicineListViewHolder(View itemView) {
            super(itemView);

            txtMedicineName = (TextView) itemView.findViewById(R.id.tv_medicine_name);
            txtDoctorName = (TextView) itemView.findViewById(R.id.tv_doctor_name);
        }

        private void setData(String medicineName, String doctorName) {

            txtMedicineName.setText(medicineName);
            txtDoctorName.setText(doctorName);
        }
    }

    public interface OnMedicationSelectedListener {

        void onMedicationSelected(String medicationName, String doctorName, int dose, int refills, int method,
                                  ArrayList<Weekday> weekdays, int period, String startTime, ArrayList<Time> scheduledTimes);
    }
}
