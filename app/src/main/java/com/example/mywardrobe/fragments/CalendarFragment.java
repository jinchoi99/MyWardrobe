package com.example.mywardrobe.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.mywardrobe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

import static com.parse.Parse.getApplicationContext;

public class CalendarFragment extends Fragment {
    public static final String TAG = "CalendarFragment";
    public static final String CURRENT_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    //api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}
    private TextView tvWeatherDegree;

    private CalendarView cvCalendar;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cvCalendar = (CalendarView) view.findViewById(R.id.cvCalendar);
        cvCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(getApplicationContext(), (month+1) + "/" + dayOfMonth + "/" + year, Toast.LENGTH_SHORT).show();
            }
        });

        tvWeatherDegree = view.findViewById(R.id.tvWeatherDegree);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("appid", getString(R.string.weather_api_key));
        params.put("q", "Los Angeles");
        params.put("units", "metric");

        client.get(CURRENT_WEATHER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject main = jsonObject.getJSONObject("main");
                    double temp = main.getDouble("temp");
                    Log.d(TAG, "temp: " + temp);
                    tvWeatherDegree.setText("The currect temperature is " + Double.toString(temp) + "°C");
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception",e);
                }
            }


            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}