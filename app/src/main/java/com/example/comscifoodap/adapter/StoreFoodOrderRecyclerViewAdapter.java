package com.example.comscifoodap.adapter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comscifoodap.R;
import com.example.comscifoodap.db.FoodOrderDBOps;
import com.example.comscifoodap.model.FoodOrderItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StoreFoodOrderRecyclerViewAdapter extends RecyclerView.Adapter<StoreFoodOrderRecyclerViewAdapter.ViewHolder> {

    private List<FoodOrderItem> itemList;
    private FoodOrderDBOps foodOrderDBOps;

    public StoreFoodOrderRecyclerViewAdapter(List<FoodOrderItem> itemList, FoodOrderDBOps foodOrderDBOps) {
        this.itemList = itemList;
        this.foodOrderDBOps = foodOrderDBOps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_order_card_view, parent, false);
        return new ViewHolder(view);
    }

    public void sendNotification2(int id, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = "your_channel_id2";
            CharSequence channelName = "Your Channel2";
            int importance = NotificationManager.IMPORTANCE_HIGH; // Set to HIGH for banners
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }

        Notification.Builder builder;
        builder = new Notification.Builder(context, "your_channel_id2");

        builder
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle("Customer")
                .setContentText("Order " + id + " is ready!");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodOrderItem item = itemList.get(position);

        // Bind data to views
        holder.idTextView.setText(String.format(Locale.ENGLISH, "%04d", item.getId()));
        holder.hashMapTextView.setText(hashMaptoStringBuilder(GsonToHashMap(item.getGsonData())));
        holder.showOrderTextView.setPaintFlags(holder.showOrderTextView.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        holder.isReady.setChecked(item.isReady());
        if (item.isReady()){
            holder.isReady.setEnabled(false);
        } else {
            holder.isReady.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = holder.getAdapterPosition();
                    FoodOrderItem item = itemList.get(position);

                    item.setReady(holder.isReady.isChecked());

                    foodOrderDBOps.open();
                    foodOrderDBOps.updateData(item);
                    foodOrderDBOps.close();
                    holder.isReady.setEnabled(false);

                    sendNotification2(item.getId(), view.getContext());
                }
            });
        }


        holder.isCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = holder.getAdapterPosition();
                FoodOrderItem item = itemList.get(position);

                foodOrderDBOps.open();
                foodOrderDBOps.deleteItem(item.getId());

                itemList.remove(position);
                notifyItemRemoved(position);
            }
        });

        holder.showOrderTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowDetailsClick(view, holder.hashMapTextView, holder.showOrderTextView);
            }
        });
    }

    public void onShowDetailsClick(View view, TextView detailsContainer, TextView Title) {
        if (detailsContainer.getVisibility() == View.VISIBLE) {
            detailsContainer.setVisibility(View.GONE);
            Title.setText("Show Details:");
        } else {
            detailsContainer.setVisibility(View.VISIBLE);
            Title.setText("Hide Details:");
        }
    }

    public static StringBuilder hashMaptoStringBuilder(HashMap<String, Integer> hashMap){
        StringBuilder stringBuilder = new StringBuilder();

        Iterator<Map.Entry<String, Integer>> iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            stringBuilder.append(entry.getKey()).append(": ").append(entry.getValue());

            if (iterator.hasNext()) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, hashMapTextView, showOrderTextView;
        CheckBox isReady, isCompleted;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView);
            hashMapTextView = itemView.findViewById(R.id.hashMapTextView);
            showOrderTextView = itemView.findViewById(R.id.showOrderButton);
            isReady = itemView.findViewById(R.id.isReady);
            isCompleted = itemView.findViewById(R.id.isDone);
        }
    }

    public static HashMap<String, Integer> GsonToHashMap(String GsonString) {
        Type type = new TypeToken<HashMap<String, Integer>>() {}.getType();
        HashMap<String, Integer> hashMap = new Gson().fromJson(GsonString, type);
        return hashMap;
    }
}
