package com.click2buy.client.service.implementation;

import com.click2buy.client.model.Role;
import com.click2buy.client.model.RoleType;
import com.click2buy.client.model.User;
import com.click2buy.client.repository.RoleRepository;
import com.click2buy.client.repository.UserRepository;
import com.click2buy.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;

public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public User findUserByPhone(int phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByRole(RoleType.STANDARD_USER);
        user.setRoles(new HashSet<Role>(Arrays.asList(role)));
        userRepository.save(user);
    }
}
