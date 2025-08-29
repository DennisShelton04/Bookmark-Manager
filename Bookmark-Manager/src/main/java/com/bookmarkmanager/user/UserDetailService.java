package com.bookmarkmanager.user;

import com.bookmarkmanager.pojo.User;
import com.bookmarkmanager.pojo.UserPrinciple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements org.springframework.security.core.userdetails.UserDetailsService {

  @Autowired
    private UserRepository userRepo;
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepo.findByUsername(username);
    if(user==null){
      System.out.println("user Not Found");
    }
    return new UserPrinciple(user);
  }
}
