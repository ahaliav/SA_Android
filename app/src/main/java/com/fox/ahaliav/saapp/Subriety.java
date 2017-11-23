package com.fox.ahaliav.saapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ahaliav_fox on 26 אוקטובר 2017.
 */

public class Subriety {

    public Subriety(int id, String name, String date) {
        this.name = name;
        this.id = id;
        this.date = date;
    }

    private int id = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String date = null;

    public String getDate() {
        return date;
    }

    public String getDays() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        String days = "";
        try {
            startDate = df.parse(date);
            Date currentTime = Calendar.getInstance().getTime();
            long mills = currentTime.getTime() - startDate.getTime();
            days = String.valueOf(mills / (24 * 60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public String getMorDetails() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date currentTime = Calendar.getInstance().getTime();
        try {
            startDate = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return getDateDifferenceInDDMMYYYY(startDate, currentTime);
    }

    public String getDateDifferenceInDDMMYYYY(Date from, Date to) {
        Calendar fromDate = Calendar.getInstance();
        Calendar toDate = Calendar.getInstance();
        fromDate.setTime(from);
        toDate.setTime(to);
        int increment = 0;
        int year, month, day;
        System.out.println(fromDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        if (fromDate.get(Calendar.DAY_OF_MONTH) > toDate.get(Calendar.DAY_OF_MONTH)) {
            increment = fromDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        // DAY CALCULATION
        if (increment != 0) {
            day = (toDate.get(Calendar.DAY_OF_MONTH) + increment) - fromDate.get(Calendar.DAY_OF_MONTH);
            increment = 1;
        } else {
            day = toDate.get(Calendar.DAY_OF_MONTH) - fromDate.get(Calendar.DAY_OF_MONTH);
        }

        // MONTH CALCULATION
        if ((fromDate.get(Calendar.MONTH) + increment) > toDate.get(Calendar.MONTH)) {
            month = (toDate.get(Calendar.MONTH) + 12) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 1;
        } else {
            month = (toDate.get(Calendar.MONTH)) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 0;
        }

        // YEAR CALCULATION
        year = toDate.get(Calendar.YEAR) - (fromDate.get(Calendar.YEAR) + increment);
        if (Locale.getDefault().getLanguage().equals("iw"))
            return year + "\tשנ'\t" + month + "\tחוד'\t" + day + "\tימ'";
        else
            return year + "\tYrs.\t" + month + "\tMon.\t" + day + "\tDys.";

    }
}
