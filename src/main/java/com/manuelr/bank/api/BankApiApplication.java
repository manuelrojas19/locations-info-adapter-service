package com.manuelr.bank.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;

@SpringBootApplication(exclude = {
    MongoReactiveAutoConfiguration.class,
    MongoReactiveDataAutoConfiguration.class,
    RedisReactiveAutoConfiguration.class,
    RedisAutoConfiguration.class
})
public class BankApiApplication {

  /**
   * Main Application.
   *
   * @param args args
   */
  public static void main(String[] args) {
    SpringApplication.run(BankApiApplication.class, args);
  }

}
