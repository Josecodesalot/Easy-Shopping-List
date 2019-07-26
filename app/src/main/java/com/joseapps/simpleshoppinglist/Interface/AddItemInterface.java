package com.joseapps.simpleshoppinglist.Interface;

public interface AddItemInterface {
    void setItemName(String itemName);
    void setQuantity(int quantity);
    void setlistName(String listName);
    void setPrice(double itemPrice);
    void openBrowser(int browser);
    void addItemToList();
    void addItemToFamilyList();
    void setPage(int i);
}