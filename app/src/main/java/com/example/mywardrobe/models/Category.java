package com.example.mywardrobe.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Category")
public class Category extends ParseObject {
    public static final String KEY_CATEGORY_NAME = "categoryName";
    public static final String KEY_CAT_CREATED_KEY = "createdAt";
    public static final String KEY_CATEGORY_OWNER = "categoryOwner";

    public String getCategoryName(){
        return getString(KEY_CATEGORY_NAME);
    }
    public void setCategoryName(String categoryName){
        put(KEY_CATEGORY_NAME, categoryName);
    }

    public ParseUser getCategoryOwner(){
        return getParseUser(KEY_CATEGORY_OWNER);
    }
    public void setCategoryOwner(ParseUser user){
        put(KEY_CATEGORY_OWNER, user);
    }
}
