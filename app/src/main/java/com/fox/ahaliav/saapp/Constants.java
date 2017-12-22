package com.fox.ahaliav.saapp;

import android.net.Uri;

/**
 * Created by ahaliav_fox on 25 אוקטובר 2017.
 */

public class Constants {
    public static final String IS_LOGEDIN_KEY = "IS_LOGEDIN";
    public static final String IS_REGISTERED_KEY = "IS_REGISTERED";
    public static final String GO_TO_REGISTER_KEY = "GO_TO_REGISTER";

    public static String getPostsUrl(){
        return "http://sa-israel.org/wp-json/wp/v2/posts";
    }
    public static String getEventsUrl(){
        return "http://sa-israel.org/wp-json/wp/v2/posts";
    }

    public static String getGroupsCsv(){
        return "http://www.sa-israel.org/wp-content/uploads/2017/11/wpdt_export.csv";
    }

    public static String getTokenUrl(){
        return "https://www.sa-israel.org/wp-json/jwt-auth/v1/token?username=user_creator&password=b(emMih0ySScy6oOGcey4RHI";
    }

    public static String getTokenBaseUrl(){

        return "https://www.sa-israel.org/wp-json/jwt-auth/v1/token?";
    }

    public static String getRegistrationUrl(String username, String password, String display_name, String phone,String comments){
        return "https://www.sa-israel.org/wp-json/wp/v2/users?username=" + username + "&email=" + username + "&first_name=" + Uri.encode(display_name) + "&password=" + Uri.encode(password) + "&roles=pending&description=" + Uri.encode(comments) + Uri.encode(", Tel:" + phone);
    }
}
