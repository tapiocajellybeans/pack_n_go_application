package com.example.comscifoodap.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.comscifoodap.adapter.StoreFoodOrderRecyclerViewAdapter;
import com.example.comscifoodap.model.FoodOrderItem;

import java.util.ArrayList;
import java.util.List;

public class FoodOrderDBOps {

    private FoodOrderDBHelper dbHelper;
    private static SQLiteDatabase database;

    public FoodOrderDBOps(Context context) {
        dbHelper = new FoodOrderDBHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public static int getNextItemId() {
        int nextId = 1;
        String query = "SELECT MAX(" +  FoodOrderDBHelper.COLUMN_ID +") FROM " + FoodOrderDBHelper.TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            nextId = cursor.getInt(0) + 1;
            cursor.close();
        }
        return nextId;
    }

    public long insertData(FoodOrderItem data) {
        ContentValues values = new ContentValues();
        values.put(FoodOrderDBHelper.COLUMN_ID, data.getId());
        values.put(FoodOrderDBHelper.COLUMN_HASH_MAP_GSON, data.getGsonData());
        values.put(FoodOrderDBHelper.COLUMN_IS_READY, data.isReady() ? 1 : 0); //1 is true and 0 is false
        return database.insert(FoodOrderDBHelper.TABLE_NAME, null, values);
    }

    public void updateData(FoodOrderItem updatedItem) {
        ContentValues values = new ContentValues();
        values.put(FoodOrderDBHelper.COLUMN_HASH_MAP_GSON, updatedItem.getGsonData());
        values.put(FoodOrderDBHelper.COLUMN_IS_READY, updatedItem.isReady() ? 1 : 0);

        String selection = FoodOrderDBHelper.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(updatedItem.getId())};
        database.update(FoodOrderDBHelper.TABLE_NAME, values, selection, selectionArgs);
    }

    public void deleteItem(int itemId) {
        String selection = FoodOrderDBHelper.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(itemId)};
        database.delete(FoodOrderDBHelper.TABLE_NAME, selection, selectionArgs);
    }

    public List<Integer> getAllFoodIds() {
        List<Integer> foodIds = new ArrayList<>();

        String[] columns = {
                FoodOrderDBHelper.COLUMN_ID
        };

        Cursor cursor = database.query(
                FoodOrderDBHelper.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(FoodOrderDBHelper.COLUMN_ID));
                foodIds.add(id);
            }
            cursor.close();
        }
        return foodIds;
    }

    public static List<FoodOrderItem> getAllFoodOrderItems() {
        List<FoodOrderItem> foodOrderItemList = new ArrayList<>();

        String[] columns = {
                FoodOrderDBHelper.COLUMN_ID,
                FoodOrderDBHelper.COLUMN_HASH_MAP_GSON,
                FoodOrderDBHelper.COLUMN_IS_READY
        };

        Cursor cursor = database.query(
                FoodOrderDBHelper.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(FoodOrderDBHelper.COLUMN_ID));
                String hashMapGson = cursor.getString(cursor.getColumnIndexOrThrow(FoodOrderDBHelper.COLUMN_HASH_MAP_GSON));
                int isReady = cursor.getInt(cursor.getColumnIndexOrThrow(FoodOrderDBHelper.COLUMN_IS_READY));
                FoodOrderItem foodOrderItem = new FoodOrderItem(id, StoreFoodOrderRecyclerViewAdapter.GsonToHashMap(hashMapGson), isReady == 1);
                foodOrderItemList.add(foodOrderItem);
            }

            cursor.close();
        }

        return foodOrderItemList;
    }

    public String getGsonForFoodId(int foodId) {
        String gsonValue = null;

        String[] columns = {
                FoodOrderDBHelper.COLUMN_HASH_MAP_GSON
        };

        String selection = FoodOrderDBHelper.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(foodId)};

        Cursor cursor = database.query(
                FoodOrderDBHelper.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            gsonValue = cursor.getString(cursor.getColumnIndexOrThrow(FoodOrderDBHelper.COLUMN_HASH_MAP_GSON));
            cursor.close();
        }

        return gsonValue;
    }

    public int getIsReadyStatus(int foodId) {
        int isReadyStatus = -1;

        String[] columns = {
                FoodOrderDBHelper.COLUMN_IS_READY
        };

        String selection = FoodOrderDBHelper.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(foodId)};

        Cursor cursor = database.query(
                FoodOrderDBHelper.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            isReadyStatus = cursor.getInt(cursor.getColumnIndexOrThrow(FoodOrderDBHelper.COLUMN_IS_READY));
            cursor.close();
        }

        return isReadyStatus;
    }

}
