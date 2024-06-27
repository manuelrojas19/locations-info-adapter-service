package com.manuelr.bank.api.handler;

import com.manuelr.bank.api.model.ApiInfoResponse;
import com.manuelr.bank.api.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class InfoHandler implements HandlerFunction<ServerResponse> {

  private final String hostName;

  public InfoHandler(@Value("${application.information.host}") final String hostName) {
    this.hostName = hostName;
  }

  @NonNull
  @Override
  public Mono<ServerResponse> handle(@NonNull ServerRequest request) {

    log.info(Constants.REQUEST_RECEIVED_LOG_MSG, InfoHandler.class.getName());

    return ServerResponse.ok()
        .body(
            BodyInserters.fromValue(
                ApiInfoResponse.builder()
                    .sourceHostName(hostName)
                    .status(HttpStatus.OK.name())
                    .build()))
        .doOnNext(
            response -> log.info(Constants.SENDING_RESPONSE_LOG_MSG, InfoHandler.class.getName()));
  }
}
