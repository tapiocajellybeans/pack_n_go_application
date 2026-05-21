package com.example.comscifoodap;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.comscifoodap.adapter.SharedPreferencesHelper;
import com.example.comscifoodap.adapter.StoreFoodOrderRecyclerViewAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DecimalFormat;
import java.util.HashMap;

public class FoodQuantityPage extends BottomSheetDialogFragment {

    private static final String ARG_FOOD_NAME = "food_name";
    private static final String ARG_FOOD_PRICE = "food_price";
    private static final String ARG_FOOD_QTY = "food_qty";
    private static final String ARG_FOOD_ID = "food_id";

    private int foodQuantity = 0;
    private double foodPrice = 0.00;
    private double finalFoodPrice = 0.00;
    private TextView foodQuantityTV, foodPriceTV;

    public static FoodQuantityPage newInstance(String foodName, double foodPrice, Integer foodQuantity, int foodId) {
        FoodQuantityPage fragment = new FoodQuantityPage();
        Bundle args = new Bundle();
        args.putString(ARG_FOOD_NAME, foodName);
        args.putDouble(ARG_FOOD_PRICE, foodPrice);
        args.putInt(ARG_FOOD_QTY, foodQuantity);
        args.putInt(ARG_FOOD_ID, foodId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_quantity_page, container, false);

        String foodName = getArguments().getString(ARG_FOOD_NAME, "");
        foodPrice = getArguments().getDouble(ARG_FOOD_PRICE, 0.0);
        foodQuantity = getArguments().getInt(ARG_FOOD_QTY, 0);

        TextView foodNameTV = view.findViewById(R.id.foodName);
        foodQuantityTV = view.findViewById(R.id.foodQuantity);
        foodPriceTV = view.findViewById(R.id.foodPrice);

        foodNameTV.setText(foodName);
        foodQuantityTV.setText(String.valueOf(foodQuantity));
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        foodPriceTV.setText("$" + decimalFormat.format(foodPrice));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView minus = view.findViewById(R.id.minus);
        ImageView plus = view.findViewById(R.id.plus);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foodQuantity == 0) {
                    Toast.makeText(getContext(), "Can't be negative!", Toast.LENGTH_SHORT).show();
                } else {
                    foodQuantity -= 1;
                    updateFoodQtyTV();
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodQuantity += 1;
                updateFoodQtyTV();
            }
        });
    }

    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        HashMap<String, Integer> cartHashMap = SharedPreferencesHelper.getHashMap(requireContext());
        String foodId = getArguments().getString(ARG_FOOD_NAME,"");

        if (foodQuantity == 0) {
            cartHashMap.remove(foodId);
            Log.d(TAG, "dead laksa! " + foodId);
        } else {
            cartHashMap.put(foodId, foodQuantity);
            Log.d(TAG, "dead laksa" + foodQuantity);
        }
        SharedPreferencesHelper.saveHashMap(requireContext(), cartHashMap);

        Log.d(TAG, "onDismiss: " + StoreFoodOrderRecyclerViewAdapter.hashMaptoStringBuilder(cartHashMap));
    }

    private Test mListener;

    public interface Test{
        void exitingFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (Test) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(mListener != null){
            mListener.exitingFragment();
        }
    }

    private void updateFoodQtyTV() {
        foodQuantityTV.setText(String.valueOf(foodQuantity));

        finalFoodPrice = foodQuantity * foodPrice;
        Log.d(TAG, "updateFoodQtyTV: " + foodQuantity);
        Log.d(TAG, "updateFoodQtyTV: " + foodPrice);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        foodPriceTV.setText("$" + decimalFormat.format(finalFoodPrice));
    }

}
