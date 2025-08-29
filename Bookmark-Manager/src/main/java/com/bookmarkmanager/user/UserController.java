package com.bookmarkmanager.user;

import com.bookmarkmanager.pojo.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
  @Autowired
  public UserService userService;

  private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);
  @PostMapping("/register")
  public User registerUser(@RequestBody User user) {
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    return userService.registerUser(user);
  }

  @PostMapping("/login")
    public String  loginUser(@RequestBody User user) {
        return userService.verifyUser(user);
    }

  @GetMapping("/exchange-token")
  public String exchangeForToken(@RequestParam String token) {
    return userService.exchangeForToken(token);
  }



}
