package com.fox.ahaliav.saapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Ahaliav on 21/12/2017.
 */

public class HttpLoginHelper extends AsyncTask<Object, Void, Object> {
    private String username = "";
    private String password = "";
    private Context context;
    IObjCallbackMethod callback;

    public HttpLoginHelper(String username, String password, IObjCallbackMethod callback, Context context) {
        this.callback = callback;
        this.username = username;
        this.password = password;
        this.context = context;
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private Object checkLogin() throws IOException, JSONException {

        Gson gson = new Gson();

        Type type = new TypeToken<Map<String, String>>() {
        }.getType();

        Object result = null;
        OutputStream out = null;

        String authUrl = "https://www.sa-israel.org/wp-json/jwt-auth/v1/token?username=" + username + "&password=" + password;
        String token = "";
        try {

            URL purl = new URL(authUrl);

            HttpsURLConnection connection = (HttpsURLConnection) purl.openConnection();
            connection.setRequestMethod("POST");
            if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 300) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                token = readAll(rd);
            }

        } catch (Exception ex) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception ex) {

            }
        }

        if (token.length() > 0) {
            checkConfirmation(username, password);
        }

        return token.length() > 0;
    }


    private void checkConfirmation(String username, String password) throws IOException, JSONException {

        InputStream is = null;
        try {
            is = new URL(Constants.getUsersConfirmedUrl()).openStream();
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

            obj = (Object) gson.fromJson(jsonText, Object.class);
            mapPost = (Map<String, Object>) obj;
            mapTitle = (Map<String, Object>) mapPost.get("title");
            String url = (String) mapTitle.get("rendered");
            is = new URL(url).openStream();
            rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            jsonText = readAll(rd);
            if (jsonText.contains(username)) {
                SQLiteDbHelper db = new SQLiteDbHelper(context);
                db.insertUser(username, "true", username, password);
            }

        } catch (Exception ex) {
            String err = ex.getMessage();

        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (Exception ex) {

            }

        }
    }

    public String getToken(String url) throws IOException, JSONException {

        try {
            URL purl = new URL(url);

            HttpsURLConnection connection = (HttpsURLConnection) purl.openConnection();
            connection.setRequestMethod("POST");
            if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 300) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                return readAll(rd);
            }

        } catch (Exception ex) {
            return "";
        }

        return "";
    }

    @Override
    protected Object doInBackground(Object... objects) {
        Object result = null;

        try {
            result = checkLogin();
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
