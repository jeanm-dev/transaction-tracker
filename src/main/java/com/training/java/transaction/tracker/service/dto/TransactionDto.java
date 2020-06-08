package com.training.java.transaction.tracker.service.dto;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionDto {

  // TODO: Decide DTO with special values or individual DTOs (identifier leads not required for create transaction objects)
  private final int identifier;
  private final String description;
  private final BigDecimal amount;
  private final Date dateOfTransaction;
  private final String type;

  public static final String UNDEFINED = "TYPE UNDEFINED";
  public static final int NEW_IDENTIFIER = -1;

  // Useful for JSON mapping
  public TransactionDto(int identifier, String description, BigDecimal amount,
      Date dateOfTransaction, String type) {
    //TODO: Deal with Null values
    this.identifier = identifier;
    this.description = description;
    this.amount = amount;
    this.dateOfTransaction = dateOfTransaction;
    this.type = type == null ? UNDEFINED : type;
  }

  public int getIdentifier() {
    return identifier;
  }

  public String getDescription() {
    return description;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Date getDateOfTransaction() {
    return dateOfTransaction;
  }

  public String getType() {
    return type;
  }
}
