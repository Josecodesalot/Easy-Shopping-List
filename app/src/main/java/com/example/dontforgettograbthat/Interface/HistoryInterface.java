package com.example.dontforgettograbthat.Interface;

import com.example.dontforgettograbthat.Models.Item;

public interface HistoryInterface {
    void restoreToCart (Item item);
    void deleteFromHistory (String itemKey);
}
