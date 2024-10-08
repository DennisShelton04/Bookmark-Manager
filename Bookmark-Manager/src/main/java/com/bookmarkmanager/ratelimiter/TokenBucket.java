package com.bookmarkmanager.ratelimiter;

public class TokenBucket {
  private long tokens;
  private long lastRefillTime;
  private final long maxTokens;
  private final long refillRate;
  private final long refillInterval;

  public TokenBucket(long maxTokens, long refillRate, long refillInterval) {
    this.maxTokens = maxTokens;
    this.refillRate = refillRate;
    this.refillInterval = refillInterval;
    this.tokens = maxTokens;
    this.lastRefillTime = System.currentTimeMillis();
  }

  public synchronized boolean tryConsume(int numTokens) {
    refill();
    if (tokens >= numTokens) {
      tokens -= numTokens;
      return true;
    }
    return false;
  }

  public synchronized long getAvailableTokens() {
    refill(); // Ensure tokens are refilled before checking
    return tokens;
  }

  public synchronized long getResetTime() {
    long nextResetTime = lastRefillTime + refillInterval;
    return Math.max(0, nextResetTime - System.currentTimeMillis());
  }



  private void refill() {
    long now = System.currentTimeMillis();
    long timeElapsed = now - lastRefillTime;

    long tokensToAdd = (timeElapsed / refillInterval) * refillRate;
    tokens = Math.min(maxTokens, tokens + tokensToAdd);
    lastRefillTime = now;
  }
}
