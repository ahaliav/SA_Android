package com.fox.ahaliav.saapp;

import android.os.AsyncTask;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ahaliav on 11/12/2017.
 */

public class HttpRequestsHelper extends AsyncTask<Object, Void, Object> {

    private String url = "";
    private String auth_url = "";

    IObjCallbackMethod callback;

    public HttpRequestsHelper(String auth_url, String url, IObjCallbackMethod callback) {
        this.callback = callback;
        this.auth_url = auth_url;
        this.url = url;
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private Object readJsonFromUrl(String url) throws IOException, JSONException {
        List<Object> list = new ArrayList<Object>();
        InputStream is = null;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);


            Object obj;

            Gson gson;
            ListView postList;
            Map<String, Object> mapPost;
            Map<String, Object> mapTitle;
            int postID;
            String postTitle[];

            gson = new Gson();
            try {
                list = (List) gson.fromJson(jsonText, List.class);
            } catch (Exception ex) {
                obj = (Object) gson.fromJson(jsonText, Object.class);
                list.add(obj);
            }

            postTitle = new String[list.size()];

            for (int i = 0; i < list.size(); ++i) {
                mapPost = (Map<String, Object>) list.get(i);
                mapTitle = (Map<String, Object>) mapPost.get("title");
                postTitle[i] = (String) mapTitle.get("rendered");
            }

            return list;

        } catch (Exception ex) {

        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (Exception ex) {

            }

        }

        return list;
    }

    private Object postJson(String auth_url, String url, String data) throws IOException, JSONException {
        Object result = null;
        OutputStream out = null;
        try {
            URL purl = new URL(url);
            String token = getToken(auth_url);
            HttpURLConnection connection = (HttpURLConnection) purl.openConnection();
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestMethod("POST");
            int responseCode = connection.getResponseCode();

            if (responseCode >= 200 && responseCode < 300)
                result = connection.getResponseMessage();

        } catch (Exception ex) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception ex) {

            }
        }

        return result;
    }

    private String getToken(String url) throws IOException, JSONException {
        Object result;
        OutputStream out = null;
        try {
            URL purl = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) purl.openConnection();

            result = connection.getResponseMessage();
        } catch (Exception ex) {
            return "";
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception ex) {

            }
        }
        return result.toString();
    }

    //
    @Override
    protected Object doInBackground(Object... objects) {
        Object result = null;

        try {
            result = postJson(this.auth_url, this.url, objects[0].toString());
        } catch (IOException ex) {

        } catch (JSONException ex) {
            String exep = ex.getMessage();
        }

        return result;
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);

        try {
            callback.onTaskDone(result);
        } catch (Exception ex) {
            String e = ex.getMessage();
        }

    }


}
