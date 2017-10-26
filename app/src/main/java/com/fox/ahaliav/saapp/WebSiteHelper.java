package com.fox.ahaliav.saapp;

/**
 * Created by ahaliav_fox on 25 אוקטובר 2017.
 */

public class WebSiteHelper {

    ICallbackMethod callback;
    public WebSiteHelper(ICallbackMethod callback){
        this.callback = callback;
    }

    public void getNewsTitles()
    {
        JsonReader reader = new JsonReader(Constants.getPostsUrl() + "?fields=id,title", this.callback);
        reader.execute();
    }

    public void getEventsTitles() {

        JsonReader reader = new JsonReader(Constants.getPostsUrl() + "?fields=id,title", this.callback);
        reader.execute();
    }
}
