package com.bookmarkmanager.ratelimiter;

import com.bookmarkmanager.annotation.RateLimited;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
public class RateLimitInterceptor implements HandlerInterceptor {
  private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (handler instanceof HandlerMethod handlerMethod) {
      RateLimited rateLimited = handlerMethod.getMethodAnnotation(RateLimited.class);
      if (rateLimited != null) {
        String clientIP = request.getRemoteAddr();
        TokenBucket bucket = buckets.computeIfAbsent(clientIP, k -> new TokenBucket(rateLimited.maxTokens(), rateLimited.refillRate(), rateLimited.refillInterval()));
        response.setHeader("X-RateLimit-Limit", String.valueOf(rateLimited.maxTokens()));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(bucket.getAvailableTokens())); // Assuming you have a method to get available tokens
        response.setHeader("X-RateLimit-Reset", String.valueOf(bucket.getResetTime())); // Implement this method in TokenBucket
        if (bucket.tryConsume(1)) {
          return true; // Proceed with the request
        } else {
          response.setStatus(429); // Rate limit exceeded
          return false;
        }
      }
    }
    return true; // Proceed if no rate limit
  }
}
