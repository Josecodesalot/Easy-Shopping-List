package com.joseapps.simpleshoppinglist.Interface;

import com.joseapps.simpleshoppinglist.Models.Item;

public interface HistoryInterface {
    void restoreToCart (Item item, int positon);
    void deleteFromHistory (Item item, int postion);
    void setChanges(Item item, int positon);
}
