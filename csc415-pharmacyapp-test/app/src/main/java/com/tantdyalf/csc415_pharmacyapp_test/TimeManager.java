package com.tantdyalf.csc415_pharmacyapp_test;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

import io.realm.Realm;


public class TimeManager {



    public static void setUpAlerts(Context context, Realm medicationRealm, Medication medicine, ArrayList<Calendar> alertTimes) {

        Medication toUpdate = medicationRealm.where(Medication.class)
                .equalTo("name", medicine.getName())
                .findFirst();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for(int i = 0; i < alertTimes.size(); i++)
        {
            int requestCode = new Random().nextInt();
            medicationRealm.beginTransaction();
            PendingIntentRequestCode code = medicationRealm.createObject(PendingIntentRequestCode.class);
            code.setRequestCode(requestCode);
            toUpdate.getResultCodes().add(code);
            medicationRealm.commitTransaction();

            Intent alert = new Intent(context, AlertReceiver.class);
            alert.putExtra("medicineName", medicine.getName());
            PendingIntent recurringAlert = PendingIntent.getBroadcast(context, requestCode, alert, PendingIntent.FLAG_CANCEL_CURRENT);

            Log.d("Time", "System " + String.valueOf(System.currentTimeMillis()));
            Log.d("Time", "Given " + String.valueOf(alertTimes.get(i).getTimeInMillis()));
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    alertTimes.get(i).getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY * 7,
                    recurringAlert);
        }
    }


    public static void removeAlerts(Context context, Realm medicationRealm, Medication medicine) {

        Medication toUpdate = medicationRealm.where(Medication.class)
                .equalTo("name", medicine.getName())
                .findFirst();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for(int i = 0; i < medicine.getResultCodes().size(); i++)
        {
            Intent alert = new Intent(context, AlertReceiver.class);
            PendingIntent recurringAlert = PendingIntent.getBroadcast(context, medicine.getResultCodes().get(i).getRequestCode(), alert, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(recurringAlert);
        }

        Log.d("Notifications", "Notifications for " + medicine.getName() + " removed");
        medicationRealm.beginTransaction();
        toUpdate.getResultCodes().clear();
        medicationRealm.commitTransaction();
    }


    public static ArrayList<Calendar> getScheduledTimes(Medication medicine) {

        ArrayList<Calendar> alertTimes = new ArrayList<>();

        for(int i = 0; i < medicine.getDaysToBeTaken().size(); i++)
        {
            for(int j = 0; j < medicine.getTimesToBeTaken().size(); j++)
            {
                Calendar newTime = generateUpdateTime(getHourMinute(medicine.getTimesToBeTaken().get(j)), medicine.getDaysToBeTaken().get(i));
                alertTimes.add(newTime);
            }
        }
        return alertTimes;
    }


    public static ArrayList<Calendar> getPeriodicalTimes(Medication medicine) {

        ArrayList<Calendar> alertTimes = new ArrayList<>();

        for(int i = 0; i < medicine.getDaysToBeTaken().size(); i++)
        {
            alertTimes.add(generateUpdateTime(getHourMinute(medicine.getStartTime()), medicine.getDaysToBeTaken().get(i)));
        }
        int hour = alertTimes.get(0).get(Calendar.HOUR_OF_DAY) + medicine.getPeriod();
        int minute = alertTimes.get(0).get(Calendar.MINUTE);

        for(int i = 0; i < medicine.getDaysToBeTaken().size(); i++)
        {
            while(hour < 24)
            {
                alertTimes.add(generateUpdateTime(new int[]{hour, minute}, medicine.getDaysToBeTaken().get(i)));
                hour += medicine.getPeriod();
            }
            hour = alertTimes.get(0).get(Calendar.HOUR_OF_DAY) + medicine.getPeriod();
        }

        return alertTimes;
    }


    public static Calendar generateUpdateTime(int[] hourMinute, Weekday day) {

        Calendar updateTime = Calendar.getInstance();
        TimeZone tz = Calendar.getInstance().getTimeZone();
        updateTime.setTimeZone(tz);
        updateTime.set(Calendar.HOUR_OF_DAY, hourMinute[0]);
        updateTime.set(Calendar.MINUTE, hourMinute[1]);

        int weekDay = day.getDay();
        int updateDay;
        switch(weekDay)
        {
            case MainActivity.SUNDAY:
                updateDay = Calendar.SUNDAY;
                break;
            case MainActivity.MONDAY:
                updateDay = Calendar.MONDAY;
                break;
            case MainActivity.TUESDAY:
                updateDay = Calendar.TUESDAY;
                break;
            case MainActivity.WEDNESDAY:
                updateDay = Calendar.WEDNESDAY;
                break;
            case MainActivity.THURSDAY:
                updateDay = Calendar.THURSDAY;
                break;
            case MainActivity.FRIDAY:
                updateDay = Calendar.FRIDAY;
                break;
            default:
                updateDay = Calendar.SATURDAY;
                break;
        }
        updateTime.set(Calendar.DAY_OF_WEEK, updateDay);

        return updateTime;
    }


    public static int[] getHourMinute(Time time) {

        String[] timeArr = time.getTime().split(":");
        String[] minuteArr = timeArr[1].split(" ");
        int minute = Integer.parseInt(minuteArr[0]);
        int hour = Integer.parseInt(timeArr[0]);

        if(minuteArr[1].equals("PM"))
        {
            hour = hour + 12;
        }
        int[] hourMinute = new int[2];
        hourMinute[0] = hour;
        hourMinute[1] = minute;

        return hourMinute;
    }
}
