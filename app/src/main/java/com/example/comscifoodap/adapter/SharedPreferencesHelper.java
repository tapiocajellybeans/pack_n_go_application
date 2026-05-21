package com.example.comscifoodap.adapter;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;

public class SharedPreferencesHelper {
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String HASHMAP_KEY = "hashMapKey";

    public static void saveHashMap(Context context, HashMap<String, Integer> hashMap) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(hashMap);
        editor.putString(HASHMAP_KEY, json);
        editor.apply();
    }

    public static HashMap<String, Integer> getHashMap(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(HASHMAP_KEY, null);

        if (json != null) {
            Type type = new com.google.gson.reflect.TypeToken<HashMap<String, Integer>>() {}.getType();
            return gson.fromJson(json, type);
        } else {
            return new HashMap<>();
        }
    }
}
