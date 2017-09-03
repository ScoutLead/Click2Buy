package com.click2buy.client.service;

import com.click2buy.client.model.User;

public interface UserService {
    User findUserByPhone(String phone);
    void saveUser(User user);
}
