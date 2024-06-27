package com.manuelr.bank.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankPage {

  @JsonProperty("ResultCount")
  Integer resultCount;

  @JsonProperty("PageNumber")
  Integer number;

  @JsonProperty("PageSize")
  Integer pageSize;

  @JsonProperty("TotalPages")
  Integer totalPages;

  @JsonProperty("TotalElements")
  Long totalElements;
}
