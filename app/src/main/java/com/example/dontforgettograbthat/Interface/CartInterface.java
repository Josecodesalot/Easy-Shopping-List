package com.example.dontforgettograbthat.Interface;

import com.example.dontforgettograbthat.Models.Item;

public interface CartInterface {
    void addToHistory(Item item);
    void delete(String itemKey);
    void addItem(Item item);
    void addSpecificItem(int position);

}
