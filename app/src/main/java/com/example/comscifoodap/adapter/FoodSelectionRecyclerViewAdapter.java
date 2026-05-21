package com.example.comscifoodap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comscifoodap.FoodDescriptionPage;
import com.example.comscifoodap.MainActivity;
import com.example.comscifoodap.R;
import com.example.comscifoodap.db.FoodItemDBHelper;
import com.example.comscifoodap.model.FoodItem;

import java.util.List;

public class FoodSelectionRecyclerViewAdapter extends RecyclerView.Adapter<FoodSelectionRecyclerViewAdapter.ViewHolder> {
    private List<FoodItem> foodItems;
    private MainActivity activity;
    private FoodItemDBHelper db;

    public FoodSelectionRecyclerViewAdapter(MainActivity activity, List<FoodItem> foodItems) {
        this.foodItems = foodItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_selection_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);

        holder.foodTitle.setText(foodItem.getFoodName());
        holder.foodPrice.setText("$" + foodItem.getFoodPrice());
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodTitle, foodPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodTitle = itemView.findViewById(R.id.foodTitle);
            foodPrice = itemView.findViewById(R.id.foodPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FoodItem clickedFoodItem = foodItems.get(getAdapterPosition());
                    showBottomSheetDialog(clickedFoodItem, getAdapterPosition());
                }
            });
        }
    }

    private void showBottomSheetDialog(FoodItem foodItem, Integer foodId) {
        String foodName = foodItem.getFoodName();
        String foodPrice = foodItem.getFoodPrice();
        String foodDescription = foodItem.getFoodDescription();

        FoodDescriptionPage bottomSheetDialogFragment = FoodDescriptionPage.newInstance(foodName, foodPrice, foodDescription, foodId);
        bottomSheetDialogFragment.show(activity.getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }
}
