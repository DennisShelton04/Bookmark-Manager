package com.bookmarkmanager.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimited {
  long maxTokens() default 10; // Maximum tokens
  long refillRate() default 1; // Tokens added per second
  long refillInterval() default 1000;
}
