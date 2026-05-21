package com.example.comscifoodap.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodItemDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "storeOwnerFoodItemsDatabase";
    public static final int DATABASE_VERSION = 5;
    public static final String TABLE_NAME = "foodItems";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FOOD_NAME = "foodName";
    public static final String COLUMN_FOOD_DESCRIPTION = "foodDescription";

    public static final String COLUMN_FOOD_PRICE = "foodPrice";

    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FOOD_NAME + " TEXT, " +
                    COLUMN_FOOD_PRICE + " TEXT, " +
                    COLUMN_FOOD_DESCRIPTION + " TEXT);";

    public FoodItemDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getFoodDetails(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_FOOD_NAME, COLUMN_FOOD_PRICE, COLUMN_ID};
        String selection = COLUMN_FOOD_NAME + "=?";
        String[] selectionArgs = {id};

        return db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
    }
}
