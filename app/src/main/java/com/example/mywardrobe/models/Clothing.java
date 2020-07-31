package com.example.mywardrobe.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Clothing")
public class Clothing extends ParseObject {
    public static final String KEY_CLOTHING_CREATED_KEY = "createdAt";
    public static final String KEY_CLOTHING_NAME = "clothingName";
    public static final String KEY_CLOTHING_OWNER = "owner";
    public static final String KEY_CLOTHING_CATEGORY = "category";
    public static final String KEY_CLOTHING_IMAGE = "image";
    public static final String KEY_CLOTHING_DESCRIPTION = "description";
    public static final String KEY_CLOTHING_PRICE = "price";
    public static final String KEY_CLOTHING_BRAND = "brand";
    public static final String KEY_CLOTHING_FAV = "favorite";

    public String getClothingName(){
        return getString(KEY_CLOTHING_NAME);
    }
    public void setClothingName(String clothingName){
        put(KEY_CLOTHING_NAME, clothingName);
    }

    public String getClothingCategoryName(){
        return getString(KEY_CLOTHING_CATEGORY);
    }
    public void setClothingCategoryName(String category){
        put(KEY_CLOTHING_CATEGORY, category);
    }

    public String getClothingDescription(){
        return getString(KEY_CLOTHING_DESCRIPTION);
    }
    public void setClothingDescription(String description){
        put(KEY_CLOTHING_DESCRIPTION, description);
    }

    public String getClothingBrand(){
        return getString(KEY_CLOTHING_BRAND);
    }
    public void setClothingBrand(String brand){
        put(KEY_CLOTHING_BRAND, brand);
    }

    public ParseUser getClothingOwner(){
        return getParseUser(KEY_CLOTHING_OWNER);
    }
    public void setClothingOwner(ParseUser owner){
        put(KEY_CLOTHING_OWNER, owner);
    }

    public ParseFile getClothingImage(){
        return getParseFile(KEY_CLOTHING_IMAGE);
    }
    public void setClothingImage(ParseFile image){
        put(KEY_CLOTHING_IMAGE, image);
    }

    public Number getClothingPrice(){
        return getNumber(KEY_CLOTHING_PRICE);
    }
    public void setClothingPrice(Number price){
        put(KEY_CLOTHING_PRICE, price);
    }

    public Boolean getClothingFavorite(){
        return getBoolean(KEY_CLOTHING_FAV);
    }
    public void setClothingFavorite(Boolean favorite){
        put(KEY_CLOTHING_FAV, favorite);
    }

}
