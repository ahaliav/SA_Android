package com.fox.ahaliav.saapp;

/**
 * Created by ahaliav_fox on 15 נובמבר 2017.
 */

public class Contact {
    public Contact(int id, String name, String phoneNumber, String comments, String email){
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.comments = comments;
        this.email = email;
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

    private String email = "";

    public String getEmail() {
        return email;
    }
}
