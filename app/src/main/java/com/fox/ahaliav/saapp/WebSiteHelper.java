package com.fox.ahaliav.saapp;

import android.content.Context;

/**
 * Created by ahaliav_fox on 25 אוקטובר 2017.
 */

public class WebSiteHelper {

    ICallbackMethod callback;
    IObjCallbackMethod callback2;
    private Context context;

    public WebSiteHelper(ICallbackMethod callback,Context context){
        this.callback = callback;
        this.context = context;
    }

    public WebSiteHelper(IObjCallbackMethod callback,Context context){
        this.callback2 = callback;
        this.context = context;
    }

    public void getNewsTitles()
    {
        JsonReader reader = new JsonReader(Constants.getPostsUrl() + "?categories=30&fields=id,title", this.callback, context, false);
        reader.execute();
    }


    public void getRegisteredUsers()
    {
        JsonReader reader = new JsonReader(Constants.getUsersConfirmedUrl(), this.callback, context, false);
        reader.execute();
    }

    public void getNewsDetails(Float id)
    {
        JsonReader reader = new JsonReader(Constants.getPostsUrl() + "/" + Math.round(id) + "?password=Awsy567FbCdeS", this.callback, context, false);
        reader.execute();
    }

    public void getEventsTitles() {

        JsonReader reader = new JsonReader(Constants.getPostsUrl() + "?categories=31&fields=id,title", this.callback, context, false);
        reader.execute();
    }

    public void getGroups() {

        CsvReader reader = new CsvReader(Constants.getGroupsCsv(), this.callback);
        reader.execute();
    }

    public void getDisKM(double latitude, double longitude,
                         double prelatitute, double prelongitude)
    {
        String url ="http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + latitude + "," + longitude + "&destinations=" + prelatitute + "," + prelongitude + "&mode=driving&language=en-EN&sensor=false";

        JsonReader reader = new JsonReader(url, this.callback, context, false);
        reader.execute();
    }

    public void register(String username, String password, String display_name,String phone,String comments) {
        HttpRequestsHelper reader = new HttpRequestsHelper(Constants.getTokenUrl(), Constants.getRegistrationUrl(username, password, display_name, phone, comments), this.callback2);
        reader.execute();
    }

    public void login(String username, String password) {
        HttpLoginHelper reader = new HttpLoginHelper(username, password, this.callback2, context);
        reader.execute();
    }
}
