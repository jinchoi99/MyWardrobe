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
import com.example.mywardrobe.activities.MainActivity;
import com.example.mywardrobe.models.Clothing;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Headers;

import static com.parse.Parse.getApplicationContext;

public class CalendarFragment extends Fragment {
    public static final String TAG = "CalendarFragment";

    //Weather
    public static final String CURRENT_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    //api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}
    private TextView tvWeatherDegree;

    //Calendar
    private CompactCalendarView ccvCalendar;
    private SimpleDateFormat dataFormatMonth;
    private TextView tvMonthYear;
    private TextView tvOutfitInfo;

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

        //Weather and temperature
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

        //Calendar
        ccvCalendar = view.findViewById(R.id.ccvCalendar);
        tvMonthYear = view.findViewById(R.id.tvMonthYear);
        dataFormatMonth = new SimpleDateFormat("MMM-YYYY", Locale.getDefault());
        tvOutfitInfo = view.findViewById(R.id.tvOutfitInfo);
        tvOutfitInfo.setVisibility(View.INVISIBLE);

        final Clothing exampleClothing = new Clothing();
        exampleClothing.setClothingName("exampleClo1");
        final Event ev1 = new Event(Color.WHITE, 1595833200000L, exampleClothing);
        ccvCalendar.addEvent(ev1);

        ccvCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                long time = dateClicked.getTime();
                //Toast.makeText(getContext(), Long.toString(time), Toast.LENGTH_SHORT).show();
                if(time==1595833200000L){
                    Clothing eventclothing = (Clothing) ev1.getData();
                    //Toast.makeText(getContext(), eventclothing.getClothingName(), Toast.LENGTH_SHORT).show();
                    tvOutfitInfo.setVisibility(View.VISIBLE);
                    tvOutfitInfo.setText(eventclothing.getClothingName());
                }
                else {
                    tvOutfitInfo.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                tvMonthYear.setText(dataFormatMonth.format(firstDayOfNewMonth));
            }
        });


    }
}