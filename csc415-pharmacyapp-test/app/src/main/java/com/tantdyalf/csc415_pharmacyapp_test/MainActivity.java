package com.tantdyalf.csc415_pharmacyapp_test;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        MedicineListFragment.OnMedicationSelectedListener {

    private Realm medicationRealm;

    public static final int SUNDAY = 0, MONDAY = 1, TUESDAY = 2, WEDNESDAY = 3, THURSDAY = 4,
            FRIDAY = 5, SATURDAY = 6;

    public static final int METHOD_SCHEDULED = 0, METHOD_PERIODICAL = 1;

    public static final String ARGUMENT_MEDICATION_NAME = "medicationName";
    public static final String ARGUMENT_MEDICATION_DOCTOR = "medicationDoctor";
    public static final String ARGUMENT_MEDICATION_DOSE = "medicationDose";
    public static final String ARGUMENT_MEDICATION_REFILLS = "medicationRefills";
    public static final String ARGUMENT_MEDICATION_WEEKDAYS = "medicationWeekdays";
    public static final String ARGUMENT_MEDICATION_METHOD = "medicationMethod";
    public static final String ARGUMENT_MEDICATION_PERIOD = "medicationPeriod";
    public static final String ARGUMENT_MEDICATION_START_TIME = "medicationStartTime";
    public static final String ARGUMENT_MEDICATION_SCHEDULED_TIMES = "medicationScheduledTimes";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        medicationRealm = Realm.getInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = null;
        try
        {
            fragment = MedicineListFragment.class.newInstance();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_content_container, fragment).commit();
        setTitle(R.string.medicine_list_fragment_label);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass;

        switch(id)
        {
            case R.id.nav_medicine_list:
                fragmentClass = MedicineListFragment.class;
                break;

            case R.id.nav_refills:
                fragmentClass = RefillsFragment.class;
                break;

            case R.id.nav_alert_list:
                fragmentClass = AlertListFragment.class;
                break;

            default:
                fragmentClass = MedicineListFragment.class;
                break;
        }

        try
        {
            fragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_content_container, fragment).commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        medicationRealm.close();
    }

    @Override
    public void onMedicationSelected(String medicationName, String doctorName, int dose,
                                     int refills, int method, ArrayList<Weekday> weekdays,
                                     int period, String startTime, ArrayList<Time> scheduledTimes) {

        ViewMedicineFragment fragment = ViewMedicineFragment.newInstance(medicationName, doctorName, dose,
                refills, method, weekdays, period, startTime, scheduledTimes);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_content_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
