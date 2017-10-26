package com.fox.ahaliav.saapp;

import android.os.AsyncTask;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class JsonReader extends AsyncTask<Void, Void, List<Object>> {

    private String url = "";
    ICallbackMethod callback;
    public JsonReader(String url, ICallbackMethod callback){
        this.url = url;
        this.callback = callback;
    }
    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private List<Object> readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);


            List<Object> list;
            Gson gson;
            ListView postList;
            Map<String,Object> mapPost;
            Map<String,Object> mapTitle;
            int postID;
            String postTitle[];

            gson = new Gson();
            list = (List) gson.fromJson(jsonText, List.class);
            postTitle = new String[list.size()];

            for(int i=0;i<list.size();++i){
                mapPost = (Map<String,Object>)list.get(i);
                mapTitle = (Map<String, Object>) mapPost.get("title");
                postTitle[i] = (String) mapTitle.get("rendered");
            }

            return list;

        } finally {
            is.close();
        }
    }


    @Override
    protected List<Object> doInBackground(Void... voids) {
        List<Object> result = null;

        try
        {
            result = readJsonFromUrl(this.url);
        }
        catch (IOException ex){

        }
        catch (JSONException ex){
            String exep = ex.getMessage();
        }

        return result;
    }

    @Override
    protected void onPostExecute(List<Object> result) {
        super.onPostExecute(result);

        callback.onTaskDone(result);
    }
}
