package com.example.comscifoodap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comscifoodap.adapter.StoreFoodOrderRecyclerViewAdapter;
import com.example.comscifoodap.db.FoodOrderDBOps;
import com.example.comscifoodap.model.FoodOrderItem;

import java.util.List;

public class StoreFoodOrderPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_order_page);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreFoodOrderPage.this, ChooseUserPage.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        FoodOrderDBOps databaseOperations = new FoodOrderDBOps(this);
        databaseOperations.open();

        // Initialize your database and retrieve data
        List<FoodOrderItem> itemList = FoodOrderDBOps.getAllFoodOrderItems();

        // Set up RecyclerView with the adapter
        StoreFoodOrderRecyclerViewAdapter adapter = new StoreFoodOrderRecyclerViewAdapter(itemList, databaseOperations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

//        databaseOperations.close();
    }


}