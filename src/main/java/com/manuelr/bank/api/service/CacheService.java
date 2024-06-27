package com.manuelr.bank.api.service;

import com.manuelr.bank.api.model.FindBankRequestFilterData;
import com.manuelr.bank.api.model.FindBankResponse;
import com.manuelr.bank.api.utils.Constants;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface CacheService<T> {

  Mono<T> save(int cacheId, T data);

  Mono<T> findById(int cacheId);
}