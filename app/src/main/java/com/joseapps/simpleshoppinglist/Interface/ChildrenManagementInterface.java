package com.joseapps.simpleshoppinglist.Interface;

import com.joseapps.simpleshoppinglist.Models.User;

public interface ChildrenManagementInterface {
    void OpenDialog(int position);
    void Delete(User user);
    void SendBack(User user);
}
