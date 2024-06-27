package com.manuelr.bank.api.enums;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum BankTypes {
  BRANCH("Sucursal"),
  PRIVATE_BANKING("Banca Privada"),
  WEALTH_MANAGEMENT("Banca Patrimonial"),
  DIGITAL_BRANCH("Sucursal Digital"),
  ATM("ATM"),
  AUTOMATIC_BRANCH("Sucursal Automatica"),
  RETIREMENT_SAVINGS("AFORE"),
  PRIORITY_EXECUTIVE("Ejecutivo Priority"),
  PRIORITY_SPACE("Espacio Priority"),
  DEFAULT("Default");

  private final String value;

  BankTypes(String value) {
    this.value = value;
  }

  public static BankTypes fromString(String text) {
    return Arrays.stream(BankTypes.values())
        .filter(b -> b.value.equalsIgnoreCase(text))
        .findFirst()
        .orElse(BankTypes.DEFAULT);
  }
}