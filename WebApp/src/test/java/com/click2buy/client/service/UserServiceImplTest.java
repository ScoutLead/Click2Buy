package com.click2buy.client.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import com.click2buy.client.model.Role;
import com.click2buy.client.model.RoleType;
import com.click2buy.client.model.User;
import com.click2buy.client.repository.RoleRepository;
import com.click2buy.client.repository.UserRepository;
import com.click2buy.client.service.implementation.UserServiceImpl;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserServiceImplTest {

  private UserRepository userRepository;

  private RoleRepository roleRepository;

  private BCryptPasswordEncoder bCryptPasswordEncoder;

  private UserServiceImpl userService;

  @Before
  public void setUp() {
    userRepository = Mockito.mock(UserRepository.class);
    roleRepository = Mockito.mock(RoleRepository.class);
    bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
    userService = new UserServiceImpl(userRepository,
        roleRepository, bCryptPasswordEncoder);
  }

  @Test
  public void saveUser() {
    String email = "alex@some.com";
    User user = new User();
    user.setEmail(email);
    user.setPassword("pass");
    String expectedPass = "pass1";
    when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn(expectedPass);
    Role role = new Role();
    role.setRole(RoleType.STANDARD_USER);
    when(roleRepository.findByRole(RoleType.STANDARD_USER)).thenReturn(role);
    when(userRepository.save(user)).thenReturn(user);

    User actualUser = userService.saveUser(user);

    assertEquals(expectedPass, actualUser.getPassword());
    assertThat(actualUser.getRoles(), contains(role));
  }


  @Test
  public void findByEmail_whenValidEmail_thenUserShouldBeFound() {
    String email = "alex@some.com";
    User user = new User();
    user.setEmail(email);
    when(userRepository.findByEmail(email)).thenReturn(user);

    Optional<User> found = userService.findByEmail(email);

    Assert.assertTrue(found.isPresent());
    assertThat(found.get().getEmail(), is(email));
  }

  @Test
  public void findByEmail_whenInValidEmail_thenUserShouldNotBeFound() {
    String email = "alex@some.com";
    User user = new User();
    user.setEmail(email);
    when(userRepository.findByEmail(email)).thenReturn(null);

    Optional<User> found = userService.findByEmail(email);

    Assert.assertFalse(found.isPresent());
  }

  @Test
  public void loadUserByUsername_whenValidEmail_thenUserShouldBeFound() {
    String email = "alex@some.com";
    String pass = "pass";
    User user = new User();
    user.setEmail(email);
    user.setPassword(pass);
    when(userRepository.findByEmail(email)).thenReturn(user);

    UserDetails found = userService.loadUserByUsername(email);

    assertEquals(email, found.getUsername());
    assertEquals(pass, found.getPassword());
    assertThat(found.getAuthorities(), empty());
  }

  @Test
  public void loadUserByUsername_whenInValidEmail_thenUsernameNotFoundExceptionThrown() {
    String email = "alex@some.com";
    User user = new User();
    user.setEmail(email);
    when(userRepository.findByEmail(email)).thenReturn(null);

    try {
      userService.loadUserByUsername(email);
      fail();
    } catch (UsernameNotFoundException e) {
      assertEquals(e.getMessage(), email);
    }

  }

}
