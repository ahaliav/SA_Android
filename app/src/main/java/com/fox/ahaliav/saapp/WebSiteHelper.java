package com.fox.ahaliav.saapp;

/**
 * Created by ahaliav_fox on 25 אוקטובר 2017.
 */

public class WebSiteHelper {

    ICallbackMethod callback;
    IObjCallbackMethod callback2;
    public WebSiteHelper(ICallbackMethod callback){
        this.callback = callback;
    }

    public WebSiteHelper(IObjCallbackMethod callback){
        this.callback2 = callback;
    }

    public void getNewsTitles()
    {
        JsonReader reader = new JsonReader(Constants.getPostsUrl() + "?fields=id,title", this.callback);
        reader.execute();
    }


    public void getNewsDetails(Float id)
    {
        JsonReader reader = new JsonReader(Constants.getPostsUrl() + "/" + Math.round(id), this.callback);
        reader.execute();
    }

    public void getEventsTitles() {

        JsonReader reader = new JsonReader(Constants.getPostsUrl() + "?fields=id,title", this.callback);
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

        JsonReader reader = new JsonReader(url, this.callback);
        reader.execute();
    }

    public void register(String username, String password, String display_name,String phone,String comments) {
        HttpRequestsHelper reader = new HttpRequestsHelper(Constants.getTokenUrl(), Constants.getRegistrationUrl(username, password, display_name, phone, comments), this.callback2);
        reader.execute();
    }

    public void login(String username, String password) {
        HttpLoginHelper reader = new HttpLoginHelper(username, password, this.callback2);
        reader.execute();
    }
}
