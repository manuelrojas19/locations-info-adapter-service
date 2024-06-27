package com.manuelr.bank.api.service.impl;

import static com.manuelr.bank.api.utils.QueryHelper.hasAddress;
import static com.manuelr.bank.api.utils.QueryHelper.hasLocation;
import static com.manuelr.bank.api.utils.QueryHelper.hasLocationQuadrant;
import static com.manuelr.bank.api.utils.QueryHelper.hasPostalCode;
import static com.manuelr.bank.api.utils.QueryHelper.hasPostalCodeAndState;
import static com.manuelr.bank.api.utils.QueryHelper.hasState;

import com.manuelr.bank.api.entity.BankEntity;
import com.manuelr.bank.api.enums.BankTypes;
import com.manuelr.bank.api.mapper.BankDataMapper;
import com.manuelr.bank.api.model.BankDto;
import com.manuelr.bank.api.model.FindBankRequestFilterData;
import com.manuelr.bank.api.model.FindBankResponse;
import com.manuelr.bank.api.repository.BankRepository;
import com.manuelr.bank.api.service.BankService;
import com.manuelr.bank.api.service.CacheService;
import com.manuelr.bank.api.utils.Constants;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Slf4j
@Service
public class BankServiceImpl implements BankService {

  private final BankRepository bankRepository;

  private final CacheService<FindBankResponse> bankCacheService;

  private final Boolean isRedisCacheEnable;

  @Autowired
  public BankServiceImpl(final BankRepository bankRepository,
      final CacheService<FindBankResponse> bankCacheService,
      @Value("${application.features.redis-cache}") final Boolean isRedisCacheEnable) {
    this.bankRepository = bankRepository;
    this.bankCacheService = bankCacheService;
    this.isRedisCacheEnable = isRedisCacheEnable;
  }

  @Override
  public Mono<FindBankResponse> findBankData(FindBankRequestFilterData requestData) {
    log.info("Retrieving bank information");
    log.info("Requested data {}", requestData);
    if (!isRedisCacheEnable) {
      log.info("Redis cache is disable retrieving directly from database");
      return getBankDataFromDatabase(requestData)
          .doOnNext(bank -> log.info(Constants.BANK_INFO_RETRIEVED_LOG_MSG, bank))
          .doOnError(error -> log.error(Constants.BANK_INFO_ERROR_LOG_MSG, error));
    }
    log.info("Redis cache is enable trying to retrieve from cache");
    return getBankDataFromCache(requestData)
        .doOnNext(bank -> log.info(Constants.BANK_INFO_RETRIEVED_LOG_MSG, bank))
        .doOnError(error -> log.error(Constants.BANK_INFO_ERROR_LOG_MSG, error));
  }

  private Mono<FindBankResponse> getBankDataFromCache(FindBankRequestFilterData requestData) {
    return bankCacheService.findById(requestData.hashCode())
        .switchIfEmpty(getBankDataFromDatabase(requestData));
  }

  private Mono<FindBankResponse> getBankDataFromDatabase(FindBankRequestFilterData requestData) {
    log.info("Retrieving bank information from database");
    final var types = requestData.getBankTypes();
    final var getDatabaseResponse = searchBankEntitiesByLocationCriteria(requestData)
        .filter(bankEntity -> filterBankEntitiesByTypes(bankEntity, types))
        .map(BankDataMapper::bankEntityToBankDto).collectList().zipWith(bankRepository.countAllBy())
        .map(tuples -> toFindBankResponse(requestData, tuples));
    if (isRedisCacheEnable) {
      log.info("Redis cache is enable, caching database response");
      return getDatabaseResponse.flatMap(
          response -> bankCacheService.save(requestData.hashCode(), response)
              .onErrorResume(e -> Mono.just(response)));
    }
    return getDatabaseResponse;
  }

  private static boolean filterBankEntitiesByTypes(BankEntity bankEntity, List<BankTypes> types) {
    if (types.isEmpty()) {
      return true;
    }
    return types.contains(BankTypes.fromString(bankEntity.getType()));
  }

  private static FindBankResponse toFindBankResponse(FindBankRequestFilterData requestData,
      Tuple2<List<BankDto>, Long> tuples) {
    final var bankData = tuples.getT1();
    final var numberOfElements = tuples.getT2();
    return BankDataMapper.toFindBankResponse(requestData.getPageable(), bankData, numberOfElements);
  }

  private Flux<BankEntity> searchBankEntitiesByLocationCriteria(
      FindBankRequestFilterData requestData) {
    final var latitude = requestData.getLatitude();
    final var longitude = requestData.getLongitude();
    final var startLatitude = requestData.getStartLatitude();
    final var startLongitude = requestData.getStartLongitude();
    final var endLatitude = requestData.getEndLatitude();
    final var endLongitude = requestData.getEndLongitude();
    final var postalCode = requestData.getPostalCode();
    final var state = requestData.getState();
    final var address = requestData.getAddress();
    final var pageable = requestData.getPageable();

    if (hasLocationQuadrant(startLatitude, endLatitude, startLongitude, endLongitude)) {
      log.info("Finding by Quadrant");
      return findByLocationQuadrant(startLatitude, endLatitude, startLongitude, endLongitude);
    } else if (hasLocation(latitude, longitude)) {
      log.info("Finding by Location");
      return findByLocation(latitude, longitude, pageable);
    } else if (hasPostalCodeAndState(postalCode, state)) {
      log.info("Finding by Postal Code and State");
      return findByPostalCodeAndState(postalCode, state, pageable);
    } else if (hasPostalCode(postalCode)) {
      log.info("Finding by Postal Code");
      return findByPostalCode(postalCode, pageable);
    } else if (hasState(state)) {
      log.info("Finding by State");
      return findByState(state, pageable);
    } else if (hasAddress(address)) {
      log.info("Finding by Address");
      return findByAddress(address, pageable);
    } else {
      return findAll(pageable);
    }
  }

  private Flux<BankEntity> findByLocationQuadrant(Double startLatitude, Double endLatitude,
      Double startLongitude, Double endLongitude) {
    final var minLat = Math.min(startLatitude, endLatitude);
    final var maxLat = Math.max(startLatitude, endLatitude);
    final var minLng = Math.min(startLongitude, endLongitude);
    final var maxLng = Math.max(startLongitude, endLongitude);
    return bankRepository.findAllByLocation_LatitudeBetweenAndLocation_LongitudeBetween(
        minLat, maxLat, minLng, maxLng);
  }

  private Flux<BankEntity> findByLocation(Double latitude, Double longitude, Pageable pageable) {
    return bankRepository.findAllByLocation_LatitudeBetweenAndLocation_LongitudeBetween(
        latitude - Constants.LOCATION_MARGIN, latitude + Constants.LOCATION_MARGIN,
        longitude - Constants.LOCATION_MARGIN, longitude + Constants.LOCATION_MARGIN, pageable);
  }

  private Flux<BankEntity> findByPostalCodeAndState(String postalCode, String state,
      Pageable pageable) {
    return bankRepository.findAllByPostalCodeAndStateIgnoreCase(postalCode, state, pageable);
  }

  private Flux<BankEntity> findByPostalCode(String postalCode, Pageable pageable) {
    return bankRepository.findAllByPostalCode(postalCode, pageable);
  }

  private Flux<BankEntity> findByState(String state, Pageable pageable) {
    return bankRepository.findAllByStateIgnoreCase(state, pageable);
  }

  private Flux<BankEntity> findByAddress(String address, Pageable pageable) {
    return bankRepository.findAllByAddressContainingIgnoreCase(address, pageable);
  }

  private Flux<BankEntity> findAll(Pageable pageable) {
    return bankRepository.findAllBy(pageable);
  }

}
