package com.fox.ahaliav.saapp;

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

    IObjCallbackMethod callback;

    public HttpLoginHelper(String username, String password, IObjCallbackMethod callback) {
        this.callback = callback;
        this.username = username;
        this.password = password;
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

        //1. get all users
        InputStream is = new URL(Constants.getAllUsersUrl()).openStream();
        BufferedReader rdUsers = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String jsonTextUsers = readAll(rdUsers);
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> mapAllUsers = gson.fromJson(jsonTextUsers, type);

        //2. find the user
        int id = 3;

        //4. get token for to check user
        String token = getToken(Constants.getTokenUrl());
        Map<String, String> mapUserDetails = gson.fromJson(token, type);

        //5. get user details by id
        URL purlUserDetails = new URL(Constants.getUserDetailsUrl(id));

        HttpsURLConnection connection = (HttpsURLConnection) purlUserDetails.openConnection();
        connection.setRequestProperty("Authorization", "Bearer " + mapUserDetails.get("token"));
        connection.setRequestMethod("POST");
        int responseCode = connection.getResponseCode();

        Object resultUserdetails = null;
        if (responseCode >= 200 && responseCode < 300)
            resultUserdetails = connection.getResponseMessage();

        //6. check the user role

        //7. if role is not approved show message

        //8. else go on....

        Object result = null;
        OutputStream out = null;

        String authUrl = "https://www.sa-israel.org/wp-json/jwt-auth/v1/token?username=" + username + "&password=" + password;
        token = "";
        try {

            URL purl = new URL(authUrl);

            connection = (HttpsURLConnection) purl.openConnection();
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

        return token.length() > 0;
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
