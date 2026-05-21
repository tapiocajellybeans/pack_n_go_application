package com.example.comscifoodap.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.comscifoodap.model.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class FoodItemDBHandler {
    private SQLiteDatabase database;
    private FoodItemDBHelper dbHelper;

    public FoodItemDBHandler(Context context) {
        dbHelper = new FoodItemDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addFoodItem(FoodItem foodItem) {
        ContentValues values = new ContentValues();
        values.put(FoodItemDBHelper.COLUMN_FOOD_NAME, foodItem.getFoodName());
        values.put(FoodItemDBHelper.COLUMN_FOOD_DESCRIPTION, foodItem.getFoodDescription());
        values.put(FoodItemDBHelper.COLUMN_FOOD_PRICE, foodItem.getFoodPrice());

        return database.insert(FoodItemDBHelper.TABLE_NAME, null, values);
    }

    public List<FoodItem> getAllFoodItems() {
        List<FoodItem> foodItems = new ArrayList<>();
        Cursor cursor = database.query(FoodItemDBHelper.TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                FoodItem foodItem = cursorToFoodItem(cursor);
                foodItems.add(foodItem);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return foodItems;
    }

    private FoodItem cursorToFoodItem(Cursor cursor) {

        String foodName = cursor.getString(cursor.getColumnIndexOrThrow(FoodItemDBHelper.COLUMN_FOOD_NAME));
        String foodDescription = cursor.getString(cursor.getColumnIndexOrThrow(FoodItemDBHelper.COLUMN_FOOD_DESCRIPTION));
        String foodPrice = cursor.getString(cursor.getColumnIndexOrThrow(FoodItemDBHelper.COLUMN_FOOD_PRICE));

        return new FoodItem(foodName, foodDescription, foodPrice);
    }
}
