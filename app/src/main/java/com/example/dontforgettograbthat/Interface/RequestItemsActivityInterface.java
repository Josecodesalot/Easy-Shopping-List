package com.example.dontforgettograbthat.Interface;

import com.example.dontforgettograbthat.Models.Item;

public interface RequestItemsActivityInterface {
    void addToCartList (Item item);
    void deleteFromRequestedItems (String ItemKey);
}
