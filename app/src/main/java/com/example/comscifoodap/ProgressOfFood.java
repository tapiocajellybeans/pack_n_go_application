package com.example.comscifoodap;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comscifoodap.adapter.FoodReceiptRecyclerViewAdapter;
import com.example.comscifoodap.adapter.SharedPreferencesHelper;
import com.example.comscifoodap.db.FoodItemDBHelper;
import com.example.comscifoodap.db.FoodOrderDBOps;
import com.example.comscifoodap.model.ReceiptItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProgressOfFood extends AppCompatActivity {

    private ProgressBar progressBar, progressBar2;
    private TextView progressText;
    private RecyclerView recyclerView;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_of_food);

        progressBar = findViewById(R.id.progress_bar);
        progressText = findViewById(R.id.progress_text);
        progressBar2 = findViewById(R.id.progress_bar2);

        displayRecyclerView();

        Intent intent = getIntent();
        int receivedNumber = intent.getIntExtra("ID", 0);
        TextView idNumber = findViewById(R.id.idNumber);
        idNumber.setText(String.format(Locale.ENGLISH, "%04d", receivedNumber));

        ImageView backButton = findViewById(R.id.backButtonMainActivity);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgressOfFood.this, ChooseUserPage.class);
                startActivity(intent);
            }
        });

        // animation function
        final Handler handler = new Handler();
        final boolean[] orderReady = {false};
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (i == 0) {
                    FoodOrderDBOps dbOps = new FoodOrderDBOps(getApplicationContext());
                    dbOps.open();

                    int isReadyStatus = dbOps.getIsReadyStatus(receivedNumber);
                    orderReady[0] = isReadyStatus == 1;
                    if (orderReady[0]) {
                        setProgressBarColor(getResources().getColor(R.color.colorPrimaryDark), progressBar);
                    }
                }

                if (i <= 110) {
                    progressBar.setProgress(i);
                    if (!orderReady[0]) {
                        progressBar2.setProgress(i - 10);
                        progressText.setText("...");
                    } else {
                        progressText.setText("✔");
                        progressText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    i++;
                    handler.postDelayed(this, 5);
                } else if (!orderReady[0]) {
                    i = 0; // Reset progress bar
                    progressText.setText("...");
                    progressBar.setProgress(i);
                    progressBar2.setProgress(i-10);
                    handler.postDelayed(this, 5);
                }
            }
        }, 5);
    }

    private void displayRecyclerView() {
        HashMap<String, Integer> cartHashMap = SharedPreferencesHelper.getHashMap(this);
        List<ReceiptItem> receiptItems = new ArrayList<ReceiptItem>();

        FoodItemDBHelper dbHelper = new FoodItemDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        for (Map.Entry<String, Integer> entry : cartHashMap.entrySet()) {
            String number = entry.getKey();
            int count = entry.getValue();
            Cursor cursor = dbHelper.getFoodDetails(number);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String foodName = cursor.getString(cursor.getColumnIndexOrThrow("foodName"));
                    double foodPrice = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("foodPrice")));
                    int foodId = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("id"))) - 1;
                    foodPrice = count * foodPrice;
                    receiptItems.add(new ReceiptItem(count, foodName, foodPrice, foodId));
                } while (cursor.moveToNext()); cursor.close();
            } else {
                Log.e("DebugCursor", "Cursor is null or empty");
            }
        }

        db.close();
        FoodReceiptRecyclerViewAdapter adapter = new FoodReceiptRecyclerViewAdapter(this, receiptItems);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setProgressBarColor(int color, ProgressBar progressBar) {
        progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        progressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

}