package app.parimal.yawa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.lib.WeatherDataModel;
import com.lib.WeatherJsonDataParser;
import com.lib.WeatherWebService;
import org.json.JSONException;
import java.util.ArrayList;


public class MainActivity extends Activity implements WeatherWebService.WeatherUpdateListener {
    ArrayList<WeatherDataModel> weatherDataList;
    RecyclerView recyclerView;
    int PLACE_PICKER_REQUEST = 1;
    WeatherWebService weatherService;
    //UI
    TextView tempView, cityView, conditionView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.design_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tempView = (TextView) findViewById(R.id.tempTextView);
        cityView  = (TextView) findViewById(R.id.cityTextView);
        conditionView  = (TextView) findViewById(R.id.conditionTextView);
        cityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                Context context = getApplicationContext();
                try {
                    startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void updateUI(int day) {
        if(weatherDataList.size()>0) {
            cityView.setText(weatherDataList.get(day).getCity());
            int temp = (int) weatherDataList.get(day).temperature.getTemp();
            tempView.setText(Integer.toString(temp));
            conditionView.setText("Condition: "+weatherDataList.get(day).getCondition());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("first_time", false))
        {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("first_time", true);
            editor.commit();
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            Context context = getApplicationContext();
            try {
                startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
        else
        {
            weatherService = new WeatherWebService();
            weatherService.setListener(this);
            weatherService.execute(prefs.getString("location","lat=12.98&lon=77.67"));
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                LatLng latlng = place.getLatLng();
                String location = "lat="+latlng.latitude+"&"+"lon="+latlng.longitude;
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("location", location);
                editor.apply();
                weatherService = new WeatherWebService();
                weatherService.setListener(this);
                weatherService.execute(location);
            }
        }
    }

    @Override
    public void weatherUpdateJson(String response) throws JSONException {
        weatherDataList = WeatherJsonDataParser.getWeatherAsList(response);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        RowAdapter mAdapter = new RowAdapter(weatherDataList, MainActivity.this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        updateUI(0);
    }
}
