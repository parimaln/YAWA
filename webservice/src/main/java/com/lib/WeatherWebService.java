package com.lib;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Parimal on 5/16/2015.
 */
public class WeatherWebService  extends AsyncTask<Object, Boolean, String> {
    private static String API_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&units=metric&APPID=2d407ea4ef6a31a9561056d12aeb2b05&cnt=16";
    boolean _isFinished  = false;
    String _result = null;
    WeatherUpdateListener mListener;
    public static interface WeatherUpdateListener {
        void weatherUpdateJson(String response) throws JSONException;
    }

    public void setListener(WeatherUpdateListener mListener){
        this.mListener = mListener;
    }

    @Override
    protected String doInBackground(Object... params) {
        String locationParam = (String) params[0];
        HttpURLConnection con = null ;
        InputStream is = null;

        try {
            con = (HttpURLConnection) ( new URL(API_URL + "&"+locationParam)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line);

            is.close();
            con.disconnect();
            return buffer.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s!=null){
            try {
                mListener.weatherUpdateJson(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
