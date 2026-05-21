package com.example.comscifoodap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.comscifoodap.adapter.SharedPreferencesHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashMap;

public class FoodDescriptionPage extends BottomSheetDialogFragment {
    private static final String ARG_FOOD_NAME = "food_name";
    private static final String ARG_FOOD_PRICE = "food_price";
    private static final String ARG_FOOD_DESCRIPTION = "food_description";
    private static final String ARG_FOOD_ID = "food_id";

    public static FoodDescriptionPage newInstance(String foodName, String foodPrice, String foodDescription, Integer foodId) {
        FoodDescriptionPage fragment = new FoodDescriptionPage();
        Bundle args = new Bundle();
        args.putString(ARG_FOOD_NAME, foodName);
        args.putString(ARG_FOOD_PRICE, foodPrice);
        args.putString(ARG_FOOD_DESCRIPTION, foodDescription);
        args.putInt(ARG_FOOD_ID, foodId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_description_page, container, false);

        String foodName = getArguments().getString(ARG_FOOD_NAME, "");
        String foodPrice = getArguments().getString(ARG_FOOD_PRICE, "");
        String foodDescription = getArguments().getString(ARG_FOOD_DESCRIPTION, "");

        TextView foodNameTV = view.findViewById(R.id.foodName);
        TextView foodDescriptionTV = view.findViewById(R.id.foodDescription);
        TextView foodPriceTV = view.findViewById(R.id.foodPrice);

        foodNameTV.setText(foodName);
        foodDescriptionTV.setText(foodDescription);
        foodPriceTV.setText("$" + foodPrice);

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String foodId = getArguments().getString(ARG_FOOD_NAME, "");
        Button addToCart = view.findViewById(R.id.addToCart);
        HashMap<String, Integer> cartHashMap = SharedPreferencesHelper.getHashMap(requireContext());

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cartHashMap.containsKey(foodId)) {
                    int intValue = cartHashMap.getOrDefault(foodId, 0);

                    cartHashMap.put(foodId, intValue);
                }

                Integer currentQuantity = cartHashMap.get(foodId) + 1;
                cartHashMap.put(foodId, currentQuantity);
                SharedPreferencesHelper.saveHashMap(requireContext(), cartHashMap);

                dismiss();
            }
        });
    }
}