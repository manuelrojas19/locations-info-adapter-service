package com.manuelr.bank.api.handler;

import com.manuelr.bank.api.model.FindBankRequestFilterData;
import com.manuelr.bank.api.service.BankService;
import com.manuelr.bank.api.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.manuelr.bank.api.utils.QueryHelper.getFindBankRequestData;

@Slf4j
@Component
public class FindBankHandler implements HandlerFunction<ServerResponse> {

  private final BankService bankService;

  @Autowired
  public FindBankHandler(BankService bankService) {
    this.bankService = bankService;
  }

  @NonNull
  @Override
  public Mono<ServerResponse> handle(@NonNull ServerRequest request) {

    log.info(Constants.REQUEST_RECEIVED_LOG_MSG, FindBankHandler.class.getName());

    final FindBankRequestFilterData requestData = getFindBankRequestData(request);

    return bankService
        .findBankData(requestData)
        .flatMap(findBankResponse -> ServerResponse.ok().bodyValue(findBankResponse))
        .doOnNext(response -> log.info(Constants.SENDING_RESPONSE_LOG_MSG, response))
        .doOnError(throwable -> log.error(Constants.BANK_INFO_ERROR_LOG_MSG, throwable));
  }

}
