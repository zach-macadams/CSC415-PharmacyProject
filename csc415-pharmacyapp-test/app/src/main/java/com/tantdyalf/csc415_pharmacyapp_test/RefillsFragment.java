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

import io.realm.Realm;
import io.realm.RealmResults;

public class RefillsFragment extends Fragment {

    private Realm medicationRealm;
    private RealmResults<Medication> medicationResults;
    private RecyclerView refillsListRecyclerView;

    public static RefillsFragment newInstance() {

        return new RefillsFragment();
    }

    public RefillsFragment() {

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

        View view = inflater.inflate(R.layout.fragment_refills, container, false);

        Activity activity = getActivity();
        activity.setTitle(R.string.medicine_refills_text);

        refillsListRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_refills);
        refillsListRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        refillsListRecyclerView.setAdapter(new RefillsListAdapter(activity));

        return view;
    }
    class RefillsListAdapter extends RecyclerView.Adapter<RefillsListViewHolder> {

        private LayoutInflater mLayoutInflater;

        public RefillsListAdapter(Context context) {

            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public RefillsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new RefillsListViewHolder(mLayoutInflater.inflate(R.layout.refills_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RefillsListViewHolder holder, int position) {

            final String medicationName = medicationResults.get(position).getName();
            holder.setData(medicationName);
        }


        @Override
        public int getItemCount() {
            return medicationResults.size();
        }
    }


    class RefillsListViewHolder extends RecyclerView.ViewHolder {

        private TextView txtMedicineName;

        public RefillsListViewHolder(View itemView) {
            super(itemView);

            txtMedicineName = (TextView) itemView.findViewById(R.id.tv_alert_medicine_name);
        }

        private void setData(final String medicineName) {

            txtMedicineName.setText(medicineName);

        }
    }
}
