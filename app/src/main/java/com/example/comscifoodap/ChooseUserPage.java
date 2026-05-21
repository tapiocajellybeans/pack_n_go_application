package com.example.comscifoodap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseUserPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_user_page);

        Button storeOwnerButton = findViewById(R.id.storeOwnerButton);
        Button customerButton = findViewById(R.id.customerButton);

        // Set OnClickListener for the first button
        customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the first activity
                Intent intent = new Intent(ChooseUserPage.this, UserOrderChoicePage.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for the second button
        storeOwnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the second activity
                Intent intent = new Intent(ChooseUserPage.this, StoreFoodOrderPage.class);
                startActivity(intent);
            }
        });
    }
}
