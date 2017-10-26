package com.fox.ahaliav.saapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ahaliav_fox on 26 אוקטובר 2017.
 */

public class Subriety {

    public Subriety(int id, String name, String date) {
        this.date = date;
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
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = null;
        String days = "";
        try {
            startDate = df.parse(date);
            Date currentTime = Calendar.getInstance().getTime();
            long mills = startDate.getTime() - currentTime.getTime();
            days = String.valueOf(mills/(24 * 60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
