package com.joseapps.simpleshoppinglist.utils;

import android.support.annotation.NonNull;

import com.joseapps.simpleshoppinglist.Models.Item;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemListHelper {
    private ArrayList<Item>items;
    public ItemListHelper(ArrayList<Item> items){
        this.items=items;
    }
    public void updateItems(ArrayList<Item>items){
        this.items=items;
    }

    public HashMap<String, ArrayList<Item>> getItemMapByListName (){
        HashMap<String, ArrayList<Item>> itemsByListName = new HashMap<>();
        ArrayList<String> listNames = getListNames();

        for (int i=0 ; i<listNames.size() ;i++){
            itemsByListName.put(listNames.get(i),getItemsByListName(listNames.get(i)));
        }
        return itemsByListName;
    }

    public ArrayList<String> getListNames(){
        ArrayList<String> listNames =  new ArrayList<>();
        for (int i=0;i<items.size();i++){
            //below exists method loops all listNames, to see if the current list name i exist within it.
            if (!exists(listNames, items.get(i).getList_name())){
                //If the title doesnt already exist, it will add it to the list, keeping only one itemnameleft
                listNames.add(items.get(i).getList_name());
            }
        }
        return listNames;
    }
    private boolean exists(@NonNull ArrayList<String> listnames, String itemListName){
        for (int i = 0 ; i< listnames.size(); i++) {
            if (listnames.get(i).equals(itemListName)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Item> getItemsByListName(String itemListName){
        ArrayList<Item> itemsByListName = new ArrayList<>();
        for (int i = 0 ; i < items.size() ; i++){
            if (items.get(i).getList_name().equals(itemListName)){
                itemsByListName.add(items.get(i));
            }
        }
        return itemsByListName;
    }
}


