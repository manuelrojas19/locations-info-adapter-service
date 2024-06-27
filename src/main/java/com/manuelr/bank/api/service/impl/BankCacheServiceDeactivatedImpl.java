package com.manuelr.bank.api.service.impl;

import com.manuelr.bank.api.model.FindBankResponse;
import com.manuelr.bank.api.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@ConditionalOnProperty(
    value = "application.features.redis-cache",
    havingValue = "false")
public class BankCacheServiceDeactivatedImpl implements CacheService<FindBankResponse> {

  private static final String MSG = "Redis cache is disabled from configuration, unsupported operation";

  @Override
  public Mono<FindBankResponse> save(int cacheId, FindBankResponse data) {
    log.info("Cache service is disabled from configuration");
    throw new UnsupportedOperationException(MSG);
  }

  @Override
  public Mono<FindBankResponse> findById(int cacheId) {
    log.info("Cache service is disabled from configuration");
    throw new UnsupportedOperationException(MSG);
  }
}
