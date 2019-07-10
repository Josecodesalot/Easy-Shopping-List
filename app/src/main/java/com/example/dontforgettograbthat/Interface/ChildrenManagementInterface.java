package com.example.dontforgettograbthat.Interface;

import com.example.dontforgettograbthat.Models.User;

public interface ChildrenManagementInterface {
    void OpenDialog(int position);
    void Delete(User user);
    void SendBack(User user);
}
