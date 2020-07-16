package com.example.mywardrobe;

import android.app.Application;

import com.example.mywardrobe.models.Category;
import com.example.mywardrobe.models.Clothing;
import com.parse.Parse;
import com.parse.ParseObject;

import com.facebook.FacebookSdk;
import com.parse.facebook.ParseFacebookUtils;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Category.class);
        ParseObject.registerSubclass(Clothing.class);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("jinny-wardrobe") // should correspond to APP_ID env variable
                .clientKey("JinnyMoveFast")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://jinny-wardrobe.herokuapp.com/parse/").build());

        ParseFacebookUtils.initialize(this);
    }
}
