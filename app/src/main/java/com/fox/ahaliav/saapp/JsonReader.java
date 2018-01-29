package com.fox.ahaliav.saapp;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class JsonReader extends AsyncTask<Void, Void, List<Object>> {

    private String url = "";
    ICallbackMethod callback;
    private Context context;
    private boolean requireToken;

    public JsonReader(String url, ICallbackMethod callback, Context context, boolean requireToken) {
        this.url = url;
        this.callback = callback;
        this.context = context;
        this.requireToken = requireToken;
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
        List<Object> list = new ArrayList<Object>();
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();

        try {
            SQLiteDbHelper db = new SQLiteDbHelper(context);

            String token = db.selectToken();
            if (this.requireToken && token.equals("")) {
                Cursor result = db.selectUser("");

                String email = "";
                String password = "";
                if (result != null) {
                    while (result.moveToNext()) {
                        int id = result.getInt(0);
                        email = result.getString(2);
                        password = result.getString(4);
                        break;
                    }
                    if (!result.isClosed()) {
                        result.close();
                    }
                }

                String path = Constants.getTokenBaseUrl() + "username=" + email + "&password=" + password;
                if (!email.equals("")) {
                    token = getToken(path);
                }

                Map<String, String> map = null;
                if (!token.equals("")) {
                    map = gson.fromJson(token, type);
                    token = map.get("token");
                    Date currentTime = Calendar.getInstance().getTime();
                    db.insertToken(token, currentTime);
                }
            }

            URL purl = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) purl.openConnection();

            if (!token.equals("")) {
                connection.setRequestProperty("Authorization", "Bearer " + token);
            }

            connection.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String jsonText = readAll(rd);


            Object obj;

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
            String mess = ex.getMessage();
        }

        return list;
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
    protected List<Object> doInBackground(Void... voids) {
        List<Object> result = null;

        try {
            result = readJsonFromUrl(this.url);
        } catch (IOException ex) {

        } catch (JSONException ex) {
            String exep = ex.getMessage();
        }

        return result;
    }

    @Override
    protected void onPostExecute(List<Object> result) {
        super.onPostExecute(result);

        try {
            callback.onTaskDone(result);
        } catch (Exception ex) {
            String e = ex.getMessage();
        }

    }
}
