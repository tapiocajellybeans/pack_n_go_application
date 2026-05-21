package com.example.comscifoodap;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comscifoodap.adapter.SharedPreferencesHelper;
import com.example.comscifoodap.adapter.StoreFoodOrderRecyclerViewAdapter;
import com.example.comscifoodap.adapter.UserOrderChoiceRecyclerViewAdapter;
import com.example.comscifoodap.db.FoodOrderDBOps;

import java.util.HashMap;
import java.util.List;

public class UserOrderChoicePage extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_order_choice_page);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FoodOrderDBOps foodOrderDBOps = new FoodOrderDBOps(this);
        foodOrderDBOps.open();
        List<Integer> itemIds = foodOrderDBOps.getAllFoodIds();

        UserOrderChoiceRecyclerViewAdapter adapter = new UserOrderChoiceRecyclerViewAdapter(itemIds, new UserOrderChoiceRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int itemId) {
                foodOrderDBOps.open();
                String gsonValue = foodOrderDBOps.getGsonForFoodId(itemId);
                SharedPreferencesHelper.saveHashMap(UserOrderChoicePage.this, StoreFoodOrderRecyclerViewAdapter.GsonToHashMap(gsonValue));

                Intent intent = new Intent(UserOrderChoicePage.this, ProgressOfFood.class);
                intent.putExtra("ID", itemId);
                startActivity(intent);
            }

            public void onAddNewClick() {
                SharedPreferencesHelper.saveHashMap(UserOrderChoicePage.this, new HashMap<String,Integer>());
                Intent intent = new Intent(UserOrderChoicePage.this, MainActivity.class);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
