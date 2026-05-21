package com.example.comscifoodap.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comscifoodap.FoodQuantityPage;
import com.example.comscifoodap.ProgressOfFood;
import com.example.comscifoodap.R;
import com.example.comscifoodap.ReceiptPage;
import com.example.comscifoodap.model.ReceiptItem;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodReceiptRecyclerViewAdapter extends RecyclerView.Adapter<FoodReceiptRecyclerViewAdapter.ViewHolder> {
    private List<ReceiptItem> receiptItems;
    private ReceiptPage activity;
    private ProgressOfFood activity2;

    public FoodReceiptRecyclerViewAdapter(ReceiptPage activity, List<ReceiptItem> receiptItems) {
        this.receiptItems = receiptItems;
        this.activity = activity;
    }

    public FoodReceiptRecyclerViewAdapter(ProgressOfFood activity2, List<ReceiptItem> receiptItems) {
        this.receiptItems = receiptItems;
        this.activity2 = activity2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_receipt_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReceiptItem ReceiptItem = receiptItems.get(position);

        holder.foodQty.setText(String.valueOf(ReceiptItem.getFoodQty()));
        holder.foodName.setText(ReceiptItem.getFoodName());

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        Log.d(TAG, "onBindViewHolder: " + decimalFormat.format(ReceiptItem.getFoodPrice()));
        holder.foodPrice.setText(decimalFormat.format(ReceiptItem.getFoodPrice()));
    }

    @Override
    public int getItemCount() {
        return receiptItems.size();
    }

    public Context getContext(){ return activity; }

    public void deleteItem(int position) {
        ReceiptItem item = receiptItems.get(position);
        HashMap<String, Integer> hashMap = SharedPreferencesHelper.getHashMap(getContext());
        hashMap.remove(item.getFoodName());
        SharedPreferencesHelper.saveHashMap(getContext(), hashMap);
        receiptItems.remove(position);
        notifyDataSetChanged();
    }


    public void editItem(int position,  View itemView) {
        Map.Entry<String, Integer> xthEntry = getXthEntry(SharedPreferencesHelper.getHashMap(getContext()), position);

        ReceiptItem item = getFoodByName(receiptItems, xthEntry.getKey());
        item.setFoodQty(xthEntry.getValue());

        ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.showBottomSheetDialog(item, activity);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodQty, foodName, foodPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodQty = itemView.findViewById(R.id.foodQuantity);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ReceiptItem clickedReceiptItem = receiptItems.get(getAdapterPosition());
                    showBottomSheetDialog(clickedReceiptItem, activity);
                }
            });

        }
        public void showBottomSheetDialog(ReceiptItem receiptItem, ReceiptPage activity) {
            String foodName = receiptItem.getFoodName();
            double foodPrice = receiptItem.getFoodPrice();
            int foodQuantity = receiptItem.getFoodQty();
            int foodId = receiptItem.getFoodId();

            FoodQuantityPage bottomSheetDialogFragment = FoodQuantityPage.newInstance(foodName, foodPrice, foodQuantity, foodId);
            bottomSheetDialogFragment.show(activity.getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
        }
    }

    private static <K, V> Map.Entry<K, V> getXthEntry(HashMap<K, V> map, int x) {
        int count = 0;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (count == x) {
                return entry;
            }
            count++;
        }
        return null;
    }

    private static ReceiptItem getFoodByName(List<ReceiptItem> foodList, String foodName) {
        for (ReceiptItem food : foodList) {
            if (food.getFoodName().equals(foodName)) {
                return food;
            }
        }
        return null;
    }
}
