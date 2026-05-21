package com.example.comscifoodap.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodOrderDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "customerFoodOrderDatabase";
    public static final String TABLE_NAME = "foodOrderTable";
    public static final int DATABASE_VERSION = 5;
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_HASH_MAP_GSON = "hashMapGson";
    public static final String COLUMN_IS_READY = "isReady";

    public FoodOrderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery =
        "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_HASH_MAP_GSON + " TEXT, " +
                COLUMN_IS_READY+ " INTEGER);";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
    }
}
