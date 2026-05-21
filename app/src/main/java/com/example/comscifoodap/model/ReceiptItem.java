package com.example.comscifoodap.model;

public class ReceiptItem {
    private int foodQty;
    private String foodName;
    private double foodPrice;
    private int foodId;

    public ReceiptItem(int foodQty, String foodName, double foodPrice, int foodId) {
        this.foodId = foodId;
        this.foodQty = foodQty;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getFoodQty() {
        return foodQty;
    }

    public void setFoodQty(int foodQty) {
        this.foodQty = foodQty;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(double foodPrice) {
        this.foodPrice = foodPrice;
    }


}
