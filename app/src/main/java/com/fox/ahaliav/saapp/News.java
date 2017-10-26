package com.fox.ahaliav.saapp;

import java.util.Date;

public class News {
    public News(String content, String title, Date date) {
        this.content = content;
        this.title = title;
    }

    private String content = "";
    public String getContent(){
        return content;
    }

    private String title = "";
    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    private Date date = null;
    public Date getDate(){
        return date;
    }
}