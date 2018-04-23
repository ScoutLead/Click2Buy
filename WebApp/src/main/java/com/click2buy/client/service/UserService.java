package com.click2buy.client.service;

import com.click2buy.client.model.User;
import java.util.Optional;

public interface UserService {

  User findUserByPhone(String phone);

  Optional<User> findByEmail(String email);

  User saveUser(User user);
}
