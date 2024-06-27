package com.manuelr.bank.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FindBankByIdResponse {
  @JsonProperty("Bank")
  BankDto bankDto;

}
