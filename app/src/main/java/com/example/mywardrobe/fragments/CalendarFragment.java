package com.example.mywardrobe.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.mywardrobe.R;
import com.example.mywardrobe.activities.MainActivity;
import com.example.mywardrobe.models.Clothing;
import com.example.mywardrobe.models.Outfit;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;

import static com.parse.Parse.getApplicationContext;

public class CalendarFragment extends Fragment {
    public static final String TAG = "CalendarFragment";

    //Weather
    //make sure to use https not http
    public static final String CURRENT_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    //api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}
    private TextView tvWeatherDegree;
    private ImageView ivWeatherIcon;
    String weathericonLink = "http://openweathermap.org/img/wn/";
    //"http://openweathermap.org/img/wn/10d@2x.png";
    private TextView tvWeatherDescription;
    private TextView tvSuggestion;

    //Calendar
    private CompactCalendarView ccvCalendar;
    private SimpleDateFormat dataFormatMonth;
    private TextView tvMonthYear;
    private EditText etOutfitEvent;
    private Button btnAddEvent;

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
        ivWeatherIcon = view.findViewById(R.id.ivWeatherIcon);
        tvWeatherDescription = view.findViewById(R.id.tvWeatherDescription);
        tvSuggestion = view.findViewById(R.id.tvSuggestion);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("appid", getString(R.string.weather_api_key));
//        params.put("q", "Los Angeles");
        params.put("q", SettingsFragment.userLocation);
        params.put("units", "metric");

        client.get(CURRENT_WEATHER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject main = jsonObject.getJSONObject("main");
                    double temp = main.getDouble("temp");
                    tvWeatherDegree.setText(Double.toString(temp) + "°C");
                    if(temp<10){
                        tvSuggestion.setText("Don't forget to bring a jacket when you go out :)!");
                    }
                    else if(temp<20){
                        tvSuggestion.setText("How about a light jacket or a light scarf :)?");
                    }
                    else{
                        tvSuggestion.setText("A light T-shirt should be great:)!");
                    }

                    JSONArray weather = jsonObject.getJSONArray("weather");
                    String icon = weather.getJSONObject(0).getString("icon");
                    Glide.with(getContext()).load(weathericonLink+ icon + "@2x.png").into(ivWeatherIcon);
                    String weatherMain = weather.getJSONObject(0).getString("main");
                    String weatherDescription = weather.getJSONObject(0).getString("description");
                    tvWeatherDescription.setText(weatherDescription);
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
        String monyear = dataFormatMonth.format(Calendar.getInstance().getTime());
        tvMonthYear.setText(monyear);
        etOutfitEvent = view.findViewById(R.id.etOutfitEvent);
        btnAddEvent = view.findViewById(R.id.btnAddEvent);

        Event event1 = new Event(Color.WHITE, 1597993200000L, "Pink T-shirt");
        ccvCalendar.addEvent(event1);
        Event event2 = new Event(Color.WHITE, 1600326000000L, "Denim Jacket");
        ccvCalendar.addEvent(event2);

        ccvCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(final Date dateClicked) {
                List<Event> events = ccvCalendar.getEvents(dateClicked.getTime());
                if(events.size()>0){
                    String edata="What to wear:";
                    for (Event e:events) {
                        edata = edata + "\n - " + e.getData().toString();
                        //Toast.makeText(getContext(), "" + e.getData(), Toast.LENGTH_SHORT).show();
                    }
//                    Toast.makeText(getContext(), edata, Toast.LENGTH_SHORT).show();

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) getView().findViewById(R.id.custom_toast_container));
                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText(edata);
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 100);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }
                btnAddEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        long time = dateClicked.getTime();
                        //Toast.makeText(getContext(), Long.toString(time), Toast.LENGTH_SHORT).show();
                        String eventName = etOutfitEvent.getText().toString();
                        if(eventName.isEmpty()){
                            return;
                        }
                        Event event = new Event(Color.WHITE, time, eventName);
                        ccvCalendar.addEvent(event);
                        Toast.makeText(getContext(), "New Outfit has been added!", Toast.LENGTH_SHORT).show();
                        etOutfitEvent.setText("");
                    }
                });
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                tvMonthYear.setText(dataFormatMonth.format(firstDayOfNewMonth));
            }
        });
    }
}