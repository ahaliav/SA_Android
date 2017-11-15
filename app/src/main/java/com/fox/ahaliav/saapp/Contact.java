package com.fox.ahaliav.saapp;

/**
 * Created by ahaliav_fox on 15 נובמבר 2017.
 */

public class Contact {
    public Contact(int id, String name, String phoneNumber, String comments){
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.comments = comments;
    }

    private int id = 0;

    public int getId() {
        return id;
    }


    private String name = "";

    public String getName() {
        return name;
    }


    private String phoneNumber = "";

    public String getPhoneNumber() {
        return phoneNumber;
    }

    private String comments = "";

    public String getComments() {
        return comments;
    }
}
