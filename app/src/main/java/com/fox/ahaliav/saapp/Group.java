package com.fox.ahaliav.saapp;

/**
 * Created by ahaliav_fox on 30 אוקטובר 2017.
 */

public class Group {
    public Group (String day, String fromTime, String toTime, String comment, String location, String lang) {
        this.day = day;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.comment = comment;
        this.location = location;
        this.lang = lang;
    }

    private String day = "";
    public String getDay(){
        return day;
    }

    private String fromTime = "";
    public String getFromTime(){
        return fromTime;
    }

    private String toTime = "";
    public String getToTime(){
        return toTime;
    }

    private String comment = "";
    public String getComment(){
        return comment;
    }

    private String location = "";
    public String getLocation(){
        return location;
    }

    private String lang = "";
    public String getLang(){
        return lang;
    }


}
