package com.example.comscifoodap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comscifoodap.R;

import java.util.List;
import java.util.Locale;

public class UserOrderChoiceRecyclerViewAdapter extends RecyclerView.Adapter<UserOrderChoiceRecyclerViewAdapter.ViewHolder> {

    private List<Integer> itemIds;
    private OnItemClickListener listener;

    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_ADD_NEW = 1;


    public UserOrderChoiceRecyclerViewAdapter(List<Integer> itemIds, OnItemClickListener listener) {
        this.itemIds = itemIds;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return position == itemIds.size() ? VIEW_TYPE_ADD_NEW : VIEW_TYPE_NORMAL;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_item_user_order_choice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == itemIds.size()) {
            // This is the "Add New" button
            holder.button.setText("Add New");
            holder.button.setOnClickListener(v -> listener.onAddNewClick());
        } else {
            // This is a normal button
            int itemId = itemIds.get(position);
            holder.bind(itemId, listener);
        }
    }

    @Override
    public int getItemCount() {
        return itemIds.size() + 1;
    }

    public interface OnItemClickListener {
        void onItemClick(int itemId);

        void onAddNewClick();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
        }

        public void bind(int itemId, OnItemClickListener listener) {
            button.setText(String.format(Locale.ENGLISH, "%04d", itemId));
            button.setOnClickListener(v -> listener.onItemClick(itemId));
        }
    }
}
