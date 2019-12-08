package com.inti.student.fitcalorie;

public class Food {
    private String foodTitle;
    private String foodCalorie;
    private String foodImage;

    public Food(){}

    public Food(String foodTitle, String foodCalorie, String foodImage) {
        this.foodTitle = foodTitle;
        this.foodCalorie = foodCalorie;
        this.foodImage = foodImage;
    }

    public String getFoodTitle() {
        return foodTitle;
    }

    public void setFoodTitle(String foodTitle) {
        this.foodTitle = foodTitle;
    }

    public String getFoodCalorie() {
        return foodCalorie;
    }

    public void setFoodCalorie(String foodCalorie) {
        this.foodCalorie = foodCalorie;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }
}
