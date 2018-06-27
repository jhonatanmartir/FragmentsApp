package com.dev.jhonyrg.fragmentsapp.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDate {
    private static final String TAG = "CustomDate";
    private static String dateString = "";
    private static String day = "";
    private static String month = "";
    private static String year = "";
    private static SimpleDateFormat localFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");  //RFC 822 time zone
    private static Date date;
    private static android.icu.text.DateFormat dateFormat;

    public String fromPickerToLocal(String day, String month, int year)
    {
        this.day = day;
        this.month = month;
        this.year = String.valueOf(year);
        dateString = day + "/" + month + "/" + year;
        try {
            this.date = localFormat.parse(dateString);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }

        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
    }

    public String fromDateToLocal(Date date)
    {
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
    }

    public String toServer()
    {
        dateString = day + "/" + month + "/" + year;
        try {
            this.date = localFormat.parse(dateString);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }

        return serverFormat.format(date);
    }

    public String fromDateToServer(Date date)
    {
        return serverFormat.format(date);
    }

    public static String fromServerToLocal(String dateToConvert)
    {
        try {
            date = serverFormat.parse(dateToConvert);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }

        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
    }
}
