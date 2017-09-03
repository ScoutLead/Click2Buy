package com.click2buy.client.service;

import com.click2buy.client.model.User;

public interface UserService {
    public User findUserByPhone(int phone);
    public void saveUser(User user);
}
