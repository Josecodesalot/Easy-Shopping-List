package com.joseapps.simpleshoppinglist.Interface;

public interface AddItemInterface {
    void setItemName(String itemName);
    void setQuantity(int quantity);
    void setlistName(String listName);
    void setPrice(double itemPrice);
    void openBrowser(int browser, String walmart, String coscto, String zhers, String metro);
    void addItemToList();
    void addItemToFamilyList();
    void setPage(int i);
}