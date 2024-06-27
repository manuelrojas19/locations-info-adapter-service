package com.manuelr.bank.api.model;

import com.google.common.base.Objects;
import com.manuelr.bank.api.enums.BankTypes;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

@Value
@Builder
public class FindBankRequestFilterData {

  Pageable pageable;

  Double latitude;

  Double longitude;

  Double startLatitude;

  Double endLatitude;

  Double startLongitude;

  Double endLongitude;

  String postalCode;

  List<BankTypes> bankTypes;

  String state;

  String address;

  MultiValueMap<String, String> headers;

  @Override
  public int hashCode() {
    return Objects.hashCode(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        latitude,
        longitude,
        startLatitude,
        endLatitude,
        startLongitude,
        endLongitude,
        postalCode,
        bankTypes,
        state,
        address);
  }
}
