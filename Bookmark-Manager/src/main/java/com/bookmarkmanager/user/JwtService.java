package com.bookmarkmanager.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private String secret;

  public JwtService(){
    try {
      KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
      SecretKey secretKey = keyGenerator.generateKey();
      secret = Base64.getEncoder().encodeToString(secretKey.getEncoded());
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }


    public String generateToken(String username) {
      Map<String, Objects> claim = new HashMap<>();

      return Jwts.builder()
              .setClaims(claim)
              .setSubject(username)
              .setIssuedAt(new Date())
              .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
              .signWith(getKey())
              .compact();
    }

  private Key getKey() {
    byte[] decodedKey = Base64.getDecoder().decode(secret);
    return Keys.hmacShaKeyFor(decodedKey);
  }

  public String extractUsername(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
  }


  public boolean validateToken(String token, UserDetails userDetails) {
    String username = extractUsername(token);  // Extract username from the token
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token) {
    Date expiration = extractExpiration(token);
    return expiration.before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractAllClaims(token).getExpiration();
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
  }
}
