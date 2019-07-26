package com.joseapps.simpleshoppinglist.Interface;

import com.joseapps.simpleshoppinglist.Models.Item;

public interface CartInterface {
    void addToHistory(Item item, int position);
    void delete(String itemKey, int position);
    void addItem(Item item, int position);
    void setChanges(Item item, int position);

}
