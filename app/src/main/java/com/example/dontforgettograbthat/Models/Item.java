package com.example.dontforgettograbthat.Models;

public class Item {
    private String item_name;
    private String list_name;
    private long quantity;
    private Double price;
    private String itemKey;

    public Item(String item_name, String list_name, long quantity, Double price, String itemKey) {
        this.item_name = item_name;
        this.list_name = list_name;
        this.quantity = quantity;
        this.price = price;
        this.itemKey = itemKey;
    }

    public Item(){

    }

    @Override
    public String toString() {
        return "Item{" +
                "item_name='" + item_name + '\'' +
                ", list_name='" + list_name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", itemKey='" + itemKey + '\'' +
                '}';
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setList_name(String list_name) {
        this.list_name = list_name;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getList_name() {
        return list_name;
    }

    public long getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public String getItemKey() {
        return itemKey;
    }
}