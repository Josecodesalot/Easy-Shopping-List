package com.joseapps.simpleshoppinglist.Interface;

import com.joseapps.simpleshoppinglist.Models.Item;

public interface CartInterface {
    void addToHistory(Item item, int position, String originalListName);
    void delete(Item item, int position, String originalListName);
    void addItem(Item item, int position, String originalListName);
    void setChanges(Item item, int position, String originalListName);

}
