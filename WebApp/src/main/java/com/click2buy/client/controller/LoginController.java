package com.click2buy.client.controller;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import com.click2buy.client.model.User;
import com.click2buy.client.security.JWTTokenUtil;
import com.click2buy.client.security.dto.AccountCredentials;
import com.click2buy.client.security.dto.LoginResponse;
import com.click2buy.client.service.UserService;
import com.click2buy.client.service.implementation.UserServiceImpl;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class LoginController {

  private final AuthenticationManager authenticationManager;
  private final UserServiceImpl userService;
  private final JWTTokenUtil jwtTokenUtil;

  public LoginController(
    AuthenticationManager authenticationManager, UserServiceImpl userService,
    JWTTokenUtil jwtTokenUtil) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
  public ResponseEntity<LoginResponse>  login(@RequestBody AccountCredentials creds) {
    authenticate(creds.getUsername(), creds.getPassword());
    UserDetails userDetails = userService.loadUserByUsername(creds.getUsername());
    final String token = jwtTokenUtil.generateToken(userDetails);
    // Return the token
    return ok(new LoginResponse(token));
  }


  @RequestMapping(value = "/sign-up", method = RequestMethod.GET)
  public ModelAndView registration() {
    ModelAndView modelAndView = new ModelAndView();
    User user = new User();
    modelAndView.addObject("user", user);
    modelAndView.setViewName("registration");
    return modelAndView;
  }

  @RequestMapping(value = "/registration", method = RequestMethod.POST)
  public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
    ModelAndView modelAndView = new ModelAndView();
    User userExists = userService.findUserByPhone(user.getPhone());
    if (userExists != null) {
      bindingResult
        .rejectValue("phone", "error.user",
          "There is already a user registered with the phone provided");
    }
    if (bindingResult.hasErrors()) {
      modelAndView.setViewName("registration");
    } else {
      userService.saveUser(user);
      modelAndView.addObject("successMessage", "User has been registered successfully");
      modelAndView.addObject("user", new User());
      modelAndView.setViewName("registration");

    }
    return modelAndView;
  }

  @ExceptionHandler({AuthenticationException.class})
  public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
  }

  private void authenticate(String username, String password) {
    Objects.requireNonNull(username);
    Objects.requireNonNull(password);
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

  }
}
