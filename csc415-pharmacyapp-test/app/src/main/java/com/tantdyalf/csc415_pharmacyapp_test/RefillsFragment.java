package com.tantdyalf.csc415_pharmacyapp_test;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

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
            final int numRefills = medicationResults.get(position).getNumRefills();

            holder.setData(medicationName, numRefills);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setView(getView(R.layout.refills_dialog_layout, medicationName, numRefills))
                           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {

                                   refillsListRecyclerView.getAdapter().notifyDataSetChanged();
                               }
                           })
                           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {

                               }
                           }).show();
                }
            });
        }

        public View getView(int layoutId, final String medicineName, final int numRefills) {

            View view = getActivity().getLayoutInflater().inflate(layoutId, null);

            TextView txtMedicineName = (TextView) view.findViewById(R.id.txt_medicine_name);
            Button btnDecreaseRefills = (Button) view.findViewById(R.id.btn_decrease_refills);
            Button btnIncreaseRefills = (Button) view.findViewById(R.id.btn_increase_refills);
            final TextView txtNumRefills = (TextView) view.findViewById(R.id.txt_refills);

            txtMedicineName.setText(medicineName);
            txtNumRefills.setText(String.valueOf(numRefills));

            btnDecreaseRefills.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Medication toUpdate = medicationRealm.where(Medication.class)
                            .equalTo("name", medicineName)
                            .findFirst();

                    medicationRealm.beginTransaction();
                    toUpdate.setNumRefills(toUpdate.getNumRefills() - 1);
                    medicationRealm.commitTransaction();

                    txtNumRefills.setText(String.valueOf(toUpdate.getNumRefills()));
                }
            });

            btnIncreaseRefills.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Medication toUpdate = medicationRealm.where(Medication.class)
                            .equalTo("name", medicineName)
                            .findFirst();

                    medicationRealm.beginTransaction();
                    toUpdate.setNumRefills(toUpdate.getNumRefills() + 1);
                    medicationRealm.commitTransaction();

                    txtNumRefills.setText(String.valueOf(toUpdate.getNumRefills()));
                }
            });

            return view;
        }


        @Override
        public int getItemCount() {
            return medicationResults.size();
        }
    }


    class RefillsListViewHolder extends RecyclerView.ViewHolder {

        private TextView txtMedicineName;
        private TextView txtRefills;

        public RefillsListViewHolder(View itemView) {
            super(itemView);

            txtMedicineName = (TextView) itemView.findViewById(R.id.tv_refills_medicine_name);
            txtRefills = (TextView) itemView.findViewById(R.id.tv_refills_number);
        }

        private void setData(String medicineName, int numRefills) {

            txtMedicineName.setText(medicineName);
            txtRefills.setText(String.valueOf(numRefills));
        }
    }
}
