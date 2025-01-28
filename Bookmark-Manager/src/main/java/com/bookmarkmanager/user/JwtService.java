package com.bookmarkmanager.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    public static final int EXPIRE = 1000 * 60 * 10;
    private final String secret;

    public JwtService(@Value("${JWT_SECRET_KEY}") String secretKey) {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalArgumentException("JWT secret key not found in environment variables.");
        }
        this.secret = secretKey;
        validateSecretKey();
    }

    private void validateSecretKey() {
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            String encodedSecret = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            System.out.println("Base64-encoded Secret Key: " + encodedSecret);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing HMAC with secret", e);
        }
    }

    public String getSecret() {
        return secret;
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(getKey())
                .compact();
    }

    private Key getKey() {
        System.out.println(secret);
        validateSecretKey();
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        System.out.println(decodedKey.length);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
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

    public String exchangeToken(String oldToken) {
        String username = extractUsername(oldToken);
        if (username != null && !isTokenExpired(oldToken)) {
            return generateToken(username);
        }
        throw new IllegalArgumentException("Invalid or expired token");
    }
}
