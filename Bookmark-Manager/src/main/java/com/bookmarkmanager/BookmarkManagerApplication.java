package com.bookmarkmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.bookmarkmanager"})
public class BookmarkManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookmarkManagerApplication.class, args);
	}

}
