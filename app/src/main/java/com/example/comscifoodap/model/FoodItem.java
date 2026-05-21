package com.example.comscifoodap.model;

public class FoodItem {

    private int id;

    private String foodName, foodDescription, foodPrice;

    public int getId() {return id;}
    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDescription() {return foodDescription;}
    public void setFoodDescription(String foodDescription) { this.foodDescription = foodDescription; }

    public String getFoodPrice() {return foodPrice;}
    public void setFoodPrice(String foodPrice) {this.foodPrice = foodPrice;}

    public FoodItem(String foodName, String foodDescription, String foodPrice) {
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
    }
}
