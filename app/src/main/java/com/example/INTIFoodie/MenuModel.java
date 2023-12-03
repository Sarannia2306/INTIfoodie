package com.example.INTIFoodie;

public class MenuModel {

    private String name;
    private String imageUrl;

    private int quantity;
    private double price;


    public MenuModel(String name, String imageUrl, double price, int quantity) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.price = price;
    }


    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public CartItem toCartItem(int quantity) {
        return new CartItem(name, price, quantity);
    }
    public double getTotalPrice() {
        return getPrice() * getQuantity();
    }
}

