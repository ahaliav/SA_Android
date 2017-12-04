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

    public static String getNonceUrl(){
        return "http://sa-israel.org/api/get_nonce/?controller=user&method=register";
    }

    public static String getRegistrationUrl(String nonce, String username, String password, String display_name, String phone,String comments){
        String pass = "";
        if(password != null && password.length() > 0)
            pass = "&user_pass=" + password;

        return "http://sa-israel.org/api/user/register/?username=" + username + "&email=" + username + "&nonce=" + nonce + "&display_name=" + display_name + "&notify=both" + pass + "&comment_shortcuts=" + phone + "&description=" + comments;
    }

}
