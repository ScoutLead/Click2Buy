package com.click2buy.client.service.implementation;

import com.click2buy.client.model.Role;
import com.click2buy.client.model.RoleType;
import com.click2buy.client.model.User;
import com.click2buy.client.repository.RoleRepository;
import com.click2buy.client.repository.UserRepository;
import com.click2buy.client.service.UserService;
import java.util.Collections;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

import static java.util.Collections.emptyList;

@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {

  private UserRepository userRepository;

  private RoleRepository roleRepository;

  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserServiceImpl(
      @Qualifier("userRepository") UserRepository userRepository,
      @Qualifier("roleRepository") RoleRepository roleRepository,
      BCryptPasswordEncoder encoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.bCryptPasswordEncoder = encoder;
  }

  @Override
  public User findUserByPhone(String phone) {
    return userRepository.findByPhone(phone);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return Optional.ofNullable(userRepository.findByEmail(email));
  }

  @Override
  public User saveUser(User user) {
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

    Role role = roleRepository.findByRole(RoleType.STANDARD_USER);
    user.setRoles(new HashSet<>(Collections.singletonList(role)));
    return userRepository.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    return new org.springframework.security.core.userdetails.User(
        user.getEmail(),
        user.getPassword(),
        emptyList());
  }
}
