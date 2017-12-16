package com.fox.ahaliav.saapp;

/**
 * Created by ahaliav_fox on 25 אוקטובר 2017.
 */

public class Constants {
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

    public static String getRegistrationUrl(String username, String password, String display_name, String phone,String comments){
        return "https://www.sa-israel.org/wp-json/wp/v2/users?username=" + username + "&email=" + username + "&display_name=" + display_name + "&password=" + password + "&roles=pending&description=" + comments + ", Tel: " + phone;
    }

}
