package com.manuelr.bank.api.service.impl;

import com.manuelr.bank.api.model.FindBankResponse;
import com.manuelr.bank.api.service.CacheService;
import com.manuelr.bank.api.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@ConditionalOnProperty(
    value = "application.features.redis-cache",
    havingValue = "true")
public class BankCacheServiceImpl implements CacheService<FindBankResponse> {

  private final ReactiveHashOperations<String, Integer, FindBankResponse> hashOperations;

  @Autowired
  public BankCacheServiceImpl(
      final ReactiveHashOperations<String, Integer, FindBankResponse> hashOperations) {
    this.hashOperations = hashOperations;
  }

  @Override
  public Mono<FindBankResponse> save(int cacheId, FindBankResponse data) {
    log.info("Saving bank information on cache");
    return hashOperations
        .put(Constants.ATM_CACHE_NAME, cacheId, data)
        .thenReturn(data)
        .doOnNext(bank -> log.info("Information saved on cache --> {}", bank))
        .doOnError(error -> log.error(Constants.BANK_INFO_ERROR_LOG_MSG, error));
  }

  @Override
  public Mono<FindBankResponse> findById(int cacheId) {
    log.info("Retrieving bank information from cache");
    return hashOperations.get(Constants.ATM_CACHE_NAME, cacheId)
        .doOnNext(bank -> log.info("Information retrieved from cache --> {}", bank))
        .doOnError(error -> log.error(Constants.BANK_INFO_ERROR_LOG_MSG, error));
  }

}
