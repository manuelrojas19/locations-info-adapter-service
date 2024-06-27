package com.manuelr.bank.api.mapper;

import com.manuelr.bank.api.entity.BankEntity;
import com.manuelr.bank.api.model.*;
import com.manuelr.bank.api.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j

public class BankDataMapper {

  private BankDataMapper() {
    throw new UnsupportedOperationException(Constants.UNSUPPORTED_OPERATION_EXCEPTION_MSG);
  }

  public static BankDto bankEntityToBankDto(BankEntity bankEntity) {
    return BankDto.builder()
        .id(bankEntity.getId())
        .name(bankEntity.getName())
        .references(bankEntity.getReferences())
        .street(bankEntity.getStreet())
        .state(bankEntity.getState())
        .address(bankEntity.getAddress())
        .postalCode(bankEntity.getPostalCode())
        .type(bankEntity.getType())
        .openingTime(
            OpeningTime.builder()
                .open(bankEntity.getOpeningTime().getOpen())
                .close(bankEntity.getOpeningTime().getClose())
                .build())
        .location(
            Location.builder()
                .latitude(bankEntity.getLocation().getLatitude())
                .longitude(bankEntity.getLocation().getLongitude())
                .build())
        .build();
  }

  private static BankData bankDtoListToBankData(List<BankDto> bankDtoList) {
    return BankData.builder().banks(bankDtoList).build();
  }

  public static FindBankResponse toFindBankResponse(
      Pageable pageable, List<BankDto> bankDtoList, Long bankTotalNumber) {
    return FindBankResponse.builder()
        .data(BankDataMapper.bankDtoListToBankData(bankDtoList))
        .page(
            buildPageResponse(bankDtoList.size(), pageable.getPageSize(), pageable.getPageNumber(),
                bankTotalNumber))
        .build();
  }

  private static BankPage buildPageResponse(Integer elementsPerPage,
      Integer pageSize, Integer pageNumber, Long totalElements) {
    return BankPage.builder()
        .resultCount(elementsPerPage)
        .pageSize(pageSize)
        .number(pageNumber)
        .totalPages(getTotalPages(elementsPerPage, (double) pageSize, totalElements))
        .totalElements(totalElements)
        .build();
  }

  private static int getTotalPages(Integer elementsPerPage, double pageSize, Long totalElements) {
    if (elementsPerPage == 0) return 0;
    if (elementsPerPage < pageSize) return 1;
    return (int) Math.ceil(totalElements / pageSize);
  }
}
