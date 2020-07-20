package com.example.mywardrobe.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

@ParseClassName("Outfit")
public class Outfit extends ParseObject{
    public static final String KEY_OUTFIT_CREATED_KEY = "createdAt";
    public static final String KEY_OUTFIT_NAME = "outfitName";
    public static final String KEY_OUTFIT_DESCRIPTION = "outfitDescription";
    public static final String KEY_OUTFIT_COUNT = "count";
    public static final String KEY_OUTFIT_OWNER = "outfitOwner";
    public static final String KEY_OUTFIT_CLOTHES = "clothes";

    public String getOutfitName(){
        return getString(KEY_OUTFIT_NAME);
    }
    public void setOutfitName(String outfitName){
        put(KEY_OUTFIT_NAME, outfitName);
    }

    public String getOutfitDescription(){
        return getString(KEY_OUTFIT_DESCRIPTION);
    }
    public void setOutfitDescription(String outfitDescription){
        put(KEY_OUTFIT_DESCRIPTION, outfitDescription);
    }

    public Number getOutfitCount(){
        return getNumber(KEY_OUTFIT_COUNT);
    }
    public void setOutfitCount(Number count){
        put(KEY_OUTFIT_COUNT, count);
    }


    public ParseUser getOutfitOwner(){
        return getParseUser(KEY_OUTFIT_OWNER);
    }
    public void setOutfitOwner(ParseUser outfitOwner){
        put(KEY_OUTFIT_OWNER, outfitOwner);
    }

    public ParseRelation<Clothing> getClothingRelation() {
        return getRelation(KEY_OUTFIT_CLOTHES);
    }

    public void addClothing(Clothing clothing) {
        getClothingRelation().add(clothing);
        saveInBackground();
    }

    public void removeClothing(Clothing clothing) {
        getClothingRelation().remove(clothing);
        saveInBackground();
    }
}
