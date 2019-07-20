package com.example.dontforgettograbthat.Interface;

import com.example.dontforgettograbthat.Models.Item;

public interface HistoryInterface {
    void restoreToCart (Item item, int positon);
    void deleteFromHistory (Item item, int postion);
    void setChanges(Item item, int positon);
}
