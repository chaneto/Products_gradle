package com.example.Products_gradle.config;

import java.util.concurrent.locks.ReentrantLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReentrantLockConfig {

  @Bean ReentrantLock reentrantLock(){
    return new ReentrantLock();
  }
}
