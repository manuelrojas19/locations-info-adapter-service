package com.manuelr.bank.api.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import java.util.Objects;
import java.util.function.Function;

public final class RequestUtils {

  private RequestUtils() {
    throw new UnsupportedOperationException(Constants.UNSUPPORTED_OPERATION_EXCEPTION_MSG);
  }

  public static PageRequest getPageRequestFromRequest(ServerRequest request) {

    final var page = getQueryParam(request, Constants.PAGE_HEADER, Integer::parseInt);
    final var size = getQueryParam(request, Constants.SIZE_HEADER, Integer::parseInt);

    return PageRequest.of(
        Objects.nonNull(page) ? page : Constants.DEFAULT_PAGE,
        Objects.nonNull(size) ? size : Constants.DEFAULT_SIZE);
  }

  public static <T> T getQueryParam(
      ServerRequest request, String queryParam, Function<String, T> queryResolver) {
    return request.queryParam(queryParam).isEmpty() ? null
        : queryResolver.apply(request.queryParam(queryParam).get());
  }

  public static <T> List<T> getQueryParamList(
      ServerRequest request, String queryParam, Function<String, T> queryResolver) {
    final var strings = request.queryParams().get(queryParam);
    if (Objects.isNull(strings)) {
      return new ArrayList<>();
    }
    return strings.stream()
        .map(queryResolver)
        .collect(Collectors.toList());
  }
}
