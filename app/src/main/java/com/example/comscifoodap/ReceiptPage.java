package com.example.comscifoodap;

import static android.content.ContentValues.TAG;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comscifoodap.adapter.FoodReceiptRecyclerViewAdapter;
import com.example.comscifoodap.adapter.SharedPreferencesHelper;
import com.example.comscifoodap.db.FoodItemDBHelper;
import com.example.comscifoodap.db.FoodOrderDBOps;
import com.example.comscifoodap.helper.RecyclerItemTouchHelperFoodReceipt;
import com.example.comscifoodap.model.FoodOrderItem;
import com.example.comscifoodap.model.ReceiptItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptPage extends AppCompatActivity implements FoodQuantityPage.Test{

    public void updateReceiptPage() {
        HashMap<String, Integer> cartHashMap = SharedPreferencesHelper.getHashMap(this);
        List<ReceiptItem> receiptItems = new ArrayList<ReceiptItem>();

        FoodItemDBHelper dbHelper = new FoodItemDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        double totalPrice = 0.00;

        for (Map.Entry<String, Integer> entry : cartHashMap.entrySet()) {
            String number = entry.getKey();
            int count = entry.getValue();

            Log.d(TAG, "onCreate: " + number + " " + count);

            Cursor cursor = dbHelper.getFoodDetails(number);

            Log.d(TAG, "cursor position " + cursor.getPosition());

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String foodName = cursor.getString(cursor.getColumnIndexOrThrow("foodName"));
                    double foodPrice = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("foodPrice")));
                    int foodId = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("id"))) - 1;
                    foodPrice = count * foodPrice;
                    totalPrice += foodPrice;
                    receiptItems.add(new ReceiptItem(count, foodName, foodPrice, foodId));
                    cursor.close();
                } while (cursor.moveToNext());
            } else {
                Log.e("DebugCursor", "Cursor is null or empty");
            }
        }

        db.close();

        FoodReceiptRecyclerViewAdapter adapter = new FoodReceiptRecyclerViewAdapter(this, receiptItems);

        RecyclerView recyclerView = findViewById(R.id.receiptRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelperFoodReceipt(adapter, receiptItems));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        TextView totalPriceTV = findViewById(R.id.totalPrice);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        totalPriceTV.setText("$" + decimalFormat.format(totalPrice));
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_page);

        updateReceiptPage();

        ImageView dustbin = findViewById(R.id.dustbin);
        dustbin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });

        Button placeOrder = findViewById(R.id.order);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Integer> hashMap = SharedPreferencesHelper.getHashMap(getApplicationContext());
                if (hashMap.isEmpty()) {
                    Toast.makeText(ReceiptPage.this, "No Food Selected!", Toast.LENGTH_SHORT).show();
                } else {
                    int id = addOrderToDBAndReturnId(hashMap, false);
                    sendNotification();
                    Intent intent = new Intent(getApplicationContext(), ProgressOfFood.class);
                    intent.putExtra("ID", id);
                    startActivity(intent);
                }
            }
        });

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Html.fromHtml("<font color='#000000'>Do you want to confirm?</font>"));

        builder.setPositiveButton(Html.fromHtml("<font color='#000000'>Confirm</font>"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                HashMap<String, Integer> cartHashMap = new HashMap<>();
                SharedPreferencesHelper.saveHashMap(getApplicationContext(), cartHashMap);
                updateReceiptPage();
                Toast.makeText(ReceiptPage.this, "Cleared!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(Html.fromHtml("<font color='#000000'>Cancel</font>"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                updateReceiptPage();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void exitingFragment() {
        Log.d(TAG, "deader laksa ");
        updateReceiptPage();
    }

    private void sendNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = "your_channel_id";
            CharSequence channelName = "Your Channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }

        Notification.Builder builder;
        builder = new Notification.Builder(this, "your_channel_id");

        builder
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle("Store Owner")
                .setContentText("A new order has been recently placed!");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    private int addOrderToDBAndReturnId(HashMap<String, Integer> hashMap, boolean status) {
        FoodOrderDBOps databaseOperations = new FoodOrderDBOps(this);
        databaseOperations.open();

        int id = FoodOrderDBOps.getNextItemId();
        FoodOrderItem dataModel1 = new FoodOrderItem(id, hashMap, status);
        long result1 = databaseOperations.insertData(dataModel1);

        databaseOperations.close();
        return id;
    }
}