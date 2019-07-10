package com.example.dontforgettograbthat.Interface;

import com.example.dontforgettograbthat.Models.User;

public interface ChildrenRequestInterface {
    void accept (User user);
    void reject (User user );
    // sends accepted user into the request category
    void dialog (int position);
}
