package com.example.dontforgettograbthat.Interface;

import com.example.dontforgettograbthat.Models.User;

public interface AcceptDeleteOrHoldInterface {
    void accept (int position);
    void reject (int position );
    // sends accepted user into the request category
    void hold (int position );
    void dialog (int position);
}
