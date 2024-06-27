package com.manuelr.bank.api.utils;

import com.manuelr.bank.api.enums.BankTypes;
import com.manuelr.bank.api.model.FindBankRequestFilterData;
import org.springframework.web.reactive.function.server.ServerRequest;

public class QueryHelper {

  private QueryHelper() {
    throw new UnsupportedOperationException(Constants.UNSUPPORTED_OPERATION_EXCEPTION_MSG);
  }

  public static FindBankRequestFilterData getFindBankRequestData(ServerRequest request) {

    final var pageable = RequestUtils.getPageRequestFromRequest(request);
    final var headers = request.headers().asHttpHeaders();

    final var latitude =
        RequestUtils.getQueryParam(request, Constants.LATITUDE_QUERY_PARAM, Double::valueOf);
    final var longitude =
        RequestUtils.getQueryParam(request, Constants.LONGITUDE_QUERY_PARAM, Double::valueOf);
    final var startLatitude =
        RequestUtils.getQueryParam(request, "startLatitude", Double::valueOf);
    final var startLongitude =
        RequestUtils.getQueryParam(request, "startLongitude", Double::valueOf);
    final var endLatitude =
        RequestUtils.getQueryParam(request, "endLatitude", Double::valueOf);
    final var endLongitude =
        RequestUtils.getQueryParam(request, "endLongitude", Double::valueOf);
    final var postalCode =
        RequestUtils.getQueryParam(request, Constants.POSTAL_CODE_QUERY_PARAM, String::toString);
    final var state =
        RequestUtils.getQueryParam(request, Constants.STATE_QUERY_PARAM, String::toString);
    final var address =
        RequestUtils.getQueryParam(request, Constants.ADDRESS_QUERY_PARAM, String::toString);
    final var types = RequestUtils.getQueryParamList(request, Constants.TYPES_QUERY_PARAM,
        QueryHelper::convertBankTypeEnum);

    return FindBankRequestFilterData.builder()
        .latitude(latitude)
        .longitude(longitude)
        .startLatitude(startLatitude)
        .startLongitude(startLongitude)
        .endLatitude(endLatitude)
        .endLongitude(endLongitude)
        .bankTypes(types)
        .postalCode(postalCode)
        .state(state)
        .address(address)
        .pageable(pageable)
        .headers(headers)
        .build();
  }

  private static BankTypes convertBankTypeEnum(String s) {
    try {
      return BankTypes.valueOf(s);
    } catch (IllegalArgumentException e) {
      return BankTypes.DEFAULT;
    }
  }

  public static boolean hasLocationQuadrant(Double startLatitude, Double endLatitude, Double startLongitude, Double endLongitude) {
    return startLatitude != null && endLatitude != null && startLongitude != null  && endLongitude != null ;
  }

  public static boolean hasLocation(Double latitude, Double longitude) {
    return latitude != null && longitude != null;
  }

  public static boolean hasPostalCodeAndState(String postalCode, String state) {
    return postalCode != null && state != null;
  }

  public static boolean hasPostalCode(String postalCode) {
    return postalCode != null;
  }

  public static boolean hasState(String state) {
    return state != null;
  }

  public static boolean hasAddress(String address) {
    return address != null;
  }
}
