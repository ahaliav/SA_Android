package sa.israel.org;

import android.net.Uri;

/**
 * Created by ahaliav_fox on 25 אוקטובר 2017.
 */

public class Constants {
    public static final String IS_LOGEDIN_KEY = "IS_LOGEDIN";
    public static final String IS_REGISTERED_KEY = "IS_REGISTERED";
    public static final String GO_TO_REGISTER_KEY = "GO_TO_REGISTER";
    public static final String REGISTERED_DATE_KEY = "REGISTERED_DATE_KEY";
    public static final String ENABLE_LOCK_APP = "ENABLE_LOCK_APP";
    public static final String PIN_KEY = "PIN_KEY";

    public static String getUsersConfirmedUrl(){
        return "https://sa-israel.org/wp-json/wp/v2/posts/1470/?password=d400b147-b12d-40b6-8593-9110cd06cd5f";
    }

    public static String getPostsUrl(){
        return "https://sa-israel.org/wp-json/wp/v2/posts";
    }
    public static String getEventsUrl(){
        return "https://sa-israel.org/wp-json/wp/v2/posts";
    }

    public static String getGroupsCsv(){
        return "https://www.sa-israel.org/wp-content/uploads/2017/11/wpdt_export.csv";
    }

    public static String getAllUsersUrl(){
        return "https://www.sa-israel.org/wp-json/wp/v2/users";
    }

    public static String getUserDetailsUrl(int id){
        return "https://www.sa-israel.org/wp-json/wp/v2/users/" + id + "?context=edit";
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
