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
        Object result = null;
        OutputStream out = null;
        Gson gson = new Gson();
        try {
            URL purl = new URL(Constants.getTokenBaseUrl() + "?username=" + username + "&password=" + password);

            String token = "";
            HttpsURLConnection connection = (HttpsURLConnection) purl.openConnection();
            connection.setRequestMethod("POST");
            if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 300) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                token = readAll(rd);
            }

            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> map = gson.fromJson(token, type);

        } catch (Exception ex) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception ex) {

            }
        }

        return true;
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
