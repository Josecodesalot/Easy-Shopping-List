package com.joseapps.simpleshoppinglist.Interface;

import com.joseapps.simpleshoppinglist.Models.User;

public interface ChildrenRequestInterface {
    void accept (User user);
    void reject (User user );
    // sends accepted user into the request category
    void dialog (int position);
}
