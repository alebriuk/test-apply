package com.challenge.salasia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ArticleApp {

  public static void main(String[] args) {
    SpringApplication.run(ArticleApp.class, args);
  }
}
