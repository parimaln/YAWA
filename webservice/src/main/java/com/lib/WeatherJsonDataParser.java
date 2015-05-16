package com.lib;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Parimal on 5/16/2015.
 */
public class WeatherJsonDataParser {

    public static ArrayList<WeatherDataModel> getWeatherAsList(String data) throws JSONException {
        //Log.d("YAWA", data);
        ArrayList<WeatherDataModel> toReturnArray = new ArrayList<>();
        JSONObject rootObj = new JSONObject(data);
        JSONObject cityObj = rootObj.getJSONObject("city");
        String cityName = cityObj.getString("name")+", "+cityObj.getString("country");

        int daysCount = rootObj.getInt("cnt");

        JSONArray weatherList = rootObj.getJSONArray("list");
        for(int i=0; i<weatherList.length(); i++){
            WeatherDataModel tempModel = new WeatherDataModel();
            tempModel.setCity(cityName);
            JSONObject tempObj = weatherList.getJSONObject(i);
            JSONObject temperature = tempObj.getJSONObject("temp");
            Float dayTemp = (float) temperature.getDouble("day");
            Float minTemp = (float) temperature.getDouble("min");
            Float maxTemp = (float) temperature.getDouble("max");
            tempModel.temperature.setMaxTemp(maxTemp);
            tempModel.temperature.setMinTemp(maxTemp);
            tempModel.temperature.setTemp(dayTemp);

            JSONArray weatherArray = tempObj.getJSONArray("weather");
            JSONObject weatherObj = weatherArray.getJSONObject(0);
            String condition = weatherObj.getString("description");
            tempModel.setCondition(condition);
            Log.d("YAWA", cityName+" "+Integer.toString(daysCount)+" "+condition);
            toReturnArray.add(tempModel);
        }
        return toReturnArray;
    }
}
