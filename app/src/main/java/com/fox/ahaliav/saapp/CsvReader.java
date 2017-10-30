package com.fox.ahaliav.saapp;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahaliav_fox on 30 אוקטובר 2017.
 */

public class CsvReader extends AsyncTask<Void, Void, List<Object>> {

    private String url = "";
    ICallbackMethod callback;
    public CsvReader(String url, ICallbackMethod callback){
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

    private List<Object> readCsvFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("Windows-1255")));

            List<Object> list = new ArrayList<Object>();

            Group g = null;
            String line = "";
            while ((line = rd.readLine()) != null) {
                String[] RowData = line.split(",");
                String day = "";
                String fromtime = "";
                String tomtime = "";
                String location = "";
                String comment = "";
                String lang = "";
                if(RowData.length > 0)
                    day = RowData[0];
                if(RowData.length > 1)
                    location = RowData[1];
                if(RowData.length > 2)
                    fromtime = RowData[2];
                if(RowData.length > 3)
                    tomtime = RowData[3];
                if(RowData.length > 4)
                    lang = RowData[4];
                if(RowData.length > 5)
                    comment = RowData[5];
                g = new Group(day, fromtime,tomtime,comment,location,lang);
                list.add(g);
            }

            return list;

        }
        catch (Exception ex){
            String err = ex.getMessage();
            return null;
        }
        finally {
            is.close();
        }
    }


    @Override
    protected List<Object> doInBackground(Void... voids) {
        List<Object> result = null;

        try
        {
            result = readCsvFromUrl(this.url);
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
