package com.example.comscifoodap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comscifoodap.adapter.FoodSelectionRecyclerViewAdapter;
import com.example.comscifoodap.db.FoodItemDBHandler;
import com.example.comscifoodap.model.FoodItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView backButton, receiptButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FoodItemDBHandler dbHandler = new FoodItemDBHandler(this);
        dbHandler.open();
        List<FoodItem> foodItems = dbHandler.getAllFoodItems();

        RecyclerView recyclerView = findViewById(R.id.foodSelectionRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FoodSelectionRecyclerViewAdapter adapter = new FoodSelectionRecyclerViewAdapter(this, foodItems);
        recyclerView.setAdapter(adapter);

        backButton = findViewById(R.id.backButtonMainActivity);
        receiptButton = findViewById(R.id.receiptButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, ChooseUserPage.class);
                startActivity(intent1);
            }
        });

        TextView storeName = findViewById(R.id.storeName);
//        storeName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FoodItem newFoodItem = new FoodItem("Chicken Rice","Rice with White Chicken", "3.30" );
//                long id = dbHandler.addFoodItem(newFoodItem);
//                foodItems.add(newFoodItem);
//                adapter.notifyItemInserted(foodItems.size());
//                Toast.makeText(MainActivity.this, "Added!", Toast.LENGTH_SHORT).show();
//            }
//        });

        receiptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReceiptPage.class);
                startActivity(intent);
            }
        });
    }
}