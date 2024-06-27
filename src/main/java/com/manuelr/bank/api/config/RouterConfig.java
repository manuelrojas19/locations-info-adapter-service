package com.manuelr.bank.api.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.manuelr.bank.api.handler.FindBankHandler;
import com.manuelr.bank.api.handler.InfoHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Configuration
public class RouterConfig {

  private final FindBankHandler findBankHandler;

  private final InfoHandler infoHandler;

  private final String basePath;

  private final String findBanksApiPath;

  private final String healthCheck;

  public RouterConfig(
      final FindBankHandler findBankHandler,
      final InfoHandler infoHandler,
      @Value("${application.apis.base-path}") final String basePath,
      @Value("${application.apis.find-banks-path}") final String findBanksApiPath,
      @Value("${application.apis.health-check-path}") final String infoPath) {
    this.findBankHandler = findBankHandler;
    this.infoHandler = infoHandler;
    this.basePath = basePath;
    this.findBanksApiPath = findBanksApiPath;
    this.healthCheck = infoPath;
  }

  @Bean
  public RouterFunction<ServerResponse> routes() {
    return RouterFunctions.route(GET(basePath.concat(findBanksApiPath)), findBankHandler)
        .and(route(GET(basePath.concat(healthCheck)), infoHandler));
  }
}
