package com.bookmarkmanager.user;

import com.bookmarkmanager.pojo.User;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtService jwtService;

  @Autowired
  AuthenticationManager authenticationManager;
  public User registerUser(User user) {
    return userRepository.save(user);
  }

  public String  verifyUser(User user) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    if(authentication.isAuthenticated()) {
      return jwtService.generateToken(user.getUsername());
    }
    return "failure";
  }

  public String exchangeForToken(@NonNull String token) {
    return jwtService.exchangeToken(token);
  }
}
